package it.slowik;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class MyFirstApp extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {

        vertx.deployVerticle(new SimpleConsumer());
        vertx
                .createHttpServer()
                .requestHandler(req -> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.put("a", "a");
                    vertx.eventBus().send("ab", jsonObject, reply -> {
                        if (reply.succeeded()) {
                            req.response().end(reply.result().body().toString());
                        } else {
                            req.response().end("Hello");
                        }
                    });
                })
                .listen(Integer.getInteger("PORT"), System.getProperty("http.address", "0.0.0.0"));
    }
}