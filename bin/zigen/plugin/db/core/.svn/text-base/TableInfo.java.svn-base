/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.sql.Timestamp;

public class TableInfo {

	private String name;

	private String comment;

	private String tableType;
	
	private Timestamp timestamp;


	public TableInfo(String name, String comment) {
		this.name = name;
		this.comment = comment;
	}

	public TableInfo() {

	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean hasComment() {
		if (this.comment != null && this.comment.length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
}
