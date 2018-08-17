package org.xtreme.com.auth.provider;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.xtreme.com.auth.domain.AuthenticationToken;
import org.xtreme.com.system.consts.EntityStatus;
import org.xtreme.com.system.util.PasswordHashUtil;
import org.xtreme.com.user.data.UserRepository;
import org.xtreme.com.user.domain.User;

import com.google.common.collect.Lists;

public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
	public static final String INVALID_BACKEND_ADMIN_CREDENTIALS = "Invalid Backend Admin Credentials";
	private static final Logger LOGGER = LoggerFactory.getLogger(UsernamePasswordAuthenticationProvider.class);
	@Autowired
	private UserRepository userRepository;





	@Override
	public Authentication authenticate(Authentication authentication) {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		User matchingUser = userRepository.findByUsernameAndStatus(username, EntityStatus.ACTIVE.getValue());
		Authentication auth = null;
		if (matchingUser != null && matchingUser.getPassword() != null) {
			String decodedPassword = PasswordHashUtil.decode(password);
			String salt = matchingUser.getSalt();
			String hashedPassword = PasswordHashUtil.getSecurePassword(decodedPassword, salt);
			if (hashedPassword.equals(matchingUser.getPassword())) {
				List<? extends GrantedAuthority> emptyList = Lists.newArrayList();
				auth = new UsernamePasswordAuthenticationToken(username, password, emptyList);
			} else {
				LOGGER.error("invalid credential - {}", username);
			}
		}
		return auth;
	}





	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(AuthenticationToken.class);
	}
}
