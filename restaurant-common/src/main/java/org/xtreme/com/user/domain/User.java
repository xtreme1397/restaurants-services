package org.xtreme.com.user.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.xtreme.com.auth.domain.RefreshToken;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class representing User
 * 
 */
@Data
@ApiModel("User")
@EqualsAndHashCode(callSuper = false)
public class User {

	@Id
	private String id;

	@ApiModelProperty(value = "xtreme")
	private String username;

	@ApiModelProperty(value = "null")
	private String password;

	@ApiModelProperty(value = "null")
	private String email;
	@ApiModelProperty(value = "John Doe")
	private String name;
	private String status;
	private List<RefreshToken> refreshTokens;
	private String salt;
}
