/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.core.TableIDXColumn;

public class Index extends TreeNode {

	private static final long serialVersionUID = 1L;

	private String name;

	private String type;

	private String paramater;

	private String indexType;


	private String schemaName;

	private String tableName;

	private static final String NONUNIQUE_INDEX = "NONUNIQUE INDEX";
	private static final String UNIQUE_INDEX = "UNIQUE INDE";

	public Index(TableIDXColumn[] idxs) {
		configure(idxs);
	}

	private void configure(TableIDXColumn[] idxs) {
		if (idxs != null && idxs.length > 0) {
			StringBuffer sb = new StringBuffer();
			int i = 0;
			for (i = 0; i < idxs.length; i++) {
				TableIDXColumn column = idxs[i];

				if (i == 0) {
					if (column.isNonUnique()) {
						type = NONUNIQUE_INDEX;
					} else {
						type = UNIQUE_INDEX;
					}
					this.schemaName = column.getSchemaName();
					this.tableName = column.getTableName();
					this.name = column.getName();
					this.indexType = column.getIndexType();

					// sb.append("(");
					sb.append(column.getColumnName());

				} else {
					sb.append(", " + column.getColumnName());
				}
			}

			// sb.append(")");
			this.paramater = sb.toString();
		}

	}

	public String getName() {
		return name;
	}

	public String getParamater() {
		return paramater;
	}

	public String getType() {
		return type;
	}

	public String getIndexType() {
		return indexType;
	}

	public String getDDL(boolean isVisibleSchemaName, char encloseChar){
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE");
		if (type.equals(UNIQUE_INDEX)) {
			sb.append("UNIQUE INDEX");
		}else{
			sb.append(" INDEX");
		}

		sb.append(" ");
		if(isVisibleSchemaName){
			sb.append(SQLUtil.enclose(SQLUtil.encodeQuotation(schemaName), encloseChar));
			sb.append(".");
			sb.append(SQLUtil.enclose(SQLUtil.encodeQuotation(name), encloseChar));

		}else{
			sb.append(SQLUtil.enclose(SQLUtil.encodeQuotation(name), encloseChar));
		}
		sb.append(" ON ");
		
		if(isVisibleSchemaName){
			sb.append(SQLUtil.enclose(SQLUtil.encodeQuotation(schemaName), encloseChar));
			sb.append(".");
			sb.append(SQLUtil.enclose(SQLUtil.encodeQuotation(tableName), encloseChar));

		}else{
			sb.append(SQLUtil.enclose(SQLUtil.encodeQuotation(tableName), encloseChar));
		}

		sb.append(" (");
		sb.append(this.paramater);
		sb.append(")");
		return sb.toString();

	}

}
