package zigen.plugin.db.core.rule;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;
import zigen.plugin.db.core.TableInfo;


public interface ITableInfoSearchFactory {

	public List getTableInfoAll(Connection con, String owner, String[] types, Character encloseChar) throws Exception;

	public TableInfo getTableInfo(Connection con, String owner, String tableName, String type, Character encloseChar) throws Exception;

	public String getDbName();

	public void setDatabaseMetaData(DatabaseMetaData meta);
}
