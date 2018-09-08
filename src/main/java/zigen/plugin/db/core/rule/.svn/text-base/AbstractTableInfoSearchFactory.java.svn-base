package zigen.plugin.db.core.rule;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.TableInfo;
import zigen.plugin.db.core.rule.mysql.MySQLTableInfoSearchFactory;
import zigen.plugin.db.core.rule.oracle.OracleTableInfoSearchFactory;
import zigen.plugin.db.core.rule.symfoware.SymfowareTableInfoSearchFactory;


abstract public class AbstractTableInfoSearchFactory implements ITableInfoSearchFactory {

	private static Map map = new HashMap();

	public static ITableInfoSearchFactory getFactory(DatabaseMetaData meta) throws SQLException{
		ITableInfoSearchFactory factory = null;
		String key = meta.getDriverName();
		if (map.containsKey(key)) {
			factory = (ITableInfoSearchFactory) map.get(key);
			factory.setDatabaseMetaData(meta);
		} else {
			factory = getExtentionFactory(meta);
			if(factory == null){
				factory = getStandardFactory(meta);
			}
			map.put(key, factory);
		}

		return factory;

	}

	private static ITableInfoSearchFactory getStandardFactory(DatabaseMetaData meta) throws SQLException{
		ITableInfoSearchFactory factory;
		String key = meta.getDriverName();
		switch (DBType.getType(key)) {
		case DBType.DB_TYPE_ORACLE:
			factory = new OracleTableInfoSearchFactory();
			factory.setDatabaseMetaData(meta);
			break;
		case DBType.DB_TYPE_SYMFOWARE:
			factory = new SymfowareTableInfoSearchFactory();
			factory.setDatabaseMetaData(meta);
			break;
		case DBType.DB_TYPE_MYSQL:
			factory = new MySQLTableInfoSearchFactory();
			factory.setDatabaseMetaData(meta);
			break;

		default:
			factory = new DefaultTableInfoSearchFactory();
			factory.setDatabaseMetaData(meta);
			break;
		}
		return factory;
	}

	private static ITableInfoSearchFactory getExtentionFactory(DatabaseMetaData meta) throws SQLException{
			final String ExtentionPointName = "tableInfoSearchFactoryPoint";
			final String ExtentionName = "factory";

			ITableInfoSearchFactory factory = null;
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint point = registry.getExtensionPoint(DbPlugin.getDefault().getBundle().getSymbolicName() + "." + ExtentionPointName);
			if(point != null){
				IExtension[] extensions = point.getExtensions();
				for (int i = 0; i < extensions.length; i++) {
					IConfigurationElement[] cfgElems = extensions[i].getConfigurationElements();
					for (int j = 0; j < cfgElems.length; j++) {
						IConfigurationElement cfgElem = cfgElems[j];
						if(ExtentionName.equals(cfgElem.getName())){
							try {
								String name = cfgElem.getAttribute("name").toLowerCase();
								Object obj = cfgElem.createExecutableExtension("class");
								String wDriverName = meta.getDriverName().toLowerCase();
								if(wDriverName.indexOf(name) >= 0){
									if(obj instanceof ITableInfoSearchFactory){
										factory= (ITableInfoSearchFactory)obj;
										factory.setDatabaseMetaData(meta);
										return factory;

									}else{
										DbPlugin.log("The mistake is found in plugin.xml.(implements ITableInfoSearchFactory)");
									}
								}
							} catch (CoreException e) {
								DbPlugin.log(e);
							}
						}
					}
				}
			}
			return factory;
	}

	public List getTableInfoAll(Connection con, String owner, String[] types, Character encloseChar) throws Exception {
		List result = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			st = con.createStatement();
			String query = getTableInfoAllSql(owner, types);
			if (query != null) {
				result = new ArrayList();
				rs = st.executeQuery(query);
				while (rs.next()) {
					TableInfo info = new TableInfo();
					info.setName(rs.getString("TABLE_NAME"));
					info.setTableType(rs.getString("TABLE_TYPE"));
					info.setComment(rs.getString("REMARKS"));
					result.add(info);
				}
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
		try {
			st = con.createStatement();
			String query = getTableInfoSql(owner, tableName, type);
			if (query != null) {
				rs = st.executeQuery(query);
				if (rs.next()) {
					info = new TableInfo();
					info.setName(rs.getString("TABLE_NAME"));
					info.setTableType(rs.getString("TABLE_TYPE"));
					info.setComment(rs.getString("REMARKS"));
				}
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


	protected DatabaseMetaData meta;

	public void setDatabaseMetaData(DatabaseMetaData meta){
		this.meta = meta;
	}
	abstract public String getDbName();
	abstract protected String getTableInfoAllSql(String owner, String[] types);
	abstract protected String getTableInfoSql(String owner, String tableName, String type);

}
