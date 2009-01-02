package org.jboss.jsfunit.analysis.el;

import java.io.File;

/**
 * An EL-expression bundled with information about its physical location.
 *
 * @author Jason
 * @since 1.0
 */
public class ELBundle
{
	private final String expression, attr;
	private final File file;
	private final int startIndex;

	public ELBundle(final String expression, final File file, final String attr,
			final int startIndex)
	{
		this.expression = expression;
		this.file = file;
		this.attr = attr;
		this.startIndex = startIndex;
	}

	public String getExpression() {
		return expression;
	}

	public File getFile() {
		return file;
	}

	public String getAttr() {
		return attr;
	}

	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * Convenience method that returns a uniquely identifying name for the
	 * expression.
	 *
	 * @return A uniquely identifying name.
	 */
	public String getName() {
		return String.format("%s-%s", file, startIndex);
	}
}
