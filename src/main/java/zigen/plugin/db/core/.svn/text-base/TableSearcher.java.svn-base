/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import zigen.plugin.db.core.rule.AbstractTableInfoSearchFactory;
import zigen.plugin.db.core.rule.ITableInfoSearchFactory;

public class TableSearcher {

	public static TableInfo[] execute(Connection con, String schemaPattern, String[] types) throws Exception {
		return execute(con, schemaPattern, types, null);
	}

	public static TableInfo execute(Connection con, String schemaPattern, String tablePattern, String type) throws Exception {
		return execute(con, schemaPattern, tablePattern, type, null);
	}

	public static TableInfo[] execute(Connection con, String schemaPattern, String[] types, Character encloseChar) throws Exception {

		List list = new ArrayList();
		ResultSet rs = null;
		Statement st = null;
		try {
			DatabaseMetaData objMet = con.getMetaData();
			ITableInfoSearchFactory factory = AbstractTableInfoSearchFactory.getFactory(objMet);
			list = factory.getTableInfoAll(con, schemaPattern, types, encloseChar);
			
			Collections.sort(list, new TableInfoSorter());
			return (TableInfo[]) list.toArray(new TableInfo[0]);

		} catch (Exception e) {
			throw e;

		} finally {
			StatementUtil.close(st);
			ResultSetUtil.close(rs);

		}

	}


	public static TableInfo execute(Connection con, String schemaPattern, String tablePattern, String type, Character encloseChar) throws Exception {
		TableInfo info = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			DatabaseMetaData objMet = con.getMetaData();
			ITableInfoSearchFactory factory = AbstractTableInfoSearchFactory.getFactory(objMet);
			info = factory.getTableInfo(con, schemaPattern, tablePattern, type, encloseChar);
			return info;

		} catch (Exception e) {
			throw e;

		} finally {
			StatementUtil.close(st);
			ResultSetUtil.close(rs);

		}

	}
}

class TableInfoSorter implements Comparator {

	public TableInfoSorter() {}

	public int compare(Object o1, Object o2) {

		String firstType = ((TableInfo) o1).getTableType();
		String secondType = ((TableInfo) o2).getTableType();

		if (firstType.equals(secondType)) {
			return 0;
		} else if (firstType.equals("TABLE")) { //$NON-NLS-1$
			return -1;
		} else {
			return (firstType.compareTo(secondType)) * -1;
		}
	}
}
