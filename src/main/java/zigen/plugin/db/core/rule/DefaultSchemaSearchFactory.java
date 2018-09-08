package zigen.plugin.db.core.rule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.StatementUtil;


public class DefaultSchemaSearchFactory extends AbstractSchemaSearchFactory{

	public List getSchemaList(Connection con) throws Exception {
		List result = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			String query = getSchemaSearchSql(ConnectionManager.getDBName(con));
			if (query != null) {
				st = con.createStatement();
				rs = st.executeQuery(query);
			} else {
				rs = meta.getSchemas();
			}
			result = new ArrayList();
			while (rs.next()) {
				String wk = rs.getString(1); //$NON-NLS-1$
				result.add(wk);
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

	protected String getSchemaSearchSql(String dbName){
		return null;
	}

	public boolean isSupport() {
		try {
			return meta.supportsSchemasInTableDefinitions();
		} catch (SQLException e) {
			return false;
		}
	}
}
