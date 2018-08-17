package org.xtreme.com.system.exception;

/**
 * This class is to report all internal server errors
 * 
 * @author Mindtree Ltd.
 *
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 5227953045998075477L;





	/**
	 * Message to be shown to end user, when this exception happens.
	 * Do not forget to log detailed reason for failure, before throwing this exception
	 *  
	 * @param message
	 */
	public ServiceException(String message) {
		super(message);
	}





	/**
	 * message - Message to be shown to end user, when this exception happens.
	 * cause - Actual cause of this error. The framework will log the complete stack, hence caller can skip logging of this error.
	 * 
	 * @param message
	 * @param cause
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}





	public ServiceException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
}
