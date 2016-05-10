package com.bala.sm.sm.service;

import java.util.Calendar;

import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class PieChartService {
	public String pie(int y, int m, int d)
	{
		JSONObject jo = new JSONObject();
		
		String s1 = "Correct Position";
		String s2 = "You are Sitting the edge";
		String s3 = "You are Leaning Back";
		String s4 = "You are Leaning Front";
		String s5 = "You are with the chair";
		String s6 = "Reclining well";
		String s7 = "Reclining too much";
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, y);
		c.set(Calendar.MONTH, m);
		c.set(Calendar.DATE , d);
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.YEAR, y);
		c1.set(Calendar.MONTH, m);
		c1.set(Calendar.DATE , d);
		c1.add(Calendar.DATE , 1);
		
		MongoClient mongoClient = new MongoClient("54.186.96.23",27018);	
		MongoDatabase db = mongoClient.getDatabase("smartchairdb");
		MongoCollection<Document> mc = db.getCollection("smartchairdataset");
		
		long p1 = mc.count(new Document()
				.append("position",s1)
				.append("date",new Document("$gte",c.getTime()))
				.append("date",new Document("$lt", c1.getTime()))
				);
		
		long p2 = mc.count(new Document("position",s2).append("date",new Document("$gte",c.getTime()))
				.append("date",new Document("$lt", c1.getTime())));
		long p3 = mc.count(new Document("position",s3).append("date",new Document("$gte",c.getTime()))
				.append("date",new Document("$lt", c1.getTime())));
		long p4 = mc.count(new Document("position",s4).append("date",new Document("$gte",c.getTime()))
				.append("date",new Document("$lt", c1.getTime())));
		long p5 = mc.count(new Document("position",s5).append("date",new Document("$gte",c.getTime()))
				.append("date",new Document("$lt", c1.getTime())));
		long p6 = mc.count(new Document("position",s6).append("date",new Document("$gte",c.getTime()))
				.append("date",new Document("$lt", c1.getTime())));
		long p7 = mc.count(new Document("position",s7).append("date",new Document("$gte",c.getTime()))
				.append("date",new Document("$lt", c1.getTime())));
		
		jo.append("p1",Long.toString(p1));
		jo.append("p2",Long.toString(p2));
		jo.append("p3",Long.toString(p3));
		jo.append("p4",Long.toString(p4));
		jo.append("p5",Long.toString(p5));
		jo.append("p6",Long.toString(p6));
		jo.append("p7",Long.toString(p7));
		
		mongoClient.close();
		
		return jo.toString();
	}
}
