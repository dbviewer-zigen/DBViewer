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
import zigen.plugin.db.core.rule.SequenceInfo;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Sequence;

public class SequeceSearchJob extends AbstractJob {

	private TreeViewer viewer;

	private Folder folder;

	public SequeceSearchJob(TreeViewer viewer, Folder folder) {
		super(Messages.getString("SequeceSearchJob.0")); //$NON-NLS-1$
		this.viewer = viewer;
		this.folder = folder;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Search Oracle Seuence...", 10);
			Connection con = Transaction.getInstance(folder.getDbConfig()).getConnection();
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			folder.removeChildAll();
			showResults(new RefreshTreeNodeAction(viewer, folder));

			Schema schema = (Schema) folder.getParent();
			String owner = schema.getName();
			
			ISourceSearchFactory factory = AbstractSourceSearcherFactory.getFactory(con.getMetaData());
			SequenceInfo[] infos = factory.getSequenceInfos(con, owner);
			//SequenceInfo[] infos = OracleSequenceSearcher.execute(con, owner);
			addSequences(con, folder, infos);

			folder.setExpanded(true);
			showResults(new RefreshTreeNodeAction(viewer, folder, RefreshTreeNodeAction.MODE_NOTHING));

			monitor.done();

		} catch (Exception e) {
			folder.setExpanded(false);
			showErrorMessage(Messages.getString("SequeceSearchJob.1"), e); //$NON-NLS-1$

		}
		return Status.OK_STATUS;
	}

	private void addSequences(Connection con, Folder folder, SequenceInfo[] infos) throws Exception {
		if(infos != null){
			for (int i = 0; i < infos.length; i++) {
				Sequence seq = new Sequence();
				seq.setSequenceInfo(infos[i]);
				folder.addChild(seq);
			}
		}
	}
}
