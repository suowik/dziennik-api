package it.slowik.teacherslog.service;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import it.slowik.teacherslog.support.EnvSupport;

/**
 * Created by
 * pawelslowik
 * on 27/01/17.
 */
public class MongoClientResolver {
    public static MongoClient resolve(Vertx vertx) {
        JsonObject config = new JsonObject();
        config.put("connection_string", EnvSupport.getEnv("MONGODB_URI", "mongodb://localhost:27018"));
        return MongoClient.createShared(vertx, config);
    }
}
