package org.jboss.jsfunit.analysis.el;

import java.lang.reflect.Method;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

public class JSFUnitELContext extends ELContext
{
	private final ELResolver resolver = new JSFUnitELResolver();
	private final VariableMapper vmapper;

	//TODO: does this need to do anything real?
	private final FunctionMapper fmapper = new FunctionMapper() {
		@Override
        public Method resolveFunction(final String prefix, final String localName)
        {
			throw new UnsupportedOperationException();
        }
    };

	public JSFUnitELContext( final Map<String, Class<?>> beanMap,
							 final ExpressionFactory factory )
	{
		vmapper = new JSFUnitVariableMapper(beanMap, factory);
	}

	@Override
    public ELResolver getELResolver()
    {
        return resolver;
    }

	@Override
    public FunctionMapper getFunctionMapper()
    {
        return fmapper;
    }

	@Override
    public VariableMapper getVariableMapper()
    {
		return vmapper;
    }
}
