package org.xtreme.com.auth.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xtreme.com.auth.consts.GrantType;
import org.xtreme.com.auth.domain.RefreshTokenPayload;
import org.xtreme.com.auth.domain.Token;
import org.xtreme.com.auth.service.AuthService;
import org.xtreme.com.system.exception.RequestException;
import org.xtreme.com.system.util.UrlUtil;
import org.xtreme.com.user.domain.User;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/v1")
public class AuthController {
	@Autowired
	private AuthService authService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ApiOperation(value = "Login", notes = "It authenticates the login and returns the user details as response")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Login Success", response = User.class) })
	public ResponseEntity<Token> login(@RequestHeader(value = "From") String userName,
			@RequestHeader(value = "Authorization") String password, HttpServletRequest req) {
		if (userName == null || "".equals(userName)) {
			throw new RequestException(103);
		}
		if (password == null || "".equals(password)) {
			throw new RequestException(104);
		}
		String audience = req.getHeader("Host");
		String device = UrlUtil.getDevice(req);
		Token token = authService.login(userName, password, audience, device);
		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ApiOperation(value = "Signup", notes = "It creates and returns the new user")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Signup Success", response = User.class) })
	public User signup(@RequestBody User user) {
		if (user == null) {
			throw new RequestException(103);
		}
		return authService.signup(user);
	}

	@RequestMapping(value = "/logout/{userName}/{redirectUri}", method = RequestMethod.GET)
	@ApiOperation(value = "Logout", notes = "It deletes the user session data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Logout Success", response = User.class) })
	public String logout(@PathVariable String userName, @PathVariable String redirectUri,
			@RequestHeader(value = "ref_token") String refToken, HttpServletRequest req, HttpServletResponse res,
			Authentication authentication) throws IOException {
		String device = UrlUtil.getDevice(req);
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(req, res, authentication);
			req.getSession().invalidate();
		}
		return authService.logout(userName, redirectUri, device, refToken);
	}

	@RequestMapping(value = "/refreshToken", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<Token> refreshToken(@ModelAttribute RefreshTokenPayload oAuthRefreshTokenPayload,
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		try {
			HttpHeaders responseHeaders = new HttpHeaders();
			if (oAuthRefreshTokenPayload.getGrantType() != null
					&& oAuthRefreshTokenPayload.getGrantType().equals(GrantType.REFRESH_TOKEN.getValue())) {
				String refreshToken = oAuthRefreshTokenPayload.getRefreshToken();
				if (refreshToken != null) {
					String device = UrlUtil.getDevice(request);
					Token oAuthToken = authService.refreshToken(refreshToken, device);

					return new ResponseEntity<>(oAuthToken, responseHeaders, HttpStatus.OK);
				} else {
					throw new RequestException(111);
				}
			} else {
				throw new RequestException(110);
			}
		} catch (Exception e) {
			throw new RequestException(111, e);
		}
	}
}