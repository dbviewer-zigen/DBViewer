/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.mysql.MySQLStatementFactory;
import zigen.plugin.db.core.rule.oracle.OracleStatementFactory;
import zigen.plugin.db.core.rule.symfoware.SymfowareStatementFactory;

public abstract class AbstractStatementFactory implements IStatementFactory {

	protected static String NULL = "null";

	protected boolean convertUnicode;

	public void setConvertUnicode(boolean convertUnicode) {
		this.convertUnicode = convertUnicode;
	}

	public static IStatementFactory getFactory(IDBConfig config) {
		return getFactory(config.getDriverName(), config.isConvertUnicode());
	}

	public static IStatementFactory getFactory(DatabaseMetaData objMet, boolean isConvertUnicode) {
		try {
			return getFactory(objMet.getDriverName(), isConvertUnicode);

		} catch (SQLException e) {
			throw new IllegalStateException("Faild get DriverName");
		}

	}

	private static Map map = new HashMap();

	public static IStatementFactory getFactory(String driverName, boolean isConvertUnicode) {

		IStatementFactory factory = null;

		String key = driverName + ":" + isConvertUnicode;

		if (map.containsKey(key)) {
			factory = (IStatementFactory) map.get(key);
			factory.setConvertUnicode(isConvertUnicode);
		} else {

			factory = getExtentionFactory(driverName, isConvertUnicode);
			if(factory == null){
				factory = getStandardFactory(driverName, isConvertUnicode);
			}

			map.put(key, factory);
		}
		return factory;

	}

	private static IStatementFactory getStandardFactory(String driverName, boolean isConvertUnicode){
		IStatementFactory factory = null;
		switch (DBType.getType(driverName)) {
		case DBType.DB_TYPE_ORACLE:
			factory = new OracleStatementFactory(isConvertUnicode);
			break;
		case DBType.DB_TYPE_MYSQL:
			factory = new MySQLStatementFactory(isConvertUnicode);
			break;
		case DBType.DB_TYPE_SYMFOWARE:
			factory = new SymfowareStatementFactory(isConvertUnicode);
			break;
		default:
			factory = new DefaultStatementFactory(isConvertUnicode);
			break;
	}
		return factory;
	}

	private static IStatementFactory getExtentionFactory(String driverName, boolean isConvertUnicode){
		final String ExtentionPointName = "statementFactoryPoint";
		final String ExtentionName = "factory";

		IStatementFactory factory = null;
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
							String wDriverName = driverName.toLowerCase();
							if(wDriverName.indexOf(name) >= 0){
								if(obj instanceof IStatementFactory){
									factory= (IStatementFactory)obj;
									factory.setConvertUnicode(isConvertUnicode);
									return factory;

								}else{
									DbPlugin.log("The mistake is found in plugin.xml.(implements IStatementFactory)");
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
}
