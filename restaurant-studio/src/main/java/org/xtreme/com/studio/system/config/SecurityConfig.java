package org.xtreme.com.studio.system.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.xtreme.com.auth.util.JwtUtil;
import org.xtreme.com.studio.system.filter.SecurityFilter;
import org.xtreme.com.system.exception.ErrorHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	ErrorHandler errorHandler;

	@Override
	public void configure(WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring().antMatchers(allowedEndPoints());
	}

	/**
	 * Defines the web based security configuration.
	 *
	 * @param http
	 *            It allows configuring web based security for specific http
	 *            requests.
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable();
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionUrlAuthorizationConfigurer = httpSecurity
				.authorizeRequests().anyRequest().authenticated();

		expressionUrlAuthorizationConfigurer.and()
				.addFilterBefore(new SecurityFilter(jwtUtil, errorHandler), BasicAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
				.authenticationEntryPoint(unauthorizedEntryPoint());
	}

	private String[] allowedEndPoints() {
		return new String[] { "/favicon.ico", "/css/**", "/js/**", "/studio/v2/api-docs", "/studio/configuration/ui",
				"/icons/**", "//icons/**", "/studio/swagger-resources", "/studio/configuration/security",
				"/studio/swagger-ui.html", "/studio/webjars/**" };
	}

	@Bean
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
		return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

}