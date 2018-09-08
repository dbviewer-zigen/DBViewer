/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
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
import zigen.plugin.db.core.rule.oracle.OracleSourceSearcherFactory;

abstract public class AbstractSourceSearcherFactory implements ISourceSearchFactory{
	protected DatabaseMetaData meta;

	public void setDatabaseMetaData(DatabaseMetaData meta){
		this.meta = meta;
	}
	private static Map map = new HashMap();

	public static ISourceSearchFactory getFactory(DatabaseMetaData meta) throws SQLException{
		ISourceSearchFactory factory = null;
		String key = meta.getDriverName();
		if (map.containsKey(key)) {
			factory = (ISourceSearchFactory) map.get(key);
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

	private static ISourceSearchFactory getStandardFactory(DatabaseMetaData meta) {
		ISourceSearchFactory factory = null;
		switch (DBType.getType(meta)) {
		case DBType.DB_TYPE_ORACLE:
			factory = new OracleSourceSearcherFactory();
			factory.setDatabaseMetaData(meta);
			break;
		default:
			factory = new DefaultSourceSearcherFactory();
			factory.setDatabaseMetaData(meta);
			break;
		}
		return factory;
	}
	private static ISourceSearchFactory getExtentionFactory(DatabaseMetaData meta){
		ISourceSearchFactory factory = null;
		final String ExtentionPointName = "sourceSearchFactoryPoint";
		final String ExtentionName = "factory";

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
								if(obj instanceof ISourceSearchFactory){
									factory= (ISourceSearchFactory)obj;
									factory.setDatabaseMetaData(meta);
									return factory;

								}else{
									DbPlugin.log("The mistake is found in plugin.xml.(implements ISourceSearchFactory)");
								}
							}
						} catch (SQLException e) {
							DbPlugin.log(e);
						} catch (CoreException e) {
							DbPlugin.log(e);
						}
					}
				}
			}
		}
		return factory;
	}
	public SourceInfo[] getSourceInfos(Connection con, String owner, String type) throws Exception {
		ResultSet rs = null;
		Statement st = null;
		List list = new ArrayList();

		try {

			st = con.createStatement();
			rs = st.executeQuery(getSourceInfoSQL(owner, type));

			while (rs.next()) {

				SourceInfo info = new SourceInfo();
				info.setOwner(rs.getString("OWNER")); //$NON-NLS-1$
				info.setName(rs.getString("NAME")); //$NON-NLS-1$
				info.setType(rs.getString("TYPE")); //$NON-NLS-1$

				list.add(info);
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

		return (SourceInfo[]) list.toArray(new SourceInfo[0]);

	}

	public SourceErrorInfo[] getSourceErrorInfos(Connection con, String owner, String type) throws Exception {
		ResultSet rs = null;
		Statement st = null;
		List list = new ArrayList();

		try {
			st = con.createStatement();
			String sql = getErrorInfoSQL(owner, type);
			if(sql == null){
				return null;
			}
			rs = st.executeQuery(sql);

			while (rs.next()) {
				SourceErrorInfo info = new SourceErrorInfo();
				info.setOwner(rs.getString("OWNER")); //$NON-NLS-1$
				info.setName(rs.getString("NAME")); //$NON-NLS-1$
				info.setType(rs.getString("TYPE")); //$NON-NLS-1$
				info.setLine(rs.getInt("LINE")); //$NON-NLS-1$
				info.setPosition(rs.getInt("POSITION")); //$NON-NLS-1$
				info.setText(rs.getString("TEXT")); //$NON-NLS-1$
				list.add(info);
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

		return (SourceErrorInfo[]) list.toArray(new SourceErrorInfo[0]);

	}

	public SourceErrorInfo[] getSourceErrorInfos(Connection con, String owner, String name, String type) throws Exception {
		List list = null;
		ResultSet rs = null;
		Statement st = null;

		try {
			st = con.createStatement();
			String sql = getErrorInfoSQL(owner, name, type);
			if(sql == null){
				return null;
			}
			rs = st.executeQuery(sql);

			list = new ArrayList();

			while (rs.next()) {
				SourceErrorInfo info = new SourceErrorInfo();
				info.setOwner(rs.getString("OWNER")); //$NON-NLS-1$
				info.setName(rs.getString("NAME")); //$NON-NLS-1$
				info.setType(rs.getString("TYPE")); //$NON-NLS-1$
				info.setLine(rs.getInt("LINE")); //$NON-NLS-1$
				info.setPosition(rs.getInt("POSITION")); //$NON-NLS-1$
				info.setText(rs.getString("TEXT")); //$NON-NLS-1$
				list.add(info);
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

		return (SourceErrorInfo[]) list.toArray(new SourceErrorInfo[0]);

	}

	public SourceDetailInfo getSourceDetailInfo(Connection con, String owner, String name, String type, boolean visibleSchema) throws Exception {
		ResultSet rs = null;
		Statement st = null;

		SourceDetailInfo info = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(getDetailInfoSQL(owner, name, type));

			StringBuffer sb = new StringBuffer();

			int i = 0;
			while (rs.next()) {

				if (i == 0) {
					info = new SourceDetailInfo();
					info.setOwner(rs.getString("OWNER")); //$NON-NLS-1$
					info.setName(rs.getString("NAME")); //$NON-NLS-1$
					info.setType(rs.getString("TYPE")); //$NON-NLS-1$

					String str = rs.getString("TEXT");//$NON-NLS-1$

					int pos = str.toUpperCase().indexOf(info.getName().toUpperCase());

					if (visibleSchema) {
						sb.append("CREATE OR REPLACE ").append(info.getType());
						sb.append(" ").append(info.getOwner()).append(".");
						sb.append(str.substring(pos));

					} else {
						sb.append("CREATE OR REPLACE ").append(info.getType());
						sb.append(" ").append(str.substring(pos));
					}


				} else {
					sb.append(rs.getString("TEXT")); //$NON-NLS-1$
				}
				i++;
			}

			if (info != null) {
				info.setText(sb.toString());
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

	public SequenceInfo[] getSequenceInfos(Connection con, String owner) throws Exception {
		ResultSet rs = null;
		Statement st = null;
		List list = new ArrayList();

		try {
			st = con.createStatement();
			String sql = getSequenceInfoSQL(owner);
			if(sql == null){
				return null;
			}
			rs = st.executeQuery(sql);

			while (rs.next()) {
				SequenceInfo info = new SequenceInfo();
				info.setSequece_owner(rs.getString("OWNER"));
				info.setSequence_name(rs.getString("NAME"));
				info.setMin_value(rs.getBigDecimal("MIN_VALUE"));
				info.setMax_value(rs.getBigDecimal("MAX_VALUE"));
				info.setIncrement_by(rs.getBigDecimal("INCREMENT_BY"));
				info.setCycle_flg(rs.getString("CYCLE_FLAG"));
				info.setOrder_flg(rs.getString("ORDER_FLAG"));
				info.setCache_size(rs.getBigDecimal("CACHE_SIZE"));
				info.setLast_number(rs.getBigDecimal("LAST_NUMBER"));

				list.add(info);
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}
		return (SequenceInfo[]) list.toArray(new SequenceInfo[0]);

	}


	public SequenceInfo getSequenceInfo(Connection con, String owner, String sequence) throws Exception {
		ResultSet rs = null;
		Statement st = null;
		SequenceInfo info = null;

		try {
			st = con.createStatement();
			String sql = getSequenceInfoSQL(owner, sequence);
			if(sql == null){
				return null;
			}
			rs = st.executeQuery(sql);

			if (rs.next()) {
				info = new SequenceInfo();
				info.setSequece_owner(rs.getString("OWNER"));
				info.setSequence_name(rs.getString("NAME"));
				info.setMin_value(rs.getBigDecimal("MIN_VALUE"));
				info.setMax_value(rs.getBigDecimal("MAX_VALUE"));
				info.setIncrement_by(rs.getBigDecimal("INCREMENT_BY"));
				info.setCycle_flg(rs.getString("CYCLE_FLAG"));
				info.setOrder_flg(rs.getString("ORDER_FLAG"));
				info.setCache_size(rs.getBigDecimal("CACHE_SIZE"));
				info.setLast_number(rs.getBigDecimal("LAST_NUMBER"));

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



	public TriggerInfo[] getTriggerInfos(Connection con, String owner, String table) throws Exception {
		ResultSet rs = null;
		Statement st = null;
		List list = new ArrayList();

		try {
			st = con.createStatement();
			String sql = getTriggerInfoSQL(owner, table);
			if(sql == null){
				return null;
			}
			rs = st.executeQuery(sql);

			while (rs.next()) {

				TriggerInfo info = new TriggerInfo();
				info.setOwner(rs.getString("OWNER")); //$NON-NLS-1$
				info.setName(rs.getString("TRIGGER_NAME")); //$NON-NLS-1$
				info.setType(rs.getString("TRIGGER_TYPE")); //$NON-NLS-1$
				info.setEvent(rs.getString("TRIGGERING_EVENT")); //$NON-NLS-1$

				list.add(info);
			}

		} catch (Exception e) {
			DbPlugin.log(e);
			throw e;
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}

		return (TriggerInfo[]) list.toArray(new TriggerInfo[0]);

	}
	abstract protected String getSourceInfoSQL(String owner, String type);

	abstract protected String getErrorInfoSQL(String owner, String type);

	abstract protected String getErrorInfoSQL(String owner, String name, String type);

	abstract protected String getDetailInfoSQL(String owner, String name, String type);

	abstract protected String getSequenceInfoSQL(String owner);

	abstract protected String getSequenceInfoSQL(String owner, String sequence);

	abstract public String getSequenceDDL(SequenceInfo sequenceInfo);

	abstract protected String getTriggerInfoSQL(String owner, String table);

}
