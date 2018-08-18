package org.xtreme.com.studio.system.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.xtreme.com.auth.domain.LoggedInUser;
import org.xtreme.com.auth.domain.UserSession;
import org.xtreme.com.auth.util.JwtUtil;
import org.xtreme.com.system.exception.Error;
import org.xtreme.com.system.exception.ErrorHandler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

public class SecurityFilter extends GenericFilterBean {

	ErrorHandler errorHandler;
	JwtUtil jwtUtil;

	private static final Logger SFLOGGER = LoggerFactory.getLogger(SecurityFilter.class);

	public SecurityFilter(JwtUtil jwtUtil, ErrorHandler errorHandler) {
		super();
		this.jwtUtil = jwtUtil;
		this.errorHandler = errorHandler;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String audience = request.getHeader("Host");
		if (!"OPTIONS".equalsIgnoreCase(request.getMethod())) {
			Optional<String> authToken = Optional.fromNullable(request.getHeader("Authorization"));
			if (!authToken.isPresent()) {
				SFLOGGER.error("Authorization header not  present {}", audience);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				Error errorObj = errorHandler.processAuthenticationExceptions(
						new AuthenticationCredentialsNotFoundException("Authorization header not  present"));
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(convertObjectToJson(errorObj));
				return;
			} else {
				try {
					Authentication auth = this.verifyToken(request, response, authToken, audience);
					SecurityContextHolder.getContext().setAuthentication(auth);
				} catch (Exception e) {
					SFLOGGER.error(e.getMessage());
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					Error errorObj = errorHandler
							.processAuthenticationExceptions(new BadCredentialsException("Authenticatin failed"));
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(convertObjectToJson(errorObj));
					return;
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	private Authentication verifyToken(HttpServletRequest request, HttpServletResponse response,
			Optional<String> authToken, String audience) throws UnsupportedEncodingException {
		JWT jwt = jwtUtil.verify(authToken.get(), audience);
		Map<String, Claim> claims = jwt.getClaims();
		List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("");
		LoggedInUser authUser = new LoggedInUser(claims.get("upn").asString(), "N/A", authorities,
				claims.get("name").asString());
		return new UserSession(authUser, null, authorities);
	}

	private String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

}
