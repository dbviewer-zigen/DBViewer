/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.actions.ConfirmConnectDBAction;
import zigen.plugin.db.ui.editors.sql.SqlEditor2;

public class RecordCountForSqlEditorJob extends AbstractJob {
	SqlEditor2 editor;
	
	int timeoutSec = 5;

	private Transaction trans;

	private String sqlString;

	int dispCount;

	public RecordCountForSqlEditorJob(SqlEditor2 editor, Transaction trans, String sqlString, int dispCount) {
		super(Messages.getString("RecordCountForQueryJob.0")); //$NON-NLS-1$
		this.editor = editor;
		this.trans = trans;
		this.sqlString = sqlString;
		this.dispCount = dispCount;
	}

	protected IStatus run(IProgressMonitor monitor) {
		// long count = 0;
		try {
			if (!trans.isConneting()) {
				Display.getDefault().syncExec(new ConfirmConnectDBAction(trans));
				if (!trans.isConneting()) {
					showWarningMessage(DbPluginConstant.MSG_NO_CONNECTED_DB);
					return Status.CANCEL_STATUS;
				}
			}

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			TimeWatcher tw = new TimeWatcher();
			tw.start();

			
			if(sqlString.toUpperCase().startsWith("EXPLAIN ")){
				return Status.OK_STATUS;
			}
			
			int timeout = store.getInt(PreferencePage.P_QUERY_TIMEOUT_FOR_COUNT);

			ISQLCreatorFactory factory = DefaultSQLCreatorFactory.getFactory(trans.getConfig(), null);
			String q = factory.createCountForQuery(sqlString);

			TotalRecordCountSearchThread t = new TotalRecordCountSearchThread(trans, q, timeout);
			Thread th = new Thread(t);
			th.start();

			if (timeout > 0) {
				th.join(timeout * 1000);
			} else {
				th.join();
			}
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			if (t.isComplete) {
				showResults(new SetTotalCountAction(t.count));
			} else {
				showResults(new SetTotalCountAction(-1));
			}

			tw.stop();
		} catch (Exception e) {
			DbPlugin.log(e);
			showResults(new SetTotalCountAction(-1));

		} finally {
		}
		return Status.OK_STATUS;

	}

	protected class SetTotalCountAction implements Runnable {

		long count;

		public SetTotalCountAction(long count) {
			this.count = count;
		}

		public void run() {
			try {
				editor.setTotalCount(dispCount, count);
			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}
	}
}
