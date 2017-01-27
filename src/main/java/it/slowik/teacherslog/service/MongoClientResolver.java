package it.slowik.teacherslog.service;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * Created by
 * pawelslowik
 * on 27/01/17.
 */
public class MongoClientResolver {
    public static MongoClient resolve(Vertx vertx) {
        JsonObject config = new JsonObject();
        config.put("connection_string", System.getProperty("MONGO_URL", "mongodb://localhost:27018"));
        return MongoClient.createShared(vertx, config);
    }
}
