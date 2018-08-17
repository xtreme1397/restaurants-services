package org.xtreme.com.auth.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.xtreme.com.auth.domain.Token;
public interface AuthService {
	Token login(String username, String password, String audience, String ipAddress);

	String logout(String username, String redirectUri, String device, String refTokenIssAt)
			throws UnsupportedEncodingException;

	Token refreshToken(String refreshToken, String device) throws IOException;

}