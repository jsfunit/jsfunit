package org.jboss.jsfunit.analysis.el;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;

/**
 * 
 * @author Jason
 * @since 1.0
 */
public class JSFUnitELResolver extends ELResolver
{
	//TODO: do we need to implement the current unsupported operations?
	//TODO: figure out how to work with expressions with operators (!, <, etc.)
	@Override
    public Class<?> getCommonPropertyType(final ELContext context, final Object base)
    {
		throw new UnsupportedOperationException();
    }

	@Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(
            final ELContext context, final Object base)
    {
		throw new UnsupportedOperationException();
    }

	@Override
    public Class<?> getType(final ELContext context, final Object base,
            final Object property)
    {
		throw new UnsupportedOperationException();
    }

	@Override
    public Object getValue(final ELContext context, final Object base,
            final Object property)
    {
		if(context == null) {
			throw new NullPointerException("context is null");
		}
		if(base instanceof Class<?>) {
			final BeanInfo beanInfo;
            try {
                beanInfo = Introspector.getBeanInfo((Class<?>)base);
            } catch ( final IntrospectionException e ) {
                throw new ELException(e);
            }

            for(final PropertyDescriptor pDes: beanInfo.getPropertyDescriptors()) {
				if(pDes.getName().equals(property)) {
					context.setPropertyResolved(true);
					return pDes.getReadMethod();
				}
			}
			for(final MethodDescriptor mDes: beanInfo.getMethodDescriptors()) {
				if(mDes.getName().equals(property)) {
					context.setPropertyResolved(true);
					return mDes.getMethod();
				}
			}

			throw new PropertyNotFoundException("Could not resolve property " +
					property + " for base " + base);
		}
		else if (base instanceof Method) {
			final Class<?> baseClass = ((Method)base).getReturnType();
			return getValue(context, baseClass, property);
		}
		else {
			//TODO:  do we need to handle (base == null) case?
			//We don't handle non-Class and non-Method bases, so according to
			//my reading of the ELResolver docs, we shouldn't throw a
			//PropertyNotFoundException.  Instead, we just rely on the
			//ELContext's propertyResolved not being set to true.
			return null;
		}
    }

	@Override
    public boolean isReadOnly(final ELContext context, final Object base,
            final Object property)
    {
		throw new UnsupportedOperationException();
    }

	@Override
    public void setValue(final ELContext context, final Object base,
            final Object property, final Object value)
    {
		throw new UnsupportedOperationException();
    }
}

