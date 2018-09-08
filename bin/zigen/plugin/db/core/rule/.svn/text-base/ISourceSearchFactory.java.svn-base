package zigen.plugin.db.core.rule;

import java.sql.Connection;
import java.sql.DatabaseMetaData;


public interface ISourceSearchFactory {

	public SourceInfo[] getSourceInfos(Connection con, String owner, String type) throws Exception;

	public SourceDetailInfo getSourceDetailInfo(Connection con, String owner, String name, String type, boolean visibleSchema) throws Exception;
		
	public SourceErrorInfo[] getSourceErrorInfos(Connection con, String owner, String type) throws Exception;

	public SourceErrorInfo[] getSourceErrorInfos(Connection con, String owner, String name, String type) throws Exception;

	public SequenceInfo[] getSequenceInfos(Connection con, String owner) throws Exception;
	
	public SequenceInfo getSequenceInfo(Connection con, String owner, String sequence) throws Exception;
	
	public TriggerInfo[] getTriggerInfos(Connection con, String owner, String table) throws Exception;
	
	public void setDatabaseMetaData(DatabaseMetaData meta);
	
	public String getSequenceDDL(SequenceInfo sequenceInfo);
}
