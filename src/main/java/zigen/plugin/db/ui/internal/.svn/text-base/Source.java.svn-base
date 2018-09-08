/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import zigen.plugin.db.core.rule.SourceInfo;

public class Source extends TreeNode {

	private static final long serialVersionUID = 1L;

	private SourceInfo info;

	boolean hasError;

	boolean canOpen = true;;

	public boolean canOpen() {
		if(info == null){
			return true;
		}else{
			return this.info.hasDDL();
		}

	}

	public Source(String name) {
		super(name);
	}

	public Source() {
		super();
	}

	public SourceInfo getSourceInfo() {
		return info;
	}

	public void setSourceInfo(SourceInfo info) {
		this.info = info;
	}

	public String getName() {
		if (info != null) {
			return this.info.getName();
		} else {
			return super.name;
		}
	}

	public String getType() {
		if (info != null) {
			return this.info.getType();
		} else {
			return "";
		}
	}

	public boolean hasError() {
		return hasError;
	}


	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public void update(Source node) {
		this.info = node.info;
		this.hasError = node.hasError;
	}
}
