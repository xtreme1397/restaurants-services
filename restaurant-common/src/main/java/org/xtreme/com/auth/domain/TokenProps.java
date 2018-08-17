package org.xtreme.com.auth.domain;

import java.util.Date;

import lombok.Data;

@Data
public class TokenProps {	
	private Date expiresAt;
	private String audience;
	private String ipAddress;
	private String name;
	private String upn;
	private String email;
	private String status;						
}
