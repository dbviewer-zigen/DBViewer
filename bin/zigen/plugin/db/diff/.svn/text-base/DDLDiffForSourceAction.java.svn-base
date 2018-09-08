/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import java.sql.Connection;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.AbstractSourceSearcherFactory;
import zigen.plugin.db.core.rule.ISourceSearchFactory;
import zigen.plugin.db.core.rule.SourceDetailInfo;
import zigen.plugin.db.ui.internal.Source;

public class DDLDiffForSourceAction extends Action implements Runnable {

	private StructuredViewer viewer = null;

	private Source left = null;

	private Source right = null;

	public DDLDiffForSourceAction(StructuredViewer viewer) {
		this.viewer = viewer;
		this.setText("&Diff DDL");
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		try {
			int index = 0;
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object obj = iter.next();

				if (obj instanceof Source) {
					Source s = (Source) obj;

					if (index == 0) {
						left = s;

						index++;
					} else if (index == 1) {
						right = s;
						index++;
					} else {
						break;
					}

				}

			}

			if (index == 2) {
				showDDLDiff();
			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	private void showDDLDiff() throws Exception {

		SourceDDLDiff diff = new SourceDDLDiff(new SourceDDL(left), new SourceDDL(right));
		DDLDiffEditorInput input = new DDLDiffEditorInput(new SourceDDLDiff[] {diff}, true);
		IWorkbenchPage page = DbPlugin.getDefault().getPage();
		IDE.openEditor(page, input, DDLDiffEditor.ID, true);

	}

	protected SourceDetailInfo getSourceDetailInfo(Source source) {
		SourceDetailInfo sourceDetail = null;
		//SourceErrorInfo[] sourceErrors = null;
		try {
			Connection con = Transaction.getInstance(source.getDbConfig()).getConnection();
			String owner = source.getSourceInfo().getOwner();
			String type = source.getSourceInfo().getType();
			String name = source.getSourceInfo().getName();
			
			ISourceSearchFactory factory = AbstractSourceSearcherFactory.getFactory(con.getMetaData());			
			sourceDetail = factory.getSourceDetailInfo(con, owner, name, type, false);
			//sourceErrors = factory.getSourceErrorInfos(con, owner, name, type);
			

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
		return sourceDetail;
	}


}
