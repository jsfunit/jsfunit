package org.jboss.jsfunit.analysis.el;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;
import javax.faces.event.ActionEvent;

import junit.framework.TestCase;
import de.odysseus.el.ExpressionFactoryImpl;

/**
 * 
 * @author Jason
 * @since 1.0
 */
public class ELTestCase extends TestCase
{
	private final ELBundle expression;
	private final Map<String, Class<?>> beanMap;

	public ELTestCase(final ELBundle expression,
					  final Map<String, Class<?>> beanMap)
	{
		super(expression.getName());
		this.expression = expression;
		this.beanMap = beanMap;
	}

	public void testExpression() {
		final String el = expression.getExpression();
		final ExpressionFactory factory = new ExpressionFactoryImpl();
		final ELContext context = new JSFUnitELContext(beanMap, factory);

		final Object outputObj;
		try {
			final ValueExpression e = factory.createValueExpression(context, el, Object.class);
			outputObj = e.getValue(context);
		}
		catch(final PropertyNotFoundException pnfe) {
			elFail(pnfe);
			return; //stop complaining about unitialized methodObj.
		}
		catch(final ELException ee) {
			elFail(ee);
			return; //stop complaining about unitialized methodObj.
		}

		if(outputObj instanceof Method) {
			final Method method = (Method)outputObj;
			//TODO:  these assume the default action/actionListener restrictions
			if("action".equals(expression.getAttr())) {
				assertEquals(String.class, method.getReturnType());
				assertEquals(0, method.getParameterTypes().length);
			}
			else if("actionListener".equals(expression.getAttr())) {
				assertEquals(Void.TYPE, method.getReturnType());
				final Class<?>[] params = method.getParameterTypes();
				assertEquals(1, params.length);
				assertEquals(ActionEvent.class, params[0]);
			}
		}
		else if(outputObj instanceof Class<?>) {
			//yay.
		}
		else {
			resolveFail();
		}
	}

	private void resolveFail() {
		final String el = expression.getExpression();
		final File file = expression.getFile();
		final int start = expression.getStartIndex();
		fail(String.format("%s contains EL %s beginning at char %s, " +
				   "but this could not be resolved to a class or method.",
				   file, el, start));
	}

	private void elFail(final Throwable t) {
		final String el = expression.getExpression();
		final File file = expression.getFile();
		final int start = expression.getStartIndex();
		fail(String.format("Error for EL %s in file %s beginning at char %s: ",
				el, file, start) + t.getClass() + ": " + t.getMessage());
	}

	@Override
    public void runTest() {
		testExpression();
	}
}
