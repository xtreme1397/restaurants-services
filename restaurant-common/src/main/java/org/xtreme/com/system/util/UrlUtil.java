package org.xtreme.com.system.util;

import javax.servlet.http.HttpServletRequest;

public class UrlUtil {


	private UrlUtil() {
		super();
	}

	public static String getDevice(HttpServletRequest req) {
		String device = req.getHeader("X-FORWARDED-FOR");
		if (device == null) {
			device = req.getRemoteAddr();
		}
		if (device == null) {
			device = req.getRemoteHost();
		}
		return device;
	}
}
