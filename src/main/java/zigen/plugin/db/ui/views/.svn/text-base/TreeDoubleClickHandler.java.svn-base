/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Sequence;
import zigen.plugin.db.ui.internal.Source;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.jobs.ConnectDBJob;
import zigen.plugin.db.ui.jobs.OpenEditorJob;
import zigen.plugin.db.ui.jobs.OpenSourceEditorJob;
import zigen.plugin.db.ui.jobs.RefreshFolderJob;
import zigen.plugin.db.ui.jobs.SequeceSearchJob;
import zigen.plugin.db.ui.jobs.SourceSearchJob;
import zigen.plugin.db.ui.jobs.TableTypeSearchJob;

public class TreeDoubleClickHandler implements IDoubleClickListener {

	private boolean showDialog = false;

	public TreeDoubleClickHandler() {}

	public void doubleClick(DoubleClickEvent event) {

		try {

			Viewer view = event.getViewer();
			ISelection selection = event.getSelection();

			if (view instanceof TreeViewer && selection instanceof StructuredSelection) {
				TreeViewer viewer = (TreeViewer) view;
				Object element = ((StructuredSelection) selection).getFirstElement();

				if (element instanceof DataBase) {
					DataBase db = (DataBase) element;
					if (!db.isExpanded()) {
						db.setConnected(true);
						db.setExpanded(true);
						ConnectDBJob job = new ConnectDBJob(viewer, db);
						job.setPriority(ConnectDBJob.SHORT);
						job.setUser(false);
						job.setSystem(false);
						job.schedule();

					} else {
						changeExpandedState(viewer, (TreeNode) element);
					}

				} else if (element instanceof ITable) {
					OpenEditorJob job = new OpenEditorJob(viewer, (ITable) element);
					job.setPriority(OpenEditorJob.SHORT);
					job.setUser(showDialog);
					job.schedule();


				} else if (element instanceof Source) {
					OpenSourceEditorJob job = new OpenSourceEditorJob(viewer);
					job.setPriority(OpenSourceEditorJob.SHORT);
					job.setUser(showDialog);
					job.schedule();

				} else if (element instanceof Sequence) {
					OpenSourceEditorJob job = new OpenSourceEditorJob(viewer);
					job.setPriority(OpenSourceEditorJob.SHORT);
					job.setUser(showDialog);
					job.schedule();

				} else if (element instanceof TreeNode) {
					changeExpandedState(viewer, (TreeNode) element);
				}

			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}

	}

	private void changeExpandedState(TreeViewer viewer, TreeNode element) {

		if (!viewer.getExpandedState(element)) {

			viewer.expandToLevel(element, 1);

			if (element instanceof Schema) {

				Schema schema = (Schema) element;

				if (!schema.isExpanded()) {
					schema.setExpanded(true);
					TableTypeSearchJob job = new TableTypeSearchJob(viewer, schema);
					job.setPriority(TableTypeSearchJob.SHORT);
					job.setUser(showDialog);
					job.schedule();
				}

			} else if (element instanceof Folder) {
				Folder folder = (Folder) element;
				if (!folder.isExpanded()) {
					folder.setExpanded(true);
					Schema schema = folder.getSchema();


					if (schema != null) {
						if (folder.getName().toUpperCase().matches("^TABLE|^VIEW|^SYNONYM|^ALIAS")) {
							RefreshFolderJob job = new RefreshFolderJob(viewer, folder);
							job.setPriority(RefreshFolderJob.SHORT);
							job.setUser(showDialog);
							job.schedule();
						}else if("SEQUENCE".equals(folder.getName())) { //$NON-NLS-1$
							SequeceSearchJob job = new SequeceSearchJob(viewer, folder);
							job.setPriority(SequeceSearchJob.SHORT);
							job.setUser(showDialog);
							job.schedule();
							return;
						}else{
							SourceSearchJob job = new SourceSearchJob(viewer, folder);
							job.setPriority(SourceSearchJob.SHORT);
							job.setUser(showDialog);
							job.schedule();
							return;
						}
					}
				}

			}

		} else {
			viewer.collapseToLevel(element, 1);
		}

	}
}
