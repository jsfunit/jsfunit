package org.jboss.jsfunit.config;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class AtomicIntegerValidator implements Validator{

	public void validate(FacesContext ctx, UIComponent ui, Object object) 
		throws ValidatorException {
		
	}

}
