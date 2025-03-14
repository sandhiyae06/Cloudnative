package com.KAFKATESTING.ConsumerTest;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import com.mongodb.MongoClient;  
	public class ClearAndDisConnectDB { 
	   	   public static void main( String args[] ) {  
	      	      // Creating a Mongo client 
		   MongoClient mongoClient = new MongoClient("localhost", 27017);									
	     	      //Accessing the database 
	     MongoDatabase database = mongoClient.getDatabase("kafkadb");  
	     //Dropping the collection
	     MongoCollection<Document> collection1 = database.getCollection("kafkamessagedata");
	      System.out.println("kafkamessagedata collection dropped successfully");	     
	     collection1.drop();
	     mongoClient.close();
	} 
	}

