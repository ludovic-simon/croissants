package fr.forfun.croissants.service;

import java.util.Calendar;
import java.util.Date;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import fr.forfun.croissants.core.TechnicalException;
import fr.forfun.croissants.entity.Historique;
import fr.forfun.croissants.entity.HistoriqueDomaine;

/**
 * Services transverses
 */
public class TransverseService {

	protected EntityManagerFactory emf;
	
	public TransverseService(){
		emf = Persistence.createEntityManagerFactory("croissants");
	}
	
	public TransverseService(String persistenceUnit){
		emf = Persistence.createEntityManagerFactory(persistenceUnit);
	}
	
	{/* CHAMPS */}

	/** URL de base de l'application */
	public static final String URL_APPLICATION = "http://www.faispeterlescroissants.com/croissants";
	
	public static final String EMAIL_FROM = "admin@faispeterlescroissants.com";
	
	public static final String EMAIL_FROM_DISPLAY = "Fais Peter Les Croissants";
	
	public static final String EMAIL_JNDI_NAME = "mail/croissants";

	{/* SERVICES */}
	
	/**
	 * Ajoute une action dans l'historique
	 * @param historique
	 */
	public void tracerHistorique(Historique historique) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		boolean txError = false;
		try {
			tx = em.getTransaction();
			tx.begin();
			if(historique.getDateAction() == null){
				//Affectation de la date actuelle a l'historique
				historique.setDateAction(new Date());
			}
			em.persist(historique);
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
	 * Ajoute un historique d'erreur avec les infos de l'exception
	 * @param exception	l'erreur technique
	 * 
	 * @return Le message d'erreur
	 */
	public String tracerErreurTechnique(Exception exception) {
		//Determination du message d'erreur
		String errorMessage = "Erreur technique";
		Throwable targetException = exception;
		Throwable rootException = ExceptionUtils.getRootCause(exception);
		if(rootException != null){
			targetException = rootException;
		}
		String exceptionMessage = ExceptionUtils.getFullStackTrace(targetException);
		if(StringUtils.isNotEmpty(exceptionMessage)){
			errorMessage += "\n" + exceptionMessage;
		}
		//Trace de l'erreur (en attendant un vrai log)
		System.err.println(errorMessage);
		//Historisation de l'erreur
		Historique historique = new Historique();
		historique.setHistoriqueDomaine(HistoriqueDomaine.ERREUR);
		historique.setAction(errorMessage);
		historique.setIsSuperAdmin(true);
		tracerHistorique(historique);
		return errorMessage;
	}

	/**
	 * Permet d'envoyer un mail depuis le serveur
	 * @param sujet	Sujet du mail
	 * @param destinataire	Destinataire du mail
	 * @param corps	Corps du mail
	 */
	public void envoyerEmail(String sujet, String destinataire, String corps) {
		//Envoi du mail
		Session session = null;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			session = (Session) envCtx.lookup(EMAIL_JNDI_NAME);
			
			Message message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(EMAIL_FROM, EMAIL_FROM_DISPLAY));
			
			InternetAddress to[] = new InternetAddress[1];
			to[0] = new InternetAddress(destinataire);
			message.setRecipients(Message.RecipientType.TO, to);
			message.setSubject(sujet);
			message.setContent(corps, "text/html;charset=UTF-8");
			Transport.send(message);
		} catch (Exception ex) {
			throw new TechnicalException(ex);
		}
		//Historisation de l'action
		Historique historique = new Historique();
		historique.setHistoriqueDomaine(HistoriqueDomaine.EMAIL);
		historique.setReference(sujet);
		historique.setAction("Envoi du mail de sujet '" + sujet + "' au destinataire '" + destinataire + "'");
		historique.setIsSuperAdmin(true);
		tracerHistorique(historique);
	}

	/**
	 * Calcul la prochaine date d'occurence pour un jour d'occurence et une date de depart
	 * @param jourOccurence	Jour d'occurence souhaite pour la date
	 * @param dateDepart	La date a partir de laquelle prendre la prochaine occurence
	 * @param prendreSiEgal	si la date de depart est un jour d'occurence alors elle sera retournee
	 */
	public Date getProchaineDateOccurence(Long jourOccurence, Date dateDepart, boolean prendreSiEgal) {
		int jourCibleCalendar = Calendar.FRIDAY;
		
		//Conversion du jourOccurence en jour Calendar qui commence par dimanche (1), lundi(2) ... 
		if(jourOccurence != null){
			if(jourOccurence.intValue() == 6){
				jourCibleCalendar = Calendar.SUNDAY;
			} else {
				jourCibleCalendar = jourOccurence.intValue() + 2;
			}
		}
		
		Calendar calendarActuel = Calendar.getInstance();
		calendarActuel.setTime(dateDepart);
		int jourActuelCalendar = calendarActuel.get(Calendar.DAY_OF_WEEK);  
		if (jourActuelCalendar == jourCibleCalendar) {  
			//Cas ou le jour de depart est le jour actuel
			//- si prendreSiEgal, on prend la date de depart
			//- sinon on prend une semaine plus tard
			if(prendreSiEgal){
				return dateDepart;
			}
			calendarActuel.add(Calendar.DAY_OF_YEAR, 7);
		} else {
			//Sinon, calcul du decalage entre le jour actuel et le jour souhaite
			int days = jourCibleCalendar - jourActuelCalendar;
			if(days < 0){
				days = 7 + days;
			}
			calendarActuel.add(Calendar.DAY_OF_YEAR, days);
		}
		Date dateCible = calendarActuel.getTime();
		
		return dateCible;
	}

	/**
	 * @return L'URL de l'ecran de connexion
	 */
	public String getUrlLogin() {
		return URL_APPLICATION + "/views/guest.html#loginView";
	}

	/**
	 * L'URL de l'ecran d'inscription
	 */
	public String getUrlInscription() {
		return URL_APPLICATION + "/views/guest.html#inscriptionView";
	}

}