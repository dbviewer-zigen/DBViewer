/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.views.TreeLabelProvider;
import zigen.sql.parser.ASTVisitorToString;
import zigen.sql.parser.Node;
import zigen.sql.parser.ast.ASTColumn;
import zigen.sql.parser.ast.ASTFunction;
import zigen.sql.parser.ast.ASTTable;
import zigen.sql.parser.ast.ASTValue;

public class SQLOutlineLabelProvider extends TreeLabelProvider {

	protected String decorateText(String text, Object obj) {
		if (obj instanceof Node) {

			if (obj instanceof ASTFunction) {
				ASTVisitorToString v = new ASTVisitorToString();
				ASTFunction f = (ASTFunction) obj;
				f.accept(v, null);
				
				text = v.toString() + " : ASTFunction"; 

			} else {
				text = ((Node) obj).getName() + " : " + ((Node) obj).getNodeClassName();

			}

		}
		return text;
	}


	public StyledString getStyledText(Object element) {
		StyledString string = new StyledString(getText(element));
		String decorated= decorateText(string.getString(), element);
		if (decorated != null) {
			return StyledCellLabelProvider.styleDecoratedString(decorated, StyledString.DECORATIONS_STYLER, string);
		}
		return string;
	}


	public String getText(Object obj) {
		if (obj instanceof Node) {

			if (obj instanceof ASTFunction) {
				ASTVisitorToString v = new ASTVisitorToString();
				ASTFunction f = (ASTFunction) obj;
				f.accept(v, null);
				
				return v.toString(); 

			} else {
				return ((Node) obj).getName();

			}
		}
		return obj.toString();
	}

	public Image getImage(Object obj) {
		if (obj instanceof ASTTable) {
			return ic.getImage(DbPlugin.IMG_CODE_TABLE);
			
		} else if (obj instanceof ASTValue) {
			return ic.getImage(DbPlugin.IMG_CODE_SQL);
			
		} else if (obj instanceof ASTColumn) {
			return ic.getImage(DbPlugin.IMG_CODE_COLUMN);
			
		} else if (obj instanceof ASTFunction) {
			return ic.getImage(DbPlugin.IMG_CODE_FUNCTION);
			
		} else {
			return ic.getImage(DbPlugin.IMG_CODE_SQL);
			
		}

	}
}
