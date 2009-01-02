package org.jboss.jsfunit.analysis.el;

import java.util.Map;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

/**
 * 
 * @author Jason
 * @since 1.0
 */
public class JSFUnitVariableMapper extends VariableMapper
{
	private final Map<String, Class<?>> managedBeans;
	private final ExpressionFactory factory;

	public JSFUnitVariableMapper(final Map<String, Class<?>> managedBeans,
			final ExpressionFactory factory)
	{
		this.managedBeans = managedBeans;
		this.factory = factory;
	}

	/**
	 * We aren't evaluating anything, so we don't need the actual object the
	 * variable refers to, we just need that object's class.  Once
	 * we have the class, our ELResolver implementation will be responsible
	 * for reflecting on that class to resolve any properties or methods.
	 */
	@Override
    public ValueExpression resolveVariable(final String variable)
    {
		final Class<?> managedBeanClass = managedBeans.get(variable);
		if(managedBeanClass != null) {
			return factory.createValueExpression(managedBeanClass, Class.class);
		}
		else {
			return null;
		}
    }

	@Override
    public ValueExpression setVariable(final String variable,
            final ValueExpression expression)
    {
		throw new UnsupportedOperationException();
    }

}
