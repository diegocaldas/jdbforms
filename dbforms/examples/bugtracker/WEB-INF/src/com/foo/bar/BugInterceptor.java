package com.foo.bar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.ValidationException;
import org.dbforms.config.Table;
import org.dbforms.config.FieldValues;
import org.dbforms.event.DbEventInterceptorSupport;

/**
 * 
 * Litte example of a database event listener/interceptor
 * -----------------------------------------------------------
 * 
 * We could do virtually _anything_ with this class, as we have - a database
 * connection (-> we could "trigger" other functionality like
 * datebase-logging,...) - a config object (-> config.getServletContext() gives
 * access to other J2EE-resources) - a request object (-> gives access to many
 * kinds of parameter info) - and in the preXxx()-methods there is a hashtable
 * containg the actual row fieds. - in preInsert() and preUpdate() changes to
 * this hashtable are automatically reflected in the database!
 * 
 * In this concrete example we decide to just override 2 methods (preInsert and
 * preUpdate) to do some basic validation checking. But keep in mind: this class
 * has _much_ more potential for use in your apps
 *  
 */

public class BugInterceptor extends DbEventInterceptorSupport {

	/*
	 * private int checkCustomer(Hashtable fieldValues) throws
	 * ValidationException { // PHP/Perl programmers: the hashtabe works just
	 * like an "associative array" in PHP/Perl
	 * 
	 * String lastName = (String) fieldValues.get("lastname"); String pCode =
	 * (String) fieldValues.get("pcode"); String city = (String)
	 * fieldValues.get("city");
	 * 
	 * if( lastName == null || lastName.trim().length()==0 || pCode == null ||
	 * pCode.trim().length()==0 || city == null || city.trim().length()==0) {
	 * 
	 * throw new ValidationException("Please fill out the form completly!"); }
	 * else return GRANT_OPERATION; }
	 */

	public int preInsert(HttpServletRequest request, Table table,
			FieldValues fieldValues, DbFormsConfig config, Connection con)
			throws ValidationException {

		Calendar calendar = new GregorianCalendar();
		java.util.Date date = new java.util.Date();
		calendar.setTime(date);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;

		StringBuffer dateBuf = new StringBuffer();
		dateBuf.append(year);
		dateBuf.append("-");
		dateBuf.append(month);
		dateBuf.append("-");
		dateBuf.append(day);

		setValue(table, fieldValues, "indate", dateBuf.toString());
		setValue(table, fieldValues, "bugstate", "0");

		return GRANT_OPERATION;
	}

	public int preUpdate(HttpServletRequest request, Table table,
			FieldValues fieldValues, DbFormsConfig config, Connection con)
			throws ValidationException {

		int newState = Integer.parseInt(fieldValues.get("bugstate")
				.getFieldValue());
		if (newState == 2) {

			Calendar calendar = new GregorianCalendar();
			java.util.Date date = new java.util.Date();
			calendar.setTime(date);

			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;

			StringBuffer dateBuf = new StringBuffer();
			dateBuf.append(year);
			dateBuf.append("-");
			dateBuf.append(month);
			dateBuf.append("-");
			dateBuf.append(day);

			setValue(table, fieldValues, "outdate", dateBuf.toString());
		}

		return GRANT_OPERATION;

	}

}
