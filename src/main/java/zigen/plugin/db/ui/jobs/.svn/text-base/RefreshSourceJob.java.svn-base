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
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Source;

public class RefreshSourceJob extends AbstractJob {

	private TreeViewer viewer;

	private Source source;

	public RefreshSourceJob(TreeViewer viewer, Source source) {
		super("RefreshSource...");
		this.viewer = viewer;
		this.source = source;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Refresh Source...", 10);
			Connection con = Transaction.getInstance(source.getDbConfig()).getConnection();

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			Schema schema = source.getSchema();
			String owner = schema.getName();
			String type = source.getType();

			source.setHasError(false);
			
			showResults(new RefreshTreeNodeAction(viewer, source.getParent(), RefreshTreeNodeAction.MODE_NOTHING));

			showResults(new RefreshTreeNodeAction(viewer, source, RefreshTreeNodeAction.MODE_NOTHING));

			monitor.done();
		} catch (Exception e) {
			showErrorMessage(Messages.getString("SourceSearchJob.1"), e); //$NON-NLS-1$

		}
		return Status.OK_STATUS;
	}

}
