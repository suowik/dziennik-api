package it.slowik.teacherslog;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import io.vertx.core.*;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import it.slowik.teacherslog.service.*;
import it.slowik.teacherslog.support.EnvSupport;

import java.util.Optional;

import static it.slowik.teacherslog.service.MongoClientResolver.resolve;

public class Main extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new Main());
    }

    @Override
    public void start(Future<Void> fut) {
        vertx.deployVerticle(new GroupsResolver(resolve(vertx)), new DeploymentOptions().setWorker(true));
        vertx.deployVerticle(new GroupByPasswordResolver(resolve(vertx)), new DeploymentOptions().setWorker(true));
        vertx.deployVerticle(new GroupSaver(resolve(vertx)), new DeploymentOptions().setWorker(true));
        vertx.deployVerticle(new GroupResolver(resolve(vertx)), new DeploymentOptions().setWorker(true));


        Router router = Router.router(vertx);
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeaders(Sets.newHashSet("Content-type", "Authorization")));
        //router.route().handler(new AuthHandler());
        router.post("/groups*").handler(new AuthHandler());
        router.get("/groups/").handler(req -> vertx.eventBus().send(GroupsResolver.LIST_GROUPS, "", reply -> {
            if (reply.succeeded()) {
                req.response().setStatusCode(200).putHeader("content-type", "application/json").end(reply.result().body().toString());
            }
        }));
        router.get("/groups/:name").handler(req -> vertx.eventBus().send(GroupResolver.FETCH_GROUP, req.request().getParam("name"), reply -> {
            if (reply.succeeded()) {
                req.response().setStatusCode(200).putHeader("content-type", "application/json").end(reply.result().body().toString());
            }
        }));
        router.get("/protected/:name").handler(req -> vertx.eventBus().send(GroupByPasswordResolver.FETCH_GROUP, req.request().getParam("name"), reply -> {
            if (reply.succeeded()) {
                req.response().setStatusCode(200).putHeader("content-type", "application/json").end(reply.result().body().toString());
            }
        }));
        router.post("/groups*").handler(BodyHandler.create());
        router.post("/groups/").handler(req -> {
            JsonObject body = req.getBodyAsJson();
            vertx.eventBus().send(GroupSaver.CREATE_GROUP, body, reply -> {
                if (reply.succeeded()) {
                    req.response().setStatusCode(201).end();
                } else {
                    req.response().setStatusCode(500).end();
                }
            });
        });

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(Integer.getInteger("http.port", 8080), System.getProperty("http.address", "0.0.0.0"));
    }
}

class AuthHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext routingContext) {
        Optional<String> auth = Optional.ofNullable(routingContext.request().getHeader("Authorization"))
                .filter(raw -> !Strings.isNullOrEmpty(raw))
                .filter(raw -> raw.matches("Basic .*"))
                .map(raw -> raw.split(" ")[1])
                .filter(raw -> raw.equalsIgnoreCase(EnvSupport.getEnv("AUTH", "verysafesecret")));

        if (auth.isPresent()) {
            routingContext.next();
        } else {
            routingContext.response().setStatusCode(401).end();
        }
    }
}