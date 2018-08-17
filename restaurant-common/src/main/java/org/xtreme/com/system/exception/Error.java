package org.xtreme.com.system.exception;

import java.time.Instant;

import lombok.Data;

/**
 * Error object carries a code and a message for end user.
 * 
 * @author Mindtree Ltd.
 *
 */
@Data
public class Error {
	private int code;
	private String message;
	private int status;
	private long timestamp;





	/**
	 * code - Error code from error_message.properties.
	 * message - Message to be shown to end user, when this error happens.
	 * status - HTTP Status Code
	 * 
	 * @param code
	 * @param message
	 */
	public Error(int code, String message, int status) {
		super();
		this.code = code;
		this.message = message;
		this.status = status;
		this.timestamp = Instant.now().toEpochMilli();
	}
}
