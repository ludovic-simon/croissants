package fr.forfun.croissants.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * tour indiquant quelle personne ramene a quelle date
 */
@Entity
@Table(name="tour")
@XmlRootElement
public class Tour implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	{/* CHAMPS */}
	
	/** Identifiant technique du tour */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id_tour")
	protected Long idTour;
	
	/** Identifiant du groupe */
	@Column(name = "id_groupe")
	protected Long idGroupe;
	
	/** Identifiant de l'utilisateur devant ramener */
	@Column(name = "id_utilisateur")
	protected Long idUtilisateur;
	
	/** Nom du tour etant par defaut celui de l'utilisateur mais gere le cas ou l'utilisateur est supprime */
	@Column(name = "nom_tour")
	protected String nomTour;
	
	/** Date d'occurence du tour */
	@Column(name = "date_tour")
	protected Date dateTour;
	
	/** Statut du tour */
	@Enumerated(EnumType.STRING)
	@Column(name = "statut_tour")
	protected StatutTour statutTour;
	
	{/* GETTERS & SETTERS */}

	public Long getIdTour() {
		return idTour;
	}

	public void setIdTour(Long idTour) {
		this.idTour = idTour;
	}

	public Long getIdGroupe() {
		return idGroupe;
	}

	public void setIdGroupe(Long idGroupe) {
		this.idGroupe = idGroupe;
	}

	public Long getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(Long idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public String getNomTour() {
		return nomTour;
	}

	public void setNomTour(String nomTour) {
		this.nomTour = nomTour;
	}

	public Date getDateTour() {
		return dateTour;
	}

	public void setDateTour(Date dateTour) {
		this.dateTour = dateTour;
	}

	public StatutTour getStatutTour() {
		return statutTour;
	}

	public void setStatutTour(StatutTour statutTour) {
		this.statutTour = statutTour;
	}

}