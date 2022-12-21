package rad.swing;

import java.sql.Connection;
import java.sql.SQLException;

import rad.DataRAD;
import rad.RadComponentFactory;

public class DataSwing extends DataRAD {

	public DataSwing(Connection conn, String tableName, String keyField) {
		super(conn, tableName, keyField);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SwingTextField createTextField(String fieldName) {
		SwingTextField tf = null;
		try {
			int colIndex = colIndex(fieldName);
			if (colIndex == 0) {
				System.out.println("Erreur RAD : Table " + getTableName() + " Champ " + fieldName + " inconnu");
			}
			int limitSize = getFieldSize(colIndex);
			if ((DataRAD.maxFieldLength > 0) && (limitSize > DataRAD.maxFieldLength))
				tf = new SwingTextField(DataRAD.maxFieldLength);
			else
				tf = new SwingTextField(limitSize);
			tf.getComponent().setDocument(new JTextFieldLimit(limitSize));
			createDataField(fieldName, tf);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tf;
	}
	
	@Override
	public SwingCheckBox createCheckBoxField(String fieldName) {
		SwingCheckBox cb = new SwingCheckBox();
		createDataField(fieldName, cb);
		return cb;
	}
	@Override
	public SwingTextField createDateField(String fieldName) {
		SwingDateField df = null;
		df = new SwingDateField();
		createDataField(fieldName, df);
		return df;
	}
	
	@Override
	public SwingStaticText createStaticText(String fieldName) {
		SwingStaticText st = new SwingStaticText();
		createDataField(fieldName, st);
		return st;
	}	
	
	@Override
	public SwingRadioGroup createRadioGroup(String fieldName, String[] values, String[] labels) {
		SwingRadioGroup rg = new SwingRadioGroup(values, labels);
		createDataField(fieldName, rg);
		return rg;
	}

	@Override
	public SwingComboBox createSQLComboBox(String fieldName, String sql) {
		SwingComboBox cb = new SwingComboBox();
		RadComponentFactory.FillSqlComboBox(cb, conn, sql);
		createDataField(fieldName, cb);
		return cb;
	}

	@Override
	public SwingButtonField createButtonField(String fieldName, String buttonlabel) {
		SwingButtonField bt = null;
		bt = new SwingButtonField(buttonlabel);
		createDataField(fieldName, bt);
		return bt;
	}

	@Override
	public DataSwing createLinkData(String linkField, String tableName, String keyField) {
		DataSwing data = new DataSwing(conn, tableName, keyField);
		createLink(linkField, data);
		return data;
	}

}
