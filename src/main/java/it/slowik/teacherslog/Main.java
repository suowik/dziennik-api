package it.slowik.teacherslog;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import it.slowik.teacherslog.service.GroupSaver;
import it.slowik.teacherslog.service.GroupsResolver;
import it.slowik.teacherslog.service.MongoClientResolver;

import static it.slowik.teacherslog.service.MongoClientResolver.resolve;

public class Main extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        vertx.deployVerticle(new GroupsResolver(resolve(vertx)), new DeploymentOptions().setWorker(true));
        vertx.deployVerticle(new GroupSaver(resolve(vertx)), new DeploymentOptions().setWorker(true));


        Router router = Router.router(vertx);
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("Content-Type"));
        router.get("/").handler(req -> req.response().end("ohai"));
        router.get("/groups/").handler(req -> vertx.eventBus().send(GroupsResolver.LIST_GROUPS, "", reply -> {
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