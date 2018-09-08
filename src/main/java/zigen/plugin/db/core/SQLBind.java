package zigen.plugin.db.core;

public class SQLBind {

	int sqlType;
	
	Object value;
	
	public int getSqlType() {
		return sqlType;
	}

	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public SQLBind(int sqlTyle, Object value){
	
		this.sqlType = sqlTyle;
		this.value = value;
		
	}
	
}
