package it.slowik;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

/**
 * Created by
 * pawelslowik
 * on 23/01/17.
 */
public class SimpleSender extends AbstractVerticle{
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        EventBus eventBus = vertx.eventBus();
        eventBus.publish("ab","Abcd");
    }
}
