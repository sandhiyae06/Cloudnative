package com.KAFKATESTING.ConsumerTest;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;  
	public class ConnectDB { 
	   	   public static void main( String args[] ) {  
	      	      // Creating a Mongo client 
		   MongoClient mongoClient = new MongoClient("localhost", 27017);									
	     	      //Accessing the database 
	     MongoDatabase database = mongoClient.getDatabase("kafkadb");  
	      	      //Creating a collection 
	     database.createCollection("kafkamessagedata"); 
	     mongoClient.close();
	} 
	}

