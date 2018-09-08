/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.oracle.OracleExplainActionFactory;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.jobs.SqlExecJob;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public abstract class AbstractExplainActionFactory extends Action implements IExplainActionFactory {

	public static final String MSG_2 = Messages.getString("AbstractExplainActionFactory.2");
	public static final String MSG_3 = Messages.getString("AbstractExplainActionFactory.3");
	public static final String MSG_5 = Messages.getString("AbstractExplainActionFactory.5");
	public static final String MSG_6 = Messages.getString("AbstractExplainActionFactory.6");
	public static final String MSG_7 = Messages.getString("AbstractExplainActionFactory.7");

	public static IExplainActionFactory getFactory(IDBConfig config, SQLSourceViewer viewer) {
		return getFactory(config.getDriverName(), viewer);
	}

	private static Map map = new HashMap();

	private static IExplainActionFactory getFactory(String driverName, SQLSourceViewer viewer) {

		IExplainActionFactory factory = null;

		String key = driverName;

		if (map.containsKey(key)) {
			factory = (IExplainActionFactory) map.get(key);
		} else {
			factory = getExtentionFactory(driverName);
			if(factory == null){
				factory = getStandardFactory(driverName);
			}
			map.put(key, factory);
		}
		if(factory != null)
			factory.setSQLSourceViewer(viewer);

		return factory;

	}

	private static IExplainActionFactory getStandardFactory(String driverName) {
		IExplainActionFactory factory = null;
		switch (DBType.getType(driverName)) {
		case DBType.DB_TYPE_ORACLE:
			factory = new OracleExplainActionFactory();
			factory.setText(MSG_2); //$NON-NLS-1$
			factory.setToolTipText(MSG_3); //$NON-NLS-1$
			factory.setEnabled(false);
			break;
		}
		return factory;
	}

	private static IExplainActionFactory getExtentionFactory(String driverName) {
		final String ExtentionPointName = "explainActionFactoryPoint";
		final String ExtentionName = "factory";

		IExplainActionFactory factory = null;
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
								if(obj instanceof IExplainActionFactory){
									factory= (IExplainActionFactory)obj;
									factory.setText(MSG_2); //$NON-NLS-1$
									factory.setToolTipText(MSG_3); //$NON-NLS-1$
									factory.setEnabled(false);
									return factory;

								}else{
									DbPlugin.log("The mistake is found in plugin.xml.(implements IExplainActionFactory)");
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

	abstract protected boolean supportOnlySelect();
	abstract protected String getExplainSql(String sql);

	protected SQLSourceViewer fSQLSourceViewer;

	public void setSQLSourceViewer(SQLSourceViewer viewer) {
		this.fSQLSourceViewer = viewer;
	}

	public void run() {

		try {
			IDBConfig config = fSQLSourceViewer.getDbConfig();
			Transaction trans = Transaction.getInstance(config);

			if (!trans.isConneting()) {
				DbPlugin.getDefault().showWarningMessage(DbPluginConstant.MSG_NO_CONNECTED_DB);
				return;
			}

			ISelection selection = fSQLSourceViewer.getSelection();
			if (!(selection instanceof TextSelection)) {
				return;
			}

			TextSelection textSelection = (TextSelection) selection;
			String sql = textSelection.getText().trim();

			String demiliter = DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_SQL_DEMILITER);

			if (sql.endsWith(demiliter)) { //$NON-NLS-1$
				sql = sql.substring(0, sql.length() - 1);
			}
			if (sql != null && sql.trim().length() > 0) {
				if(supportOnlySelect()){
					if (SQLUtil.isSelect(sql)) {
						execSqlJob(trans, getExplainSql(sql), fSQLSourceViewer.getSecondaryId());
					}else{
						DbPlugin.getDefault().showWarningMessage(AbstractExplainActionFactory.MSG_5); //$NON-NLS-1$
					}
				}else{
					execSqlJob(trans, getExplainSql(sql), fSQLSourceViewer.getSecondaryId());
				}
			} else {
				DbPlugin.getDefault().showWarningMessage(AbstractExplainActionFactory.MSG_6); //$NON-NLS-1$
			}


		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	protected void execSqlJob(Transaction trans, String explainSql, String secondaryId) throws Exception{
		SqlExecJob job = new SqlExecJob(trans, explainSql, secondaryId);
		job.setPriority(SqlExecJob.INTERACTIVE);
		job.setUser(false);
		job.schedule();
	}
}
