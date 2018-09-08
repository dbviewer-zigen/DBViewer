package zigen.plugin.db.ui.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.IDBConfig;
import zigen.sql.parser.INode;
import zigen.sql.parser.ast.ASTBind;
import zigen.sql.parser.ast.ASTRoot;

public class BindingParamArea {
	int[] defaultWeight = {700, 300};
	final SashForm parent;
	
	List bindDataList = new ArrayList();
		
	public BindingParamArea(SashForm parent){
		this.parent = parent;
	}
	
	TableViewer columnTableViewer;
	
	Table table;
	
	IDBConfig config;
	
	BindingTableKeyEventHandler handler = null;
	
	public void setVisibled(boolean b){
		if(b){
			
			if(columnTableViewer != null || handler != null){
				columnTableViewer.getTable().setFocus();
				handler.selectRow(0);
				handler.editTableElement(0, 2);
			}
			
		}
		
	}
	
	public void createPartControl(IDBConfig config, INode node, int scope) {
		this.config = config;
		
		createBindingDataList(node, scope);
 				
		if(columnTableViewer == null){
			columnTableViewer = new TableViewer(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
			columnTableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));	
			table = columnTableViewer.getTable();
			//table.setFont(DbPlugin.getDefaultFont());
	
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
				
			setHeaderColumn(table, headers);
	
			handler = new BindingTableKeyEventHandler(columnTableViewer);
			setCellModify(columnTableViewer, handler);
			columnTableViewer.setLabelProvider(new ColumnSelectLabelProvider());
			columnTableViewer.setContentProvider(new ColumnSelectContentProvider());
			
			/*
			columnTableViewer.setColumnProperties(new String[] {"dummy", "name", "value", "check", "memo"});
	
			CheckboxCellEditor checkEditor = new CheckboxCellEditor(table);
			TextCellEditor textCellEditor = new TextCellEditor(table);
			
			CellEditor[] editors = new CellEditor[] {null, null, textCellEditor, checkEditor};
	
			columnTableViewer.setCellEditors(editors);
			columnTableViewer.setCellModifier(new ColumnSelectCellModifier());
			columnTableViewer.setLabelProvider(new ColumnSelectLabelProvider());
			columnTableViewer.setContentProvider(new ColumnSelectContentProvider());
			
			textCellEditor.getControl().addFocusListener(new FocusAdapter(){
				public void focusGained(FocusEvent e) {
					DbPlugin.fireStatusChangeListener(e, IStatusChangeListener.EVT_FOCUS_GAIN_BIND_AREA);
				}
				public void focusLost(FocusEvent e) {
					DbPlugin.fireStatusChangeListener(e, IStatusChangeListener.EVT_FOCUS_LOST_BIND_AREA);
				}
			}
			);*/
	
		}
		
