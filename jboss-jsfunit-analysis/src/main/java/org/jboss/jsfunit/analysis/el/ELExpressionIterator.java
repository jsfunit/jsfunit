package org.jboss.jsfunit.analysis.el;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Jason
 * @since 1.0
 */
public class ELExpressionIterator implements Iterator<ELBundle>
{
	private static final Pattern EL_EXPRESSION_PATTERN =
		Pattern.compile( "(?:\\s(\\S+?)\\s*=)?" + //Optional attribute name
						 "\\s*[\"']?\\s*" + //Separator junk
						 "([#$]\\{(.*?)})", //EL expression
						 Pattern.MULTILINE );

	private final File file;
	private ELBundle next;
	private final Matcher matcher;
	final Collection<Pattern> skipPatterns;

	private static CharBuffer fileAsCharBuffer(final File f)
		throws IOException
	{
		final FileChannel fc = new FileInputStream(f).getChannel();
		final ByteBuffer bb =
			fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());
		final CharBuffer cb = Charset.forName("UTF-8").newDecoder().decode(bb);
		return cb;
	}

	public ELExpressionIterator(final File file,
					  final Collection<Pattern> skipPatterns)
		throws IOException
	{
		this.file = file;
		this.skipPatterns = skipPatterns;
		matcher = EL_EXPRESSION_PATTERN.matcher(fileAsCharBuffer(file));
	}

	private ELBundle makeELBundle(final MatchResult matchResult) {
		final String expression, context;
		switch(matchResult.groupCount()) {
		//TODO:  docs say group 0 not included in the count, which seems
		//wrong?
		case 2:
			//just an EL expression, no attribute.
			context = "";
			expression = matchResult.group(1);
			break;
		case 3:
			context = matchResult.group(1);
			expression = matchResult.group(2);
			break;
		default:
			final StringBuilder err = new StringBuilder("Unexpected match result:");
			for(int i=0;i<matchResult.groupCount();i++) {
				err.append(" " + i +": " + matchResult.group(i));
			}
			throw new RuntimeException(err.toString());
		}
		return new ELBundle(expression, file, context, matchResult.start());
	}

	public boolean hasNext()
    {
		top:
		while(next == null && matcher.find()) {
			for(final Pattern p: skipPatterns) {
				if(p.matcher(matcher.group()).find()) {
					continue top;
				}
			}
			next = makeELBundle(matcher.toMatchResult());
		}
		return next != null;
    }

	public ELBundle next()
    {
        if(hasNext()) {
        	final ELBundle out = next;
        	next = null;
        	return out;
        }
        else {
        	throw new NoSuchElementException();
        }
    }

	public void remove()
    {
		throw new UnsupportedOperationException();
    }

}
