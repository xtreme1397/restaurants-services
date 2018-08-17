package org.xtreme.com.system.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RequestException extends RuntimeException {
	private static final long serialVersionUID = 1617896099203132932L;
	private final int code;

	public RequestException(int code) {
		super("Error Code - " + code);
		this.code = code;
	}

	public RequestException(int code, Throwable cause) {
		super("Error Code - " + code, cause);
		this.code = code;
	}

	public RequestException(String message) {
		super(message);
		this.code = 0;
	}

}