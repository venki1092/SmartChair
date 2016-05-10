package com.bala.sm.sm;

import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("x")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getIt() {
		MongoClient mongoClient = new MongoClient("54.186.96.23",27018);
		MongoDatabase db = mongoClient.getDatabase("smartchairdb");
		MongoCollection<Document> coll = db.getCollection("smartchairdataset");
		//Document d = new Document().append(1, value)
		FindIterable<Document> iter = coll.find();
		Iterator<Document> i = iter.iterator();
		Document d = i.next();
		String p1 = d.getString("pressure1");
		String p2 =  d.getString("pressure2");
		String json = "";
		json += ("{\"p1\" : " + p1 + ",");
		 json += ("\"p2\" : " + p2 );
		 json += "}";
		 mongoClient.close();
		 return json;		
}
}