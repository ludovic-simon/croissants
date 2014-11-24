package fr.forfun.croissants.core;

import java.util.List;

public class AppUtils {

	public static <T> T first(List<T> elements){
		if(elements == null || elements.size() < 1){
			return null;
		}
		return elements.get(0);
	}
	
}
