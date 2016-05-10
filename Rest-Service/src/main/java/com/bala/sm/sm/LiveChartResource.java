package com.bala.sm.sm;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.bala.sm.sm.service.LiveChartService;

@Path("livechart")
public class LiveChartResource {
	LiveChartService lcs = new LiveChartService();
	@GET
	//@Path("/{year}/{month}/{day}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getStatus(@PathParam("date") String date) 
	{
		String data = lcs.getData();
		JSONObject jo = new JSONObject();
		jo.append("data" ,data);
		return jo.toString();	
	}
}
