package config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class Config {

    static MongoDatabase db = null;

    public Config() {

        //String user = "student01";
        //String password = "student01";
        String host = "localhost";
        int port = 27017;
        String database = "psrlab5";

        String clientURI = "mongodb://" + host + ":" + port + "/" + database;
        MongoClientURI uri = new MongoClientURI(clientURI);

        MongoClient mongoClient = new MongoClient(uri);

        db = mongoClient.getDatabase(database);
    }

    public static MongoDatabase getDB() {
        return db;
    }
}
