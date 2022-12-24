package org.example.exception;

public class ServiceException extends Exception{

	private static final long serialVersionUID = 4711150021340232158L;

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}
}
