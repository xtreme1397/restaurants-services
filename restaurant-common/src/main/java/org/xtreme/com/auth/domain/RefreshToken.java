package org.xtreme.com.auth.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("OAuthRefreshToken")
@Data
public class RefreshToken {
	private String device;
	private String upn;
	private String expiresIn;
	private String expiresAt;
	private String issuedAt;
	private String tokenId;
	private String audience;
	private String ssoRefreshToken;
}
