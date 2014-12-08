package fr.forfun.croissants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.forfun.croissants.core.TechnicalException;

public class DateUtils {

	protected static final String DEFAULT_PATTERN = "dd/MM/yyyy";
	
	/**
	 * @return	la date du jour sans heure
	 */
	public static Date getCurrentDayDate(){
		return getDateWithoutTime(new Date());
	}
	
	/**
	 * @param inputDate
	 * @return	la date sans heure
	 */
	public static Date getDateWithoutTime(Date inputDate){
		return parseDate(formatDate(inputDate, DEFAULT_PATTERN), DEFAULT_PATTERN);
	}
	
	/**
	 * @param date
	 * @param pattern
	 * @return	la date formatte au pattern ou dd/MM/yyyy par defaut
	 */
	public static String formatDate(Date date, String pattern){
		if(pattern == null){
			pattern = DEFAULT_PATTERN;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/**
	 * @param dateString
	 * @param pattern		pattern de la chaine ou dd/MM/yyyy par defaut
	 * @return	la date pour la chaine
	 */
	public static Date parseDate(String dateString, String pattern){
		if(pattern == null){
			pattern = DEFAULT_PATTERN;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			throw new TechnicalException(e);
		}
	}
	
}
