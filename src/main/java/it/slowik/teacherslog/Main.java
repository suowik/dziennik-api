package it.slowik.teacherslog;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import it.slowik.teacherslog.service.GroupSaver;
import it.slowik.teacherslog.service.GroupsResolver;

public class Main extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {

        vertx.deployVerticle(new GroupsResolver(), new DeploymentOptions().setWorker(true));
        vertx.deployVerticle(new GroupSaver(), new DeploymentOptions().setWorker(true));


        Router router = Router.router(vertx);
        router.get("/").handler(req -> req.response().end("ohai"));
        router.get("/groups/").handler(req -> vertx.eventBus().send(GroupsResolver.LIST_GROUPS, "", reply -> {
            if (reply.succeeded()) {
                req.response().setStatusCode(200).putHeader("content-type","application/json").end(reply.result().body().toString());
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