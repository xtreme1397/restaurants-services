package org.xtreme.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthApplication {
	public static void main(String[] args) {
		//loadProp();
		SpringApplication.run(AuthApplication.class, args);
		System.out.println("application launched");
	}

//	private static void loadProp() {
//		Properties prop = new Properties();
//		try {
//			prop.load(new FileInputStream("common.properties"));
//			System.setProperty("application.instanceId", "cape-build-server.southeastasia.cloudapp.azure.com:studio:1");
//			System.setProperty("err.log", prop.getProperty("cape.error.logs-dir") + "/auth.err");
//		} catch (IOException e) {
//			throw new ServiceException("AuthApplication IOExecption while loading properties", e);
//		}
//	}
}
