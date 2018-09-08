/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import zigen.plugin.db.DbPlugin;

public class BindingTableKeyAdapter implements KeyListener, TraverseListener {

	private BindingTableKeyEventHandler handler;

	int columnCount;	
	Table table;

	public BindingTableKeyAdapter(BindingTableKeyEventHandler handler) {
		this.handler = handler;
		this.table = handler.table;
		this.columnCount = handler.table.getColumnCount();
	}

	public void keyTraversed(TraverseEvent e) {
		int row = handler.getSelectedRow();
		int col = handler.getSelectedCellEditorIndex();

		if (e.character == SWT.TAB) {
			if ((e.stateMask & SWT.SHIFT) != 0) {
				int prevCol = handler.getEditablePrevColumn(col);
				handler.editTableElement(row, prevCol);
				
			} else {
				int nextCol = handler.getEditableNextColumn(col);
				handler.editTableElement(row, nextCol);
			}
			e.doit = false;
		}
	}

	private void enterEvent(KeyEvent e) throws Exception {	
		int row = handler.getSelectedRow();		
		int col = handler.getSelectedCellEditorIndex();
		//handler.selectRow(row);
		if(handler.table.getItemCount()-1 == row){
			handler.editTableElement(row, 0);	// 行選択状態にする
		}else{
			handler.editTableElement(row + 1, col);
		}
		e.doit = false;
	}

	private void arrowEvent(KeyEvent e, Text text) throws Exception {
		int row = handler.getSelectedRow();
		int col = handler.getSelectedCellEditorIndex();
		int prevCol = handler.getEditablePrevColumn(col);
		int nextCol = handler.getEditableNextColumn(col);
		int maxRow = handler.table.getItemCount();
		int maxCol = handler.table.getColumnCount();
		int caretPostion = text.getCaretPosition();
		int carCount = text.getCharCount();
		int selectionCount = text.getSelectionCount();
		//TableElement element = (TableElement) handler.viewer.getElementAt(row);
		switch (e.keyCode) {
		case SWT.ARROW_UP:
			// if (row > 0) {
			if (row >= 0) {				
				handler.editTableElement(row - 1, col);
				e.doit = false;
			}
			break;
		case SWT.ARROW_DOWN:
			if (row < maxRow - 1) {
				handler.editTableElement(row + 1, col);
				e.doit = false;
			}
			break;
		case SWT.ARROW_LEFT:
			if (col == 1) {
				if (selectionCount == carCount) {
					e.doit = false;

				} else {
					e.doit = true;
				}
			} else if (col > 1) {
				if ((selectionCount == 0 && caretPostion == 0) || selectionCount == carCount) {
					handler.editTableElement(row, prevCol);
					e.doit = false;
				}
			} else {
				e.doit = false;
			}
			break;
		case SWT.ARROW_RIGHT:
			if (col == maxCol - 1) {
				if (selectionCount == carCount) {
					e.doit = false;

				} else {
					e.doit = true;
				}
			} else if (col < maxCol - 1) {
				if (selectionCount == carCount || caretPostion == carCount) {
					handler.editTableElement(row, nextCol);
					e.doit = false;
				}
			} else {
				e.doit = false;
			}
			break;
		default:
			break;
		}

	}

	public void keyReleased(KeyEvent e) {
		;
	}

	public void keyPressed(KeyEvent e) {
		Text text = null;
		try {
			if (e.widget instanceof Text) {
				text = (Text) e.widget;

				// ENTER
				if (e.character == SWT.CR) {
					System.out.println("Enter!");
					enterEvent(e);

					// F2
				} else if (e.keyCode == SWT.F2) {
					text.clearSelection();

				} else if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT) {
					arrowEvent(e, text);
				}

				// CTRL+V
				if (e.stateMask == SWT.CTRL && e.keyCode == 118) {					
					e.doit = false;					
				}

//			} else if (e.widget instanceof Button) {
//				if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT) {
//					arrowEvent(e);
//				}


			}
		} catch (Exception e1) {
			DbPlugin.getDefault().showErrorDialog(e1);
		}
	}

//	private void arrowEvent(KeyEvent e) throws Exception {
//		int row = handler.getSelectedRow();
//		int col = handler.getSelectedCellEditorIndex();
//		int prevCol = handler.getEditablePrevColumn(col);
//		int nextCol = handler.getEditableNextColumn(col);
//		int maxRow = handler.table.getItemCount();
//		int maxCol = handler.table.getColumnCount();
//		//TableElement element = (TableElement) handler.viewer.getElementAt(row);
//		switch (e.keyCode) {
//		case SWT.ARROW_UP:
//			// if (row > 0) {
//			if (row >= 0) {			
//				handler.editTableElement(row - 1, col);
//				e.doit = false;
//			}
//			break;
//		case SWT.ARROW_DOWN:
//			if (row < maxRow - 1) {
//				handler.editTableElement(row + 1, col);
//				e.doit = false;
//			}
//			break;
//		case SWT.ARROW_LEFT:
//			if (col == 1) {
//				e.doit = true;
//			} else if (col > 1) {
//				handler.editTableElement(row, prevCol);
//				e.doit = false;
//			} else {
//				e.doit = false;
//			}
//			break;
//		case SWT.ARROW_RIGHT:
//			if (col == maxCol - 1) {
//				e.doit = true;
//			} else if (col < maxCol - 1) {
//				handler.editTableElement(row, nextCol);
//				e.doit = false;
//			} else {
//				e.doit = false;
//			}
//			break;
//		default:
//			break;
//		}
//
//	}
}
