package interceptors;

import javax.servlet.http.HttpServletRequest;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.ValidationException;
import org.dbforms.config.Table;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.event.DbEventInterceptorSupport;
import org.dbforms.util.ParseUtil;

import java.sql.Connection;

/**
 * @author Viviana
 * @version
 */
public class BLOBTESTEventInterceptor extends DbEventInterceptorSupport {

	public int preUpdate(HttpServletRequest request, Table table,
			FieldValues fieldValues, DbFormsConfig config, Connection con)
			throws ValidationException {

		String deleteImage1 = ParseUtil.getParameter(request, "delete_image1");

		if ("true".equalsIgnoreCase(deleteImage1)) {

			//here something that deletes the field "FILE" of the "BLOBTEST"
			// table
			//without deleting the entire record (in my table I have a lot of
			// others fields
			//I would preserve)

			//this way don't work for me
			FieldValue fv = fieldValues.get("FILE");
			fv.setFileHolder(null);
			//this way don't work for me
			setValue(table, fieldValues, "FILE", null);

		}
		return GRANT_OPERATION;
	}

}