package org.dbforms.event;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.ValidationException;
import org.dbforms.config.Table;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.event.DbEventInterceptorSupport;
import org.dbforms.util.FileHolder;

/**
 * This Interceptor can be used to automatically store the filenames of BLOBS in
 * some table column specified, so that it is not lost.
 * 
 * Currently, the interceptor must be configured manually in dbforms-config.xml
 * inside the adequate table definition (see user guide)  
 *
 * The interceptor MUST be initialized with the following parameters:
 * <ul>
 * <li><b>blob-column</b> - the name of the BLOB field
 * <li><b>blob-name</b> - a character field to store the FILENAME of the uploaded file
 * </ul>
 *
 * if the table contains multiple BLOBs, then a unique integer n has to be appended
 * to blob-column and blob-name to associate the correct pairs of BLOB and NAME fields.
 * 
 * Usage Examples:
 *
 * <p>if one BLOB:</p>
 *<p>
 *&lt;interceptor className="org.dbforms.event.BlobInterceptor"&gt;<br>
 *&nbsp;&nbsp;&lt;param name="blob-column" value="file"/&gt;<br>
 *&nbsp;&nbsp;&lt;param name="name-column" value="filename"/&gt;<br>  
 *&lt;/interceptor&gt;<br>
 *</p>
 *
 * </p>if mulitple BLOBs:</p>
 *<p>
 *&lt;interceptor className="org.dbforms.event.BlobInterceptor"&gt;<br>
 *&nbsp;&nbsp;&lt;param name="blob-column1" value="file"/&gt;<br>
 *&nbsp;&nbsp;&lt;param name="name-column1" value="filename"/&gt;<br>  
 *&nbsp;&nbsp;&lt;param name="blob-column2" value="otherfile"/&gt;<br>
 *&nbsp;&nbsp;&lt;param name="name-column2" value="otherfilename"/&gt;<br>
 *&nbsp;&nbsp;&lt;param name="blob-column3" value="foofile"/&gt;<br>
 *&nbsp;&nbsp;&lt;param name="name-column3" value="foofilename"/&gt;<br>    
    
 *&lt;/interceptor&gt;<br>
 *</p>
 * 
 * @author Joe Peer
 */

public class BlobInterceptor extends DbEventInterceptorSupport {

	protected HashMap blobFieldData;
	protected final int BLOB_COL = 0;
	protected final int NAME_COL = 1;		
	
	/**
	 * takes params, sorts/groups and stores for later use
	 */
	public void setParams(Map params) {
		super.setParams(params);
		blobFieldData = new HashMap();
		for(Iterator iter = params.entrySet().iterator(); iter.hasNext(); ) {
			Map.Entry me = (Map.Entry) iter.next();
			String key = (String) me.getKey();
			String value = (String) me.getValue();
			if(key.startsWith("blob-column")) {
				Integer ii = getSuffixAsInteger(key, "blob-column");
				String[] s = (String[]) blobFieldData.get(ii);
				if(s == null) {
					s = new String[2];
					blobFieldData.put(ii, s);					
				}
				s[BLOB_COL] = value;
			} else if(key.startsWith("name-column")) {
				Integer ii = getSuffixAsInteger(key, "name-column");
				String[] s = (String[]) blobFieldData.get(ii);
				if(s == null) {
					s = new String[2];
					blobFieldData.put(ii, s);
				}
				s[NAME_COL] = value;
			}
		}
	}
	
	/**
	 * utility function 
	 */
	protected Integer getSuffixAsInteger(String value, String stub) {
		String suffix = value.substring(stub.length());
		if(suffix.length()==0) return new Integer(Integer.MIN_VALUE); 
		else return new Integer(suffix);
	}

	/**
	 * goes through params and makes sure that the fileholder's file name 
	 * info associated to the blob field BLOB_COLUMN is stored in the NAME_COLUMN 
	 * field.
	 */	
	protected void assignBlobData(Table table, FieldValues fieldValues) {
		for(Iterator iter = blobFieldData.values().iterator(); iter.hasNext(); ) {
			String[] s = (String[]) iter.next();
			FieldValue fv = fieldValues.get(s[BLOB_COL]);
			if(fv != null) {
				Object o = fv.getFieldValueAsObject();
				if(o != null && o instanceof FileHolder) {
					String fileName = ((FileHolder) o).getFileName();
					setValue(table, fieldValues, s[NAME_COL], fileName);
				}
			}
		}
	}
	
	/**
	 * stores filenames of all blobs in the NAME_COLUMNs specified
	 */
	public int preInsert(HttpServletRequest request,  Table table, FieldValues fieldValues,
		DbFormsConfig config, Connection con) throws ValidationException {
		assignBlobData(table, fieldValues);								
    return GRANT_OPERATION;
  }

	/**
	 * stores filenames of all blobs in the NAME_COLUMNs specified
	 */	
  public int preUpdate(HttpServletRequest request, Table table, FieldValues fieldValues, 
	   DbFormsConfig config, Connection con)
     throws ValidationException
  {
		assignBlobData(table, fieldValues);
    return GRANT_OPERATION;
  }

}