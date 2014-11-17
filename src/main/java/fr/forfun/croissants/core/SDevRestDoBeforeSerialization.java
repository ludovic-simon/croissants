package fr.forfun.croissants.core;

import fr.forfun.croissants.core.HibernateDetachUtil.SerializationType;

public class SDevRestDoBeforeSerialization {

	public static void run(Object value){
		try {
			HibernateDetachUtil.nullOutUninitializedFields(value, SerializationType.SERIALIZATION);
		} catch (Exception e) {
			throw new TechnicalException(e);
		}
	}
	
}
