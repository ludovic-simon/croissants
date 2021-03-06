package fr.forfun.croissants.service;

import java.util.List;

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
	
	{
		utilisateurService.setTransverseService(new TransverseService());
	}
	
	@Context
	private HttpServletRequest request;

	@GET
	@Path("connecterUtilisateur")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Utilisateur connecterUtilisateur(@QueryParam("email") String email, @QueryParam("motDePasse") String motDePasse){
		Utilisateur res = utilisateurService.connecterUtilisateur(email, motDePasse);
		SDevRestDoBeforeSerialization.run(res);
		//Mise de l'utilisateur en session
		updateUtilisateurSession(res);
		return res;
	}
	
	@POST
	@Path("seDeconnecter")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void seDeconnecter(){
		//Invalidation de la session
		HttpSession session = request.getSession();
		session.invalidate();
	}
	
	@POST
	@Path("creerUtilisateur")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Utilisateur creerUtilisateur(Utilisateur utilisateur){
		Utilisateur res = utilisateurService.creerUtilisateur(utilisateur);
		SDevRestDoBeforeSerialization.run(res);
		
		//Mise de l'utilisateur pour pouvoir le rediriger direct sur le site connecte
		updateUtilisateurSession(res);
		
		return res;
	}
	
	@GET
	@Path("getUtilisateurSession")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Utilisateur getUtilisateurSession(){
		HttpSession session = request.getSession();
		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
		if(utilisateur == null){
			throw new BusinessException("Veuillez vous authentifier");
		}
		return utilisateur;
	}
	
	@POST
	@Path("motDePassePerdu")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void motDePassePerdu(@QueryParam("email") String email){
		utilisateurService.motDePassePerdu(email);
	}
	
	@POST
	@Path("changerMotDePasse")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void changerMotDePasse(@QueryParam("idUtilisateur") Long idUtilisateur, @QueryParam("ancienMdp") String ancienMdp, @QueryParam("nouveauMdp") String nouveauMdp){
		utilisateurService.changerMotDePasse(idUtilisateur, ancienMdp, nouveauMdp);
	}
	
	{/* UTILITAIRES */}
	
	protected void updateUtilisateurSession(Utilisateur utilisateur){
		//Non mise a disposition du mot de passe cote presentation
		utilisateur.setMotDePasse(null);
		//Mise en session
		HttpSession session = request.getSession();
		session.setAttribute("utilisateur", utilisateur);
	}
	
}