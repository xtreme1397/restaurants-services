package org.xtreme.com.system.exception;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
public class ErrorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);
	Properties errorMessages = new Properties();

	@PostConstruct
	public void errorHandalerPostConstruct() {
		String msgFile = "/error_message.properties";
		InputStream msgIS = this.getClass().getResourceAsStream(msgFile);
		if (msgIS == null) {
			LOGGER.error("Unable to find " + msgFile);
			return;
		}
		// load a properties file from class path, inside static method
		try {
			errorMessages.load(msgIS);
		} catch (IOException e) {
			LOGGER.error("", e);
		}
	}

	@ExceptionHandler(RequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Error processRequestException(RequestException ex) {
		LOGGER.error("Bad Request Error : ", ex);
		Error error = null;
		if (ex.getCode() > 0) {
			String message = errorMessages.getProperty(String.valueOf(ex.getCode()));
			if (message != null) {
				error = new Error(ex.getCode(), message, HttpStatus.BAD_REQUEST.value());
			} else {
				LOGGER.error("Missing error code - " + ex.getCode());
				error = new Error(910, "Missing error code", HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
		} else {
			error = new Error(900, ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		}
		return error;
	}

	@ExceptionHandler(ServletRequestBindingException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Error processServletRequestBindingExceptions(ServletRequestBindingException ex) {
		LOGGER.error("ServletRequestBindingException Error : ", ex);
		return new Error(900, ex.getMessage(), HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public Error processAuthenticationExceptions(AuthenticationException ex) {
		LOGGER.error("Authentication Error : ", ex);
		return new Error(901, ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public Error processAuthorizationExceptions(AccessDeniedException ex) {
		LOGGER.error("Security Error : ", ex);
		return new Error(903, ex.getMessage(), HttpStatus.FORBIDDEN.value());
	}

	@ExceptionHandler(DataAccessResourceFailureException.class)
	@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
	@ResponseBody
	public Error processDataAccessResourceFailureException(DataAccessResourceFailureException ex) {
		LOGGER.error("Connection Timeout Error : ", ex);
		return new Error(904, "Resource access timed out", HttpStatus.GATEWAY_TIMEOUT.value());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Error processException(Exception ex) {
		LOGGER.error("Unhandled error occured at runtime : ", ex);
		return new Error(910, "Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}