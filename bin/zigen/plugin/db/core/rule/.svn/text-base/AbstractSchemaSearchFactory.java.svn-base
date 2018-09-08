package zigen.plugin.db.core.rule;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
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
import zigen.plugin.db.core.rule.mysql.MySQLSchemaSearchFactory;
import zigen.plugin.db.core.rule.symfoware.SymfowareSchemaSearchFactory;


abstract public class AbstractSchemaSearchFactory implements ISchemaSearchFactory {

	private static Map map = new HashMap();

	protected DatabaseMetaData meta;

	public void setDatabaseMetaData(DatabaseMetaData meta){
		this.meta = meta;
	}

	public static ISchemaSearchFactory getFactory(DatabaseMetaData meta) throws SQLException{
		ISchemaSearchFactory factory = null;
		String key = meta.getDriverName();
		if (map.containsKey(key)) {
			factory = (ISchemaSearchFactory) map.get(key);
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

	private static ISchemaSearchFactory getStandardFactory(DatabaseMetaData meta) throws SQLException{
		ISchemaSearchFactory factory;
		String key = meta.getDriverName();
		switch (DBType.getType(key)) {
		case DBType.DB_TYPE_SYMFOWARE:
			factory = new SymfowareSchemaSearchFactory();
			factory.setDatabaseMetaData(meta);
			break;
		case DBType.DB_TYPE_MYSQL:
			factory = new MySQLSchemaSearchFactory();
			factory.setDatabaseMetaData(meta);
			break;

		default:
			factory = new DefaultSchemaSearchFactory();
			factory.setDatabaseMetaData(meta);
			break;
		}
		return factory;
	}

	private static ISchemaSearchFactory getExtentionFactory(DatabaseMetaData meta) throws SQLException{
			final String ExtentionPointName = "schemaSearchFactoryPoint";
			final String ExtentionName = "factory";

			ISchemaSearchFactory factory = null;
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
									if(obj instanceof ISchemaSearchFactory){
										factory= (ISchemaSearchFactory)obj;
										factory.setDatabaseMetaData(meta);
										return factory;

									}else{
										DbPlugin.log("The mistake is found in plugin.xml.(implements ISchemaSearchFactory)");
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

	abstract public List getSchemaList(Connection con) throws Exception;

	abstract public boolean isSupport();

	abstract protected String getSchemaSearchSql(String dbName);


}
