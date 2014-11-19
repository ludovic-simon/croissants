package fr.forfun.croissants.entity;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel(ConstitutionGroupe.class)
public class ConstitutionGroupe_ {
	
	public static volatile SingularAttribute<ConstitutionGroupe, Long> idConstitutionGroupe;	
	public static volatile SingularAttribute<ConstitutionGroupe, Long> idGroupe;	
	public static volatile SingularAttribute<ConstitutionGroupe, Long> idUtilisateur;	
	public static volatile SingularAttribute<ConstitutionGroupe, Boolean> admin;	
	public static volatile SingularAttribute<ConstitutionGroupe, Boolean> parDefaut;	
	public static volatile SingularAttribute<ConstitutionGroupe, Date> dateArriveeGroupe;	
	public static volatile SingularAttribute<ConstitutionGroupe, Date> dateSortieGroupe;	
	public static volatile SingularAttribute<ConstitutionGroupe, Groupe> groupe;	
	public static volatile SingularAttribute<ConstitutionGroupe, Utilisateur> utilisateur;	

}