		if(bindDataList.size() > 0){
			columnTableViewer.setInput(bindDataList.toArray(new BindingData[0]));
			parent.setWeights(defaultWeight);
			columnsPack(table);
			setVisibled(true);
		}else{
			columnTableViewer.setInput(null);
			parent.setWeights(new int[] {1000, 0});
			setVisibled(false);
		}

	}
	private void setCellModify(TableViewer viewer, BindingTableKeyEventHandler handler) {
		
		//final IActionBars bars = getEditorSite().getActionBars();
		
		String[] properties = new String[4];
		CellEditor[] cellEditors = new CellEditor[4];
		BindingTableKeyAdapter keyAdapter = new BindingTableKeyAdapter(handler);
		for (int i = 0; i < cellEditors.length; i++) {
			properties[i] = String.valueOf(i);
			if (i > 0) {
				CellEditor cellEditor = null;
				
				if(i == 3){
					cellEditor = new CheckboxCellEditor(table, i);					
				}else{
					cellEditor = new TextCellEditor(table, i);
					cellEditor.getControl().addKeyListener(keyAdapter);
					cellEditor.getControl().addTraverseListener(keyAdapter);
					cellEditor.getControl().addFocusListener(new FocusAdapter() {
						public void focusGained(FocusEvent e) {
							//setInfomationText(EDIT_MODE_ON);
							//isEditing = true;
							//bars.clearGlobalActionHandlers();
							//bars.updateActionBars();
							DbPlugin.fireStatusChangeListener(e, IStatusChangeListener.EVT_FOCUS_GAIN_BIND_AREA);
						}

						public void focusLost(FocusEvent e) {
							//setInfomationText(EDIT_MODE_OFF);
							//isEditing = false;
							DbPlugin.fireStatusChangeListener(e, IStatusChangeListener.EVT_FOCUS_LOST_BIND_AREA);
						}
					});

				}				

				// cellEditor.getControl().addFocusListener(new
				// CellFocusAdapter(element));

				cellEditors[i] = cellEditor;

			}
		}
		
		viewer.setColumnProperties(properties);		
		viewer.setCellModifier(new ColumnSelectCellModifier());
		viewer.setCellEditors(cellEditors);
	}
	
	public void createPartControl(IDBConfig config, String sql) {			
		INode node = new ASTRoot();
		zigen.sql.parser.SqlParser parser = null;
		try {
			parser = new zigen.sql.parser.SqlParser(sql, DbPlugin.getSqlFormatRult());
			parser.parse(node);
			// for debug
			//parser.dump(node);

			createPartControl(config, node, parser.getScope());
			
		} catch (Exception e) {
			DbPlugin.log(e);
		} finally {
			if (parser != null)
				parser = null;
		}	
	}
	
	private void columnsPack(Table table) {
		table.setVisible(false);
		TableColumn[] cols = table.getColumns();
		for (int i = 1; i < cols.length; i++) {
			if(i == 1 || i == 3 || i == 4)
			cols[i].pack();
		}
		table.setVisible(true);
	}

	
	
	private static final String[] headers = {"", "変数名", "値", "文字型", "備考"};

	private void setHeaderColumn(Table table, String[] headers) {
		for (int i = 0; i < headers.length; i++) {
			TableColumn col;

			switch (i) {
			case 0:
				col = new TableColumn(table, SWT.NONE, i);
				col.setText(headers[i]);
				col.setResizable(false);
				col.setWidth(0);
				break;
			case 1:
				col = new TableColumn(table, SWT.LEFT, i);
				col.setText(headers[i]);
				col.setResizable(true);
				col.setWidth(100);
				break;
			case 2:
				col = new TableColumn(table, SWT.LEFT, i);
				col.setText(headers[i]);
				col.setResizable(true);
				col.setWidth(100);
				break;
			case 3:
				col = new TableColumn(table, SWT.CENTER, i);
				col.setText(headers[i]);
				col.setResizable(false);
				col.setWidth(100);
				break;
			default:
				col = new TableColumn(table, SWT.LEFT, i);
				col.setText(headers[i]);
				col.setResizable(true);
				col.pack();
				break;
			}

		}
	}
	
