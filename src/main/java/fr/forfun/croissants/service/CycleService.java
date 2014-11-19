package fr.forfun.croissants.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import fr.forfun.croissants.core.BusinessException;
import fr.forfun.croissants.entity.ConstitutionGroupe;
import fr.forfun.croissants.entity.ConstitutionGroupe_;
import fr.forfun.croissants.entity.Groupe;
import fr.forfun.croissants.entity.Utilisateur;

public class CycleService {

	protected EntityManagerFactory emf;
	
	public CycleService(){
		emf = Persistence.createEntityManagerFactory("croissants");
	}
	
	public CycleService(String persistenceUnit){
		emf = Persistence.createEntityManagerFactory(persistenceUnit);
	}
	
	{/* SERVICES */}
	
	/**
	 * Recherche les groupes d'un utilisateur
	 * @param idUtilisateur	Identifiant de l'utilisateur
	 * 
	 * @return les groupes de l'utilisateur
	 */
	public List<ConstitutionGroupe> rechercherGroupesUtilisateur(Long idUtilisateur) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			CriteriaBuilder qb = em.getCriteriaBuilder();
			controleUtilisateurExistant(idUtilisateur, false);
			//Recherche des constitution groupe pour l'utilisateur
			CriteriaQuery<ConstitutionGroupe> constitutionGroupeTableCriteriaQuery = qb.createQuery(ConstitutionGroupe.class);
			Root<ConstitutionGroupe> constitutionGroupeTable = constitutionGroupeTableCriteriaQuery.from(ConstitutionGroupe.class);
			List<Predicate> constitutionGroupePredicates = new ArrayList<Predicate>();
			constitutionGroupePredicates.add(qb.equal(constitutionGroupeTable.get(ConstitutionGroupe_.idUtilisateur), idUtilisateur));
			if(constitutionGroupePredicates.size() > 0){
				constitutionGroupeTableCriteriaQuery.where(constitutionGroupePredicates.toArray(new Predicate[constitutionGroupePredicates.size()]));
			}
			TypedQuery<ConstitutionGroupe> constitutionGroupeTableQuery = em.createQuery(constitutionGroupeTableCriteriaQuery);
			List<ConstitutionGroupe> constitutionsGroupes = constitutionGroupeTableQuery.getResultList();
			return constitutionsGroupes;
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive()){tx.rollback();}
			txError = true;
			throw e;
		} finally {
			if(!txError){tx.commit();}
			em.close();
		}
	}

	/**
	 * Permet a un utilisateur de rejoindre un groupe existant
	 * @param idUtilisateur	Identifiant de l'utilisateur
	 * @param idGroupe	Identifiant du groupe
	 * @param jeton	Jeton du groupe
	 * @param motDePasse	Mot de passe du groupe
	 * 
	 * @return la constitution de groupe
	 */
	public ConstitutionGroupe rejoindreGroupe(Long idUtilisateur, Long idGroupe, String jeton, String motDePasse) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			//Controles de surface et d'existance
			controleUtilisateurExistant(idUtilisateur, false);
			controleGroupeExistant(idGroupe, false);
			//Le jeton du groupe est obligatoire
			if(StringUtils.isEmpty(jeton)){
				throw new BusinessException("Le jeton du groupe est obligatoire");
			}
			//Le mot de passe du groupe est obligatoire
			if(StringUtils.isEmpty(motDePasse)){
				throw new BusinessException("Le mot de passe du groupe est obligatoire");
			}
			//Controle du couple jeton / mot de passe pour le groupe
			Groupe groupe = em.find(Groupe.class, idGroupe);
			if(!groupe.getJeton().equals(jeton) || !groupe.getMotDePasse().equals(motDePasse)){
				throw new BusinessException("Le couple jeton et mot du passe du groupe est incorrect");
			}
			//Creation d'une constitution groupe pour l'utilisateur et le groupe
			ConstitutionGroupe constitutionGroupe = new ConstitutionGroupe();
			constitutionGroupe.setIdGroupe(idGroupe);
			constitutionGroupe.setIdUtilisateur(idUtilisateur);
			constitutionGroupe.setDateArriveeGroupe(new Date());
			//Persistence en base
			em.persist(constitutionGroupe);
			return constitutionGroupe;
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive()){tx.rollback();}
			txError = true;
			throw e;
		} finally {
			if(!txError){tx.commit();}
			em.close();
		}
	}

	/**
	 * Permet de creer un groupe
	 * @param idUtilisateur	Utilisateur creant le groupe
	 * @param nomGroupe	Nom du groupe
	 * 
	 * @return Le groupe cree
	 */
	public Groupe creerGroupe(Long idUtilisateur, String nomGroupe) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			//Controles de surface
			controleUtilisateurExistant(idUtilisateur, false);
			//Le nom du groupe doit etre renseigne
			if(StringUtils.isEmpty(nomGroupe)){
				throw new BusinessException("Le nom du groupe doit etre renseigne");
			}
			//Creation et persistence du groupe
			//Determination d'un jeton aleatoire pour le groupe
			String jeton = RandomStringUtils.randomAlphanumeric(10);
			//Determination d'un mot de passe aleatoire pour le groupe
			String motDePasse = RandomStringUtils.randomAlphanumeric(10);
			Groupe groupe = new Groupe();
			groupe.setNom(nomGroupe);
			groupe.setJeton(jeton);
			groupe.setMotDePasse(motDePasse);
			groupe.setDateCreation(new Date());
			em.persist(groupe);
			//Creation d'une constitution de groupe pour ce groupe et l'utilisateur et affectation du droit administrateur
			ConstitutionGroupe constitutionGroupe = new ConstitutionGroupe();
			constitutionGroupe.setIdGroupe(groupe.getIdGroupe());
			constitutionGroupe.setIdUtilisateur(idUtilisateur);
			constitutionGroupe.setAdmin(true);
			constitutionGroupe.setDateArriveeGroupe(new Date());
			em.persist(constitutionGroupe);
			return groupe;
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive()){tx.rollback();}
			txError = true;
			throw e;
		} finally {
			if(!txError){tx.commit();}
			em.close();
		}
	}

	/**
	 * Permet de modifier le droit d'administrateur d'un utilisateur sur un groupe
	 * @param idUtilisateur	Identifiant de l'utilisateur
	 * @param idGroupe	Identifiant du groupe
	 * @param admin	Droit d'administration de l'utilisateur sur le groupe
	 * 
	 * @return La constitution de groupe correspondante apres la mise a jour
	 */
	public ConstitutionGroupe affecterDroitAdministrateur(Long idUtilisateur, Long idGroupe, boolean admin) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			CriteriaBuilder qb = em.getCriteriaBuilder();
			//Controles de surface et d'existance
			controleUtilisateurExistant(idUtilisateur, false);
			controleGroupeExistant(idGroupe, false);
			//Recuperation de la constitution de groupe
			CriteriaQuery<ConstitutionGroupe> constitutionGroupeTableCriteriaQuery = qb.createQuery(ConstitutionGroupe.class);
			Root<ConstitutionGroupe> constitutionGroupeTable = constitutionGroupeTableCriteriaQuery.from(ConstitutionGroupe.class);
			List<Predicate> constitutionGroupePredicates = new ArrayList<Predicate>();
			constitutionGroupePredicates.add(qb.equal(constitutionGroupeTable.get(ConstitutionGroupe_.idUtilisateur), idUtilisateur));
			constitutionGroupePredicates.add(qb.equal(constitutionGroupeTable.get(ConstitutionGroupe_.idGroupe), idGroupe));
			if(constitutionGroupePredicates.size() > 0){
				constitutionGroupeTableCriteriaQuery.where(constitutionGroupePredicates.toArray(new Predicate[constitutionGroupePredicates.size()]));
			}
			TypedQuery<ConstitutionGroupe> constitutionGroupeTableQuery = em.createQuery(constitutionGroupeTableCriteriaQuery);
			List<ConstitutionGroupe> constitutionsGroupes = constitutionGroupeTableQuery.getResultList();
			//Cas ou l'utilisateur ne fait pas partie du groupe
			if(CollectionUtils.isEmpty(constitutionsGroupes)){
				throw new BusinessException("L'utilisateur " + idUtilisateur + " ne fait pas partie du groupe " + idGroupe);
			}
			//Affectation du droit admin a la constitution de groupe
			ConstitutionGroupe constitutionGroupe = constitutionsGroupes.get(0);
			constitutionGroupe.setAdmin(admin);
			constitutionGroupe = em.merge(constitutionGroupe);
			return constitutionGroupe;
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive()){tx.rollback();}
			txError = true;
			throw e;
		} finally {
			if(!txError){tx.commit();}
			em.close();
		}
	}

	/**
	 * Controle l'existance d'un utilisateur
	 * @param idUtilisateur	Identifiant de l'utilisateur
	 * @param autoriseNull	Indique si la valeur null est autorisee
	 */
	protected void controleUtilisateurExistant(Long idUtilisateur, boolean autoriseNull) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			//Cas ou l'identifiant utilisateur devrait etre renseigne
			if(!autoriseNull && idUtilisateur == null){
				throw new BusinessException("L'identifiant utilisateur est obligatoire");
			}
			//Controle de l'existance de l'utilisateur
			if(idUtilisateur != null){
				Utilisateur utilisateur = em.find(Utilisateur.class, idUtilisateur);
				if(utilisateur == null){
					throw new BusinessException("Aucun utilisateur pour l'identifiant " + idUtilisateur);
				}
			}
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive()){tx.rollback();}
			txError = true;
			throw e;
		} finally {
			if(!txError){tx.commit();}
			em.close();
		}
	}

	/**
	 * Controle l'existance d'un groupe
	 * @param idGroupe	Identifiant du groupe
	 * @param autoriseNull	Indique si l'identifiant de groupe peut etre null
	 */
	protected void controleGroupeExistant(Long idGroupe, boolean autoriseNull) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			//Cas ou l'identifiant du groupe devrait etre renseigne
			if(!autoriseNull && idGroupe == null){
				throw new BusinessException("Cas ou l'identifiant du groupe devrait etre renseigne");
			}
			//Controle de l'existance du groupe
			if(idGroupe != null){
				Groupe groupe = em.find(Groupe.class, idGroupe);
				if(groupe == null){
					throw new BusinessException("Aucun groupe pour l'identifiant " + idGroupe);
				}
			}
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive()){tx.rollback();}
			txError = true;
			throw e;
		} finally {
			if(!txError){tx.commit();}
			em.close();
		}
	}

}