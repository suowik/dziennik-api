package it.slowik.teacherslog.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.stream.Stream;

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
    public void start(Future<Void> startFuture) {
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer(FETCH_GROUP, handler -> client.findOne("groups",
                new JsonObject().put("password", handler.body()),
                new JsonObject()
                        .put("name", 1)
                        .put("announcements", 1)
                        .put("activeYear", 1)
                        .put("activeSemester", 1)
                        .put("dateOfActivities", 1)
                        .put("description", 1)
                        .put("semesters.year", 1)
                        .put("semesters.semester", 1)
                        .put("semesters.students.tests", 1)
                        .put("semesters.students.id", 1)
                        .put("semesters.students.surname", 1)
                        .put("semesters.students.name", 1),
                queryRes -> {
                    if (queryRes.succeeded()) {
                        handler.reply(queryRes.result());
                    } else {
                        handler.reply(new JsonObject());
                    }
                }));
    }
}
