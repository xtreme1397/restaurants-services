package org.xtreme.com.auth.domain;

import lombok.Data;

@Data
public class RefreshTokenPayload {
	private String grantType;
	private String refreshToken;
}
