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
 * Historisation d'une action
 */
@Entity
@Table(name="historique")
@XmlRootElement
public class Historique implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	{/* CHAMPS */}
	
	/** Identifiant technique */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id_historique_action")
	protected Long idHistorique;
	
	/** Date de l'action */
	@Column(name = "date_action")
	protected Date dateAction;
	
	/** Identifiant de l'utilisateur provoquant l'action */
	@Column(name = "id_utilisateur_action")
	protected Long idUtilisateurAction;
	
	/** Domaine de cette action d'historique */
	@Enumerated(EnumType.STRING)
	@Column(name = "historique_domaine")
	protected HistoriqueDomaine historiqueDomaine;
	
	/** Reference liee au domaine, ex id du groupe pour une action de groupe */
	@Column(name = "reference")
	protected String reference;
	
	/** Description de l'action */
	@Column(name = "action")
	protected String action;
	
	/** Indique si l'action est faite par un super admin (via batch ou transactionnel) */
	@Column(name = "is_super_admin")
	protected boolean isSuperAdmin;
	
	{/* GETTERS & SETTERS */}

	public Long getIdHistorique() {
		return idHistorique;
	}

	public void setIdHistorique(Long idHistorique) {
		this.idHistorique = idHistorique;
	}

	public Date getDateAction() {
		return dateAction;
	}

	public void setDateAction(Date dateAction) {
		this.dateAction = dateAction;
	}

	public Long getIdUtilisateurAction() {
		return idUtilisateurAction;
	}

	public void setIdUtilisateurAction(Long idUtilisateurAction) {
		this.idUtilisateurAction = idUtilisateurAction;
	}

	public HistoriqueDomaine getHistoriqueDomaine() {
		return historiqueDomaine;
	}

	public void setHistoriqueDomaine(HistoriqueDomaine historiqueDomaine) {
		this.historiqueDomaine = historiqueDomaine;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

}