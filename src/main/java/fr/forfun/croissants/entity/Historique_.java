package fr.forfun.croissants.entity;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel(Historique.class)
public class Historique_ {
	
	public static volatile SingularAttribute<Historique, Long> idHistorique;	
	public static volatile SingularAttribute<Historique, Date> dateAction;	
	public static volatile SingularAttribute<Historique, Long> idUtilisateurAction;	
	public static volatile SingularAttribute<Historique, HistoriqueDomaine> historiqueDomaine;	
	public static volatile SingularAttribute<Historique, String> reference;	
	public static volatile SingularAttribute<Historique, String> action;	
	public static volatile SingularAttribute<Historique, Boolean> isSuperAdmin;	

}