/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import java.text.NumberFormat;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.ImageCacher;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLInvoker;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.csv.CreateCSVForQueryAction;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.actions.CopyRecordDataAction;
import zigen.plugin.db.ui.actions.GlobalAction;
import zigen.plugin.db.ui.actions.SelectAllRecordAction;
import zigen.plugin.db.ui.editors.IPageChangeListener;
import zigen.plugin.db.ui.editors.IQueryViewEditor;
import zigen.plugin.db.ui.editors.ITableViewEditor;
import zigen.plugin.db.ui.editors.Messages;
import zigen.plugin.db.ui.editors.QueryViewerPager;
import zigen.plugin.db.ui.editors.TableViewContentProvider;
import zigen.plugin.db.ui.editors.TableViewLabelProvider;
import zigen.plugin.db.ui.editors.TextCellEditor;
import zigen.plugin.db.ui.editors.event.TableKeyAdapter;
import zigen.plugin.db.ui.editors.event.TableKeyEventHandler;
import zigen.plugin.db.ui.editors.event.TableSortListener;
import zigen.plugin.db.ui.editors.internal.CellEditorType;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.jobs.ChangeColorJob;
import zigen.plugin.db.ui.jobs.RecordCountForQueryJob;
import zigen.plugin.db.ui.jobs.RecordCountForSqlEditorJob;
import zigen.plugin.db.ui.jobs.SqlExecJob;
import zigen.plugin.db.ui.views.ISQLOperationTarget;
import zigen.plugin.db.ui.views.StatusLineContributionItem;
import zigen.plugin.db.ui.views.internal.SQLOutinePage;
import zigen.plugin.db.ui.views.internal.SQLToolBarForSqlEditor;

public class SqlEditor2 extends SqlEditor implements IPageChangeListener, ITableViewEditor, IQueryViewEditor, IStatusChangeListener, IDocumentListener {

	public void documentAboutToBeChanged(DocumentEvent event) {
	}

	public void documentChanged(DocumentEvent event) {
		setDirty(true);
	}

	private ImageCacher ic = ImageCacher.getInstance();

	Table table;

	TableViewer viewer;

	TableElement[] elements;

	private String query;

	private TableSortListener sortListener;

	protected StatusLineContributionItem responseTimeItem;

	protected String responseTime;

	protected SelectAllRecordAction selectAllRecordAction;

	protected CopyRecordDataAction copyAction;

	protected CreateCSVForQueryAction createCSVForQueryAction;

	protected ChangeColorJob changeColorJob;

	protected Label infoLabel;

	TableKeyEventHandler handler;

	CellEditor[] cellEditors;

	SashForm sash;

	int[] defaultWeight = {500, 500};

	boolean isFocusResultView = false;

	public SqlEditor2() {
		super();
	}

	public void createPartControl(Composite parent) {
		sash = new SashForm(parent, SWT.VERTICAL | SWT.NONE);
		super.createPartControl(sash);
		createResultPartControl();
		makeActions();
		getSqlViewer().getDocument().addDocumentListener(this);
		// setKeyBinding();
	}

	private void makeActions() {
		selectAllRecordAction = new SelectAllRecordAction();
		// selectAllRecordAction.setActionDefinitionId("org.eclipse.ui.edit.selectAll");
		copyAction = new CopyRecordDataAction();
		createCSVForQueryAction = new CreateCSVForQueryAction();

		selectAllRecordAction.setActiveEditor(this);
		copyAction.setActiveEditor(this);
		createCSVForQueryAction.setActiveEditor(this);

	}

