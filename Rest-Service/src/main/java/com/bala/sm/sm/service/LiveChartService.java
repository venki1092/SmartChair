package com.bala.sm.sm.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class LiveChartService {
	public String getData()
	{
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND,-20);
		Date date = calendar.getTime();
		
		
		//Calendar calendar = Calendar.getInstance();
		//calendar.set(Calendar.HOUR_OF_DAY, 14);
		//calendar.set(Calendar.MINUTE,1);
		//calendar.set(Calendar.SECOND,22);
		//Date date =calendar.getTime();
		
		MongoClient mongoClient = new MongoClient("54.186.96.23",27018);	
		MongoDatabase db = mongoClient.getDatabase("smartchairdb");
		MongoCollection<Document> mc = db.getCollection("smartchairdataset");
		Document d = new Document("$natural",-1);
		Document condition = new Document("$gte",date);
		Document conditionStatement = new Document("date",condition);
		FindIterable<Document> findIter = mc.find(conditionStatement).limit(1).sort(d);
		Iterator<Document> iter =  findIter.iterator();
		Document doc = null;
		Integer s = 0;
		if(iter.hasNext())
		{
			doc = iter.next();
			 s = doc.getInteger("isHarmful");
		}
		mongoClient.close();
		return Integer.toString(s);
		
		/* hard coded data */
		/*
		Random r = new Random();
		int x = r.nextInt(2);
		return Integer.toString(x);
		*/
	}
}
