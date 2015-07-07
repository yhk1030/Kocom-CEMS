package cems;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.*;

public class mongoConnect {
	Setting s = new Setting();
	
	static String	userName = "sc";
	static String	database = "scconfig";
	static String	password = "1";
	
	static String	host	 = Setting.readFile("config", "DB_Host");
	static int		port	 = Integer.parseInt(Setting.readFile("config", "DB_Port"));

	static MongoClient mongoClient;
	static MongoCredential credential;
	
	public static String receiveKey() {
		
		
		String Key = null;
		try {
//			credential = MongoCredential.createMongoCRCredential(userName, database, password.toCharArray() );
//			mongoClient = new MongoClient(new ServerAddress( host, port ), Arrays.asList(credential));
			mongoClient = new MongoClient(host, port);
		} catch (UnknownHostException e) {
			System.out.println("Mongo Connection Error");
		}
		DB db = mongoClient.getDB(database);
		DBCollection coll = db.getCollection("key");		
		DBCursor cursor = coll.find();
		
		try {
			   while(cursor.hasNext()) {
				   Key = cursor.next().get("key").toString();
			   }
			} finally {
			   cursor.close();
		}
//		System.out.println(Key);
		return Key;
	}
}