	public int getOffset() {
		return getSourceViewer().getTextWidget().getCaretOffset();
	}

	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {

		// Composite header = new Composite(parent, SWT.NONE);
		FormLayout layout = new FormLayout();
		parent.setLayout(layout);
		// createToolbarPart(parent);
		toolBar = new SQLToolBarForSqlEditor(this);
		toolBar.createPartControl(parent);

		Composite sqlComposite = new Composite(parent, SWT.NONE);
		sqlComposite.setLayout(new FillLayout());
		FormData data = new FormData();
		data.top = new FormAttachment(toolBar.getCoolBar(), 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		sqlComposite.setLayoutData(data);

		fAnnotationAccess = getAnnotationAccess();
		fOverviewRuler = createOverviewRuler(getSharedColors());

		SQLSourceViewer2 viewer = new SQLSourceViewer2(sqlComposite, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
		viewer.setSqlEditor(this);
		getSourceViewerDecorationSupport(viewer);

		viewer.getTextWidget().addFocusListener(new FocusAdapter() {

			public void focusGained(FocusEvent e) {
				IActionBars bars = getEditorSite().getActionBars();
				setGlobalActionForEditor(bars);
				bars.updateActionBars();
				isFocusResultView = false;
			}
		});

		return viewer;
	}

	Composite result = null;
	CoolBar coolBar1 = null;
	CoolItem pagerItem;
	QueryViewerPager pager;

	private void createResultPartControl() {
		if(result == null){
			result = new Composite(sash, SWT.NONE);
			result.setLayout(new FormLayout());
		}
		table = new Table(result, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		coolBar1 = new CoolBar(result, SWT.NONE);
		pagerItem = new CoolItem(coolBar1, SWT.FLAT);
		limit = DbPlugin.getDefault().getPreferenceStore().getInt(PreferencePage.P_MAX_VIEW_RECORD);
		pager = new QueryViewerPager(limit);
		pagerItem.setControl(pager.createStackedButtons(coolBar1));
		computeSize(pagerItem);
		pager.setPageNo(1);
		pager.addPageChangeListener(this);

		// ボタン
		FormData cData = new FormData();
		cData.left = new FormAttachment(0,0);
		cData.right = new FormAttachment(100,0);
		cData.bottom   = new FormAttachment(100,0);
		coolBar1.setLayoutData(cData);

		FormData tData = new FormData();
		tData.top   = new FormAttachment(0,0);
		tData.left = new FormAttachment(0,0);
		tData.right = new FormAttachment(100,0);
		tData.bottom   = new FormAttachment(coolBar1,0);
		table.setLayoutData(tData);


		//GridData gridData2 = new GridData(GridData.FILL_BOTH);
		//table.setLayoutData(gridData2);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(DbPlugin.getDefaultFont());
		viewer = new TableViewer(table);
		setHeaderColumn(table);
		viewer.setContentProvider(new TableViewContentProvider());
		viewer.setLabelProvider(new TableViewLabelProvider());

		table.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.F2) {
					int row = handler.getSelectedRow();
					handler.editTableElement(row, 1);

				}
			}
		});

