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
		Date now = new Date();
		return parseDate(formatDate(now, DEFAULT_PATTERN), DEFAULT_PATTERN);
	}
	
	public static String formatDate(Date date, String pattern){
		if(pattern == null){
			pattern = DEFAULT_PATTERN;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
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
