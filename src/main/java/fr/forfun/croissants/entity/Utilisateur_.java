package fr.forfun.croissants.entity;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel(Utilisateur.class)
public class Utilisateur_ {
	
	public static volatile SingularAttribute<Utilisateur, Long> idUtilisateur;	
	public static volatile SingularAttribute<Utilisateur, String> nom;	
	public static volatile SingularAttribute<Utilisateur, String> email;	
	public static volatile SingularAttribute<Utilisateur, String> motDePasse;	
	public static volatile SingularAttribute<Utilisateur, Date> dateCreation;	
	public static volatile SingularAttribute<Utilisateur, Date> dateDerniereConnexion;	

}