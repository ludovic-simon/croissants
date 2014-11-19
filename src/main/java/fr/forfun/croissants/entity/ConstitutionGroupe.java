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
 * Liaison entre groupes et utilisateurs
 */
@Entity
@Table(name="constitution_groupe")
@XmlRootElement
public class ConstitutionGroupe implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	{/* CHAMPS */}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id_constitution_groupe")
	protected Long idConstitutionGroupe;
	
	/** Identifiant du groupe lie */
	@Column(name = "id_groupe")
	protected Long idGroupe;
	
	/** identifiant de l'utilisateur lie */
	@Column(name = "id_utilisateur")
	protected Long idUtilisateur;
	
	/** Indique si l'utilisateur est administrateur dans ce groupe */
	@Column(name = "admin")
	protected boolean admin;
	
	/** Indique si le groupe est le groupe par defaut de l'utilisateur */
	@Column(name = "par_defaut")
	protected boolean parDefaut;
	
	/** Date de sortie du groupe pour l'utilisateur (suppression logique) */
	@Column(name = "date_sortie_groupe")
	protected Date dateSortieGroupe;
	
	{/* GETTERS & SETTERS */}

	public Long getIdConstitutionGroupe() {
		return idConstitutionGroupe;
	}

	public void setIdConstitutionGroupe(Long idConstitutionGroupe) {
		this.idConstitutionGroupe = idConstitutionGroupe;
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

	public boolean getAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean getParDefaut() {
		return parDefaut;
	}

	public void setParDefaut(boolean parDefaut) {
		this.parDefaut = parDefaut;
	}

	public Date getDateSortieGroupe() {
		return dateSortieGroupe;
	}

	public void setDateSortieGroupe(Date dateSortieGroupe) {
		this.dateSortieGroupe = dateSortieGroupe;
	}

}