package fr.forfun.croissants.core;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;


/**
 * Gestionnaire d'exception REST, il traitera toutes les exceptions non rattrappees 
 * survenues lors de appels aux services REST.
 * - Pour les exceptions metier BusinessException, le message de l'exception est renvoye  
 * - Pour toutes les autres exceptions (considerees comme technique), le message "Erreur technique" suivi de l'exception
 * est renvoye
 */
@Provider
public class SDevExceptionMapper implements ExceptionMapper<Exception> {

    public SDevExceptionMapper() {
    }

    public Response toResponse(Exception ex) {
    	
    	String errorMessage = "";
    	if(ex instanceof BusinessException){
    		//Cas d'une erreur fonctionnelle
    		errorMessage = ex.getMessage();
    	} else {
    		//Cas d'une erreur technique : message "Erreur technique" et impression de la cause de l'erreur
    		errorMessage = "Erreur technique";
    		Throwable targetException = ex;
    		Throwable rootException = ExceptionUtils.getRootCause(ex);
    		if(rootException != null){
    			targetException = rootException;
    		}
    		String exceptionMessage = ExceptionUtils.getFullStackTrace(targetException);
    		if(StringUtils.isNotEmpty(exceptionMessage)){
    			exceptionMessage = ExceptionUtils.getFullStackTrace(ex);
    			errorMessage += "\n" + exceptionMessage;
    		}
    		
    		//Trace de l'erreur technique en log
    		System.err.println(errorMessage);
    	}
    	
    	//Envoi de l'erreur dans une reponse HTTP avec le code 500 et portant le message de l'erreur
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                errorMessage).type(MediaType.APPLICATION_XML).build();

    }

}
