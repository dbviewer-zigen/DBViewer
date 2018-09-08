/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import zigen.plugin.db.core.rule.SequenceInfo;

public class Sequence extends TreeNode {

	private static final long serialVersionUID = 1L;

	private SequenceInfo sequenceInfo;

	public Sequence(String name) {
		super(name);
	}

	public Sequence() {
		super();
	}

	public String getName() {
		if (sequenceInfo != null) {
			return this.sequenceInfo.getSequence_name();
		} else {
			return super.name;
		}
	}

	public SequenceInfo getSequenceInfo() {
		return sequenceInfo;
	}

	public void setSequenceInfo(SequenceInfo oracleSequenceInfo) {
		this.sequenceInfo = oracleSequenceInfo;
	}

}
