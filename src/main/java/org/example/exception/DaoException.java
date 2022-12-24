package org.example.exception;

public class DaoException extends Exception{

	private static final long serialVersionUID = 700869183404026065L;

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(String message) {
		super(message);
	}
}
