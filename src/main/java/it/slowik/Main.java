package it.slowik;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class Main extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        vertx.deployVerticle(new SimpleConsumer());

        Router router = Router.router(vertx);

        router.route("/").handler(req -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("a", "a");
            vertx.eventBus().send("ab", jsonObject, reply -> {
                if (reply.succeeded()) {
                    req.response().end(reply.result().body().toString());
                } else {
                    req.response().end("Hello");
                }
            });
        });

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(Integer.getInteger("http.port", 8080), System.getProperty("http.address", "0.0.0.0"));
    }
}