/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.diff.DDLDiff;
import zigen.plugin.db.diff.IDDLDiff;
import zigen.plugin.db.preference.DBTreeViewPreferencePage;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkRoot;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.Function;
import zigen.plugin.db.ui.internal.History;
import zigen.plugin.db.ui.internal.HistoryFolder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Root;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.Sequence;
import zigen.plugin.db.ui.internal.Source;
import zigen.plugin.db.ui.internal.Synonym;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.internal.View;

public class TreeLabelProvider extends LabelProvider implements ILabelProvider, IColorProvider, IStyledLabelProvider{
	protected ImageCacher ic = ImageCacher.getInstance();
	protected List fLabelDecorators;

	public void addLabelDecorator(ILabelDecorator decorator) {
		if (fLabelDecorators == null) {
			fLabelDecorators= new ArrayList();
		}
		fLabelDecorators.add(decorator);
	}

	protected Image decorateImage(Image image, Object element) {
		if (fLabelDecorators != null && image != null) {
			for (int i= 0; i < fLabelDecorators.size(); i++) {
				ILabelDecorator decorator= (ILabelDecorator) fLabelDecorators.get(i);
				image= decorator.decorateImage(image, element);
			}
		}
		return image;
	}

	public Color getBackground(Object element) {
		return null;
	}

	public Color getForeground(Object element) {
		return null;
	}


