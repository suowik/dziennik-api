package it.slowik.teacherslog.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;

/**
 * Created by
 * pawelslowik
 * on 27/01/17.
 */
public class GroupsResolver extends AbstractVerticle {

    public static final String LIST_GROUPS = "groups.list";
    public static final FindOptions FIND_OPTIONS = new FindOptions(
            new JsonObject()
                    .put("fields",
                            new JsonObject()
                                    .put("name", true)
                                    .put("description", true)
                                    .put("dateOfActivities", true)));

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        MongoClient mongo = MongoClientResolver.resolve(vertx);
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer(LIST_GROUPS, handler -> mongo.findWithOptions("groups", new JsonObject(), FIND_OPTIONS, queryRes -> {
            if (queryRes.succeeded()) {
                if (queryRes.result().isEmpty()) {
                    handler.reply(new JsonArray());
                } else {
                    JsonArray res = new JsonArray();
                    queryRes.result().forEach(res::add);
                    handler.reply(res);
                }
            } else {
                handler.reply(new JsonObject());
            }
        }));
    }
}
