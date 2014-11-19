package fr.forfun.croissants.service;

import java.util.ArrayList;
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

import fr.forfun.croissants.core.BusinessException;
import fr.forfun.croissants.entity.ConstitutionGroupe;
import fr.forfun.croissants.entity.ConstitutionGroupe_;
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
	 * 
	 * @return la constitution de groupe
	 */
	public ConstitutionGroupe rejoindreGroupe(Long idUtilisateur, Long idGroupe) {
		//Controles de surface
		//L'identifiant utilisateur est obligatoire
		if(idUtilisateur == null){
			throw new BusinessException("L'identifiant utilisateur est obligatoire");
		}
		//L'identifiant de groupe est obligatoire
		if(idGroupe == null){
			throw new BusinessException("L'identifiant de groupe est obligatoire");
		}
		return null;
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
					throw new BusinessException("Aucun utilisateur pour l'identifiant");
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