//	private INode parseSql(String sql) {
//		INode node = new ASTRoot();
//		zigen.sql.parser.ISqlParser parser = null;
//		try {
//			parser = new zigen.sql.parser.SqlParser(sql, DbPlugin.getSqlFormatRult());
//			parser.parse(node);
//			// for debug
//			//parser.dump(node);
//		} catch (Exception e) {
//			DbPlugin.log(e);
//		} finally {
//			if (parser != null)
//				parser = null;
//		}
//		return node;
//	}
	
	
	
	private void createBindingDataList(INode node, int scope){

		List wk = new ArrayList();
		
		ASTVisitorToBind visitor = new ASTVisitorToBind();
		node.accept(visitor, null);		
		List list = visitor.getASTBindList();
		if(list != null){
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				ASTBind ast = (ASTBind) iterator.next();				
				wk.add(new BindingData(config, ast, scope));
			}
		}
		
		int index = 0;
		for (Iterator iterator = wk.iterator(); iterator.hasNext();) {
			BindingData newData = (BindingData) iterator.next();
	
			
			if("?".equals(newData.name)){
				int index2 = 0;
				for (Iterator iterator2 = bindDataList.iterator(); iterator2.hasNext();) {
					BindingData oldData = (BindingData) iterator2.next();
					if("?".equals(oldData.name) && index == index2){
						newData.value = oldData.value;
						//newData.isCharacter = oldData.isCharacter;
					}
					
					index2++;
				}
				
			}else{
				for (Iterator iterator2 = bindDataList.iterator(); iterator2.hasNext();) {
					BindingData oldData = (BindingData) iterator2.next();
					if(newData.name.equals(oldData.name)){					
						newData.value = oldData.value;
						newData.isCharacter = oldData.isCharacter;
					}
				}
					
				
			}
			
			index++;
			
		}
		bindDataList = wk;
		
	}
	
	public List getBindingDataList(){
		return bindDataList;
	}
	
	private class ColumnSelectLabelProvider extends LabelProvider implements ITableLabelProvider {

		private ImageCacher imageCacher = ImageCacher.getInstance();

		private Image getImage(boolean isSelected) {
			String key = isSelected ? DbPlugin.IMG_CODE_CHECKED_CENTER : DbPlugin.IMG_CODE_UNCHECKED_CENTER;
			return imageCacher.getImage(key);
		}

		public String getColumnText(Object element, int columnIndex) {
			String result = ""; //$NON-NLS-1$
			BindingData data = (BindingData) element;
			switch (columnIndex) {
			case 0:
				break;
			case 1:
				result = data.ast.getName();
				break;
			
			case 2:
				result = data.value;
				break;
			case 3:
				result = "";
				//result = String.valueOf(data.isCharacter);
				break;
				
			case 4:
				if(data.getTargetColumn() != null){
					result = data.getTargetColumn().getColumnLabel(false);	
				}else{
					return "";
				}
				
				break;
				
			default:
				break;
			}
			return result;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			BindingData data = (BindingData) element;
			if (columnIndex == 3) {
				return getImage(data.isCharacter);				
			}
			return null;
		}

		public Image getImage(Object obj) {
			return null;
		}
	}

	private class ColumnSelectContentProvider implements IStructuredContentProvider {

		private List contents = null;

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof BindingData[]) {
				return (BindingData[]) inputElement;
			}
			return null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			contents = null;
		}

		public void dispose() {
			contents = null;
		}

	}

	private class ColumnSelectCellModifier implements ICellModifier {

		public ColumnSelectCellModifier() {}

		public boolean canModify(Object element, String property) {
			try {
				int index = Integer.parseInt(property);
				switch (index) {
				//case 1: // << for test
				case 2:
				case 3:					
					return true;
				default:
					return false;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}			
		}

		public Object getValue(Object element, String property) {
			BindingData data = (BindingData) element;

			try {
				int index = Integer.parseInt(property);
				switch (index) {
				case 1:
					return data.ast.getName(); 
				case 2:
					return data.value;
				case 3:
					return new Boolean(data.isCharacter);		
					//return String.valueOf(data.isCharacter);		
					
				case 4:
					return data.getTargetColumn();
					
				default:
					return null;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}	
			
		}

		public void modify(Object element, String property, Object value) {
			if (element instanceof Item) {
				element = ((Item) element).getData();
			}
			BindingData data = (BindingData) element;

			try {
				int index = Integer.parseInt(property);
				switch (index) {
				 
				case 2:
					data.value = ((String)value).toString();
					columnTableViewer.update(element, new String[] {"2"}); //$NON-NLS-1$
					break;
					
				case 3:
					data.isCharacter = ((Boolean) value).booleanValue();
					//data.isCharacter = Boolean.parseBoolean((String)value);
					columnTableViewer.update(element, new String[] {"3"}); //$NON-NLS-1$
					break;
				default:
					return;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
		}
	}
}
