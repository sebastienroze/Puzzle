package rad.swing;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SwingDateField extends SwingTextField {

	public SwingDateField() {
		super(10);
	}

	@Override
	public void setValue(String value) {
		if ("".equals(value) || value == null) {
			super.setValue(null);
		} else {
			SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
			Date date = Date.valueOf(value);
			super.setValue(date_format.format(date));
		}
	}

	@Override
	public String getValue() {
		String stringDate = super.getValue();
		if ("".equals(stringDate) || stringDate == null)
			return null;
		SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat date_formatSQL = new SimpleDateFormat("yyyy-MM-dd");
		String stringDateSQL = stringDate;
		try {
			stringDateSQL = date_formatSQL.format(date_format.parse(stringDate));
		} catch (ParseException parseException) {
		}
		return stringDateSQL;
	}
}
