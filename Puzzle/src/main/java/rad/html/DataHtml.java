package rad.html;

import java.sql.Connection;
import java.sql.SQLException;

import rad.DataRAD;
import rad.RadComponentFactory;

public class DataHtml extends DataRAD {
	private WebService webService = null;

	public DataHtml(WebService webService,Connection conn, String tableName, String keyField) {
		super(conn, tableName, keyField);
		this.webService = webService;
	}

	@Override
	public HtmlTextField createTextField(String fieldName) {
		HtmlTextField tf = new HtmlTextField(fieldName);
		try {
			int colIndex = colIndex(fieldName);
			if (colIndex == 0) {
				System.out.println("Erreur RAD : Table " + getTableName() + " Champ " + fieldName + " inconnu");
			}
			int limitSize = getFieldSize(colIndex);
			tf.setInputSize(limitSize);
			if ((DataRAD.maxFieldLength > 0) && (limitSize > DataRAD.maxFieldLength))
				tf.setVisualSize(DataRAD.maxFieldLength);
			else
				tf.setVisualSize(limitSize);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		createDataField(fieldName, tf);
		webService.addComponent(tf);		
		return tf;
	}

	@Override
	public HtmlCheckBox createCheckBoxField(String fieldName) {
		HtmlCheckBox cb = new HtmlCheckBox(fieldName);
		createDataField(fieldName, cb);
		webService.addComponent(cb);				
		return cb;
	}
	
	@Override
	public HtmlTextField createDateField(String fieldName) {
		HtmlTextField df = new HtmlDateField(fieldName);
		createDataField(fieldName, df);
		webService.addComponent(df);		
		return df;
	}		
	
	@Override
	public HtmlStaticText createStaticText(String fieldName) {
		HtmlStaticText st = new HtmlStaticText(fieldName);
		createDataField(fieldName, st);
		webService.addComponent(st);
		return st;
	}	
	
	@Override
	public HtmlRadioGroup createRadioGroup(String fieldName, String[] values, String[] labels) {
		HtmlRadioGroup rg = new HtmlRadioGroup(fieldName, values, labels);
		createDataField(fieldName, rg);
		webService.addComponent(rg);
return rg;
	}

	@Override
	public HtmlComboBox createSQLComboBox(String fieldName, String sql) {
		HtmlComboBox cb = new HtmlComboBox(fieldName);
		cb.addItem("", "");
		RadComponentFactory.FillSqlComboBox(cb, conn, sql);
		createDataField(fieldName, cb);
		webService.addComponent(cb);
		return cb;
	}

	@Override
	public DataHtml createLinkData(String linkField, String tableName, String keyField) {
		DataHtml data = new DataHtml(webService,conn, tableName, keyField);
		createLink(linkField, data);
		return data;
	}

	@Override
	public HtmlButtonField createButtonField(String fieldName, String buttonlabel) {
		HtmlButtonField bf = new HtmlButtonField(fieldName, buttonlabel);
		createDataField(fieldName, bf);
		webService.addComponent(bf);
		return bf;
	}	

}
