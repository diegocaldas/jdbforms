package org.dbforms.util;

import java.util.ResourceBundle;
import java.util.Locale;
import java.util.HashMap;
import java.util.Enumeration;
import org.apache.log4j.Category;
import javax.servlet.http.*;

/**
 * 	This class have been created to cached ResourcesBundle in HashMap.
 *  The keys is created like the ResourcesBundle do : "language_country_variant".
 * 
 *  @author Eric Beaumier 
 */

/* 
 * 	Benchmark :
 * 		Call alone:
 * 		============
 * 			Using new ResourceBundle each time 			  	:	141 millis.
 * 			Using ResourceBundle cached with this class 	: 	 15 millis.
 * 
 * 		Call with few Threads:
 *		=======================
 * 		  	Using 500 Threads and a Loop of 100x getMessage() by Thread :
 * 				Without this class : 			4854 millis. (CPU usage : 100% during about 3.5 sec.)
 *   		   	By caching with this class :	 425 millis. (CPU usage : not enought time to reach 100%)	
 * 
 */


public class MessageResources {

	// LOCALE_KEY is not final.  Allowing to modify the session attribute name
	// for sharing with other apps.  Ex: to avoid to store two Locale, one with Struts
	// and one with DbForms.  By setting LOCALE_KEY to "org.apache.struts.action.LOCALE"
	// you can share the same Locale in the session scope.
	public static String LOCALE_KEY = "org.dbforms.LOCALE";

	static Category logCat = Category.getInstance(MessageResources.class.getName());

	/*********************************************************************************************
	 *  Use of HashMap for allowing null value (ReourceBundle)
	 *  and avoiding to call getBundle each time if resources file is not present.
	 ********************************************************************************************/
	private static HashMap hashResources = new HashMap();	
	
	
	private static String subClass = null;

	/*********************************************************************************************
	 *  Set the ResourceBundle subclass used by the getBundle() method
	 ********************************************************************************************/	
	public static void setSubClass(String res){
		subClass = res;
	}
	
	public static String getSubClass(){
		return subClass ;
	}

	/*********************************************************************************************
	 *  Get the message from ResourceBundle.  If not present, return the defaultMsg at
	 *  the place of a null.  To avoid to doing this condition everywhere in the code ...
	 * 
	 * @param  <code>String</code> : Message key to lookup.
	 * @param  <code>Locale</code> : Locale object to map message with good ResourceBundle. 
	 * @param  <code>String</code> : String to return if the lookup message key is not found.
	 *
     * @return	<code>String</code> : Message resolve.
	 ********************************************************************************************/	
	public static String getMessage(	String msg, 
										Locale locale,
										String defaultMsg){
											
		String result = getMessage( msg, locale);											
		if(result==null) return defaultMsg;
		return result;
	}
	
	/********************************************************************************************
	 *  Retrieve message from ResourceBundle.  If the ResourceBundle is not yet cached, 
	 *  cache it and retreive message.
	 * 
	 * 	@param  <code>String</code> : Message key to lookup.
	 * 	@param  <code>Locale</code> : Locale object to map message with good ResourceBundle. 
	 *
	 * 	@return	<code>String</code> : Message resolve, null if not found.
	 ********************************************************************************************/	
	
	public static String getMessage(String msg, Locale loc){
		
		if(subClass==null) return null;	
		
		ResourceBundle rb = null;

		// Faster than String (immuable) concatenation
		String key = new StringBuffer().append(loc.getLanguage()).append("_").append(loc.getCountry()).append("_").append(loc.getVariant()).toString();
		
		if(hashResources.containsKey(key)){
			rb = (ResourceBundle) hashResources.get(key);	
		} else {
			try {
				rb = ResourceBundle.getBundle( subClass, loc);
			}catch(Exception e){ }

			// Put the ResourceBundle or null value in HashMap with the key
			hashResources.put(key, rb);
		}
		
		try{
			return rb.getString(msg);
		}catch(Exception e){
			return null;
		}

		
	}
	
/*********************************************************************************************
 *  Retrieve message from ResourceBundle and replace parameter "{x}" with values in parms array.  
 *  
 * 	@param  <code>String</code> : Message key to lookup.
 * 	@param  <code>Locale</code> : Locale object to map message with good ResourceBundle. 
 * 	@param  <code>String[]</code> : Parameters to replace "{x}" in message . 
 *
 * 	@return	<code>String</code> : Message resolve with parameter replaced, null if message key not found.
 ********************************************************************************************/		
	public static String getMessage(	String msg, 
										Locale loc,
										String[] parms) {
		
		String result = getMessage( msg, loc);
		if(result==null) return null;
		
		String search = null;
		
		for(int i=0;i<parms.length;i++){
			search = "{"+i+"}";
			result = MessageResources.replaceAll(result, search, parms[i]);	
		}
		return result;
	}
	

	/*********************************************************************************************
    *  Replace all expression {...} by the appropriate string.
    *
	* @param  <code>String</code> : Original string.
	* @param  <code>String</code> : Expression to search.
	* @param  <code>String</code> : Replacement string.
	*
    * @return	<code>String</code> : The string with all expression replaced.
	********************************************************************************************/
  
 
 	public static String replaceAll(String str, String search, String replace) {
		
	    StringBuffer result = null;
	    int oldpos = 0;
	    
	    do {
	        int pos = str.indexOf(search, oldpos);
	        
	        if (pos < 0) break;
	        
	        if (result == null) result = new StringBuffer();
	        
	        result.append(str.substring(oldpos, pos));
	        
	        result.append(replace);

	        pos += search.length();
	        oldpos = pos;
	    } while (true);
	    
	    if (oldpos == 0) {
	        return str;
	    } else {
	        result.append(str.substring(oldpos));
	        return new String(result);
	    }
	}

	public static Locale getLocale(HttpServletRequest request){
		HttpSession session = request.getSession(); 
		if(session.getAttribute(MessageResources.LOCALE_KEY)==null){
			session.setAttribute(MessageResources.LOCALE_KEY, request.getLocale());
			return request.getLocale();
		} else {
			return (Locale) session.getAttribute(MessageResources.LOCALE_KEY);
		}
	}

	// Set this Locale in the session scope	
	public static void setLocale(HttpServletRequest request, Locale locale){
		HttpSession session = request.getSession(); 
		session.setAttribute(MessageResources.LOCALE_KEY, locale);
	}
}

