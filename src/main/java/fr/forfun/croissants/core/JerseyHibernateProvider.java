package fr.forfun.croissants.core;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

@Provider
public class JerseyHibernateProvider implements ContextResolver<ObjectMapper> {
	
    public ObjectMapper getContext(Class<?> type) {
    	ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Hibernate4Module());
        return mapper;
    }
}