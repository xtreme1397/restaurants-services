package org.xtreme.com;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xtreme.com.system.exception.ServiceException;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class AuthApplication {
	public static void main(String[] args) {
		loadProp();
		SpringApplication.run(AuthApplication.class, args);
	}

	private static void loadProp() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("common.properties"));
		} catch (IOException e) {
			throw new ServiceException("AuthApplication IOExecption while loading properties", e);
		}
	}
}
