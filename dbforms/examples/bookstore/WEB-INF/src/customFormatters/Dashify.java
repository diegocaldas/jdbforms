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
 * @author Neal Katz
 *
 * the fmtArg is a string.
 * the string will be inserted between every digit in the input
 * when used any # is replaced by input.
 * ex. string="--" , input="1234", output="1--2--3--4--5"
 */
public class Dashify implements Formatter {
	Locale locale=null;
	String dashStr = null;
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
			for (int i = 0; i != s.length(); i++) {
				if (i>0) {
					r+=this.dashStr;
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
		dashStr = fmtArg;
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
