package fr.forfun.croissants.test;

import fr.forfun.croissants.entity.Utilisateur;
import fr.forfun.croissants.service.CycleService;
import fr.forfun.croissants.service.TransverseService;
import fr.forfun.croissants.service.UtilisateurService;


public class CroissantsTestFactory {

	public static final String TEST_PERSISTENCE_UNIT = "croissants-test";
	
	public static UtilisateurService createUtilisateurService(){
		UtilisateurService service = new UtilisateurService(TEST_PERSISTENCE_UNIT);
		service.setTransverseService(new TransverseService(TEST_PERSISTENCE_UNIT));
		return service;
	}
	
	public static CycleService createCycleService(){
		CycleService service = new CycleService(TEST_PERSISTENCE_UNIT);
		service.setTransverseService(new TransverseService(TEST_PERSISTENCE_UNIT));
		return service;
	}
	
	public static void main(String[] args) {
		UtilisateurService utilisateurService = createUtilisateurService();
		Utilisateur connecterUtilisateur = utilisateurService.connecterUtilisateur(null, "toto");
		System.out.println(connecterUtilisateur);
	}
	
}