	protected String decorateText(String text, Object obj) {
		if(obj instanceof Root){
			Root root = (Root)obj;
			text = root.getName() + " " + DbPlugin.getPluginVersion();

		}else if(obj instanceof DataBase){
			DataBase db = (DataBase)obj;
			if(db.isConnected()){
				text = db.getName() + " [" + db.getDbConfig().getUrl() + "]";
			}
		}else if(obj instanceof Schema){
			Schema schema = (Schema)obj;
			DataBase db = schema.getDataBase();
			if(db.getDefaultSchema() != null && !"".equals(db.getDefaultSchema()) && schema.getName().equalsIgnoreCase(db.getDefaultSchema())){
				text = schema.getSchema() + " [default]";
			}
		} else if (obj instanceof ITable) {
			ITable table = (ITable) obj;
			if (DbPlugin.getDefault().getPreferenceStore().getBoolean(DBTreeViewPreferencePage.P_DISPLAY_TBL_COMMENT)) {
				text = table.getLabel();

				if(text != null){
					text = text.replaceAll("\\n","\\\\n");
					text = text.replaceAll("\\r","\\\\r");
				}
			}
		}else if (obj instanceof Column) {
			Column column = (Column) obj;
			TableColumn _column = column.getColumn();
			if (_column.getTypeName() != null) {
				text = column.getColumnLabel(true);

				if(text != null){
					text = text.replaceAll("\\n","\\\\n");
					text = text.replaceAll("\\r","\\\\r");
				}

			}

		} else if (obj instanceof Source) {
			Source source = (Source) obj;
			if(!source.canOpen()){
				text = source.getName() + " [no source]";
			}else{
				text = source.getName();
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
		String result = null;
		if(obj instanceof DataBase){
			DataBase db = (DataBase)obj;
			result = db.getName();

		}else if (obj instanceof Column) {
			Column column = (Column) obj;
			TableColumn _column = column.getColumn();
			if (_column.getTypeName() != null) {
				result = column.getColumnLabel(false);
			} else {
				result = obj.toString();
			}
		} else if (obj instanceof ITable) {
			ITable table = (ITable) obj;
			result = table.getName();

		} else if (obj instanceof Schema) {
			Schema schema = (Schema) obj;
			result = schema.getName();

		} else if (obj instanceof History) {
			History history = (History) obj;
			result = history.getName();

		} else if (obj instanceof DDLDiff) {
			IDDLDiff diff = (IDDLDiff) obj;
			result = diff.getName();

		} else {
			result = obj.toString();
		}

		return result;
	}

	public Image getImage(Object obj) {
		Image img = null;

		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		if (obj instanceof Root) {
			return decorateImage(ic.getImage(DbPlugin.IMG_CODE_DB), obj);

		} else if (obj instanceof DataBase) {
			DataBase db = (DataBase) obj;
			if (db.isConnected()) {
				return decorateImage(ic.getImage(DbPlugin.IMG_CODE_CONNECTED_DB), obj);
			} else {
				return decorateImage(ic.getImage(DbPlugin.IMG_CODE_DB), obj);
			}

		} else if (obj instanceof Schema) {
			return decorateImage(ic.getImage(DbPlugin.IMG_CODE_SCHEMA), obj);

		} else if (obj instanceof BookmarkRoot) {
			return decorateImage(ic.getImage(DbPlugin.IMG_CODE_BOOKMARK), obj);

		} else if (obj instanceof Synonym) {
			Synonym synonym = (Synonym) obj;
			if (synonym.isEnabled()) {
				return decorateImage(ic.getImage(DbPlugin.IMG_CODE_SYNONYM), obj);
			} else {
				return decorateImage(ic.getImage(DbPlugin.IMG_CODE_DISABLED_SYNONYM), obj);
			}

		} else if (obj instanceof View) {
			return decorateImage(ic.getImage(DbPlugin.IMG_CODE_VIEW), obj);

		} else if (obj instanceof Bookmark) {
			Bookmark bm = (Bookmark) obj;
			if (Bookmark.TYPE_TABLE == bm.getType()) {
				return decorateImage(ic.getImage(DbPlugin.IMG_CODE_TABLE), obj);

			} else if (Bookmark.TYPE_VIEW == bm.getType()) {
				return decorateImage(ic.getImage(DbPlugin.IMG_CODE_VIEW), obj);

			} else if (Bookmark.TYPE_SYNONYM == bm.getType()) {
				return decorateImage(ic.getImage(DbPlugin.IMG_CODE_SYNONYM), obj);

			} else {
				imageKey = ISharedImages.IMG_OBJ_FILE;
			}
		} else if (obj instanceof ITable) {
			ITable table = (ITable) obj;
			if (table.isEnabled()) {
				return decorateImage(ic.getImage(DbPlugin.IMG_CODE_TABLE), obj);
			} else {
				return decorateImage(ic.getImage(DbPlugin.IMG_CODE_DISABLED_TABLE), obj);
			}

		} else if (obj instanceof Column) {
			Column col = (Column) obj;
			if (col.hasPrimaryKey()) {
				img = ic.getImage(DbPlugin.IMG_CODE_PK_COLUMN);
				return decorateImage(ic.getImage(DbPlugin.IMG_CODE_PK_COLUMN), obj);
			} else {
				if (col.isNotNull()) {
					return decorateImage(ic.getImage(DbPlugin.IMG_CODE_NOTNULL_COLUMN), obj);
				} else {
					return decorateImage(ic.getImage(DbPlugin.IMG_CODE_COLUMN), obj);
				}
			}

		} else if (obj instanceof History) {
			return decorateImage(ic.getImage(DbPlugin.IMG_CODE_CLOCK), obj);

		} else if (obj instanceof HistoryFolder) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER;

		} else if (obj instanceof DDLDiff) {
			return decorateImage(ic.getImage(DbPlugin.IMG_CODE_TABLE), obj);

		} else if (obj instanceof Function) {
			Function function = (Function)obj;
			if(function.hasError()){
				return decorateImage(ic.getImage(DbPlugin.IMG_CODE_FUNCTION_ERR), obj);
			}else{
				return decorateImage(ic.getImage(DbPlugin.IMG_CODE_FUNCTION), obj);
			}

		} else if (obj instanceof Source) {
			Source source = (Source) obj;
			if (source.hasError()) {
				return decorateImage(ic.getImage(DbPlugin.IMG_CODE_FILE_ERR), obj);
			} else{

				return decorateImage(ic.getImage(DbPlugin.IMG_CODE_FILE), obj);
			}

		} else if (obj instanceof Sequence) {
			return decorateImage(ic.getImage(DbPlugin.IMG_CODE_SEQUENCE), obj);

		} else if(obj instanceof Folder){
			Folder folder = (Folder)obj;
			if(folder.hasError()){
				return ic.getImage(DbPlugin.IMG_CODE_FOLDER_ERR);
			}else{
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			}
		} else if (obj instanceof TreeNode) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		}

		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);

	}
}
