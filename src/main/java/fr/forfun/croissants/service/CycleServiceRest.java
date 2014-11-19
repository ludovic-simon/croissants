package fr.forfun.croissants.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.spi.resource.Singleton;

import fr.forfun.croissants.core.SDevRestDoBeforeSerialization;
import fr.forfun.croissants.entity.ConstitutionGroupe;

@Path("/cycleService")
@Singleton
public class CycleServiceRest {

	protected CycleService cycleService = new CycleService();

	@GET
	@Path("rechercherGroupesUtilisateur")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ConstitutionGroupe> rechercherGroupesUtilisateur(@QueryParam("idUtilisateur") Long idUtilisateur){
		List<ConstitutionGroupe> res = cycleService.rechercherGroupesUtilisateur(idUtilisateur);
		SDevRestDoBeforeSerialization.run(res);
		return res;
	}
	
	@POST
	@Path("rejoindreGroupe")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ConstitutionGroupe rejoindreGroupe(@QueryParam("idUtilisateur") Long idUtilisateur, @QueryParam("idGroupe") Long idGroupe){
		ConstitutionGroupe res = cycleService.rejoindreGroupe(idUtilisateur, idGroupe);
		SDevRestDoBeforeSerialization.run(res);
		return res;
	}
	
}