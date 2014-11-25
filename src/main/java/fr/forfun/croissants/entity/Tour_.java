package fr.forfun.croissants.entity;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel(Tour.class)
public class Tour_ {
	
	public static volatile SingularAttribute<Tour, Long> idTour;	
	public static volatile SingularAttribute<Tour, Long> idGroupe;	
	public static volatile SingularAttribute<Tour, Long> idUtilisateur;	
	public static volatile SingularAttribute<Tour, String> nomTour;	
	public static volatile SingularAttribute<Tour, Date> dateTour;	
	public static volatile SingularAttribute<Tour, StatutTour> statutTour;	

}