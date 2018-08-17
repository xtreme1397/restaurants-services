package org.xtreme.com.auth.util;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;
import org.xtreme.com.auth.domain.RefreshToken;
import org.xtreme.com.system.consts.EntityStatus;
import org.xtreme.com.user.data.UserRepository;
import org.xtreme.com.user.domain.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class RefreshTokenUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenUtil.class);
	@Autowired
	private UserRepository userRepository;
	@Value("${refreshtoken.validity-in-minute}")
	private long refreshTokenValidityInMinute;
	ObjectMapper mapper = new ObjectMapper();

	public RefreshToken generate(Instant issuedAt, String upn, String device, String audience) {
		int secondPerMinute = 60;
		long refreshTokenExpiresAt = issuedAt.getEpochSecond() + (refreshTokenValidityInMinute * secondPerMinute);
		RefreshToken refreshToken;
		List<RefreshToken> refreshTokenList;
		User user = userRepository.findByUsername(upn);
		refreshTokenList = user.getRefreshTokens();
		if (refreshTokenList == null || refreshTokenList.isEmpty()) {
			refreshTokenList = new ArrayList<>();
		} else {
			for (int i = refreshTokenList.size() - 1; i >= 0; i--) {
				boolean isExpired = isTokenExpired(refreshTokenList.get(i));
				if (isExpired) {
					refreshTokenList.remove(i);
				}
			}
		}
		refreshToken = new RefreshToken();
		refreshToken.setUpn(upn);
		refreshToken.setDevice(device);
		refreshToken.setExpiresAt(Instant.ofEpochSecond(refreshTokenExpiresAt).toString());
		refreshToken.setExpiresIn(String.valueOf(refreshTokenExpiresAt - issuedAt.getEpochSecond()));
		refreshToken.setAudience(audience);
		refreshToken.setIssuedAt(Instant.ofEpochSecond(issuedAt.getEpochSecond()).toString());
		refreshToken.setTokenId(UUID.randomUUID().toString().replace("-", ""));
		refreshTokenList.add(refreshToken);
		user.setRefreshTokens(refreshTokenList);
		userRepository.save(user);
		return refreshToken;
	}

	public ObjectNode verify(String tokenId, String device) throws IOException {
		ObjectNode verifiedToken = JsonNodeFactory.instance.objectNode();
		User user = getUser(tokenId);
		List<RefreshToken> refreshTokenList = user.getRefreshTokens();
		RefreshToken refreshToken = null;
		if (refreshTokenList != null) {
			refreshToken = findTokenByTokenId(refreshTokenList, tokenId);
		}
		if (refreshTokenList != null && refreshToken != null) {
			if (!isTokenExpired(refreshToken)) {
				this.deviceCheck(refreshToken, device);
				this.update(user, refreshTokenList);
			} else {
				throw new CredentialsExpiredException("Refresh Token Expired");
			}
		} else {
			throw new InsufficientAuthenticationException("invalid refresh token");
		}
		verifiedToken.putPOJO("user", user);
		verifiedToken.putPOJO("refreshToken", refreshToken);
		return verifiedToken;
	}

	private void deviceCheck(RefreshToken refreshToken, String device) {
		if (!refreshToken.getDevice().equals(device)) {
			throw new InsufficientAuthenticationException("invalid device");
		}
	}

	private User update(User user, List<RefreshToken> refreshTokenList) {
		user.setRefreshTokens(refreshTokenList);
		userRepository.save(user);
		return user;
	}

	private boolean isTokenExpired(RefreshToken token) {
		boolean isExpired = true;
		String timeStamp = token.getExpiresAt().trim();
		long exp = Instant.parse(timeStamp).getEpochSecond();
		long currentTime = Instant.now().getEpochSecond();
		if (currentTime < exp) {
			isExpired = false;
		}
		return isExpired;
	}

	public RefreshToken findTokenByTokenId(List<RefreshToken> refreshTokenList, String tokenId) {
		RefreshToken filteredToken = null;
		if (refreshTokenList != null) {
			for (int i = 0; i <= refreshTokenList.size() - 1; i++) {
				RefreshToken itr = refreshTokenList.get(i);
				if (itr.getTokenId().equals(tokenId)) {
					filteredToken = itr;
					break;
				}
			}
		}
		return filteredToken;
	}

	private User getUser(String tokenId) {
		LOGGER.info("verifying refresh token againest {} key", tokenId);
		Map<String, String> condition = new HashMap<>();
		condition.put("refreshTokenList.tokenId", tokenId);
		User user = userRepository.findByRefreshTokensTokenID(tokenId);
		if (user != null) {
			verifyUser(user.getUsername());
		} else {
			throw new DisabledException("invalid token id");
		}
		return user;
	}

	private User verifyUser(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null || user.getStatus().equals(EntityStatus.INACTIVE.getValue())) {
			if (user == null) {
				throw new BadCredentialsException("user not found");
			} else {
				throw new DisabledException("user account has been Disabled");
			}
		}
		return user;
	}
}
