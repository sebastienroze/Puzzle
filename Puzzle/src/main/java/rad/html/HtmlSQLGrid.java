package rad.html;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import rad.SQLGridCalculatedField;
import rad.SQLGridSearch;
import rad.ListenerRAD;
import rad.RadSQLGrid;

import java.sql.ResultSetMetaData;

public class HtmlSQLGrid extends HtmlComponent implements RadSQLGrid {
	private ListenerRAD rowSelect = null;
	private ListenerRAD mouseDoubleClick = null;
	private Connection conn;
	private String sql;
	private int hiddenFieldsCount;
	private SQLGridCalculatedField[] calculatedFields;
	private String KeyField;
	private String keyValue = null;
	private SQLGridSearch gridSearch = null;
	private int selectedLine = -1;
	private ArrayList<Long> keyValues;
	private String orderBy = "";
	private String groupBy = "";

	private boolean toRefresh = false;

	public HtmlSQLGrid(String name, Connection conn, String KeyField, String sql, int hiddenFieldsCount,
			SQLGridCalculatedField[] calculatedFields) {
		super(name);
		useDataValue = true;
		this.conn = conn;
		this.sql = sql;
		this.hiddenFieldsCount = hiddenFieldsCount;
		this.calculatedFields = calculatedFields;
		this.KeyField = KeyField;
		keyValues = new ArrayList<Long>();
	}

	public ArrayList<Long> getKeyValues() {
		return keyValues;
	}

	public void setGridSearch(SQLGridSearch gs) {
		gridSearch = gs;
		gs.grille = this;
	}

	public SQLGridSearch getGridSearch() {
		return gridSearch;
	}

	public String getSearchHtml() {
		if (gridSearch != null) {
			return ((HtmlContainer) (gridSearch.getContainer())).getHtml();
		} else
			return null;
	}

