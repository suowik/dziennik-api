package it.slowik.teacherslog.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * Created by
 * pawelslowik
 * on 27/01/17.
 */
public class GroupByPasswordResolver extends AbstractVerticle {

    public static final String FETCH_GROUP = "groups.fetchByPassword";

    private MongoClient client;

    public GroupByPasswordResolver(MongoClient client) {
        this.client = client;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer(FETCH_GROUP, handler -> client.findOne("groups", new JsonObject().put("password", handler.body()), new JsonObject(), queryRes -> {
            if (queryRes.succeeded()) {
                handler.reply(queryRes.result());
            } else {
                handler.reply(new JsonObject());
            }
        }));
    }
}
