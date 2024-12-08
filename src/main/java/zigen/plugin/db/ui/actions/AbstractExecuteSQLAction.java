/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.parser.util.CurrentSql;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.editors.exceptions.NoBindingDataException;
import zigen.plugin.db.ui.jobs.SqlExecJob;
import zigen.plugin.db.ui.views.BindingData;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

abstract public class AbstractExecuteSQLAction extends SQLSourceViewerAction implements Runnable {


	protected IDBConfig config;

	protected IDocument doc;

	protected int offset;

	protected SQLSourceViewer viewer;

	protected String secondaryId;

	public AbstractExecuteSQLAction(IDBConfig config, SQLSourceViewer viewer, String secondaryId) {
		super(viewer);
		this.config = config;
		this.doc = viewer.getDocument();
		this.offset = viewer.getTextWidget().getCaretOffset();
		this.secondaryId = secondaryId;
		this.viewer = viewer;
		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_EXECUTE));
	}

	protected String getDemiliter() {
		return DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
	}

	protected String getAllSql() {
		return doc.get();
	}

	protected String getCurrentSql() {
		String demiliter = getDemiliter();
		return new CurrentSql(doc, offset, demiliter).getSql();
	}

//	protected void executeSql(String sql) {
//		if (sql != null && sql.trim().length() > 0) {
//			Transaction trans = Transaction.getInstance(config);
//			SqlExecJob job = new SqlExecJob(trans, sql, secondaryId); // isReload = false;
//			job.setPriority(SqlExecJob.INTERACTIVE);
//			job.setUser(false);
//			job.schedule();
//			
//		} else {
//			DbPlugin.getDefault().showInformationMessage(Messages.getString("AbstractExecuteSQLAction.Message")); //$NON-NLS-1$
//		}
//	}
	
	protected void executeBindSql(String sql, String bindSql) {
		if (sql != null && sql.trim().length() > 0) {
			Transaction trans = Transaction.getInstance(config);
			SqlExecJob job = new SqlExecJob(trans, sql, secondaryId); // isReload = false;
			job.setBindSqlString(bindSql);
			
			job.setPriority(SqlExecJob.INTERACTIVE);
			job.setUser(false);
			job.schedule();
			
		} else {
			DbPlugin.getDefault().showInformationMessage(Messages.getString("AbstractExecuteSQLAction.Message")); //$NON-NLS-1$
		}
	}
	
	
	public String createBindingedData(String sql) throws MalformedTreeException, BadLocationException, NoBindingDataException{
		
		viewer.getBindingParamArea().createPartControl(config, sql);
		
		List bindingDataList = viewer.getBindingParamArea().getBindingDataList();
		
		if(bindingDataList.size() == 0){
			return sql;
		}else{
			
			Document doc = new Document(sql);
			MultiTextEdit multiTextEdit = new MultiTextEdit();						
			for (Iterator iterator = bindingDataList.iterator(); iterator.hasNext();) {
				BindingData data = (BindingData) iterator.next();
				
				if(!data.isCharacter && "".equals(data.value)){
					throw new NoBindingDataException("Please input binding data.");
				}
				
				int offset = data.ast.getOffset();
				int length = data.ast.getLength();
				String text = data.value;				
				if(data.isCharacter){
					text = "'" + SQLUtil.encodeQuotation(text) + "'";
				}
				ReplaceEdit re = new ReplaceEdit(offset, length, text);				
				multiTextEdit.addChild(re);				
			}			
			multiTextEdit.apply(doc);
			sql = doc.get();				
			return sql;
		}
	}

}
