package it.slowik;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

/**
 * Created by
 * pawelslowik
 * on 23/01/17.
 */
public class SimpleConsumer extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("ab", objectMessage -> {
            JsonObject resp = new JsonObject();
            resp.put("abc", true);
            objectMessage.reply(resp);
        });
    }
}
