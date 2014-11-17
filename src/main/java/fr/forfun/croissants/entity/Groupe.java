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
 * Groupe d'utilisateurs
 */
@Entity
@Table(name="groupe")
@XmlRootElement
public class Groupe implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	{/* CHAMPS */}
	
	/** Identifiant technique du groupe */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id_groupe")
	
	protected Long idGroupe;
	
	/** Cle technique pour joindre le groupe */
	@Column(name = "jeton")
	
	protected String jeton;
	
	/** Mot de passe pour rejoindre le groupe */
	@Column(name = "mot_de_passe")
	
	protected String motDePasse;
	
	/** Date de creation du groupe */
	@Column(name = "date_creation")
	
	protected Date dateCreation;
	
	{/* GETTERS & SETTERS */}

	public Long getIdGroupe() {
		return idGroupe;
	}

	public void setIdGroupe(Long idGroupe) {
		this.idGroupe = idGroupe;
	}

	public String getJeton() {
		return jeton;
	}

	public void setJeton(String jeton) {
		this.jeton = jeton;
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