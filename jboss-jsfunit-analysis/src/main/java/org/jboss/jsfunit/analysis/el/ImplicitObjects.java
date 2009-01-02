package org.jboss.jsfunit.analysis.el;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author Jason
 * @since 1.0
 */
public class ImplicitObjects
{
	private static final Map<String, Class<?>> implicitObjectMap
		= new HashMap<String, Class<?>>() {
            private static final long serialVersionUID = 20080721L;
		{
			put("application", ServletContext.class);
			put("cookie", Map.class);
			put("facesContext", FacesContext.class);
			put("header", Map.class);
			put("headerValues", Map.class);
			put("param", Map.class);
			put("paramValues", Map.class);
			put("request", ServletRequest.class);
			put("session", HttpSession.class);
			put("requestScope", Map.class);
			put("sessionScope", Map.class);
			put("applicationScope", Map.class);
			put("initParam", Map.class);
			put("view", UIViewRoot.class);
	}};


	/**
	 * Returns a map that is safe for modification.
	 *
	 * @return
	 */
	public static Map<String, Class<?>> getMap() {
		return new HashMap<String, Class<?>>(implicitObjectMap);
	}

}
