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
import fr.forfun.croissants.entity.Groupe;

@Path("/cycleService")
@Singleton
public class CycleServiceRest {

	protected CycleService cycleService = new CycleService();

	@GET
	@Path("rechercherGroupeParId")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Groupe rechercherGroupeParId(@QueryParam("idGroupe") Long idGroupe){
		Groupe res = cycleService.rechercherGroupeParId(idGroupe);
		SDevRestDoBeforeSerialization.run(res);
		return res;
	}
	
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
	public ConstitutionGroupe rejoindreGroupe(@QueryParam("idUtilisateur") Long idUtilisateur, @QueryParam("jeton") String jeton, @QueryParam("motDePasse") String motDePasse){
		ConstitutionGroupe res = cycleService.rejoindreGroupe(idUtilisateur, jeton, motDePasse);
		SDevRestDoBeforeSerialization.run(res);
		return res;
	}
	
	@POST
	@Path("editerGroupe")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Groupe editerGroupe(@QueryParam("idUtilisateur") Long idUtilisateur, Groupe groupe){
		Groupe res = cycleService.editerGroupe(idUtilisateur, groupe);
		SDevRestDoBeforeSerialization.run(res);
		return res;
	}
	
	@POST
	@Path("affecterDroitAdministrateur")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ConstitutionGroupe affecterDroitAdministrateur(@QueryParam("idUtilisateur") Long idUtilisateur, @QueryParam("idGroupe") Long idGroupe, @QueryParam("admin") boolean admin){
		ConstitutionGroupe res = cycleService.affecterDroitAdministrateur(idUtilisateur, idGroupe, admin);
		SDevRestDoBeforeSerialization.run(res);
		return res;
	}
	
	@POST
	@Path("supprimerGroupe")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void supprimerGroupe(@QueryParam("idUtilisateur") Long idUtilisateur, @QueryParam("idGroupe") Long idGroupe){
		cycleService.supprimerGroupe(idUtilisateur, idGroupe);
	}
	
}