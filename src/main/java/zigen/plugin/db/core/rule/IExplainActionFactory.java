/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import org.eclipse.jface.action.IAction;

import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public interface IExplainActionFactory extends IAction {
	
	abstract public void run();

	abstract public void setSQLSourceViewer(SQLSourceViewer viewer);
	
	abstract public void setText(String text);
	
	abstract public void setToolTipText(String text);
	
	abstract public void setEnabled(boolean b);
}
