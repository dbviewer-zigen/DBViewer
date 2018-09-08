/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;

import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.AbstractSourceSearcherFactory;
import zigen.plugin.db.core.rule.ISourceSearchFactory;
import zigen.plugin.db.core.rule.SourceErrorInfo;
import zigen.plugin.db.core.rule.SourceInfo;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.Function;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Source;
import zigen.plugin.db.ui.internal.TreeLeaf;

public class SourceSearchJob extends AbstractJob {

	private TreeViewer viewer;

	private Folder folder;

	public SourceSearchJob(TreeViewer viewer, Folder folder) {
		super(Messages.getString("SourceSearchJob.0")); //$NON-NLS-1$ // TODO:modify message
		this.viewer = viewer;
		this.folder = folder;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Search Source...", 10);
			Connection con = Transaction.getInstance(folder.getDbConfig()).getConnection();

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			Schema schema = (Schema) folder.getParent();
			String owner = schema.getName();
			String type = folder.getName();

			ISourceSearchFactory factory = AbstractSourceSearcherFactory.getFactory(con.getMetaData());							
			SourceInfo[] infos = factory.getSourceInfos(con, owner, type);
			SourceErrorInfo[] errors = factory.getSourceErrorInfos(con, owner, type);
//			
//			SourceInfo[] infos = OracleSourceSearcher.execute(con, owner, type);
//			SourceErrorInfo[] errors  = OracleSourceErrorSearcher.execute(con, owner, type);

			Map errorMap = new HashMap();
			if (errors != null) {
				for (int i = 0; i < errors.length; i++) {
					SourceErrorInfo err = errors[i];
					if (!errorMap.containsKey(err.getName())) {
						errorMap.put(err.getName(), err);
					}
				}
			}
			addSources(con, folder, infos, errorMap);


			folder.setExpanded(true);
			showResults(new RefreshTreeNodeAction(viewer, folder, RefreshTreeNodeAction.MODE_NOTHING));

			monitor.done();
		} catch (Exception e) {
			folder.setExpanded(false);
			showErrorMessage(Messages.getString("OracleSourceSearchJob.1"), e); //$NON-NLS-1$

		}
		return Status.OK_STATUS;
	}

	private void addSources(Connection con, Folder folder, SourceInfo[] infos, Map errorMap) throws Exception {

		List newList = new ArrayList();

		for (int i = 0; i < infos.length; i++) {
			newList.add(infos[i].getName());

			Source source;
			if ("FUNCTION".equals(folder.getName())) { //$NON-NLS-1$
				source = new Function();
			} else {
				source = new Source();
			}
			source.setSourceInfo(infos[i]);
			source.setHasError(errorMap.containsKey(source.getName()));

			TreeLeaf leaf = folder.getChild(source.getName());
			if (leaf == null) {
				folder.addChild(source);

			} else {

				Source os = (Source) leaf;
				os.update(source);
				RefreshOracleSourceJob job = new RefreshOracleSourceJob(viewer, os);
				job.setPriority(RefreshOracleSourceJob.SHORT);
				job.setUser(true);
				job.schedule();

			}

		}

		TreeLeaf[] leafs = folder.getChildrens();
		for (int i = 0; i < leafs.length; i++) {
			TreeLeaf leaf = leafs[i];
			if (!newList.contains(leaf.getName())) {
				folder.removeChild(leaf);
			}
		}

	}
}
