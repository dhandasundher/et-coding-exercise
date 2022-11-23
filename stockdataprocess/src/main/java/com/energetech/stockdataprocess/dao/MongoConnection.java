package com.energetech.stockdataprocess.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {

	static final String MONGODB_URI = "mongodb://localhost:27017";
	private static MongoClient mongoClient;
	public static MongoDatabase db;
	
	public static void open() {
		mongoClient = MongoClients.create(MONGODB_URI);
		db = mongoClient.getDatabase("energetech");
		System.out.println("Opening Mongo Connection");
	}
	
	public static void close() {
		mongoClient.close();
		System.out.println("Closing Mongo Connection");
	}
}
