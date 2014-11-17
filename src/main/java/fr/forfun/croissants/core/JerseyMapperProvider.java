package fr.forfun.croissants.core;

import java.text.SimpleDateFormat;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Mapper JAX-RS pour configurer la serialisation / deserialisation JSON
 * Gere les dates au format dd/MM/yyyy
 */
@Provider
public class JerseyMapperProvider implements ContextResolver<ObjectMapper> {
	
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public ObjectMapper getContext(Class<?> type) {
    	ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(dateFormat);
        return mapper;
    }
}