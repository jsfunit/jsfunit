package org.jboss.jsfunit.analysis.el;

import java.util.regex.Pattern;

import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * Sometimes you don't want a particular set of expressions to be checked.
 * Providing a SkipExpressionSpec gives you a simple way to express this set.
 *
 * A SkipExpressionSpec has 2 coordinates: a commons-io file filter, and a
 * regular expression string.  An empty spec (both coordinates null) will cause
 * everything to be skipped (this isn't really seen as a common use case, but
 * it keeps things conceptually simple).  Additions to the spec narrow the set
 * of skipped items.
 *
 * So, for instance, specifying a file filter, but no regular expression, will
 * cause all ELExpressions in the specified files to be skipped.  Specifying a
 * regular expression, but no file filter, will cause the ELExpressions that
 * match the regular expression to be skipped in all files.
 *
 * The reason we're choosing which expressions to skip, rather than those to
 * include, is that we're assuming that by default, we want an expression to be
 * checked.
 *
 * We're using regular expressions to filter ELExpressions because I'm lazy.
 *
 * @author Jason
 *
 */
public class SkipExpressionSpec
{
	private final IOFileFilter files;
	private final Pattern expr;
	public SkipExpressionSpec(final IOFileFilter files, final String expr)
	{
		this.files = files;
		this.expr = expr == null ? null : Pattern.compile(expr);
	}

	public IOFileFilter getFileFilter() {
		return files;
	}

	public Pattern getExpressionFilter() {
		return expr;
	}
}
