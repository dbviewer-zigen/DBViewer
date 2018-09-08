/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule.oracle;

import java.sql.Connection;
import java.sql.SQLException;

import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.SQLInvoker;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.core.rule.AbstractExplainActionFactory;
import zigen.plugin.db.explain.ExplainResultDialog;
import zigen.plugin.db.explain.ExplainTreeModel;
import zigen.plugin.db.explain.ExplainVo;


public class OracleExplainActionFactory extends AbstractExplainActionFactory {
	protected boolean supportOnlySelect(){
		return true;
	}

	protected String getExplainSql(String sql){
		return "EXPLAIN PLAN FOR " + sql;
	}

	private static String TRUNCATE_SQL = "truncate table plan_table"; //$NON-NLS-1$

	protected void execSqlJob(Transaction trans, String explainSql, String secondaryId) throws Exception{
		Connection con = trans.getConnection();
		// truncate
		// SQLInvoker.executeUpdate(con, TRUNCATE_SQL);
		truncate(con);

		// explain plan for ...
		SQLInvoker.executeUpdate(con, explainSql);

		OracleExplainDao dao = new OracleExplainDao();
		ExplainVo[] vos = dao.execute(con);

		con.commit();

		ExplainTreeModel invisibleRoot = new ExplainTreeModel(new ExplainVo());
		if (vos.length > 0) {

			for (int i = 0; i < vos.length; i++) {
				ExplainVo vo = vos[i];
				ExplainTreeModel model = new ExplainTreeModel(vo);
				invisibleRoot.addEntry(model);
			}

			Shell shell = DbPlugin.getDefault().getShell();
			ExplainResultDialog dialog = new ExplainResultDialog(shell, invisibleRoot);
			dialog.open();
		}
	}
	public static final int ORA_00942 = 942; // table or view does not exist

	private void truncate(Connection con) throws Exception {
		// truncate
		try {
			SQLInvoker.executeUpdate(con, TRUNCATE_SQL);
		} catch (SQLException e) {
			if (ORA_00942 == e.getErrorCode()) {
				if (DbPlugin.getDefault().confirmDialog(Messages.getString("ExplainForQueryAction.7"))) { //$NON-NLS-1$
					SQLInvoker.executeUpdate(con, getCreatePlanTableSql());
					return;
				}
			}
			throw e;
		}
	}

	private String getCreatePlanTableSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE PLAN_TABLE"); //$NON-NLS-1$
		sb.append("("); //$NON-NLS-1$
		sb.append("    STATEMENT_ID                VARCHAR2(30),"); //$NON-NLS-1$
		sb.append("    TIMESTAMP                   DATE,"); //$NON-NLS-1$
		sb.append("    REMARKS                     VARCHAR2(80),"); //$NON-NLS-1$
		sb.append("    OPERATION                   VARCHAR2(30),"); //$NON-NLS-1$
		sb.append("    OPTIONS                     VARCHAR2(255),"); //$NON-NLS-1$
		sb.append("    OBJECT_NODE                 VARCHAR2(128),"); //$NON-NLS-1$
		sb.append("    OBJECT_OWNER                VARCHAR2(30),"); //$NON-NLS-1$
		sb.append("    OBJECT_NAME                 VARCHAR2(30),"); //$NON-NLS-1$
		sb.append("    OBJECT_INSTANCE             NUMBER(22),"); //$NON-NLS-1$
		sb.append("    OBJECT_TYPE                 VARCHAR2(30),"); //$NON-NLS-1$
		sb.append("    OPTIMIZER                   VARCHAR2(255),"); //$NON-NLS-1$
		sb.append("    SEARCH_COLUMNS              NUMBER(22),"); //$NON-NLS-1$
		sb.append("    ID                          NUMBER(22),"); //$NON-NLS-1$
		sb.append("    PARENT_ID                   NUMBER(22),"); //$NON-NLS-1$
		sb.append("    POSITION                    NUMBER(22),"); //$NON-NLS-1$
		sb.append("    COST                        NUMBER(22),"); //$NON-NLS-1$
		sb.append("    CARDINALITY                 NUMBER(22),"); //$NON-NLS-1$
		sb.append("    BYTES                       NUMBER(22),"); //$NON-NLS-1$
		sb.append("    OTHER_TAG                   VARCHAR2(255),"); //$NON-NLS-1$
		sb.append("    PARTITION_START             VARCHAR2(255),"); //$NON-NLS-1$
		sb.append("    PARTITION_STOP              VARCHAR2(255),"); //$NON-NLS-1$
		sb.append("    PARTITION_ID                NUMBER(22),"); //$NON-NLS-1$
		sb.append("    OTHER                       LONG,"); //$NON-NLS-1$
		sb.append("    DISTRIBUTION                VARCHAR2(30),"); //$NON-NLS-1$
		sb.append("    CPU_COST                    NUMBER(22),"); //$NON-NLS-1$
		sb.append("    IO_COST                     NUMBER(22),"); //$NON-NLS-1$
		sb.append("    TEMP_SPACE                  NUMBER(22),"); //$NON-NLS-1$
		sb.append("    ACCESS_PREDICATES           VARCHAR2(4000),"); //$NON-NLS-1$
		sb.append("    FILTER_PREDICATES           VARCHAR2(4000)"); //$NON-NLS-1$
		sb.append(")"); //$NON-NLS-1$

		return sb.toString();

	}

}
