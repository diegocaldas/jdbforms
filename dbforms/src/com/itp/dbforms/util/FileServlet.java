/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <joepeer@excite.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.itp.dbforms.util;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.net.FileNameMap;
import java.net.URLConnection;
import com.itp.dbforms.*;
import org.apache.log4j.Category;

/**
#fixme - add appropriate exception-handling..
*/

public class FileServlet extends HttpServlet {

    static Category logCat = Category.getInstance(FileServlet.class.getName()); // logging category for this class

	private DbFormsConfig config;
	private FileNameMap fileNameMap;

  /**
   * Initialize this servlet.
   */

  public void init() throws ServletException {

		// take Config-Object from application context - this object should have been
		// initalized by Config-Servlet on Webapp/server-startup!
		config = (DbFormsConfig) getServletContext().getAttribute(DbFormsConfig.CONFIG);
		fileNameMap = URLConnection.getFileNameMap();
  }


	/**

	*/

	private void writeToClient(HttpServletResponse response, String fileName, InputStream is) throws IOException {


		String contentType  = fileNameMap.getContentTypeFor(fileName);

		logCat.info("writing to client:"+fileName+" ct="+contentType);

		response.setContentType(contentType);

		ServletOutputStream out = response.getOutputStream();

		byte[] b = new byte[1024];
		int read;
		while( (read = is.read(b)) != -1) {
			out.write(b,0,read);
		}
		out.close();
	}

    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response)
       throws    ServletException, IOException {

				Connection con = config.getDbConnection().getConnection();

				String tf = request.getParameter("tf");
				String keyValuesStr = request.getParameter("keyval");

				int tableId = Integer.parseInt(ParseUtil.getEmbeddedString(tf, 0, '_'));
			  Table table = config.getTable(tableId);

        int fieldId = Integer.parseInt(ParseUtil.getEmbeddedString(tf, 1, '_'));
        Field field = table.getField(fieldId);

        StringBuffer queryBuf = new StringBuffer();
        queryBuf.append("SELECT ");
        queryBuf.append(field.getName());
        queryBuf.append(" FROM ");
        queryBuf.append(table.getName());
        queryBuf.append(" WHERE ");
        queryBuf.append(table.getWhereClauseForPS());

				logCat.info("fs- "+queryBuf);

				try {

        	PreparedStatement ps = con.prepareStatement(queryBuf.toString());
        	table.populateWhereClauseForPS(keyValuesStr, ps, 1);
					ResultSet rs = ps.executeQuery();

					if(rs.next()) {


						if(field.getType() == FieldTypes.DISKBLOB) {

								String fileName = rs.getString(1);
								if(fileName!=null) fileName = fileName.trim();
								logCat.info("READING DISKBLOB field.getDirectory()="+field.getDirectory()+" "+"fileName="+fileName);
								File file = new File(field.getDirectory(), fileName);

								if(file.exists()) {
									logCat.info("fs- file found "+file.getName());

									FileInputStream fis = new FileInputStream(file);
									writeToClient(response, fileName, fis);

								} else
									logCat.info("fs- file not found");


						} else if(field.getType() == FieldTypes.BLOB) {

								logCat.info("READING BLOB");

								try {

									InputStream blobIS = rs.getBinaryStream(1);

									if(blobIS != null) {

										ObjectInputStream ois = new ObjectInputStream(blobIS);
										FileHolder fh = (FileHolder) ois.readObject();
										writeToClient(response, fh.getFileName(), fh.getInputStreamFromBuffer());
									} else logCat.warn("blob null, no response sent");

								} catch(ClassNotFoundException cnfe) {
									throw new IOException("error:"+cnfe.toString());
								}
						}



					} else {
						logCat.info("fs- we have got no result"+queryBuf);
					}


      	}	catch(SQLException sqle) {
					sqle.printStackTrace();
				} finally {
					try {
					  con.close();
					} catch(SQLException sqle2) {
						sqle2.printStackTrace();
					}
				}
    }

    //Process the HTTP Post request
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request,response);
    }


}

