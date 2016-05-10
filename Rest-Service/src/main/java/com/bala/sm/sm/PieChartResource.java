package com.bala.sm.sm;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.bala.sm.sm.service.PieChartService;

@Path("/piechart")
public class PieChartResource {
	
	PieChartService pcs = new PieChartService();
	@GET
	@Path("/{y}/{m}/{d}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	public String getPie(@PathParam("y")int y, @PathParam("m")int m ,@PathParam("d")int d)
	{
		String res = "";
		res = pcs.pie(y,m,d);
		return res;
	}
}
