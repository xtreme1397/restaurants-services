package org.xtreme.com.auth.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuthUser extends User {
	private static final long serialVersionUID = 3102294056807388456L;
	private String name = null;

	public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String name) {
		super(username, password, authorities);
		this.name = name;
	}
}
