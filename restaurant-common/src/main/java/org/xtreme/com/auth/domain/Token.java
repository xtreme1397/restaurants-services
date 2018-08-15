package org.xtreme.com.auth.domain;

import lombok.Data;

@Data()
public class Token {
	private String accessToken;
	private String tokenType;
	private String expiresIn;
	private String expiresAt;
	private String refreshTokenId;
	private String refreshTokenExpiresIn;
	private String refreshTokenExpiresAt;
	private String refreshTokenIssuedAt;
}
