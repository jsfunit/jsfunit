package org.jboss.jsfunit.analysis.el;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

/**
 * Convenience class for some common filters.
 * @author Jason
 */
public class Filters
{
	public static final IOFileFilter XHTML_FILTER = new SuffixFileFilter(".xhtml");
	public static final IOFileFilter JSP_FILTER = new SuffixFileFilter(".jsp");
	public static final IOFileFilter JSPX_FILTER = new SuffixFileFilter(".jspx");

	public static final IOFileFilter IGNORE_HIDDEN_AND_CVS_FILTER
		= FileFilterUtils.makeCVSAware(HiddenFileFilter.VISIBLE);
}
