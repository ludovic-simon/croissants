package fr.forfun.croissants;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import fr.forfun.croissants.core.TechnicalException;

/**
 * Classe utilitaire pour le cycle
 */
public class CycleUtils implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** URL de base de l'application */
	public static final String URL_APPLICATION = "http://www.lud007.jvmhost.net/croissants";
	
	public static final String EMAIL_FROM = "croissants@jvmhost.com";
	
	public static final String EMAIL_FROM_DISPLAY = "Application des croissants";
	
	public static final String EMAIL_JNDI_NAME = "mail/croissants";

	{/* METHODES */}
	
	/**
	 * Calcul la prochaine date d'occurence pour un jour d'occurence et une date de depart
	 * @param jourOccurence	Jour d'occurence souhaite pour la date
	 * @param dateDepart	La date a partir de laquelle prendre la prochaine occurence
	 * @param prendreSiEgal	si la date de depart est un jour d'occurence alors elle sera retournee
	 */
	public static Date getProchaineDateOccurence(Long jourOccurence, Date dateDepart, boolean prendreSiEgal) {
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
	 * Permet d'envoyer un mail depuis le serveur
	 * @param sujet	Sujet du mail
	 * @param destinataire	Destinataire du mail
	 * @param corps	Corps du mail
	 */
	public static void envoyerEmail(String sujet, String destinataire, String corps) {
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
	}

	/**
	 * @return L'URL de l'ecran de connexion
	 */
	public static String getUrlLogin() {
		return URL_APPLICATION + "/views/guest.html#loginView";
	}

	/**
	 * L'URL de l'ecran d'inscription
	 */
	public static String getUrlInscription() {
		return URL_APPLICATION + "/views/guest.html#inscriptionView";
	}

}