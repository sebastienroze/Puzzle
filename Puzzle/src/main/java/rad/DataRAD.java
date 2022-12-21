package rad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public abstract class DataRAD {
	public static int maxFieldLength = 0;
	protected Connection conn = null;
	protected ResultSetMetaData md = null;
	private String keyField;
	private HashMap<String, DataFieldRAD> fields;
	private String tableName;
	private Statement statement = null;
	private ResultSet rs = null;
	private HashMap<String, DataRAD> links;
	private ListenerBooleanRAD onValidateSave = null;
	private KeySelectListener onRefresh = null;
	private DataFieldRAD keyValueComponent = null;
	private Long keyValue = null;
	private boolean readOnly = false;

	public DataRAD(Connection conn, String tableName, String keyField) {
		super();
		this.tableName = tableName;
		this.keyField = keyField;
		this.fields = new HashMap<String, DataFieldRAD>();
		this.links = new HashMap<String, DataRAD>();
		this.conn = conn;
		try {
			this.statement = conn.createStatement();
			rs = statement.executeQuery("SELECT * FROM " + tableName + " WHERE " + keyField + " IS NULL");
			md = rs.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public abstract RadStaticText createStaticText(String fieldName);

	public abstract RadTextField createTextField(String fieldName);

	public abstract RadRadioGroup createRadioGroup(String fieldName, String[] values, String[] labels);

	public abstract RadComboBox createSQLComboBox(String fieldName, String sql);

	public abstract RadCheckBox createCheckBoxField(String fieldName);
	
	public abstract RadButtonField createButtonField(String fieldName, String buttonlabel);

	public abstract DataRAD createLinkData(String linkField, String tableName, String keyField);

	public abstract RadTextField createDateField(String fieldName);

	public String getTableName() {
		return tableName;
	}

	public DataFieldRAD createField(String fieldName) {
		DataFieldRAD df = new DataFieldRAD() {
			private String value;

			@Override
			public void setVisible(boolean visible) {
			}

			@Override
			public void setValue(String value) {
				this.value = value;
			}

			@Override
			public void setReadOnly(boolean readonly) {
			}

			@Override
			public String getValue() {
				return value;
			}
		};
		createDataField(fieldName, df);
		return df;
	}

	public void createDataField(String fieldName, DataFieldRAD dataField) {
		if (fieldName.equals(keyField)) {
			keyValueComponent = dataField;
		} else {
			fields.put(fieldName, dataField);
		}
		dataField.setReadOnly(readOnly);
	}

	public void createLink(String fieldName, DataRAD dr) {
		links.put(fieldName, dr);
	}

	public Long getKeyValue() {
		if (this.keyValueComponent == null)
			return keyValue;
		// Long value = 0L;
		Long value = null;
		try {
			value = Long.parseLong(keyValueComponent.getValue());
		} catch (Exception e) {
		}
		return value;
	}

	public void setKeyValue(Long keyValue) {
		if (this.keyValueComponent == null) {
			this.keyValue = keyValue;
		} else {
			if (keyValue == null)
				this.keyValueComponent.setValue(null);
			else
				this.keyValueComponent.setValue(keyValue + "");
		}
	}

	public void readLink(String linkField, Long KeyValue) {
		links.get(linkField).read(KeyValue);
	}

	public void readLink(String linkField) {
		if (fields.get(linkField).getValue() == null)
			readLink(linkField, null);
		else
			readLink(linkField, Long.parseLong(fields.get(linkField).getValue()));
	}

	public void setReadOnly(boolean readonly) {
		this.readOnly = readonly;
		for (Map.Entry<String, DataFieldRAD> entry : fields.entrySet()) {
			entry.getValue().setReadOnly(readonly);
		}
	}

	public void clear() {
		setKeyValue(null);
		for (Map.Entry<String, DataFieldRAD> entry : fields.entrySet()) {
			entry.getValue().setValue("");
		}
		for (Map.Entry<String, DataRAD> entry : links.entrySet()) {
			entry.getValue().clear();
		}
		doRefresh();
	}

	public boolean read(Long keyValue) {
		if (keyValue == null || keyValue == 0) {
			clear();
			return false;
		}
		setKeyValue(keyValue);
		try {
			rs = statement.executeQuery("SELECT * FROM " + tableName + " WHERE " + keyField + " = " + keyValue);
			if (rs.next()) {
				for (Map.Entry<String, DataFieldRAD> entry : fields.entrySet()) {
					entry.getValue().setValue(rs.getString(entry.getKey()));
				}
				for (Map.Entry<String, DataRAD> entry : links.entrySet()) {
					entry.getValue().read(rs.getLong(entry.getKey()));
				}
				doRefresh();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean read() {
		return read(getKeyValue());
	}

	public void delete() throws SQLException {
		delete(getKeyValue());
	}

	public void delete(long keyValue) throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("DELETE FROM " + tableName + " WHERE " + keyField + " = " + keyValue);
		ps.executeUpdate();
		setKeyValue(null);
		doRefresh();
	}

	public boolean update() throws SQLException {
		if (!doValidateSave())
			return false;
		int nbUpdate = 0;
		StringBuilder sqlUpdate = null;

		for (Map.Entry<String, DataFieldRAD> entry : fields.entrySet()) {
			if (sqlUpdate == null) {
				sqlUpdate = new StringBuilder("UPDATE " + tableName + " SET ");
			} else {
				sqlUpdate.append(",");
			}
			sqlUpdate.append(entry.getKey() + "=?");
		}
		sqlUpdate.append(" WHERE ID=?");
		PreparedStatement ps = conn.prepareStatement(sqlUpdate.toString());
		int i = 1;
		for (Map.Entry<String, DataFieldRAD> entry : fields.entrySet()) {
			String value = entry.getValue().getValue();
			if (value == null || "".equals(value)) {
				ps.setNull(i++, java.sql.Types.NULL);
			} else {
				ps.setString(i++, value);
			}
		}
		ps.setLong(i, getKeyValue());
		nbUpdate = ps.executeUpdate();
		if (nbUpdate > 0) {
			doRefresh();
			return true;
		} else {
			return false;
		}
	}

	public void create() throws SQLException {
		if (!doValidateSave())
			return;
		StringBuilder sqlInsert = null;
		StringBuilder sqlInsertVal = null;
		for (Map.Entry<String, DataFieldRAD> entry : fields.entrySet()) {
			if (sqlInsert == null) {
				sqlInsert = new StringBuilder("INSERT INTO " + tableName + " (");
				sqlInsertVal = new StringBuilder(") VALUES(");
			} else {
				sqlInsert.append(",");
				sqlInsertVal.append(",");
			}
			sqlInsert.append(entry.getKey());
			sqlInsertVal.append("?");
		}
		sqlInsert.append(sqlInsertVal + ")");
		PreparedStatement ps = conn.prepareStatement(sqlInsert.toString(), Statement.RETURN_GENERATED_KEYS);
		int i = 1;
		for (Map.Entry<String, DataFieldRAD> entry : fields.entrySet()) {
			String value = entry.getValue().getValue();
			if ("".equals(value)) {
				ps.setNull(i++, java.sql.Types.NULL);
			} else {
				ps.setString(i++, value);
			}
		}
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		setKeyValue(rs.getLong(1));
		doRefresh();
	}

	public void doRefresh() {
		if (onRefresh != null) {
			onRefresh.KeySelected(getKeyValue());
		}
	}

	public boolean doValidateSave() {
		if (onValidateSave != null) {
			return onValidateSave.getBoolean(this);
		}
		return true;
	}

	public DataRAD addRefresh(KeySelectListener ksl) {
		onRefresh = ksl;
		return this;
	}

	public DataRAD addValidateSaveListener(ListenerBooleanRAD vl) {
		onValidateSave = vl;
		return this;
	}

	protected int colIndex(String fieldName) throws SQLException {
		int count = md.getColumnCount();
		for (int i = 1; i <= count; i++) {
			if (md.getColumnName(i).equals(fieldName)) {
				return i;
			}
		}
		return 0;
	}

	protected int getFieldSize(int colIndex) throws SQLException {
		int size = md.getColumnDisplaySize(colIndex);
		if (md.getColumnType(colIndex) == 3) {
			size++;
		}
		return size;
	}

	public String getFieldValue(String fieldName) {
		return this.fields.get(fieldName).getValue();
	}

	public Double getFieldValueDouble(String fieldName) {
		try {
			return Double.parseDouble(fields.get(fieldName).getValue());
		} catch (Exception e) {
			return 0.0;
		}
	}

	public void setFieldValue(String fieldName, String value) {
		this.fields.get(fieldName).setValue(value);
	}

}

/*
 * private ArrayList<DataFieldRAD> getDataFields() { ArrayList<DataFieldRAD> df
 * = new ArrayList<DataFieldRAD>(); for (Map.Entry<String, DataFieldRAD> entry :
 * fields.entrySet()) { df.add(entry.getValue()); } for (Map.Entry<String,
 * DataRAD> entry : links.entrySet()) {
 * df.addAll(entry.getValue().getDataFields()); } return df; }
 */