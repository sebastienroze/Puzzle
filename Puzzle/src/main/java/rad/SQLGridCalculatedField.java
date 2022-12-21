package rad;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SQLGridCalculatedField {
	public int col;
	public String title;
	public SQLGridCalculatedField(int col, String title) {
		super();
		this.col = col;
		this.title = title;
	}
	public abstract String Calculate(ResultSet rs) throws SQLException;	
}
