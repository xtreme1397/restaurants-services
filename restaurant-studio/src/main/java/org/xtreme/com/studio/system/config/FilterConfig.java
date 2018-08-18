package org.xtreme.com.studio.system.config;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xtreme.com.studio.system.filter.CORSFilter;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean registerCORSFilterBean() {
		FilterRegistrationBean corsBean = new FilterRegistrationBean();
		corsBean.setFilter(registerCORSFilter());
		corsBean.setOrder(0);
		return corsBean;
	}

	@Bean
	public Filter registerCORSFilter() {
		return new CORSFilter();
	}

}