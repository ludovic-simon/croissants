package fr.forfun.croissants.entity;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel(Groupe.class)
public class Groupe_ {
	
	public static volatile SingularAttribute<Groupe, Long> idGroupe;	
	public static volatile SingularAttribute<Groupe, String> nom;	
	public static volatile SingularAttribute<Groupe, String> jeton;	
	public static volatile SingularAttribute<Groupe, String> motDePasse;	
	public static volatile SingularAttribute<Groupe, String> message;	
	public static volatile SingularAttribute<Groupe, Long> jourOccurence;	
	public static volatile SingularAttribute<Groupe, Date> dateCreation;	

}