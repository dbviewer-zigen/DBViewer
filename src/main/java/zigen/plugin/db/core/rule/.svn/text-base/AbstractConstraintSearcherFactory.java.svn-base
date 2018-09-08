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
import zigen.plugin.db.core.rule.mysql.MySQLConstraintSearcharFactory;
import zigen.plugin.db.core.rule.oracle.OracleConstraintSearcharFactory;

public abstract class AbstractConstraintSearcherFactory implements IConstraintSearcherFactory {

	public static final String getIndexTypeName(int indexType) {
		switch (indexType) {
		case TABLE_INDEX_STATISTIC:
			return "TABLE_INDEX_STATISTIC"; //$NON-NLS-1$
		case TABLE_INDEX_CLSTERED:
			return "TABLE_INDEX_CLSTERED"; //$NON-NLS-1$
		case TABLE_INDEX_HASHED:
			return "TABLE_INDEX_HASHED"; //$NON-NLS-1$
		case TABLE_INDEX_OTHER:
			return "TABLE_INDEX_OTHER"; //$NON-NLS-1$
		default:
			return "TABLE_INDEX_UNKNOWN"; //$NON-NLS-1$
		}
	}

	public static IConstraintSearcherFactory getFactory(IDBConfig config) {
		return getFactory(config.getDriverName());
	}

	public static IConstraintSearcherFactory getFactory(DatabaseMetaData objMet) {
		try {
			return getFactory(objMet.getDriverName());

		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}

	}

	private static Map map = new HashMap();

	public static IConstraintSearcherFactory getFactory(String driverName) {
		IConstraintSearcherFactory factory = null;

		String key = driverName;

		if (map.containsKey(key)) {
			factory = (IConstraintSearcherFactory) map.get(key);
		} else {

			factory = getExtentionFactory(driverName);
			if(factory == null){
				factory = getStandardFactory(driverName);
			}
			map.put(key, factory);
		}

		return factory;

	}
	private static IConstraintSearcherFactory getStandardFactory(String driverName){
		IConstraintSearcherFactory factory = null;
		switch (DBType.getType(driverName)) {
		case DBType.DB_TYPE_ORACLE:
			factory = new OracleConstraintSearcharFactory();
			break;
		case DBType.DB_TYPE_MYSQL:
			factory = new MySQLConstraintSearcharFactory();
			break;
		default:
			factory = new DefaultConstraintSearcherFactory();
			break;
		}
		return factory;
	}

	private static IConstraintSearcherFactory getExtentionFactory(String driverName){
		final String ExtentionPointName = "constraintSearchFactoryPoint";
		final String ExtentionName = "factory";

		IConstraintSearcherFactory factory = null;
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
								if(obj instanceof IConstraintSearcherFactory){
									factory= (IConstraintSearcherFactory)obj;
									return factory;

								}else{
									DbPlugin.log("The mistake is found in plugin.xml.(implements IConstraintSearcherFactory)");
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
