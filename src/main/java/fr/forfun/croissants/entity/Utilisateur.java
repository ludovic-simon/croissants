package fr.forfun.croissants.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * utilisateur du site
 */
@Entity
@Table(name="utilisateur")
@XmlRootElement
public class Utilisateur implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	{/* CHAMPS */}
	
	/** Identifiant technique de l'utilisateur */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id_utilisateur")
	
	protected Long idUtilisateur;
	
	/** Nom affiche pour l'utilisateur */
	@Column(name = "nom")
	
	protected String nom;
	
	/** email de l'utilisateur */
	@Column(name = "email")
	
	protected String email;
	
	/** mot de passe de connexion */
	@Column(name = "mot_de_passe")
	
	protected String motDePasse;
	
	/** date de creation de l'utilisateur */
	@Column(name = "date_creation")
	
	protected Date dateCreation;
	
	{/* GETTERS & SETTERS */}

	public Long getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(Long idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

}