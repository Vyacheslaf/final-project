package org.example.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

public class Messages {

	private static final String LOCALES_BASE_NAME = "i18n/text";
	private static final String WRONG_MESSAGE = "OOPS!";
	private static final String SESSION_ATTR_LANG = "lang";
	
	public static String getMessage(HttpServletRequest req, String key) {
		String language = (String)req.getSession().getAttribute(SESSION_ATTR_LANG);
		if (req == null || language == null || key == null) {
			return WRONG_MESSAGE;
		}
		Locale locale = new Locale(language);
		ResourceBundle resourceBundle = ResourceBundle.getBundle(LOCALES_BASE_NAME, locale);
		if (resourceBundle.containsKey(key)) {
			return resourceBundle.getString(key);
		}
		return WRONG_MESSAGE;
	}

}