	public RadSQLGrid addRowSelectListener(ListenerRAD al) {
		rowSelect = al;
		return this;
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
	public String getHtml() {
		if (gridSearch != null) {
			HtmlContainer searchContainer = (HtmlContainer) gridSearch.getContainer();
			if (searchContainer == null)
				return getTableHtml();
			return searchContainer.getHtml() + getTableHtml();
		}
		return getTableHtml();
	}

	public String getTableHtml() {
		StringBuilder html = new StringBuilder();
		keyValues = new ArrayList<Long>();
		try {
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
			html.append("<thead>");
			html.append(getColumnsLabel(rs));
			html.append("</thead><tbody>");
			Long longKeyValue = getLongValue();
			selectedLine = -1;
			while (rs.next()) {
				if (rs.getLong(KeyField) == longKeyValue) {
					selectedLine = keyValues.size();
				}
				html.append(getRow(rs));
				keyValues.add(rs.getLong(KeyField));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		html.append("</tbody></table>");
		this.setRadInit("scroolRADGrid");
		html.insert(0, "<table " + "id = \"id" + name + "\"" + "name=\"" + getName() + "\" " + htmlExtra()
				+ "data-value = " + (selectedLine + 1) + ">");
		setValue((selectedLine + 1) + "");
		return html.toString();
	}

	public void filter() {
		toRefresh = true;
	}

	public void refreshKey(long keyValue) {
		this.keyValue = keyValue + "";
		toRefresh = true;
	}

	private String getColumnsLabel(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int nbCalcFieldCount = 0;
		if (calculatedFields != null) {
			nbCalcFieldCount = calculatedFields.length;
		}
		StringBuilder columnsLabel = new StringBuilder();
		int nbCalcField = 0;
		columnsLabel.append("<tr>");
		for (int i = 0; i < rsmd.getColumnCount() - hiddenFieldsCount + nbCalcFieldCount; i++) {
			boolean calculated = false;
			columnsLabel.append("<td>");
			if (nbCalcField < nbCalcFieldCount) {
				if (calculatedFields[nbCalcField].col == i) {
					calculated = true;
					columnsLabel.append(calculatedFields[nbCalcField].title);
					nbCalcField++;
				}
			}
			if (!calculated)
				columnsLabel.append(rsmd.getColumnLabel(i + 1 - nbCalcField + hiddenFieldsCount));
			columnsLabel.append("</td>");

		}
		columnsLabel.append("</tr>");
		return columnsLabel.toString();
	}

	private String getRow(ResultSet rs) throws SQLException {
		int nbCalcFieldCount = 0;
		if (calculatedFields != null) {
			nbCalcFieldCount = calculatedFields.length;
		}
		int nbCalcField = 0;
		String onclick = "";
		if (rowSelect != null) {
			onclick = getRADEvent("select");
		}
		String onDblClick = "";
		if (mouseDoubleClick != null) {
			onDblClick = getRADEvent("dblclick");
		}

		String selected = "";

		if (selectedLine == keyValues.size()) {
			selected = "class = \"line_selected\"";
		}

		StringBuilder row = new StringBuilder();
		row.append("<tr " + "onclick=\"selectGridLine(this);" + onclick + "\" " + "ondblclick=\"selectGridLine(this);"
				+ onDblClick + "\" " + selected + ">");
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 0; i < rsmd.getColumnCount() - hiddenFieldsCount + nbCalcFieldCount; i++) {
			boolean calculated = false;
			row.append("<td>");
			String valRow = null;
			if (nbCalcField < nbCalcFieldCount) {
				if (calculatedFields[nbCalcField].col == i) {
					calculated = true;
					valRow = calculatedFields[nbCalcField].Calculate(rs);
					nbCalcField++;
				}
			}
			if (!calculated)
				valRow = (rs.getString(i + 1 - nbCalcField + hiddenFieldsCount));
			row.append(HTMLString(valRow));
			row.append("</td>");
		}
		row.append("</tr>");
		return row.toString();

	}

	@Override
	public void setValue(String value) {
		if ("".equals(value) || value == null)
			setSelectedLine(-1);
		else
			setSelectedLine(Integer.parseInt(value) - 1);
	}

	public void setSelectedLine(Integer value) {
		super.setValue((value + 1) + "");
		selectedLine = value;
		if ((selectedLine > 0) && (selectedLine < keyValues.size()))
			keyValue = keyValues.get(selectedLine) + "";
		else
			this.keyValue = null;
	}

	public Long getLongValue() {
		Long val = -1L;
		try {
			val = Long.parseLong(keyValue);
		} catch (Exception e) {
		}
		return val;
	}

	@Override
	public void setReadOnly(boolean readonly) {

	}

	@Override
	public void selectKey(long keyValue) {
		this.keyValue = keyValue + "";
		selectedLine = -1;
		toRefresh = true;
	}

	@Override
	public String getResponseProperties() {
		if (toRefresh) {
			return "[\"id" + getName() + "\",\"innerHTML\"," + JSONString(getTableHtml()) + "]";
		}
		return null;
	}

	@Override
	public String getResponseFunctions() {
		if (toRefresh) {
			return "[\"scroolRADGridByID\",\"id" + getName() + "\"]";
		}
		return null;
	}

	@Override
	public void triggerEvent(String eventName) {
		if ("select".equals(eventName))
			rowSelect.actionPerformed(this);
		else if ("dblclick".equals(eventName))
			mouseDoubleClick.actionPerformed(this);
	}

	public RadSQLGrid addDoubleClicListener(ListenerRAD al) {
		mouseDoubleClick = al;
		return this;
	}

	@Override
	public int getSelectedLine() {
		return selectedLine;
	}

	@Override
	public long getSelectedKey() {
		if ((selectedLine < 0) || (selectedLine >= keyValues.size()))
			return -1;
		return keyValues.get(selectedLine);
	}

	@Override
	public void setDefault() {
		super.setDefault();
		toRefresh = false;
	}
}
