package it.slowik.teacherslog.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * Created by
 * pawelslowik
 * on 27/01/17.
 */
public class GroupSaver extends AbstractVerticle {

    public static String CREATE_GROUP = "groups.create";

    @Override
    public void start() throws Exception {
        MongoClient client = MongoClientResolver.resolve(vertx);
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer(CREATE_GROUP, message -> {
            JsonObject group = (JsonObject) message.body();
            group.put("_id", group.getString("name"));
            client.insert("groups", group, res -> {
                if (res.succeeded()) {
                    message.reply(new JsonObject().put("id", res.result()));
                } else {
                    message.fail(0, "a");
                }
            });
        });
    }
}
