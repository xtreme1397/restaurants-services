package org.xtreme.com.user.domain;

import org.springframework.data.annotation.Id;

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
}
