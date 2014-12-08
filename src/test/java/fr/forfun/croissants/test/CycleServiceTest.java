package fr.forfun.croissants.test;

import fr.forfun.croissants.service.CycleService;

public class CycleServiceTest {

	public static void main(String[] args) {
		System.out.println("DEBUT CycleServiceTest");
		CycleService cycleService = CroissantsTestFactory.createCycleService();
		
//		System.out.println(cycleService.calculerProchainCycle(4L));
		
		cycleService.annulerTour(66L, "Message annulation");
		
		System.out.println("FIN CycleServiceTest");
	}
	
}
