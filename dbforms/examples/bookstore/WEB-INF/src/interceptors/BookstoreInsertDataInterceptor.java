package interceptors;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dbforms.event.DbEventInterceptorSupport;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.ValidationException;
import org.dbforms.config.Table;
import org.dbforms.config.FieldValues;
import org.dbforms.config.FieldValue;

/**
 * @author hkk
 * 
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu
 * ändern: Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und
 * Kommentare
 */
public class BookstoreInsertDataInterceptor extends DbEventInterceptorSupport {

	public int preInsert(HttpServletRequest request, Table table,
			FieldValues fieldValues, DbFormsConfig config, Connection con)
			throws ValidationException {
		long new_id = 0;
		String fieldName = table.getName() + "_ID";
		FieldValue fv = fieldValues.get(fieldName);
		new_id = ((Integer) fv.getFieldValueAsObject()).intValue();
		if (new_id == 0) {
			String qry = "select max(" + fieldName + ") from "
					+ table.getName();
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(qry);
				rs.next();
				new_id = rs.getLong(1);
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			new_id++;
			setValue(table, fieldValues, fieldName, String.valueOf(new_id));
		}
		return GRANT_OPERATION;
	}

}
