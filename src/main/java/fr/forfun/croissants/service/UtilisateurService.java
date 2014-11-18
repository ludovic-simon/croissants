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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.forfun.croissants.core.BusinessException;
import fr.forfun.croissants.entity.Utilisateur;
import fr.forfun.croissants.entity.Utilisateur_;

/**
 * Services lie a l'utilisateur et aux groupes
 */
public class UtilisateurService {

	protected EntityManagerFactory emf;
	
	public UtilisateurService(){
		emf = Persistence.createEntityManagerFactory("croissants");
	}
	
	public UtilisateurService(String persistenceUnit){
		emf = Persistence.createEntityManagerFactory(persistenceUnit);
	}
	
	{/* SERVICES */}
	
	/**
	 * @param email	
	 * @param motDePasse	
	 * 
	 * @return L'utilisateur connecte ou une exception sinon
	 */
	public Utilisateur connecterUtilisateur(String email, String motDePasse) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			CriteriaBuilder qb = em.getCriteriaBuilder();
			//Controles de surface
			//L'email est obligatoire
			if(StringUtils.isEmpty(email)){
				throw new BusinessException("L'email est obligatoire");
			}
			//Le mot de passe est obligatoire
			if(StringUtils.isEmpty(motDePasse)){
				throw new BusinessException("Le mot de passe est obligatoire");
			}
			//Recuperation de l'utilisateur pour l'email et mot de passe
			CriteriaQuery<Utilisateur> utilisateurCriteriaQuery = qb.createQuery(Utilisateur.class);
			Root<Utilisateur> utilisateur = utilisateurCriteriaQuery.from(Utilisateur.class);
			List<Predicate> utilisateurPredicates = new ArrayList<Predicate>();
			utilisateurPredicates.add(qb.equal(utilisateur.get(Utilisateur_.email), email));
			utilisateurPredicates.add(qb.equal(utilisateur.get(Utilisateur_.motDePasse), motDePasse));
			if(utilisateurPredicates.size() > 0){
				utilisateurCriteriaQuery.where(utilisateurPredicates.toArray(new Predicate[utilisateurPredicates.size()]));
			}
			TypedQuery<Utilisateur> utilisateurQuery = em.createQuery(utilisateurCriteriaQuery);
			List<Utilisateur> utilisateurs = utilisateurQuery.getResultList();
			//Cas ou aucun utilisateur n'est trouve
			if(CollectionUtils.isEmpty(utilisateurs)){
				throw new BusinessException("Aucun utilisateur pour cet email / mot de passe");
			}
			//Retour du 1er utilisateur (normalement il n'y en a qu'un)
			return utilisateurs.get(0);
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
	 * Creer un utilisateur
	 * @param utilisateur
	 */
	public Utilisateur creerUtilisateur(Utilisateur utilisateur) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			CriteriaBuilder qb = em.getCriteriaBuilder();
			//Controles de surface
			//Le mail est obligatoire
			if(StringUtils.isEmpty(utilisateur.getEmail())){
				throw new BusinessException("Le mail est obligatoire");
			}
			//Le mot de passe est obligatoire
			if(StringUtils.isEmpty(utilisateur.getMotDePasse())){
				throw new BusinessException("Le mot de passe est obligatoire");
			}
			//Le nom est obligatoire
			if(StringUtils.isEmpty(utilisateur.getNom())){
				throw new BusinessException("Le nom est obligatoire");
			}
			//Test de l'existance d'un utilisateur avec cet email
			CriteriaQuery<Utilisateur> utilisateurTableCriteriaQuery = qb.createQuery(Utilisateur.class);
			Root<Utilisateur> utilisateurTable = utilisateurTableCriteriaQuery.from(Utilisateur.class);
			List<Predicate> utilisateurPredicates = new ArrayList<Predicate>();
			utilisateurPredicates.add(qb.equal(utilisateurTable.get(Utilisateur_.email), utilisateur.getEmail()));
			if(utilisateurPredicates.size() > 0){
				utilisateurTableCriteriaQuery.where(utilisateurPredicates.toArray(new Predicate[utilisateurPredicates.size()]));
			}
			TypedQuery<Utilisateur> utilisateurTableQuery = em.createQuery(utilisateurTableCriteriaQuery);
			List<Utilisateur> utilisateursPourEmail = utilisateurTableQuery.getResultList();
			if(CollectionUtils.isNotEmpty(utilisateursPourEmail)){
				throw new BusinessException("Il y a deja un utilisateur enregistre pour cet email");
			}
			//Persistence de l'utilisateur
			em.persist(utilisateur);
			return utilisateur;
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
	 * Recuperation de l'utilisateur connecte
	 * @return L'utilisateur connecte ou une exception si aucun utilisateur
	 */
	public Utilisateur getUtilisateurSession() {
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setIdUtilisateur(1L);
		utilisateur.setNom("Toto");
		utilisateur.setEmail("toto@gmail.com");
		return utilisateur;
	}

}