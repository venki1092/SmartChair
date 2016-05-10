package com.bala.sm.sm.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class StatsService {
	@SuppressWarnings("deprecation")
	public String stats(int year, int month, int date)
	{
		 String res = "";

		 Calendar calendar = Calendar.getInstance();
		 calendar.set(Calendar.YEAR, year);
		 calendar.set(Calendar.MONTH, month-1);
		 calendar.set(Calendar.DATE, date);
		/* if(calendar.getTime().getDate() <=6)
		 {
			 calendar.add(Calendar.DATE, -6);
			 calendar.add(Calendar.MONTH,-1);
		 }
		 else */	 
		 	calendar.add(Calendar.DATE, -6);
		
		 //Document condition1 = new
		 String[] h = new String[7]; 
		 for(int i=0;i<7;i++)
		 {
			 calendar.add(Calendar.DATE, i);
			 Date d  = calendar.getTime();
			 h[i] = statsForTheDay(d);
		 }
		 
		 /* hard code data */
		 /*
		 Random r = new Random();
		 h[0] = Integer.toString(r.nextInt(2000));
		 h[1] = Integer.toString(r.nextInt(2000));
		 h[2] = Integer.toString(r.nextInt(2000));
		 h[3] = Integer.toString(r.nextInt(2000));
		 h[4] = Integer.toString(r.nextInt(2000));
		 h[5] = Integer.toString(r.nextInt(2000));
		 h[6] = Integer.toString(r.nextInt(2000));
		 */
		 
		 JSONObject jo = new JSONObject();
		 jo.append("day1" ,h[0]);
		 jo.append("day2" ,h[1]);
		 jo.append("day3" ,h[2]);
		 jo.append("day4" ,h[3]);
		 jo.append("day5" ,h[4]);
		 jo.append("day6" ,h[5]);
		 jo.append("day7" ,h[6]);
		 
		 res = jo.toString();
		 return res;
	}
	
	public String statsForTheDay(Date date)
	{
		MongoClient mongoClient = new MongoClient("54.186.96.23",27018);	
		MongoDatabase db = mongoClient.getDatabase("smartchairdb");
		MongoCollection<Document> mc = db.getCollection("smartchair");
		String res = "";
		
		
		Document con = new Document("$eq" , date );
		
		Document condition = new Document("date", con).append("isHarmful", "0");
		
		long count = mc.count(condition);
		count*=4;
		res = String.valueOf(count);
		
		mongoClient.close();
		return res;
	}
}
