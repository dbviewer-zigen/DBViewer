package zigen.plugin.db.ui.views;

import java.util.Iterator;
import java.util.List;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.parser.util.ASTUtil2;
import zigen.plugin.db.ui.contentassist.ContentInfo;
import zigen.plugin.db.ui.internal.Column;
import zigen.sql.parser.INode;
import zigen.sql.parser.ISqlParser;
import zigen.sql.parser.ast.ASTBind;
import zigen.sql.parser.ast.ASTColumn;
import zigen.sql.parser.ast.ASTFrom;
import zigen.sql.parser.ast.ASTParentheses;
import zigen.sql.parser.ast.ASTSelectStatement;
import zigen.sql.parser.ast.ASTTable;

public class BindingData {

	public ASTBind ast;

	public String name;

	public boolean isCharacter = false;

	public String value;

	ContentInfo ci = null;

	Column column;

	private int scope;
	
	public BindingData(IDBConfig config, ASTBind ast, int scope) {
		this.ast = ast;
		this.name = ast.getName();
		this.value = "";
		this.scope = scope;
		ci = new ContentInfo(config);
		setup();
	}

	private void setup() {
		ASTSelectStatement st = findParentASTSelectStatement(ast);
		if (st != null) {
			ASTFrom fromList = findASTFrom(st);
			if (fromList.getChildrenSize() == 0) {

			}
			if (fromList.getChildrenSize() == 1) {
				// 単一テーブルの場合
				Column[] cols = getColumns(fromList.getChild(0));
				if (cols != null) {
					for (int i = 0; i < cols.length; i++) {
						Column _col = cols[i];
						if (_col.getColumn().getColumnName().equalsIgnoreCase(getASTColumn().getColumnName())) {
							column = _col;
							if (column.getTypeName().toUpperCase().indexOf("CHAR") >= 0) {
								this.isCharacter = true;
							} else {
								this.isCharacter = false;
							}
							break;

						}
					}
				}

			} else {

			}
		}

	}


	private ASTColumn getASTColumn() {
		switch (scope) {
		case ISqlParser.SCOPE_WHERE:
			INode parent = ast.getParent();
			if (parent == null) {
				return null;
			}
			
			if(parent instanceof ASTParentheses){
				// for ASTParentheses
				ASTParentheses p = (ASTParentheses)parent;
				return getASTColumnForParent(p.getParent());
				
			}else{
				// for parent is ASTOperator, etc
				return getASTColumnForParent(ast.getParent());			
				
			}

		default:
			break;
		}
		
		return null;

	}

	private ASTColumn getASTColumnForParent(INode parent){
		if(parent == null) return null;
		
		List list = parent.getChildren();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			INode node = (INode) iterator.next();
			if (node instanceof ASTColumn) {
				return (ASTColumn) node;
			}
		}
		return null;
	}
	
	
	public Column getTargetColumn() {
		return column;
	}

	private ASTSelectStatement findParentASTSelectStatement(INode node) {
		return (ASTSelectStatement) ASTUtil2.findParent(node,
				"ASTSelectStatement");
	}

	private ASTFrom findASTFrom(ASTSelectStatement node) {
		return (ASTFrom) node.getChild("ASTFrom");
	}

	protected Column[] getColumns(INode target) {
		if (target != null) {
			if (target instanceof ASTTable) {
				return getColumnsForASTTable((ASTTable) target);

			} else if (target instanceof ASTSelectStatement) {
				// createColumn((ASTSelectStatement) target);

			} else if (target instanceof ASTParentheses) {
				//				ASTSelectStatement select = (ASTSelectStatement) ASTUtil2.findFirstChild(target, "ASTSelectStatement"); //$NON-NLS-1$
				// if (select != null) {
				// createColumnProposal(select);
				// } else {
				//					throw new IllegalStateException(Messages.getString("DefaultProcessor.13")); //$NON-NLS-1$
				// }
			}
		}
		return null;

	}

	private Column[] getColumnsForASTTable(ASTTable target) {
		if (target != null) {
			if (ci.isConnected()) {
				String schemaName = ((ASTTable) target).getSchemaName();
				if (schemaName == null)
					schemaName = ci.getCurrentSchema();
				String tableName = ((ASTTable) target).getTableName();
				Column[] cols = ci.getColumns(schemaName, tableName);
				return cols;
			}
		}
		return null;
	}

}