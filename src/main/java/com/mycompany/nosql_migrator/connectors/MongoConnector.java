package com.mycompany.nosql_migrator.connectors;

import com.google.gson.JsonObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import org.bson.Document;

public class MongoConnector {

    static private MongoClient mongoClient;
    static private MongoDatabase database;
    static public MongoCollection collection;
    String databaseName = "NorthWind_MDF"; // this is the name that the database has on MongoDB client. If it does not exist already it will created.

    public void connect(String dbName, String collectionName) {

        mongoClient = new MongoClient("localhost", 27017);// change these attributes as needed to connect to your MongoDB client.
        database = mongoClient.getDatabase(dbName);
        collection = database.getCollection(collectionName);

    }

    public void connect_and_load(String CollectionName, List<JsonObject> JsonList) {
        //connect and load onto MongoDB
        System.out.println("Connecting to Mongo and loading "+CollectionName+"...");
        try {

            connect(this.databaseName, CollectionName);

            for (JsonObject temp : JsonList) {

                Document doc = Document.parse(temp.toString());

                collection.insertOne(doc);

            }
        } catch (Exception e) {
            System.out.println("Error loading onto MongoDB! \n" + e);
        } finally {
            disconnect();
            System.out.println("Succesfully loaded data and disconnected from MongoDB!");
        }

    }

    public void disconnect() {
        mongoClient.close();
    }

}
