/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import java.sql.Connection;

import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.AbstractExplainActionFactory;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.jobs.SqlExecJob;


abstract public class DefaultExplainActionFactory extends AbstractExplainActionFactory {

	protected String getExplainSql(String sql){
		return null;
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

			Connection con = trans.getConnection();

			if (sql != null && sql.trim().length() > 0) {

				// explain plan for ...
				String explainSql = getExplainSql(sql);
				if(explainSql == null){
					return;
				}
				SqlExecJob job = new SqlExecJob(trans, getExplainSql(sql), fSQLSourceViewer.getSecondaryId());
				job.setPriority(SqlExecJob.INTERACTIVE);
				job.setUser(false);
				job.schedule();

			} else {
				DbPlugin.getDefault().showWarningMessage(AbstractExplainActionFactory.MSG_6); //$NON-NLS-1$
			}


		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

}
