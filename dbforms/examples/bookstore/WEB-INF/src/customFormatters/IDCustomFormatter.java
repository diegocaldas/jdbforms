/*
 * Created on May 30, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package customFormatters;

import java.util.Locale;

import javax.servlet.jsp.tagext.Tag;

import org.dbforms.config.Field;
import org.dbforms.util.Formatter;

/**
 * @author cthon
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
/**
 * @author Neal Katz
 *
 * the fmtArg is a string in the form ####-####
 * when used any # is replaced by input.
 * ex. "###-##" and "12345" ==> "123-45" 
 */
public class IDCustomFormatter implements Formatter {
	Locale locale=null;
	String pattern = null;
	/* (non-Javadoc)
	 * @see org.dbforms.util.Formatter#sprintf(java.lang.Object[])
	 */
	public String sprintf(Object[] o) {
		String s = null;
		Field f = null;
		Tag tag = null;
		if ((o.length >= 1) && (o[0] instanceof String)) {
			s = (String) o[0];
		}
		if ((o.length >= 2) && (o[1] instanceof Field)) {
			f = (Field) o[1];
		}
		if ((o.length >= 3) && (o[2] instanceof Tag)) {
			tag = (Tag) o[2];
		}
		String r = "";
		if (s != null) {
			int i = 0, j = 0;
			char pch, ch;
			for (i = 0; i != s.length(); i++) {
				while ((j < pattern.length())
					&& ((pch = pattern.charAt(j++)) != '#')) {
					r += pch;
				}
				r += s.charAt(i);
			}
		}
		return r;
	}

	/* (non-Javadoc)
	 * @see org.dbforms.util.Formatter#setFormat(java.lang.String)
	 */
	public void setFormat(String fmtArg) throws IllegalArgumentException {
		pattern = fmtArg;
	}

	/* (non-Javadoc)
	 * @see org.dbforms.util.Formatter#getLocale()
	 */
	public Locale getLocale() {
		return locale;
	}

	/* (non-Javadoc)
	 * May be null
	 * @see org.dbforms.util.Formatter#setLocale(java.util.Locale)
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;

	}

}
