package org.xtreme.com.auth.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.glassfish.jersey.internal.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.xtreme.com.auth.domain.AuthenticationToken;
import org.xtreme.com.auth.domain.RefreshToken;
import org.xtreme.com.auth.domain.Token;
import org.xtreme.com.auth.domain.TokenProps;
import org.xtreme.com.auth.service.AuthService;
import org.xtreme.com.auth.util.JwtUtil;
import org.xtreme.com.auth.util.RefreshTokenUtil;
import org.xtreme.com.system.consts.EntityStatus;
import org.xtreme.com.user.data.UserRepository;
import org.xtreme.com.user.domain.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service("userService")
public class AuthServiceImpl implements AuthService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private RefreshTokenUtil refreshTokenUtill;
	ObjectMapper mapper = new ObjectMapper();

	@Override
	public Token login(String username, String password, String audience, String ipAddress) {
		LOGGER.info("Validating credentials for user {}", username);
		Authentication auth = new AuthenticationToken(username, password);
		try {
			authManager.authenticate(auth);
			return generateJwtToken(username, audience, ipAddress, false, null);
		} catch (ProviderNotFoundException e) {
			User matchingInactiveUser = userRepository.findByUsernameAndStatus(username,
					EntityStatus.INACTIVE.getValue());
			if (matchingInactiveUser != null) {
				throw new DisabledException("User account is deactivated");
			}
			throw new BadCredentialsException("Invalid username or password");
		}
	}

	@Override
	public String logout(String username, String redirectUri, String device, String refToken)
			throws UnsupportedEncodingException {
		LOGGER.info("Logout request for user {}", username);
		User user = userRepository.findByUsername(username);
		List<RefreshToken> refreshTokenList = user.getRefreshTokens();
		if (refreshTokenList != null) {
			RefreshToken refreshToken = refreshTokenUtill.findTokenByTokenId(refreshTokenList, refToken);
			if (refreshToken != null) {
				refreshTokenList.remove(refreshToken);
				user.setRefreshTokens(refreshTokenList);
				userRepository.save(user);
			}
		}
		LOGGER.info("Logout successful for user {}", username);
		return URLDecoder.decode(Base64.decodeAsString(redirectUri), "UTF-8");
	}

	private Token generateJwtToken(String username, String audience, String ipAddress, boolean isSSOUser,
			JsonNode ssoOAuthToken) {
		User user = userRepository.findByUsernameAndStatus(username, EntityStatus.ACTIVE.getValue());
		Token oAuthToken = null;
		try {
			TokenProps jwtTokenProps = new TokenProps();
			jwtTokenProps.setAudience(audience);
			jwtTokenProps.setIpAddress(ipAddress);
			jwtTokenProps.setEmail(user.getEmail());
			jwtTokenProps.setName(user.getName());
			jwtTokenProps.setUpn(user.getUsername());
			jwtTokenProps.setStatus(user.getStatus());
			oAuthToken = jwtUtil.generate(jwtTokenProps);
		} catch (Exception e) {
			LOGGER.error("error  while generating jwt token", e);
		}
		LOGGER.info("Authentication successful for user {}", username);
		return oAuthToken;
	}

	@Override
	public Token refreshToken(String refreshToken, String device) throws IOException {
		ObjectNode verifiedToken = refreshTokenUtill.verify(refreshToken, device);
		RefreshToken oAuthRefreshToken = mapper.convertValue(verifiedToken.get("refreshToken"), RefreshToken.class);
		User user = mapper.convertValue(verifiedToken.get("user"), User.class);
		TokenProps jwtTokenProps = new TokenProps();
		jwtTokenProps.setAudience(oAuthRefreshToken.getAudience());
		jwtTokenProps.setIpAddress(oAuthRefreshToken.getDevice());
		jwtTokenProps.setEmail(user.getEmail());
		jwtTokenProps.setName(user.getName());
		jwtTokenProps.setStatus(user.getStatus());
		jwtTokenProps.setUpn(user.getUsername());
		Token oAuthToken = jwtUtil.generate(jwtTokenProps);
		oAuthToken.setRefreshTokenId(oAuthRefreshToken.getTokenId());
		oAuthToken.setRefreshTokenExpiresAt(oAuthRefreshToken.getExpiresAt());
		oAuthToken.setRefreshTokenExpiresIn(oAuthRefreshToken.getExpiresIn());
		oAuthToken.setRefreshTokenIssuedAt(oAuthRefreshToken.getIssuedAt());
		return oAuthToken;
	}
}