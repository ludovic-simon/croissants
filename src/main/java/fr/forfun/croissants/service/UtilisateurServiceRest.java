package fr.forfun.croissants.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.spi.resource.Singleton;

import fr.forfun.croissants.core.BusinessException;
import fr.forfun.croissants.core.SDevRestDoBeforeSerialization;
import fr.forfun.croissants.entity.Utilisateur;

@Path("/utilisateurService")
@Singleton
public class UtilisateurServiceRest {
 
	protected UtilisateurService utilisateurService = new UtilisateurService();

	@Context
	private HttpServletRequest request;
	
	@GET
	@Path("connecterUtilisateur")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Utilisateur connecterUtilisateur(@QueryParam("email") String email, @QueryParam("motDePasse") String motDePasse){
		Utilisateur res = utilisateurService.connecterUtilisateur(email, motDePasse);
		SDevRestDoBeforeSerialization.run(res);
		
		//Mise de l'utilisateur en session
		HttpSession session = request.getSession();
		session.setAttribute("utilisateur", res);
		
		return res;
	}
	
	@POST
	@Path("creerUtilisateur")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Utilisateur creerUtilisateur(Utilisateur utilisateur){
		Utilisateur res = utilisateurService.creerUtilisateur(utilisateur);
		SDevRestDoBeforeSerialization.run(res);
		return res;
	}
	
	@GET
	@Path("getUtilisateurSession")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Utilisateur getUtilisateurSession(){
		Utilisateur res = utilisateurService.getUtilisateurSession();
		SDevRestDoBeforeSerialization.run(res);
		
		HttpSession session = request.getSession();
		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
		if(utilisateur == null){
			throw new BusinessException("Veuillez vous authentifier");
		}
		return utilisateur;
	}
	
}