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

import fr.forfun.croissants.core.AppUtils;
import fr.forfun.croissants.core.BusinessException;
import fr.forfun.croissants.entity.Historique;
import fr.forfun.croissants.entity.HistoriqueDomaine;
import fr.forfun.croissants.entity.Utilisateur;
import fr.forfun.croissants.entity.Utilisateur_;

/**
 * Services lie a l'utilisateur et aux groupes
 */
public class UtilisateurService {

	protected EntityManagerFactory emf;
	
	protected TransverseService transverseService;
	
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
			utilisateurCriteriaQuery.where(utilisateurPredicates.toArray(new Predicate[utilisateurPredicates.size()]));
			TypedQuery<Utilisateur> utilisateurQuery = em.createQuery(utilisateurCriteriaQuery);
			List<Utilisateur> utilisateurs = utilisateurQuery.getResultList();
			//Cas ou aucun utilisateur n'est trouve
			if(CollectionUtils.isEmpty(utilisateurs)){
				throw new BusinessException("Aucun utilisateur pour cet email / mot de passe");
			}
			Utilisateur utilisateurConnecte = utilisateurs.get(0);
			//MAJ de la date de derniere connexion
			utilisateurConnecte.setDateDerniereConnexion(new Date());
			utilisateurConnecte = em.merge(utilisateurConnecte);
			return utilisateurConnecte;
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
	 * Deconnexion de l'utilisateur en session
	 */
	public void seDeconnecter() {

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
			utilisateurTableCriteriaQuery.where(utilisateurPredicates.toArray(new Predicate[utilisateurPredicates.size()]));
			TypedQuery<Utilisateur> utilisateurTableQuery = em.createQuery(utilisateurTableCriteriaQuery);
			List<Utilisateur> utilisateursPourEmail = utilisateurTableQuery.getResultList();
			if(CollectionUtils.isNotEmpty(utilisateursPourEmail)){
				throw new BusinessException("Il y a deja un utilisateur enregistre pour cet email");
			}
			//Affectation de la date de creation
			utilisateur.setDateCreation(new Date());
			//Persistence de l'utilisateur
			em.persist(utilisateur);
			//Historisation de l'action
			Historique historique = new Historique();
			historique.setIdUtilisateurAction(utilisateur.getIdUtilisateur());
			historique.setHistoriqueDomaine(HistoriqueDomaine.INSCRIPTION);
			historique.setReference(utilisateur.getEmail());
			historique.setAction("Inscription de l'utilisateur '" + utilisateur.getEmail() + "'");
			historique.setIsSuperAdmin(false);
			transverseService.tracerHistorique(historique);
			//Envoi d'un email d'inscription
			StringBuffer bf = new StringBuffer();
			bf.append("Bonjour " + utilisateur.getNom() + ",<br/><br/>");
			bf.append("Bienvenue sur le site <a href='http://www.faispeterlescroissants.com'>http://www.faispeterlescroissants.com</a>.<br/>");
			bf.append("Tu peux dès maintenant te connecter avec ton email '" + utilisateur.getEmail() + "' et ton mot de passe<br/><br/>");
			bf.append("May the chouquette be with you");
			String corpsEmail = bf.toString();
			transverseService.envoyerEmail("Bienvenue sur 'Fais Péter Les Croissants'", utilisateur.getEmail(), corpsEmail);
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
	
	{/* UTILES */}
	
	public TransverseService getTransverseService() {
		return transverseService;
	}
	
	public void setTransverseService(TransverseService transverseService) {
		this.transverseService = transverseService;
	}

	/**
	 * Change le mot de passe de l'utilisateur et lui envoie un email lui indiquant son nouveau mot de passe
	 * @param email	L'email de l'utilisateur
	 */
	public void motDePassePerdu(String email) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			CriteriaBuilder qb = em.getCriteriaBuilder();
			//Controle qu'un utilisateur existe bien pour cet email
			CriteriaQuery<Utilisateur> utilisateurIteCriteriaQuery = qb.createQuery(Utilisateur.class);
			Root<Utilisateur> utilisateurIte = utilisateurIteCriteriaQuery.from(Utilisateur.class);
			List<Predicate> utilisateurPredicates = new ArrayList<Predicate>();
			utilisateurPredicates.add(qb.equal(utilisateurIte.get(Utilisateur_.email), email));
			if(utilisateurPredicates.size() > 0){
				utilisateurIteCriteriaQuery.where(utilisateurPredicates.toArray(new Predicate[utilisateurPredicates.size()]));
			}
			TypedQuery<Utilisateur> utilisateurIteQuery = em.createQuery(utilisateurIteCriteriaQuery);
			Utilisateur utilisateur = AppUtils.first(utilisateurIteQuery.getResultList());
			if(utilisateur == null){
				throw new BusinessException("Aucun compte 'Fais Peter Les Croissants' n'existe pour cet email");
			}
			//Changement du mot de passe en base
			String nouveauMdp = RandomStringUtils.randomAlphanumeric(10);
			utilisateur.setMotDePasse(nouveauMdp);
			utilisateur = em.merge(utilisateur);
			//Envoi du mail indiquant le nouveau mot de passe
			StringBuffer bf = new StringBuffer();
			bf.append("Bonjour,<br/><br/>");
			bf.append("Voilà votre nouveau mot de passe :<br/>");
			bf.append("<b>" + nouveauMdp + "</b><br/><br/>");
			bf.append("A bientôt sur <a href='http://www.faispeterlescroissants.com'>http://www.faispeterlescroissants.com</a>");
			String corpsEmail = bf.toString();
			transverseService.envoyerEmail("Votre nouveau mot de passe", email, corpsEmail);
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
	 * Permet a un utilisateur de modifier son mot de passe
	 * @param idUtilisateur	
	 * @param ancienMdp	
	 * @param nouveauMdp
	 */
	public void changerMotDePasse(Long idUtilisateur, String ancienMdp, String nouveauMdp) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			//Controles
			if(StringUtils.isEmpty(nouveauMdp)){
				throw new BusinessException("Le nouveau mot de passe ne doit pas être vide");
			}
			Utilisateur utilisateur = em.find(Utilisateur.class, idUtilisateur);
			if(utilisateur == null){
				throw new BusinessException("L'utilisateur n'existe pas");
			}
			if(!utilisateur.getMotDePasse().equals(ancienMdp)){
				throw new BusinessException("L'ancien mot de passe ne correspond pas");
			}
			//Changement du mot de passe en base
			utilisateur.setMotDePasse(nouveauMdp);
			utilisateur = em.merge(utilisateur);
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