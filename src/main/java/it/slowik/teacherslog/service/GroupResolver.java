package it.slowik.teacherslog.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;

/**
 * Created by
 * pawelslowik
 * on 27/01/17.
 */
public class GroupResolver extends AbstractVerticle {

    public static final String FETCH_GROUP = "groups.fetchById";
    public static final FindOptions FIND_OPTIONS = new FindOptions(
            new JsonObject()
                    .put("fields",
                            new JsonObject()
                                    .put("name", true)
                                    .put("description", true)
                                    .put("dateOfActivities", true)));

    private MongoClient client;

    public GroupResolver(MongoClient client) {
        this.client = client;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer(FETCH_GROUP, handler -> client.findOne("groups", new JsonObject().put("_id", handler.body()), new JsonObject(), queryRes -> {
            if (queryRes.succeeded()) {
                handler.reply(queryRes.result());
            } else {
                handler.reply(new JsonObject());
            }
        }));
    }
}
