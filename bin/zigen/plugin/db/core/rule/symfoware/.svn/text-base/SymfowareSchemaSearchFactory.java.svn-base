package zigen.plugin.db.core.rule.symfoware;

import zigen.plugin.db.core.rule.DefaultSchemaSearchFactory;

public class SymfowareSchemaSearchFactory extends DefaultSchemaSearchFactory{
	
	protected String getSchemaSearchSql(String dbName){
		return "SELECT TRIM(SCHEMA_NAME) AS TABLE_SCHEM FROM RDBII_SYSTEM.RDBII_SCHEMA WHERE DB_NAME = '"+dbName+"'";
	}

}
