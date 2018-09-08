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
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.rule.derby.DerbyValidatorFactory;
import zigen.plugin.db.core.rule.mysql.MySQLValidatorFactory;
import zigen.plugin.db.core.rule.oracle.OracleValidatorFactory;
import zigen.plugin.db.core.rule.symfoware.SymfowareValidatorFactory;
import zigen.plugin.db.preference.PreferencePage;

public abstract class AbstractValidatorFactory implements IValidatorFactory {

	String nullSymbol;

	public static IValidatorFactory getFactory(IDBConfig config) {
		return getFactory(config.getDriverName());

	}

	public static IValidatorFactory getFactory(DatabaseMetaData objMet) {
		try {
			return getFactory(objMet.getDriverName());

		} catch (SQLException e) {
			throw new IllegalStateException("Failed get DriverName"); //$NON-NLS-1$
		}
	}

	private static Map map = new HashMap();

	private static IValidatorFactory getFactory(String driverName) {

		IValidatorFactory factory = null;

		String key = driverName;

		if (map.containsKey(key)) {
			factory = (IValidatorFactory) map.get(key);
		} else {
			factory = getExtentionFactory(driverName);
			if(factory == null){
				factory = getStandardFactory(driverName);
			}
			map.put(key, factory);
		}
		factory.setNullSymbol(DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL));

		return factory;

	}

	private static IValidatorFactory getStandardFactory(String driverName) {
		IValidatorFactory factory = null;
		switch (DBType.getType(driverName)) {
		case DBType.DB_TYPE_ORACLE:
			factory = new OracleValidatorFactory();
			break;
		case DBType.DB_TYPE_SYMFOWARE:
			factory = new SymfowareValidatorFactory();
			break;
		case DBType.DB_TYPE_MYSQL:
			factory = new MySQLValidatorFactory();
			break;
		case DBType.DB_TYPE_DERBY:
			factory = new DerbyValidatorFactory();
			break;
		default:
			factory = new DefaultValidatorFactory();
			break;
		}
		return factory;
	}

	private static IValidatorFactory getExtentionFactory(String driverName) {
		final String ExtentionPointName = "validatorFactoryPoint";
		final String ExtentionName = "factory";

		IValidatorFactory factory = null;
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
								if(obj instanceof IValidatorFactory){
									factory= (IValidatorFactory)obj;
									return factory;

								}else{
									DbPlugin.log("The mistake is found in plugin.xml.(implements IValidatorFactory)");
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

	public String validate(TableColumn column, Object value) throws UnSupportedTypeException {

		String columnName = column.getColumnName();

		if (nullSymbol.equals(value)) {
			if (column.isNotNull()) {
				return columnName + Messages.getString("AbstractValidatorFactory.0"); //$NON-NLS-1$
			} else {
				return null;
			}
		}

		return validateDataType(column, value);

	}

	public String getNullSymbol() {
		return nullSymbol;
	}

	public void setNullSymbol(String nullSymbol) {
		this.nullSymbol = nullSymbol;
	}

	abstract String validateDataType(TableColumn column, Object value) throws UnSupportedTypeException;

}
