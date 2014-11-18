package fr.forfun.croissants.test;

import fr.forfun.croissants.entity.Utilisateur;
import fr.forfun.croissants.service.CycleService;
import fr.forfun.croissants.service.UtilisateurService;


public class CroissantsTestFactory {

	public static UtilisateurService createUtilisateurService(){
		UtilisateurService service = new UtilisateurService("croissants-test");
		return service;
	}
	
	public static CycleService createCycleService(){
		CycleService service = new CycleService("croissants-test");
		return service;
	}
	
	public static void main(String[] args) {
		UtilisateurService utilisateurService = createUtilisateurService();
		Utilisateur connecterUtilisateur = utilisateurService.connecterUtilisateur(null, "toto");
		System.out.println(connecterUtilisateur);
	}
	
}
