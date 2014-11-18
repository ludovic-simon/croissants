package fr.forfun.croissants.service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;



/**
 * Services lies au cycle
 */
public class CycleService {

	protected EntityManagerFactory emf;
	
	public CycleService(){
		emf = Persistence.createEntityManagerFactory("croissants");
	}
	
	public CycleService(String persistenceUnit){
		emf = Persistence.createEntityManagerFactory(persistenceUnit);
	}
	
}