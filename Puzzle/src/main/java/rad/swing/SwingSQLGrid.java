package rad.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import rad.SQLGridCalculatedField;
import rad.SQLGridSearch;
import rad.ListenerRAD;
import rad.RadSQLGrid;

public class SwingSQLGrid extends SwingComponent implements RadSQLGrid {
	private ListenerRAD rowSelect = null;
	private ListenerRAD mouseDoubleClick = null;
	private JScrollPane scrollPane;
	private int hiddenFieldsCount;
	private SQLGridCalculatedField[] calculatedFields;
	private DefaultTableModel model;
	private ArrayList<Long> keyValues;
	private Connection conn;
	private String sql;
	private JTable table;
	private String KeyField;
	private boolean noSelection;
	private SQLGridSearch gridSearch = null;
	private String orderBy = "";
	private String groupBy = "";
	public SwingSQLGrid(Connection conn, String KeyField, String sql, int hiddenFieldsCount,
			SQLGridCalculatedField[] calculatedFields) {
		super();
		this.conn = conn;
		this.sql = sql;
		this.hiddenFieldsCount = hiddenFieldsCount;
		this.calculatedFields = calculatedFields;
		this.KeyField = KeyField;
		noSelection = false;
		keyValues = new ArrayList<Long>();
		table = new JTable();
		table.setAutoCreateRowSorter(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		model = null;

/*		try {
			model = new javax.swing.table.DefaultTableModel(new String[][] {}, getColumnsLabel(rs)) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			table.setModel(model);
			filter();
			
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				model.addRow(getRow(rs));
				keyValues.add(rs.getLong(KeyField));
			}
			
			
		} catch (SQLException e) {
			System.out.println("Erreur SQL " + sql);
			e.printStackTrace();
		}
		
		*/
		scrollPane = new JScrollPane(table);
	}

	private void setColomnsLabel(ResultSet rs) throws SQLException {
		if (model !=null) return;
		model = new javax.swing.table.DefaultTableModel(new String[][] {}, getColumnsLabel(rs)) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(model);		
	}
	
	@Override
	public void setOrderBy(String orderBy) {
		if (orderBy == null || "".equals(orderBy)) {
			this.orderBy = "";
		} else {
			this.orderBy = " ORDER BY " + orderBy;
		}
	}

	@Override
	public void setGroupBy(String groupBy) {
		if (groupBy == null || "".equals(groupBy)) {
			this.groupBy = "";
		} else {
			this.groupBy = " GROUP BY " + groupBy;
		}
	}

	@Override
	public void filter() {
		noSelection = true;
		try {
//			Statement statement = conn.createStatement();
//			ResultSet rs = statement.executeQuery(sql + sqlFilter);
			ResultSet rs = null;
			if (gridSearch != null) {
				ArrayList<String> values = new ArrayList<String>();
				String sqlFilter = gridSearch.getFilter(values);
				PreparedStatement statement;
				if (sqlFilter == null || "".equals(sqlFilter)) {
					statement = conn.prepareStatement(sql + orderBy + groupBy);
				} else {
					statement = conn.prepareStatement(sql + " WHERE " + sqlFilter + orderBy + groupBy);
				}
				for (int i = 0; i < values.size(); i++) {
					statement.setString(i + 1, values.get(i));
				}
				rs = statement.executeQuery();
			} else {
				rs = conn.createStatement().executeQuery(sql + orderBy + groupBy);
			}
			setColomnsLabel(rs);			
			keyValues.clear();
			model.setRowCount(0);
			while (rs.next()) {
				keyValues.add(rs.getLong(KeyField));
				model.addRow(getRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		noSelection = false;
	}

	@Override
	public void selectKey(long keyValue) {
		noSelection = true;
		int rowIndex = keyValues.indexOf(keyValue);
		table.clearSelection();
		if (rowIndex >= 0) {
			rowIndex = table.convertRowIndexToView(rowIndex);
			table.addRowSelectionInterval(rowIndex, rowIndex);
			SwingUtil.scrollToVisible(table, rowIndex, 0);
		}
		noSelection = false;
	}

	@Override
	public void refreshKey(long keyValue) {
		Statement statement;
		noSelection = true;
		int rowIndex = keyValues.indexOf(keyValue);
		try {
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql + " WHERE " + KeyField + "=" + keyValue);
			if (rs.next()) {
				if (rowIndex >= 0) {
					model.removeRow(rowIndex);
					model.insertRow(rowIndex, getRow(rs));
					rowIndex = table.convertRowIndexToView(rowIndex);
					table.addRowSelectionInterval(rowIndex, rowIndex);
				} else {
					model.addRow(getRow(rs));
					keyValues.add(rs.getLong(KeyField));
					table.clearSelection();
					rowIndex = table.getRowCount() - 1;
					rowIndex = table.convertRowIndexToView(rowIndex);
					table.addRowSelectionInterval(rowIndex, rowIndex);

					/*
					 * scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().
					 * getMaximum()); SwingUtilities.invokeLater(new Runnable() { public void run()
					 * {
					 * scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().
					 * getMaximum()); } });
					 * 
					 */
				}
				SwingUtil.scrollToVisible(table, rowIndex, 0);
			} else {
				if (rowIndex >= 0) {
					model.removeRow(rowIndex);
					keyValues.remove(rowIndex);
				} else {
					table.clearSelection();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		noSelection = false;
	}

	private String[] getColumnsLabel(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int nbCalcFieldCount = 0;
		if (calculatedFields != null) {
			nbCalcFieldCount = calculatedFields.length;
		}
		String[] columnsLabel = new String[rsmd.getColumnCount() - hiddenFieldsCount + nbCalcFieldCount];
		int nbCalcField = 0;
		for (int i = 0; i < rsmd.getColumnCount() - hiddenFieldsCount + nbCalcFieldCount; i++) {
			boolean calculated = false;
			if (nbCalcField < nbCalcFieldCount) {
				if (calculatedFields[nbCalcField].col == i) {
					calculated = true;
					columnsLabel[i] = calculatedFields[nbCalcField].title;
					nbCalcField++;
				}
			}
			if (!calculated)
				columnsLabel[i] = rsmd.getColumnLabel(i + 1 - nbCalcField + hiddenFieldsCount);
		}
		return columnsLabel;
	}

	private Object[] getRow(ResultSet rs) throws SQLException {
		int colCount = rs.getMetaData().getColumnCount();
		int nbCalcFieldCount = 0;
		if (calculatedFields != null) {
			nbCalcFieldCount = calculatedFields.length;
		}
		int nbCalcField = 0;
		Object[] row = new Object[colCount - hiddenFieldsCount + nbCalcFieldCount];
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 0; i < rsmd.getColumnCount() - hiddenFieldsCount + nbCalcFieldCount; i++) {
			boolean calculated = false;
			if (nbCalcField < nbCalcFieldCount) {
				if (calculatedFields[nbCalcField].col == i) {
					calculated = true;
					row[i] = calculatedFields[nbCalcField].Calculate(rs);
					nbCalcField++;
				}
			}
			if (!calculated)
				row[i] = rs.getString(i + 1 - nbCalcField + hiddenFieldsCount);
		}
		return row;
	}

	@Override
	public RadSQLGrid addDoubleClicListener(ListenerRAD al) {
		mouseDoubleClick = al;
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
//					long id = keyValues.get(selectedLine);					
					SwingSQLGrid.this.mouseDoubleClick.actionPerformed(SwingSQLGrid.this);
				}
			}
		});
		return this;
	}

	@Override
	public RadSQLGrid addRowSelectListener(ListenerRAD al) {
		rowSelect = al;
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = SwingSQLGrid.this.table.getSelectedRow();
				if (!SwingSQLGrid.this.noSelection && (row >= 0) && !e.getValueIsAdjusting()) {
					SwingSQLGrid.this.rowSelect.actionPerformed(SwingSQLGrid.this);
				}
			}
		});
		return this;
	}

	@Override
	public Component getComponent() {
		if (gridSearch == null) {
			return scrollPane;
		} else {
			Component searchPane = getSearchPane();
			if (searchPane == null)
				return scrollPane;
			JPanel jpGrille = new JPanel();
			jpGrille.setLayout(new BorderLayout());
			jpGrille.add(scrollPane, BorderLayout.CENTER);
			jpGrille.add(searchPane, BorderLayout.NORTH);
			return jpGrille;
		}
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public Component getSearchPane() {
		if (gridSearch != null) {
			SwingContainer radContainer = (SwingContainer) gridSearch.getContainer();
			if (radContainer == null)
				return null;
			return radContainer.getComponent();
		} else
			return null;
	}

	@Override
	public void setGridSearch(SQLGridSearch gs) {
		gridSearch = gs;
		gs.grille = this;
	}

	@Override
	public void setVisible(boolean visible) {
		scrollPane.setVisible(visible);
	}

	@Override
	public SQLGridSearch getGridSearch() {
		return gridSearch;
	}

	@Override
	public ArrayList<Long> getKeyValues() {
		return keyValues;
	}

	@Override
	public int getSelectedLine() {
		int row = SwingSQLGrid.this.table.getSelectedRow();
		if (row < 0)
			return -1;
		return SwingSQLGrid.this.table.convertRowIndexToModel(row);
	}

	@Override
	public long getSelectedKey() {
		int selectedLine = getSelectedLine();
		if ((selectedLine < 0) || (selectedLine >= keyValues.size()))
			return -1;
		return keyValues.get(selectedLine);
	}

}
