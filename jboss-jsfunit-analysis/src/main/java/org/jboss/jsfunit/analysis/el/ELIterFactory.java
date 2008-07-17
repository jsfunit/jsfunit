package org.jboss.jsfunit.analysis.el;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;

public class ELIterFactory
{
	public static final Iterator<ELBundle> EMPTY_ITER = new Iterator<ELBundle>() {
		public boolean hasNext()
        {
	        return false;
        }

		public ELBundle next()
        {
	        throw new NoSuchElementException();
        }

		public void remove()
        {
			throw new UnsupportedOperationException();
        }
	};


	/**
	 * Uses a default dirFilter which skips hidden and CVS directories.
	 */
	public static Iterable<ELBundle> getIterable( final File baseDir,
												  final IOFileFilter fileFilter,
												  final Collection<SkipExpressionSpec> toSkip)
	{
		return new Iterable<ELBundle>() {
			public Iterator<ELBundle> iterator()
            {
				return ELIterFactory.getIterator(baseDir, fileFilter, toSkip);
            }
		};
	}

	public static Iterable<ELBundle> getIterable( final File baseDir,
												  final IOFileFilter fileFilter,
												  final IOFileFilter dirFilter,
												  final Collection<SkipExpressionSpec> toSkip)
	{
		return new Iterable<ELBundle>() {
			public Iterator<ELBundle> iterator()
            {
				return ELIterFactory.getIterator(baseDir, fileFilter, dirFilter, toSkip);
            }
		};
	}

	/**
	 * Uses a default dirFilter which skips hidden and CVS directories.
	 */
	public static Iterator<ELBundle> getIterator( final File baseDir,
															final IOFileFilter fileFilter,
															final Collection<SkipExpressionSpec> toSkip)
	{
		return getIterator(baseDir, fileFilter, Filters.IGNORE_HIDDEN_AND_CVS_FILTER, toSkip);
	}

	public static Iterator<ELBundle> getIterator(	final File baseDir,
															final IOFileFilter fileFilter,
															final IOFileFilter dirFilter,
															final Collection<SkipExpressionSpec> toSkip)
	{
		final OrFileFilter alwaysSkipFiles = new OrFileFilter();
		final Collection<Pattern> alwaysSkipPatterns = new HashSet<Pattern>();
		final Map<IOFileFilter, Pattern> sometimesSkipPatterns = new HashMap<IOFileFilter, Pattern>();
		for( final SkipExpressionSpec spec: toSkip ) {
			final IOFileFilter ff = spec.getFileFilter();
			final Pattern ef = spec.getExpressionFilter();
			if(ff == null) {
				if(ef == null) {
					return EMPTY_ITER;  //both exprs null == skip everything.
				}
				else {
					alwaysSkipPatterns.add(ef);
				}
			}
			else if(ef == null) {
				alwaysSkipFiles.addFileFilter(ff);
			}
			else {
				sometimesSkipPatterns.put(ff, ef);
			}
		}
		final IOFileFilter compositeFilter
			= new AndFileFilter(fileFilter,
								new NotFileFilter(alwaysSkipFiles));
		final Iterator<?> baseIter
			= FileUtils.iterateFiles(baseDir, compositeFilter, dirFilter);
		return new MultiFileELExpressionIterator(asFileIter(baseIter), alwaysSkipPatterns, sometimesSkipPatterns);
	}

	@SuppressWarnings("unchecked")
    private static Iterator<File> asFileIter(final Iterator<?> baseIter) {
		return (Iterator<File>)baseIter;
	}


}
