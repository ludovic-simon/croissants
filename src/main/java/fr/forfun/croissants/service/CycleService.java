package fr.forfun.croissants.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import fr.forfun.croissants.CycleUtils;
import fr.forfun.croissants.DateUtils;
import fr.forfun.croissants.core.AppUtils;
import fr.forfun.croissants.core.BusinessException;
import fr.forfun.croissants.entity.ConstitutionGroupe;
import fr.forfun.croissants.entity.ConstitutionGroupe_;
import fr.forfun.croissants.entity.Groupe;
import fr.forfun.croissants.entity.Groupe_;
import fr.forfun.croissants.entity.StatutTour;
import fr.forfun.croissants.entity.Tour;
import fr.forfun.croissants.entity.Tour_;
import fr.forfun.croissants.entity.Utilisateur;
import fr.forfun.croissants.entity.Utilisateur_;

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
	 * Permet de recuperer la constitution groupe pour le groupe et l'utilisateur
	 * @param idUtilisateur	L'identifiant de l'utilisateur
	 * @param idGroupe	L'identifiant du groupe
	 * 
	 * @return La constitution de groupe
	 */
	public ConstitutionGroupe rechercherConstitutionGroupe(Long idUtilisateur, Long idGroupe) {
		ConstitutionGroupe constitutionGroupe = controleConstitutionGroupeEtUtilisateur(idUtilisateur, idGroupe);
		return constitutionGroupe;
	}

	/**
	 * Recherche d'un groupe par son identifiant
	 * @param idGroupe	Identifiant du groupe
	 * 
	 * @return Le groupe pour l'identifiant
	 */
	public Groupe rechercherGroupeParId(Long idGroupe) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			//Controle de l'existance du groupe
			controleGroupeExistant(idGroupe, false);
			//Recuperation du groupe pour l'identifiant
			Groupe groupe = em.find(Groupe.class, idGroupe);
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
	 * Recherche les constitution de groupe des utilisateurs de ce groupe
	 * @param idGroupe	Identifiant du groupe
	 * 
	 * @return Les constitution de groupe de ce groupe
	 */
	public List<ConstitutionGroupe> rechercherUtilisateursGroupe(Long idGroupe) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			CriteriaBuilder qb = em.getCriteriaBuilder();
			controleGroupeExistant(idGroupe, false);
			//Recherche des constitution groupe pour cet id de groupe
			CriteriaQuery<ConstitutionGroupe> constitutionGroupeIteCriteriaQuery = qb.createQuery(ConstitutionGroupe.class);
			Root<ConstitutionGroupe> constitutionGroupeIte = constitutionGroupeIteCriteriaQuery.from(ConstitutionGroupe.class);
			List<Predicate> constitutionGroupePredicates = new ArrayList<Predicate>();
			if(idGroupe != null){
				constitutionGroupePredicates.add(qb.equal(constitutionGroupeIte.get(ConstitutionGroupe_.idGroupe), idGroupe));
			}
			if(constitutionGroupePredicates.size() > 0){
				constitutionGroupeIteCriteriaQuery.where(constitutionGroupePredicates.toArray(new Predicate[constitutionGroupePredicates.size()]));
			}
			TypedQuery<ConstitutionGroupe> constitutionGroupeIteQuery = em.createQuery(constitutionGroupeIteCriteriaQuery);
			List<ConstitutionGroupe> constitutionGroupes = constitutionGroupeIteQuery.getResultList();
			return constitutionGroupes;
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
	 * @param jeton	Jeton du groupe
	 * @param motDePasse	Mot de passe du groupe
	 * 
	 * @return la constitution de groupe
	 */
	public ConstitutionGroupe rejoindreGroupe(Long idUtilisateur, String jeton, String motDePasse) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			CriteriaBuilder qb = em.getCriteriaBuilder();
			//Controles de surface et d'existance
			controleUtilisateurExistant(idUtilisateur, false);
			//Le jeton du groupe est obligatoire
			if(StringUtils.isEmpty(jeton)){
				throw new BusinessException("Le jeton du groupe est obligatoire");
			}
			//Le mot de passe du groupe est obligatoire
			if(StringUtils.isEmpty(motDePasse)){
				throw new BusinessException("Le mot de passe du groupe est obligatoire");
			}
			//Controle du couple jeton / mot de passe pour le groupe
			//Recuperation du groupe pour le jeton et le mot de passe
			CriteriaQuery<Groupe> groupeIteCriteriaQuery = qb.createQuery(Groupe.class);
			Root<Groupe> groupeIte = groupeIteCriteriaQuery.from(Groupe.class);
			List<Predicate> groupePredicates = new ArrayList<Predicate>();
			groupePredicates.add(qb.equal(groupeIte.get(Groupe_.jeton), jeton));
			groupePredicates.add(qb.equal(groupeIte.get(Groupe_.motDePasse), motDePasse));
			if(groupePredicates.size() > 0){
				groupeIteCriteriaQuery.where(groupePredicates.toArray(new Predicate[groupePredicates.size()]));
			}
			TypedQuery<Groupe> groupeIteQuery = em.createQuery(groupeIteCriteriaQuery);
			Groupe groupePourJeton = AppUtils.first(groupeIteQuery.getResultList());
			if(groupePourJeton == null){
				throw new BusinessException("Le couple jeton et mot du passe du groupe est incorrect");
			}
			//Controle que l'utilisateur ne fait pas deja partie de ce groupe
			CriteriaQuery<ConstitutionGroupe> constitutionGroupeIteCriteriaQuery = qb.createQuery(ConstitutionGroupe.class);
			Root<ConstitutionGroupe> constitutionGroupeIte = constitutionGroupeIteCriteriaQuery.from(ConstitutionGroupe.class);
			List<Predicate> constitutionGroupePredicates = new ArrayList<Predicate>();
			constitutionGroupePredicates.add(qb.equal(constitutionGroupeIte.get(ConstitutionGroupe_.idUtilisateur), idUtilisateur));
			constitutionGroupePredicates.add(qb.equal(constitutionGroupeIte.get(ConstitutionGroupe_.idGroupe), groupePourJeton.getIdGroupe()));
			if(constitutionGroupePredicates.size() > 0){
				constitutionGroupeIteCriteriaQuery.where(constitutionGroupePredicates.toArray(new Predicate[constitutionGroupePredicates.size()]));
			}
			TypedQuery<ConstitutionGroupe> constitutionGroupeIteQuery = em.createQuery(constitutionGroupeIteCriteriaQuery);
			ConstitutionGroupe constitutionGroupeExistante = AppUtils.first(constitutionGroupeIteQuery.getResultList());
			if(constitutionGroupeExistante != null){
				throw new BusinessException("Vous faites deja partie du groupe " + groupePourJeton.getNom());
			}
			//Creation d'une constitution groupe pour l'utilisateur et le groupe
			ConstitutionGroupe constitutionGroupe = new ConstitutionGroupe();
			constitutionGroupe.setIdGroupe(groupePourJeton.getIdGroupe());
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
	 * Supprime un utilisateur d'un groupe
	 * @param idUtilisateur	Identifiant de l'utilisateur
	 * @param idGroupe	Identifiant du groupe
	 */
	public void quitterGroupe(Long idUtilisateur, Long idGroupe) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			ConstitutionGroupe constitutionGroupe = controleConstitutionGroupeEtUtilisateur(idUtilisateur, idGroupe);
			//Suppression de la constitution de groupe
			em.remove(em.find(ConstitutionGroupe.class, constitutionGroupe.getIdConstitutionGroupe()));
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
	 * @param groupe	Le groupe a editer
	 * 
	 * @return Le groupe cree
	 */
	public Groupe editerGroupe(Long idUtilisateur, Groupe groupe) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			//Controles de surface
			controleUtilisateurExistant(idUtilisateur, false);
			//Le groupe est obligatoire
			if(groupe == null){
				throw new BusinessException("Le groupe est obligatoire");
			}
			//Controle de l'existance du groupe en modification
			controleGroupeExistant(groupe.getIdGroupe(), true);
			//Le nom du groupe doit etre renseigne
			if(StringUtils.isEmpty(groupe.getNom())){
				throw new BusinessException("Le nom du groupe doit etre renseigne");
			}
			//Gestion de l'ajout ou modification du groupe
			if(groupe.getIdGroupe() != null){
				//Recuperation du groupe en base
				Groupe groupeBdd = em.find(Groupe.class, groupe.getIdGroupe());
				//Modification du nom, message et jour
				groupeBdd.setNom(groupe.getNom());
				groupeBdd.setMessage(groupe.getMessage());
				groupeBdd.setJourOccurence(groupe.getJourOccurence());
				//Merge du groupe en base avec les modifs saisies
				groupe = em.merge(groupeBdd);
			} else {
				//Gestion du jeton et mdp et persistence du groupe
				//Determination d'un jeton aleatoire pour le groupe
				String jeton = "" + new Date().getTime();
				//Determination d'un mot de passe aleatoire pour le groupe
				String motDePasse = RandomStringUtils.randomAlphanumeric(10);
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
			}
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
	 * Permet d'affecter un groupe par defaut a un utilisateur
	 * @param idUtilisateur	Identifiant de l'utilisateur
	 * @param idGroupe	Identifiant du groupe
	 * 
	 * @return La constitution de groupe modifiee
	 */
	public ConstitutionGroupe affecterGoupeParDefaut(Long idUtilisateur, Long idGroupe) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			//Controles de surface
			ConstitutionGroupe constitutionGroupe = controleConstitutionGroupeEtUtilisateur(idUtilisateur, idGroupe);
			//Les constitutions de groupe de l'utilisateur devienne par defaut a false
			Query query = em.createQuery(
				"UPDATE ConstitutionGroupe SET parDefaut = false " +
				"WHERE idUtilisateur = :idUtilisateur");
			query.setParameter("idUtilisateur", idUtilisateur);
			query.executeUpdate();
			//Passage a par defaut a true et merge de la constitutionGroupe
			constitutionGroupe.setParDefaut(true);
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
	 * Permet a un administrateur de supprimer un groupe
	 * @param idUtilisateur	Identifiant de l'utilisateur sollicitant la suppression du groupe
	 * @param idGroupe	Identifiant du groupe
	 */
	public void supprimerGroupe(Long idUtilisateur, Long idGroupe) {
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
			//Controle que l'utilisateur est administrateur du groupe
			CriteriaQuery<ConstitutionGroupe> constitutionGroupeTableCriteriaQuery = qb.createQuery(ConstitutionGroupe.class);
			Root<ConstitutionGroupe> constitutionGroupeTable = constitutionGroupeTableCriteriaQuery.from(ConstitutionGroupe.class);
			List<Predicate> constitutionGroupePredicates = new ArrayList<Predicate>();
			constitutionGroupePredicates.add(qb.equal(constitutionGroupeTable.get(ConstitutionGroupe_.idUtilisateur), idUtilisateur));
			constitutionGroupePredicates.add(qb.equal(constitutionGroupeTable.get(ConstitutionGroupe_.idGroupe), idGroupe));
			if(constitutionGroupePredicates.size() > 0){
				constitutionGroupeTableCriteriaQuery.where(constitutionGroupePredicates.toArray(new Predicate[constitutionGroupePredicates.size()]));
			}
			TypedQuery<ConstitutionGroupe> constitutionGroupeTableQuery = em.createQuery(constitutionGroupeTableCriteriaQuery);
			ConstitutionGroupe constitutionGroupe = AppUtils.first(constitutionGroupeTableQuery.getResultList());
			//Cas ou l'utilisateur ne fait pas partie du groupe
			if(constitutionGroupe == null){
				throw new BusinessException("L'utilisateur " + idUtilisateur + " ne fait pas partie du groupe " + idGroupe);
			}
			//Cas ou l'utilisateur n'est pas administrateur du groupe
			if(!constitutionGroupe.getAdmin()){
				throw new BusinessException("L'utilisateur n'est pas administrateur du groupe, il ne peut pas le supprimer");
			}
			//Suppression du groupe
			em.remove(em.find(Groupe.class, idGroupe));
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
	 * Calcule les tours du groupe pour que le cycle en cours comporte une fois chaque utilisateur
	 * @param idGroupe	L'identifiant du groupe pour lequel calculer le cycle
	 * 
	 * @return L'action effectuee lors du calcul du cycle
	 */
	public String calculerProchainCycle(Long idGroupe) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			CriteriaBuilder qb = em.getCriteriaBuilder();
			//Controle de surface
			controleGroupeExistant(idGroupe, false);
			//Determination des utilisateurs du groupe et de leur nombre
			//Recuperation des utilisateurs du groupe
			CriteriaQuery<Utilisateur> utilisateurIteCriteriaQuery = qb.createQuery(Utilisateur.class);
			Root<Utilisateur> utilisateurIte = utilisateurIteCriteriaQuery.from(Utilisateur.class);
			ListJoin<Utilisateur, ConstitutionGroupe> constitutionGroupeJoin = utilisateurIte.join(Utilisateur_.constitutionGroupes); 
			List<Predicate> utilisateurPredicates = new ArrayList<Predicate>();
			utilisateurPredicates.add(qb.equal(constitutionGroupeJoin.get(ConstitutionGroupe_.idGroupe), idGroupe));
			if(utilisateurPredicates.size() > 0){
				utilisateurIteCriteriaQuery.where(utilisateurPredicates.toArray(new Predicate[utilisateurPredicates.size()]));
			}
			List<Order> utilisateurIteOrderBy = new ArrayList<Order>();
			utilisateurIteOrderBy.add(qb.asc(utilisateurIte.get(Utilisateur_.nom)));
			if(utilisateurIteOrderBy.size() > 0){
				utilisateurIteCriteriaQuery.orderBy(utilisateurIteOrderBy);
			}
			TypedQuery<Utilisateur> utilisateurIteQuery = em.createQuery(utilisateurIteCriteriaQuery);
			List<Utilisateur> utilisateursDuGroupe = utilisateurIteQuery.getResultList();
			//Cas ou il n'y a pas d'utilisateur dans le groupe, il n'y a aucun cycle a generer
			if(CollectionUtils.isEmpty(utilisateursDuGroupe)){
				return "Il n'y a pas d'utilisateur dans ce groupe donc aucun cycle a generer";
			}
			int nbUtilisateurs = utilisateursDuGroupe.size();
			//Recuperation des derniers tours pour le groupe, recupere autant de tour que d'utilisateurs
			CriteriaQuery<Tour> tourIteCriteriaQuery = qb.createQuery(Tour.class);
			Root<Tour> tourIte = tourIteCriteriaQuery.from(Tour.class);
			List<Predicate> tourPredicates = new ArrayList<Predicate>();
			tourPredicates.add(qb.equal(tourIte.get(Tour_.idGroupe), idGroupe));
			if(tourPredicates.size() > 0){
				tourIteCriteriaQuery.where(tourPredicates.toArray(new Predicate[tourPredicates.size()]));
			}
			List<Order> tourIteOrderBy = new ArrayList<Order>();
			tourIteOrderBy.add(qb.desc(tourIte.get(Tour_.dateTour)));
			if(tourIteOrderBy.size() > 0){
				tourIteCriteriaQuery.orderBy(tourIteOrderBy);
			}
			TypedQuery<Tour> tourIteQuery = em.createQuery(tourIteCriteriaQuery);
			tourIteQuery.setMaxResults(nbUtilisateurs);
			List<Tour> derniersTours = tourIteQuery.getResultList();
			Collections.reverse(derniersTours);
			//Determination des tours existants en cours et de l'ordre des utilisateurs du precedent cycle
			List<Tour> toursExistantsEnCours = new ArrayList<Tour>();
			Date dateJour = DateUtils.getCurrentDayDate();
			Map<Long, Tour> mapUtilisateurTourExistant = new HashMap<Long, Tour>();
			final Map<Long, Integer> mapUtilisateurOrdre = new HashMap<Long, Integer>();
			int ordre = 1;
			if(derniersTours != null){
				for(Tour tour : derniersTours) {
					if(tour.getDateTour().after(dateJour)||tour.getDateTour().equals(dateJour)){
						toursExistantsEnCours.add(tour);
						mapUtilisateurTourExistant.put(tour.getIdUtilisateur(), tour);
					}
					mapUtilisateurOrdre.put(tour.getIdUtilisateur(), ordre);
					ordre++;
				}
			}
			//Determination des utilisateurs n'ayant pas de tour
			List<Utilisateur> utilisateursSansTours = new ArrayList<Utilisateur>();
			if(utilisateursDuGroupe != null){
				for(Utilisateur utilisateur : utilisateursDuGroupe) {
					if(!mapUtilisateurTourExistant.containsKey(utilisateur.getIdUtilisateur())){
						utilisateursSansTours.add(utilisateur);
					}
				}
			}
			//Cas ou il n'y a pas d'utilisateurs sans tour, le cycle est deja calcule
			if(CollectionUtils.isEmpty(utilisateursSansTours)){
				return "il n'y a pas d'utilisateur sans tour, le cycle est a jour";
			}
			//Tri des utilisateurs avec d'abord ceux qui n'ont pas de tour par ordre alphab�tique puis ceux qui
			//ont un tour par ordre du tour
			Collections.sort(utilisateursSansTours, new Comparator<Utilisateur>() {
				@Override
				public int compare(Utilisateur o1, Utilisateur o2) {
					//Determination du cas d'un nouvel utilisateur ou d'un utilisateur ayant deja un tour
					Integer ordreUtilisateur1 = mapUtilisateurOrdre.get(o1.getIdUtilisateur());
					Integer ordreUtilisateur2 = mapUtilisateurOrdre.get(o2.getIdUtilisateur());
					if(ordreUtilisateur1 == null && ordreUtilisateur2 != null){
						//Cas ou l'utilisateur 1 n'a pas d'ordre contrairement au 2, il passe avant 
						return - 1;
					} else if(ordreUtilisateur1 != null && ordreUtilisateur2 == null){
						//Cas ou l'utilisateur 1 a un ordre mais pas le 2, il passe apres 
						return 1;
					} else if (ordreUtilisateur1 != null && ordreUtilisateur2 != null){
						//Cas ou les 2 ont un ordre, on conserve l'ordre
						return ordreUtilisateur1.compareTo(ordreUtilisateur2);
					} else {
						//Cas ou aucun utilisateur n'est present dans les anciens tours, tri alpha
						return o1.getNom().compareToIgnoreCase(o2.getNom());
					}
				}
			});
			//Determination de la date a partir de laquelle generer les nouveaux tours
			Date dateDepart = DateUtils.getCurrentDayDate();
			if(CollectionUtils.isNotEmpty(toursExistantsEnCours)){
				Tour dernierTourExistant = toursExistantsEnCours.get(toursExistantsEnCours.size() - 1);
				dateDepart = dernierTourExistant.getDateTour();
			}
			Groupe groupe = em.find(Groupe.class, idGroupe);
			dateDepart = CycleUtils.getProchaineDateOccurence(groupe.getJourOccurence(), dateDepart, false);
			//Creation des tours manquant a partir de la date de depart pour chaque utilisateur n'ayant pas de tour
			String actionFeedback = "Creation des tours pour : ";
			String prefixeNom = "";
			if(utilisateursSansTours != null){
				for(Utilisateur utilisateurSansTour : utilisateursSansTours) {
					//Creation du tour pour l'utilisateur
					Tour nouveauTour = new Tour();
					nouveauTour.setIdGroupe(idGroupe);
					nouveauTour.setIdUtilisateur(utilisateurSansTour.getIdUtilisateur());
					nouveauTour.setNomTour(utilisateurSansTour.getNom());
					nouveauTour.setDateTour(dateDepart);
					nouveauTour.setStatutTour(StatutTour.ACTIF);
					em.persist(nouveauTour);
					dateDepart = CycleUtils.getProchaineDateOccurence(groupe.getJourOccurence(), dateDepart, false);
					actionFeedback += prefixeNom + nouveauTour.getNomTour() + " (" + DateUtils.formatDate(nouveauTour.getDateTour(), null) + ")";
					prefixeNom = ", ";
				}
			}
			return actionFeedback;
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
	 * Permet de recuperer le cycle en cours d'un groupe
	 * @param idGroupe	Identifiant du groupe
	 * 
	 * @return Les tours a venir pour ce groupe
	 */
	public List<Tour> rechercherCycleEnCours(Long idGroupe) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			CriteriaBuilder qb = em.getCriteriaBuilder();
			controleGroupeExistant(idGroupe, false);
			Date dateJour = DateUtils.getCurrentDayDate();
			//Recuperation des tours du groupe avec une date superieure a celle du jour
			CriteriaQuery<Tour> tourIteCriteriaQuery = qb.createQuery(Tour.class);
			Root<Tour> tourIte = tourIteCriteriaQuery.from(Tour.class);
			List<Predicate> tourPredicates = new ArrayList<Predicate>();
			tourPredicates.add(qb.equal(tourIte.get(Tour_.idGroupe), idGroupe));
			if(dateJour != null){
				tourPredicates.add(qb.greaterThanOrEqualTo(tourIte.get(Tour_.dateTour), dateJour));
			}
			if(tourPredicates.size() > 0){
				tourIteCriteriaQuery.where(tourPredicates.toArray(new Predicate[tourPredicates.size()]));
			}
			List<Order> tourIteOrderBy = new ArrayList<Order>();
			tourIteOrderBy.add(qb.asc(tourIte.get(Tour_.dateTour)));
			if(tourIteOrderBy.size() > 0){
				tourIteCriteriaQuery.orderBy(tourIteOrderBy);
			}
			TypedQuery<Tour> tourIteQuery = em.createQuery(tourIteCriteriaQuery);
			List<Tour> tours = tourIteQuery.getResultList();
			return tours;
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
	 * Permet d'annuler un tour en creant un tour vide et en decalant les suivants
	 * @param idTour	Identifiant du tour a annuler
	 * @param messageAnnulation	Le message affiche pour ce tour d'annulation
	 * 
	 * @return Le tour d'annulation
	 */
	public Tour annulerTour(Long idTour, String messageAnnulation) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			CriteriaBuilder qb = em.getCriteriaBuilder();
			//Controle des donnees et determination de la date du tour
			Tour tour = controleTourExistant(idTour);
			Date dateAnnulation = DateUtils.getDateWithoutTime(tour.getDateTour());
			Groupe groupe = controleGroupeExistant(tour.getIdGroupe(), false);
			//Decalage des tours a partir de cette date
			//Recuperation des tours du groupe ayant lieu apres la date d'annulation
			CriteriaQuery<Tour> tourIteCriteriaQuery = qb.createQuery(Tour.class);
			Root<Tour> tourIte = tourIteCriteriaQuery.from(Tour.class);
			List<Predicate> tourPredicates = new ArrayList<Predicate>();
			tourPredicates.add(qb.equal(tourIte.get(Tour_.idGroupe), groupe.getIdGroupe()));
			tourPredicates.add(qb.greaterThanOrEqualTo(tourIte.get(Tour_.dateTour), dateAnnulation));
			if(tourPredicates.size() > 0){
				tourIteCriteriaQuery.where(tourPredicates.toArray(new Predicate[tourPredicates.size()]));
			}
			TypedQuery<Tour> tourIteQuery = em.createQuery(tourIteCriteriaQuery);
			List<Tour> toursAReporter = tourIteQuery.getResultList();
			if(toursAReporter != null){
				for(Tour tourAReporter : toursAReporter) {
					Date dateSuivante = CycleUtils.getProchaineDateOccurence(groupe.getJourOccurence(), tourAReporter.getDateTour(), false);
					tourAReporter.setDateTour(dateSuivante);
					tourAReporter = em.merge(tourAReporter);
				}
			}
			//Creation du tour d'annulation pour la date du tour indique
			Tour tourAnnulation = new Tour();
			tourAnnulation.setIdGroupe(groupe.getIdGroupe());
			tourAnnulation.setNomTour(messageAnnulation);
			tourAnnulation.setDateTour(dateAnnulation);
			tourAnnulation.setStatutTour(StatutTour.ANNULE);
			em.persist(tourAnnulation);
			return tourAnnulation;
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
	protected Utilisateur controleUtilisateurExistant(Long idUtilisateur, boolean autoriseNull) {
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
				return utilisateur;
			}
			return null;
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
	protected Groupe controleGroupeExistant(Long idGroupe, boolean autoriseNull) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			//Cas ou l'identifiant du groupe devrait etre renseigne
			if(!autoriseNull && idGroupe == null){
				throw new BusinessException("L'identifiant du groupe est obligatoire");
			}
			//Controle de l'existance du groupe
			if(idGroupe != null){
				Groupe groupe = em.find(Groupe.class, idGroupe);
				if(groupe == null){
					throw new BusinessException("Aucun groupe pour l'identifiant " + idGroupe);
				}
				return groupe;
			}
			return null;
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
	 * Controle que l'utilisateur est bien dans ce groupe
	 * @param idUtilisateur	Identifiant de l'utilisateur
	 * @param idGroupe	Identifiant de groupe
	 * 
	 * @return La constitution de groupe associee
	 */
	protected ConstitutionGroupe controleConstitutionGroupeEtUtilisateur(Long idUtilisateur, Long idGroupe) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			CriteriaBuilder qb = em.getCriteriaBuilder();
			//L'identifiant utilisateur est obligatoire
			if(idUtilisateur == null){
				throw new BusinessException("L'identifiant utilisateur est obligatoire");
			}
			//L'identifiant du groupe est obligatoire
			if(idGroupe == null){
				throw new BusinessException("L'identifiant du groupe est obligatoire");
			}
			//Recuperation de la constitution groupe pour l'utilisateur et le groupe
			CriteriaQuery<ConstitutionGroupe> constitutionGroupeIteCriteriaQuery = qb.createQuery(ConstitutionGroupe.class);
			Root<ConstitutionGroupe> constitutionGroupeIte = constitutionGroupeIteCriteriaQuery.from(ConstitutionGroupe.class);
			List<Predicate> constitutionGroupePredicates = new ArrayList<Predicate>();
			if(idUtilisateur != null){
				constitutionGroupePredicates.add(qb.equal(constitutionGroupeIte.get(ConstitutionGroupe_.idUtilisateur), idUtilisateur));
			}
			if(idGroupe != null){
				constitutionGroupePredicates.add(qb.equal(constitutionGroupeIte.get(ConstitutionGroupe_.idGroupe), idGroupe));
			}
			if(constitutionGroupePredicates.size() > 0){
				constitutionGroupeIteCriteriaQuery.where(constitutionGroupePredicates.toArray(new Predicate[constitutionGroupePredicates.size()]));
			}
			TypedQuery<ConstitutionGroupe> constitutionGroupeIteQuery = em.createQuery(constitutionGroupeIteCriteriaQuery);
			ConstitutionGroupe constitutionGroupe = AppUtils.first(constitutionGroupeIteQuery.getResultList());
			//Aucune constitution de groupe pour l'utilisateur
			if(constitutionGroupe == null){
				throw new BusinessException("Aucune constitution de groupe pour l'utilisateur " + idUtilisateur + " et le groupe " + idGroupe);
			}
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
	 * Controle que le tour existe
	 * @param idTour	Identifiant du tour
	 * 
	 * @return Le tour existant
	 */
	protected Tour controleTourExistant(Long idTour) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			//L'identifiant du tour est obligatoire
			if(idTour == null){
				throw new BusinessException("L'identifiant du tour est obligatoire");
			}
			Tour tour = em.find(Tour.class, idTour);
			//Aucun tour pour cet id
			if(tour == null){
				throw new BusinessException("Aucun tour pour cet identifiant");
			}
			return tour;
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