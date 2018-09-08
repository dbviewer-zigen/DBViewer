package zigen.plugin.db.core.rule;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.SchemaSearcher;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.TableInfo;


public class DefaultTableInfoSearchFactory extends AbstractTableInfoSearchFactory{

	protected String getTableInfoAllSql(String owner, String[] types) {
		return null;   // no execute sql
	}
	protected String getTableInfoSql(String owner, String tableName, String type) {
		return null;   // no execute sql
	}

	public List getTableInfoAll(Connection con, String owner, String[] types, Character encloseChar) throws Exception {
		List result = null;
		ResultSet rs = null;
		Statement st = null;
		DatabaseMetaData objMet = null;
		try {
			st = con.createStatement();
			result = new ArrayList();
			objMet = con.getMetaData();
			if (SchemaSearcher.isSupport(con)) {
				rs = objMet.getTables(null, owner, "%", types); //$NON-NLS-1$
			} else {
				rs = objMet.getTables(null, "%", "%", types); //$NON-NLS-1$ //$NON-NLS-2$
			}
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME"); //$NON-NLS-1$
				TableInfo info = new TableInfo();
				if (encloseChar != null) {
					info.setName(SQLUtil.enclose(tableName, encloseChar.charValue()));
				} else {
					info.setName(tableName);
				}
				info.setTableType(rs.getString("TABLE_TYPE")); //$NON-NLS-1$
				info.setComment(rs.getString("REMARKS")); //$NON-NLS-1$
				result.add(info);
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}
		return result;
	}
	public TableInfo getTableInfo(Connection con, String owner, String tableName, String type, Character encloseChar) throws Exception {
		TableInfo info = null;
		ResultSet rs = null;
		Statement st = null;
		DatabaseMetaData objMet = null;
		try {
			st = con.createStatement();
			objMet = con.getMetaData();
			if (SchemaSearcher.isSupport(con)) {
				rs = objMet.getTables(null, owner, tableName, new String[]{type}); //$NON-NLS-1$
			} else {
				rs = objMet.getTables(null, "%", tableName, new String[]{type}); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (rs.next()) {
				String wTableName = rs.getString("TABLE_NAME"); //$NON-NLS-1$
				info = new TableInfo();
				if (encloseChar != null) {
					info.setName(SQLUtil.enclose(wTableName, encloseChar.charValue()));
				} else {
					info.setName(tableName);
				}
				info.setTableType(rs.getString("TABLE_TYPE")); //$NON-NLS-1$
				info.setComment(rs.getString("REMARKS")); //$NON-NLS-1$

			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}
		return info;
	}
	public String getDbName() {
		return null;
	}

}
