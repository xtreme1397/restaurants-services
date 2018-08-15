package org.xtreme.com.system.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "org.xtreme.com.*" })
public class ApplicationConfig {
}
