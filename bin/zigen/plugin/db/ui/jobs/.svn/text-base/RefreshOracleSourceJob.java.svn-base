/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.AbstractSourceSearcherFactory;
import zigen.plugin.db.core.rule.ISourceSearchFactory;
import zigen.plugin.db.core.rule.SourceErrorInfo;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Source;

public class RefreshOracleSourceJob extends AbstractJob {

	private TreeViewer viewer;

	private Source source;

	public RefreshOracleSourceJob(TreeViewer viewer, Source source) {
		super("Refresh Source...");
		this.viewer = viewer;
		this.source = source;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Refresh Oracle Source...", 10);
			Connection con = Transaction.getInstance(source.getDbConfig()).getConnection();

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}


			Schema schema = source.getSchema();
			String owner = schema.getName();
			String type = source.getType();

			ISourceSearchFactory factory = AbstractSourceSearcherFactory.getFactory(con.getMetaData());
			SourceErrorInfo[] errors = factory.getSourceErrorInfos(con, owner, source.getName(), type);
			if(errors != null){
				source.setHasError(errors.length > 0);
			}else{
				source.setHasError(false);
			}

			showResults(new RefreshTreeNodeAction(viewer, source.getParent(), RefreshTreeNodeAction.MODE_NOTHING));

			showResults(new RefreshTreeNodeAction(viewer, source, RefreshTreeNodeAction.MODE_NOTHING));

			monitor.done();
		} catch (Exception e) {
			showErrorMessage(Messages.getString("OracleSourceSearchJob.1"), e); //$NON-NLS-1$

		}
		return Status.OK_STATUS;
	}

}