		table.addFocusListener(new FocusAdapter() {

			public void focusGained(FocusEvent e) {
				if (table.getSelectionIndex() == -1) {
					table.select(0);
					table.notifyListeners(SWT.Selection, null);
				}
				IActionBars bars = getEditorSite().getActionBars();
				setGlobalActionForResultView(bars);
				bars.updateActionBars();
				isFocusResultView = true;
			}

			public void focusLost(FocusEvent e) {
				table.deselectAll();
			}
		});

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent e) {
				selectionChangeHandler(e);
			}
		});

		viewer.setInput(elements);

		changeColorJob = new ChangeColorJob(table);
		changeColorJob.setPriority(ChangeColorJob.LONG);
		changeColorJob.setUser(false);
		changeColorJob.schedule();

		handler = new TableKeyEventHandler(this);
		setCellModify(viewer, handler);

		columnsPack(table);
		getSite().setSelectionProvider(viewer);

		table.addControlListener(new ControlListener() {

			public void controlMoved(ControlEvent e) {
				int[] weight = sash.getWeights();
				if (weight[0] != 1000) {
					defaultWeight = sash.getWeights();
				}
			}

			public void controlResized(ControlEvent e) {}
		});

		if (elements == null) {
			setResultVisible(false);
		} else {
			setResultVisible(true);
		}
		hookContextMenu();
		
		//table.setBounds(table.getParent().getBounds());
		//table.pack(true);
		//table.update();
		//table.setVisible(true);

	}

	private void computeSize(CoolItem item) {
		Control control = item.getControl();
		Point pt = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		pt = item.computeSize(pt.x + 5, pt.y);
		item.setSize(pt);
	}

	void setGlobalActionForEditor(IActionBars bars) {

		bars.clearGlobalActionHandlers();
		copyAction.refresh();

		bars.setGlobalActionHandler(ActionFactory.COPY.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.COPY));
		bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), new GlobalAction(sqlViewer, ITextOperationTarget.SELECT_ALL));
		bars.setGlobalActionHandler("zigen.plugin.db.actions.SQLExecuteAction", new GlobalAction(sqlViewer, ISQLOperationTarget.ALL_EXECUTE)); //$NON-NLS-1$
		bars.setGlobalActionHandler("zigen.plugin.db.actions.SQLCurrentExecuteAction", new GlobalAction(sqlViewer, ISQLOperationTarget.CURRENT_EXECUTE)); //$NON-NLS-1$
		bars.setGlobalActionHandler("zigen.plugin.db.actions.SQLSelectedExecuteAction", new GlobalAction(sqlViewer, ISQLOperationTarget.SELECTED_EXECUTE)); //$NON-NLS-1$

		bars.updateActionBars();

		ICommandService commandService = (ICommandService) getSite().getService(ICommandService.class);
		// CTRL+C
		Command copy = commandService.getCommand("org.eclipse.ui.edit.copy");
		copy.setHandler(new ActionHandler(new GlobalAction(sqlViewer, ITextOperationTarget.COPY)));

		// CTRL+A
		Command select = commandService.getCommand("org.eclipse.ui.edit.selectAll");
		select.setHandler(new ActionHandler(new GlobalAction(sqlViewer, ITextOperationTarget.SELECT_ALL)));

	}

	void setGlobalActionForResultView(IActionBars bars) {

		bars.clearGlobalActionHandlers();
		copyAction.refresh();

		bars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);
		bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAllRecordAction);
		bars.updateActionBars();

		// CTRL+C
		ICommandService commandService = (ICommandService) getSite().getService(ICommandService.class);
		Command copy = commandService.getCommand("org.eclipse.ui.edit.copy");
		copy.setHandler(new ActionHandler(copyAction));

		// CTRL+A
		Command select = commandService.getCommand("org.eclipse.ui.edit.selectAll");
		select.setHandler(new ActionHandler(selectAllRecordAction));

	}

	void setCellModify(TableViewer viewer, TableKeyEventHandler handler) {
		if (elements == null)
			return;
		final IActionBars bars = getEditorSite().getActionBars();
		TableElement element = elements[0];
		int size = element.getColumns().length + 1;
		String[] properties = new String[size];
		zigen.plugin.db.core.TableColumn[] cols = element.getColumns();
		cellEditors = new CellEditor[size];
		TableKeyAdapter keyAdapter = new TableKeyAdapter(handler);
		for (int i = 0; i < cellEditors.length; i++) {
			properties[i] = String.valueOf(i);
			if (i > 0) {
				CellEditor cellEditor = new TextCellEditor(table, i);

				if (cellEditor.getControl() instanceof Text) {
					Text txt = (Text) cellEditor.getControl();
					txt.setEditable(false);
				}
				cellEditor.getControl().addKeyListener(keyAdapter);
				cellEditor.getControl().addTraverseListener(keyAdapter);
				cellEditor.getControl().addFocusListener(new FocusAdapter() {

					public void focusGained(FocusEvent e) {
						bars.clearGlobalActionHandlers();
						bars.updateActionBars();
					}

					public void focusLost(FocusEvent e) {
						// setInfomationText(EDIT_MODE_OFF); non message
					}
				});
				cellEditors[i] = cellEditor;

			}
		}
		viewer.setColumnProperties(properties);
		viewer.setCellModifier(new ICellModifier() {

			public boolean canModify(Object element, String property) {
				return true;
			}

			public Object getValue(Object element, String property) {
				int index = Integer.parseInt(property);
				if (element instanceof TableElement) {
					TableElement elem = (TableElement) element;
					Object obj = elem.getItems()[index - 1];
					if (obj != null) {
						if (obj instanceof String) {
							return (String) obj;
						} else {
							return CellEditorType.getDataTypeName(elem.getColumns()[index]);
						}
					} else {
						return ""; //$NON-NLS-1$

					}
				}
				return null;
			}

			public void modify(Object element, String property, Object value) {}

		});
		viewer.setCellEditors(cellEditors);
	}

	public void refleshAction() {
		// selectAllRecordAction.refresh();
		copyAction.refresh();
	}

	void selectionChangeHandler(SelectionChangedEvent event) {
		refleshAction();
	}

	void setHeaderColumn(Table table) {
		if (elements != null) {
			// TableColumn row = new TableColumn(table, SWT.LEFT);
			TableColumn row = new TableColumn(table, SWT.RIGHT);

			sortListener = new TableSortListener(this, 0);
			row.addSelectionListener(sortListener);
			row.pack();
			TableElement element = elements[0];
			zigen.plugin.db.core.TableColumn[] columns = element.getColumns();
			for (int i = 0; i < columns.length; i++) {
				zigen.plugin.db.core.TableColumn tColumn = columns[i];
				TableColumn col = new TableColumn(table, SWT.LEFT);
				col.setText(tColumn.getColumnName());
				col.addSelectionListener(new TableSortListener(this, i + 1));
				col.pack();
			}
		}
	}

	void columnsPack(Table table) {
		table.setVisible(false);
		TableColumn[] cols = table.getColumns();
		for (int i = 0; i < cols.length; i++) {
			cols[i].pack();
		}
		table.setVisible(true);
	}

	public void changeColumnColor() {
		throw new UnsupportedOperationException("Unsupported"); //$NON-NLS-1$

	}

	public void changeColumnColor(Column column) {
		throw new UnsupportedOperationException("Unsupported"); //$NON-NLS-1$
	}

	public void editTableElement(Object element, int column) {
		throw new UnsupportedOperationException("Unsupported"); //$NON-NLS-1$
	}

	public String getCondition() {
		throw new UnsupportedOperationException("Unsupported"); //$NON-NLS-1$
	}

	public IDBConfig getDBConfig() {
		return super.getConfig();
	}

	public TableElement getHeaderTableElement() {
		if (this.elements.length > 0) {
			return elements[0];
		}
		return null;
	}

	public ITable getTableNode() {
		return null;
	}

	public TableViewer getViewer() {
		return viewer;
	}

	public void setEnabled(boolean enabled) {
		throw new UnsupportedOperationException("Unsupported"); //$NON-NLS-1$

	}

	public void setTotalCount(int dispCount, long totalCount) {
		//this.totalCount = totalCount;

		NumberFormat format = NumberFormat.getInstance();
		String displayCount = format.format(dispCount);
		String displayTotalCount = format.format(totalCount);

		StringBuffer sb = new StringBuffer();
		sb.append("["); //$NON-NLS-1$
		sb.append(getDBConfig().getDbName());
		sb.append("] "); //$NON-NLS-1$
		sb.append(displayCount);
		sb.append(""); //$NON-NLS-1$
		if (!"".equals(displayTotalCount)) { //$NON-NLS-1$
			if ("-1".equals(displayTotalCount)) { //$NON-NLS-1$
				;
			} else {
				sb.append(" / "); //$NON-NLS-1$
				sb.append(""); //$NON-NLS-1$
				sb.append(displayTotalCount);
				sb.append(Messages.getString("TableViewEditorFor31.12")); //$NON-NLS-1$
			}
		} else {
			sb.append(Messages.getString("TableViewEditorFor31.13")); //$NON-NLS-1$
		}
		sb.append(Messages.getString("TableViewEditorFor31.14")); //$NON-NLS-1$
		sb.append(responseTime);
		sb.append("]"); //$NON-NLS-1$

		//setPageText(0, sb.toString());

		pager.setLimit(limit);
		pager.setRecordCount((int) totalCount);
		computeSize(pagerItem);

	}

	public void setResultVisible(boolean visibled) {
		if (visibled) {
			sash.setWeights(defaultWeight);
		} else {
			sash.setWeights(new int[] {1000, 0});
		}
	}

	public void update(String query, TableElement[] elements, String responseTime, boolean isNew) {
		try {
			this.query = query;
			this.elements = elements;
			
			if(isNew){
				result.dispose();
				result = null;
				createResultPartControl();
				sash.layout(true);
				sash.getParent().layout(true);			
			}else{
				viewer.setInput(elements);
				columnsPack(table);
				changeColorJob = new ChangeColorJob(table);
				changeColorJob.setPriority(ChangeColorJob.LONG);
				changeColorJob.setUser(false);
				changeColorJob.schedule();
			}
			
			setResponseTime(responseTime);
			int dispCnt = elements.length - 1;

			Transaction trans = Transaction.getInstance(getDBConfig());
			RecordCountForSqlEditorJob job2 = new RecordCountForSqlEditorJob(this, trans, query, dispCnt);
			job2.setUser(false);
			job2.setPriority(RecordCountForQueryJob.LONG);
			job2.schedule();

			
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}


	void setResponseTime(String responseTime) {
		this.responseTime = responseTime;

		if (responseTimeItem != null && responseTime != null && !"".equals(responseTime)) { //$NON-NLS-1$
			StringBuffer sb = new StringBuffer();
			sb.append(Messages.getString("QueryViewEditor2.13")); //$NON-NLS-1$
			sb.append(responseTime);
			responseTimeItem.setText(sb.toString());

		}
	}
	
	public String getQuery() {
		return query;
	}

	void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				if (isFocusResultView) {
					getContributor().fillContextMenuForResultView(manager);
				} else {
					getContributor().fillContextMenu(manager);
				}
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		super.editorContextMenuAboutToShow(menu);
		menu.add(new Separator());
	}

	private SqlEditorContributor getContributor() {
		IEditorActionBarContributor contributor = getEditorSite().getActionBarContributor();
		if (contributor instanceof SqlEditorContributor) {
			return (SqlEditorContributor) contributor;
		} else {
			return null;
		}
	}

	public Object getAdapter(Class adapter) {
		if (IContentOutlinePage.class.equals(adapter)) {
			if (outlinePage == null) {
				outlinePage = new SQLOutinePage(this);
			}
			return outlinePage;
		}
		return super.getAdapter(adapter);
	}

	public int getRecordLimit() {
		return 0;
	}

	public int getRecordOffset() {
		return 0;
	}
	protected int limit = 0;
	protected int offset = 0;

	public void pageChanged(int status, int offset, int limit) {
		this.offset = offset;
		this.limit = limit;
		updateTableViewer(offset, limit);
	}
	protected void updateTableViewer(int offset, int limit) {
		if (limit == 0) {
			offset = 0;
		}
		Transaction trans = Transaction.getInstance(getDBConfig());
		MySqlExecForPagerJob job = new MySqlExecForPagerJob(trans, query, offset, limit);
		// job.setPriority(Job.SHORT);
		job.setUser(false);
		job.schedule();
//

	}
	
	class MySqlExecForPagerJob extends SqlExecJob {

		protected int offset;
		protected int limit;

		public MySqlExecForPagerJob(Transaction trans, String sqlString, int offset, int limit) {
			super(trans, sqlString, null, true);
			this.offset = offset;
			this.limit = limit;
		}

		protected void showDBEditor(String query) throws Exception {
			TableElement[] elements = null;
			TimeWatcher time = new TimeWatcher();
			time.start();
			IDBConfig config = trans.getConfig();
			try {
				elements = SQLInvoker.executeQueryForPager(trans.getConnection(), query, config.isConvertUnicode(), config.isNoLockMode(), offset, limit);
				time.stop();
				showResults(new MyShowResultAction(config, query, elements, time.getTotalTime()));
			} catch (Exception e) {
				throw e;
			}
		}
	}
	
	class MyShowResultAction implements Runnable {

		IDBConfig config = null;

		String query = null;

		TableElement[] elements = null;

		String responseTime = null;

		public MyShowResultAction(IDBConfig config, String query, TableElement[] elements, String responseTime) {
			this.config = config;
			this.query = query;
			this.elements = elements;
			this.responseTime = responseTime;
		}

		public void run() {
			try {
				update(query, elements, responseTime, false);				
			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}

	}
}


