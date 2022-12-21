package rad.swing;

import java.sql.Connection;

import rad.SQLGridCalculatedField;
import rad.RadButton;
import rad.RadComponentFactory;
import rad.RadContainer;
import rad.RadSQLGrid;
import rad.RadRadioGroup;
import rad.RadStaticText;
import rad.RadTextField;

public class SwingComponentFactory extends RadComponentFactory {
	@Override
	public RadTextField createTextField(String name) {
		return createTextField(name, 10);
	}

	@Override
	public RadTextField createTextField(String name, int size) {
		SwingTextField tf = new SwingTextField(size);
		return tf;
	}

	@Override
	public RadContainer createContainer(String name) {
		return new SwingContainer();
	}

	@Override
	public RadRadioGroup createRadRadioGroup(String name, String[] values, String[] labels) {
		return new SwingRadioGroup(values, labels);
	}

	@Override
	public SwingComboBox createSQLComboBox(String name, Connection conn, String sql) {
		SwingComboBox cb = new SwingComboBox();
		RadComponentFactory.FillSqlComboBox(cb, conn, sql);
		return cb;
	}

	@Override
	public RadSQLGrid createRadSQLGrid(String nom, Connection conn, String KeyField, String sql, int hiddenFieldsCount,
			SQLGridCalculatedField[] calculatedFields) {
		return new SwingSQLGrid(conn, KeyField, sql, hiddenFieldsCount, calculatedFields);
	}

	@Override
	public RadButton createButton(String name, String text) {
		return new SwingButton(text);
	}

	@Override
	public RadStaticText createStaticText(String name) {
		return new SwingStaticText();
	}

}
