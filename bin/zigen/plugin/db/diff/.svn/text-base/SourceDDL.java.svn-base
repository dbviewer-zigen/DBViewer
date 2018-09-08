/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.ResultSetUtil;
import zigen.plugin.db.core.StatementUtil;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.AbstractSourceSearcherFactory;
import zigen.plugin.db.core.rule.ISourceSearchFactory;
import zigen.plugin.db.core.rule.SourceDetailInfo;
import zigen.plugin.db.core.rule.SourceInfo;
import zigen.plugin.db.ui.internal.Source;

public class SourceDDL implements IDDL, Serializable {

	private static final long serialVersionUID = 1L;

	IDBConfig config = null;

	Source source;

	SourceInfo sourceInfo;

	SourceDetailInfo sourceDetailInfo;

	String SqlSouceName;

	public SourceDDL() {

	}

	public SourceDDL(Source oracleSource) {
		setSource(oracleSource);
	}

	protected final SourceDetailInfo getSourceDetailInfo() {
		SourceDetailInfo info = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			Connection con = Transaction.getInstance(config).getConnection();
			String owner = sourceInfo.getOwner();
			String name = sourceInfo.getName();
			String type = sourceInfo.getType();
//			info = OracleSourceDetailSearcher.execute(con, owner, name, type, false);
			ISourceSearchFactory factory = AbstractSourceSearcherFactory.getFactory(con.getMetaData());			
			info = factory.getSourceDetailInfo(con, owner, name, type, false);
			

		} catch (Exception e) {
			DbPlugin.log(e);
		} finally {
			ResultSetUtil.close(rs);
			StatementUtil.close(st);
		}
		return info;

	}


	public String getType() {
		return sourceInfo.getType();
	}


	public String getDdl() {
		return (sourceDetailInfo != null) ? sourceDetailInfo.getText() : "";
	}

	public String getDbName() {
		return config.getDbName();
	}

	public String getDisplayedName() {
		return getSchemaName() + "." + getTargetName() + "[" + getType() + "]";
	}

	public String getSchemaName() {
		return sourceInfo.getOwner();
	}

	public String getTargetName() {
		return sourceInfo.getName();
	}

	public boolean isSchemaSupport() {
		return true;
	}

	public IDBConfig getConfig() {
		return config;
	}

	public void setConfig(IDBConfig config) {
		this.config = config;
	}

	public SourceInfo getSourceInfo() {
		return sourceInfo;
	}

	public void setSourceInfo(SourceInfo sourceInfo) {
		this.sourceInfo = sourceInfo;

	}

	public String getSqlSouceName() {
		return SqlSouceName;
	}

	public void setSqlSouceName(String sqlSouceName) {
		SqlSouceName = sqlSouceName;
	}

	public void setSourceDetailInfo(SourceDetailInfo sourceDetailInfo) {
		this.sourceDetailInfo = sourceDetailInfo;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
		this.config = source.getDbConfig();
		this.sourceInfo = source.getSourceInfo();
		this.sourceDetailInfo = getSourceDetailInfo();
	}


}
