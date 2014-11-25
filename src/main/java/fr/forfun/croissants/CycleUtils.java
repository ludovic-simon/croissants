package fr.forfun.croissants;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Classe utilitaire pour le cycle
 */
public class CycleUtils implements Serializable {
	
	private static final long serialVersionUID = 1L;

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
			if(jourOccurence.intValue() == 7){
				jourCibleCalendar = Calendar.SUNDAY;
			} else {
				jourCibleCalendar = jourOccurence.intValue() + 1;
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

}