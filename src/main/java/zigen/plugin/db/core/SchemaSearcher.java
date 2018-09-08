/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import zigen.plugin.db.core.rule.DefaultSchemaSearchFactory;
import zigen.plugin.db.core.rule.ISchemaSearchFactory;

public class SchemaSearcher {

	public static String[] execute(Connection con) throws Exception {
		try {
			if (!isSupport(con)) {
				return new String[0];
			}else{
				ISchemaSearchFactory factory = DefaultSchemaSearchFactory.getFactory(con.getMetaData());
				List list = factory.getSchemaList(con);
				return (String[])list.toArray(new String[0]);				
			}
		} catch (SQLException e) {
			throw e;
		}
		
	}
	public static String[] execute(IDBConfig config) throws Exception {
		Connection con = Transaction.getInstance(config).getConnection();
		return execute(con);
	}

	public static boolean isSupport(Connection con) {
		try {
			ISchemaSearchFactory factory = DefaultSchemaSearchFactory.getFactory(con.getMetaData());
			return factory.isSupport();
		} catch (SQLException e) {
			return false;
		}
	}

//	public static void existSchemaName(Connection con, String target) throws SQLException {
//		ResultSet rs = null;
//		Statement st = null;
//		try {
//			DatabaseMetaData objMet = con.getMetaData();
//
//			if (!isSupport(con)) {
//				return;
//			}
//			String s = getSchemaSearchSql(con);
//			if (s != null) {
//				st = con.createStatement();
//				rs = st.executeQuery(s);
//			} else {
//				rs = objMet.getSchemas();
//			}
//			while (rs.next()) {
////				String wk = rs.getString("TABLE_SCHEM"); //$NON-NLS-1$
//				String wk = rs.getString(1); //$NON-NLS-1$
//				if(wk.equalsIgnoreCase(target)){
//					return;
//				}
//			}
//
//			if(s != null){
//				throw new SQLException("The schema doesn't exist.\n" + s);
//			}else{
//				throw new SQLException("The schema doesn't exist.");
//			}
//
//		} finally {
//			StatementUtil.close(st);
//			ResultSetUtil.close(rs);
//		}
//
//	}
}
