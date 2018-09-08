/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

public interface IStatusChangeListener {

	public static final int EVT_UpdateHistory = 101;

	public static final int EVT_ChangeTransactionMode = 102;

	public static final int EVT_ModifyTableDefine = 103;

	public static final int EVT_RefreshTable = 104;

	public static final int EVT_LinkTable = 105;

	public static final int EVT_ChangeDataBase = 106;

	public static final int EVT_UpdateDataBaseList = 107;

	public static final int EVT_AddSchemaFilter = 200;

	public static final int EVT_RemoveSchemaFilter = 201;

	public static final int EVT_RefreshOracleSource = 202;
	

	public static final int EVT_FOCUS_GAIN_BIND_AREA = 301;
	public static final int EVT_FOCUS_LOST_BIND_AREA = 302;

	public static final int EVT_FOCUS_GAIN_SQLVIEWER_AREA = 303;
	public static final int EVT_FOCUS_LOST_SQLVIEWER_AREA = 304;
	

	public void statusChanged(Object obj, int status);

}
