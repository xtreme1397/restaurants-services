package org.xtreme.com.auth.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xtreme.com.auth.domain.RefreshToken;
import org.xtreme.com.auth.domain.Token;
import org.xtreme.com.auth.domain.TokenProps;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;;

@Service
public class JwtUtil {
	private static final String SECRET_KEY = "c06492426b78c0418758cb9166d331e0";
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
	private static String issuer;
	@Autowired
	RefreshTokenUtil refreshTokenUtill;
	@Value("${accesstoken.validity-in-minute}")
	private long accessTokenValidityInMinute;
	static {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("common.properties"));
			issuer = prop.getProperty("application.studio.host");
		} catch (IOException e) {
			LOGGER.error("Failed while reading common.properties file", e);
		}
	}

	public Token generate(TokenProps jwtTokenProps) throws IOException {
		int milliSecondPerMinute = 60 * 1000;
		Instant instant = Instant.now();
		Date issuedAt = new Date(instant.toEpochMilli());
		if (jwtTokenProps.getExpiresAt() == null) {
			jwtTokenProps.setExpiresAt(
					new Date(instant.toEpochMilli() + accessTokenValidityInMinute * milliSecondPerMinute));
		}
		Token token = new Token();
		Builder tokenBuilder = JWT.create().withIssuer(issuer).withIssuedAt(issuedAt)
				.withAudience(jwtTokenProps.getAudience()).withNotBefore(issuedAt)
				.withClaim("name", jwtTokenProps.getName()).withClaim("upn", jwtTokenProps.getUpn())
				.withClaim("unique_name", jwtTokenProps.getUpn()).withClaim("status", jwtTokenProps.getStatus())
				.withExpiresAt(jwtTokenProps.getExpiresAt());
		tokenBuilder.withClaim("upn", jwtTokenProps.getUpn()).withClaim("email", jwtTokenProps.getEmail());
		RefreshToken refreshToken = refreshTokenUtill.generate(instant, jwtTokenProps.getUpn(),
				jwtTokenProps.getIpAddress(), jwtTokenProps.getAudience());
		token.setRefreshTokenId(refreshToken.getTokenId());
		token.setRefreshTokenIssuedAt(refreshToken.getIssuedAt());
		token.setRefreshTokenExpiresIn(refreshToken.getExpiresIn());
		token.setRefreshTokenExpiresAt(refreshToken.getExpiresAt());
		String accessToken = tokenBuilder.sign(Algorithm.HMAC256(SECRET_KEY));
		token.setAccessToken(accessToken);
		token.setTokenType("Bearer");
		token.setExpiresAt(Instant.ofEpochSecond(jwtTokenProps.getExpiresAt().getTime() / 1000).toString());
		token.setExpiresIn(String.valueOf((jwtTokenProps.getExpiresAt().getTime() - issuedAt.getTime()) / 1000));
		return token;
	}

	public JWT verify(String token, String audience) throws UnsupportedEncodingException {
		int index = token.trim().indexOf("Bearer ");
		JWT jwt;
		if (index != 0) {
			throw new JWTVerificationException("Bearer as prefix is missing in token");
		} else {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).withIssuer(issuer).build(); // Reusable
			jwt = (JWT) verifier.verify(token.trim().substring(7).trim());
			this.audienceCheck(audience, jwt);
		}
		return jwt;
	}

	private void audienceCheck(String audience, JWT jwt) {
		if (audience == null) {
			throw new JWTVerificationException("Invalid audience");
		} else {
			List<String> audienceList = jwt.getAudience();
			if (!audienceList.contains(audience)) {
				throw new JWTVerificationException("Invalid audience");
			}
		}
	}
}
