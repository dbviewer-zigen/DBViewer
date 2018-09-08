/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.action;

import java.sql.Connection;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.AbstractSourceSearcherFactory;
import zigen.plugin.db.core.rule.ISourceSearchFactory;
import zigen.plugin.db.core.rule.SourceDetailInfo;
import zigen.plugin.db.core.rule.SourceErrorInfo;
import zigen.plugin.db.ui.actions.TableViewEditorAction;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.sql.SourceEditorInput;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Trigger;

public class OpenTriggerAction extends TableViewEditorAction {

	public OpenTriggerAction() {
		this.setText("Open Trigger Source"); //$NON-NLS-1$
		this.setToolTipText("Open Trigger Source"); //$NON-NLS-1$
		this.setImageDescriptor(ISharedImages.IMG_OBJ_FILE);
	}

	public void run() {

		try {
			if (editor instanceof TableViewEditorFor31) {
				TableViewEditorFor31 tEditor = (TableViewEditorFor31) editor;

				IStructuredSelection selection = (IStructuredSelection) tEditor.getTableDefineEditor().getConstraintViewer().getSelection();

				Object obj = selection.getFirstElement();
				if (obj instanceof Trigger) {
					Trigger trigger = (Trigger) obj;
					ITable table = tEditor.getTableNode();
					SourceDetailInfo sourceDetail = null;
					SourceErrorInfo[] sourceErrors = null;
					Connection con = Transaction.getInstance(table.getDbConfig()).getConnection();
					String owner = trigger.getSourceInfo().getOwner();
					String type = trigger.getSourceInfo().getType();
					String name = trigger.getSourceInfo().getName();
					//sourceDetail = OracleSourceDetailSearcher.execute(con, owner, name, type, true);
					//sourceErrors = OracleSourceErrorSearcher.execute(con, owner, name, type);
					ISourceSearchFactory factory = AbstractSourceSearcherFactory.getFactory(con.getMetaData());			
					sourceDetail = factory.getSourceDetailInfo(con, owner, name, type, true);
					sourceErrors = factory.getSourceErrorInfos(con, owner, name, type);
					
					SourceEditorInput input = new SourceEditorInput(table.getDbConfig(), trigger, sourceDetail, sourceErrors);
					IWorkbenchPage page = DbPlugin.getDefault().getPage();
					IEditorPart editor = IDE.openEditor(page, input, DbPluginConstant.EDITOR_ID_SOURCE, true);
				}
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

	

}
