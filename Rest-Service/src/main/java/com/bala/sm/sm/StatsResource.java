package com.bala.sm.sm;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bala.sm.sm.service.StatsService;

@Path("/stats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatsResource {
	StatsService ss = new StatsService();
	@GET
	@Path("/{year}/{month}/{day}")
	public String getStats(@PathParam("year") int year,@PathParam("month") int month,@PathParam("day") int day)
	{
		String json = ss.stats(year, month, day);
		return json;
	}
}
