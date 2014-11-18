package fr.forfun.croissants.service;


import javax.ws.rs.Path;

import com.sun.jersey.spi.resource.Singleton;

@Path("/cycleService")
@Singleton
public class CycleServiceRest {

	protected CycleService cycleService = new CycleService();

}