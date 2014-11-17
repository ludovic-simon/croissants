package fr.forfun.croissants.core;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BusinessException(String message){
		super(message);
	}
	
}
