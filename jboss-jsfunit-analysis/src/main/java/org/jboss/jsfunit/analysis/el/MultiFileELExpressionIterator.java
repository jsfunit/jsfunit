package org.jboss.jsfunit.analysis.el;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.collection.CompositeCollection;
import org.apache.commons.io.filefilter.IOFileFilter;

public class MultiFileELExpressionIterator implements Iterator<ELBundle>
{
	private final Iterator<File> fileIter;
	private Iterator<ELBundle> currIter = ELIterFactory.EMPTY_ITER;
	private final Collection<Pattern> alwaysSkipPatterns;
	private final Map<IOFileFilter, Pattern> sometimesSkipPatterns;
	private final ArrayList<Pattern> skipBuffer;

    @SuppressWarnings("unchecked")
    private static <T> Collection<T> unmodifiableComposite(final Collection<T> coll1, final Collection<T> coll2) {
    	final CompositeCollection compColl = new CompositeCollection();
    	compColl.addComposited(coll1, coll2);
		return Collections.unmodifiableCollection(compColl);
	}

	public MultiFileELExpressionIterator(final Iterator<File> fileIter,
							   final Collection<Pattern> alwaysSkipPatterns,
							   final Map<IOFileFilter, Pattern> sometimesSkipPatterns)
	{
		this.fileIter = fileIter;
		this.alwaysSkipPatterns = alwaysSkipPatterns;
		this.sometimesSkipPatterns = sometimesSkipPatterns;
		final int buffersz = sometimesSkipPatterns.size();
		skipBuffer = new ArrayList<Pattern>(buffersz);
	}

	private Collection<Pattern> getSkipPatterns(final File file) {
		skipBuffer.clear();
		for(final Map.Entry<IOFileFilter, Pattern> e: sometimesSkipPatterns.entrySet()) {
			final IOFileFilter filter = e.getKey();
			if(filter.accept(file.getParentFile(), file.getName())) {
				skipBuffer.add(e.getValue());
			}
		}
		return unmodifiableComposite(alwaysSkipPatterns, skipBuffer);
	}

	public boolean hasNext()
    {
		while(!currIter.hasNext() && fileIter.hasNext()) {
			try {
				final File file = fileIter.next();
				currIter = new ELExpressionIterator(file, getSkipPatterns(file));
			} catch(final IOException ioe) {
				//TODO: should we just go on to the next file instead?
				throw new RuntimeException(ioe);
			}
		}
		return currIter.hasNext();
    }

	public ELBundle next()
    {
		hasNext(); //iter may need to cycle to next file.
		return currIter.next();
    }

	public void remove()
    {
		throw new UnsupportedOperationException();
    }
}
