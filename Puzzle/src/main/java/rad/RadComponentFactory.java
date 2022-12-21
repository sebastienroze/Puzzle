package rad;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class RadComponentFactory {
	public abstract RadTextField createTextField(String name);
	public abstract RadTextField createTextField(String name,int size);
	public abstract RadStaticText createStaticText(String name);	
	public abstract RadButton createButton(String name,String text);
	public abstract RadContainer createContainer(String name);
	public abstract RadRadioGroup createRadRadioGroup(String name, String[] values, String[] labels);
	public abstract RadComboBox createSQLComboBox(String name,Connection conn, String sql);
	public abstract RadSQLGrid createRadSQLGrid(String name,Connection conn, String KeyField, String sql, int hiddenFieldsCount,
			SQLGridCalculatedField[] calculatedFields) ;
	
	public static void FillSqlComboBox(RadComboBox cb, Connection conn, String sql) {
		try {
			ResultSet rsql = conn.createStatement().executeQuery(sql);
			while (rsql.next()) {
				cb.addItem(rsql.getString(1),rsql.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
//			cb.addItem("erreur",e.getLocalizedMessage());
		}		
		
	}
}