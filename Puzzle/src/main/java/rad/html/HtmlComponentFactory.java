package rad.html;

import java.sql.Connection;

import rad.SQLGridCalculatedField;
import rad.RadButton;
import rad.RadComponentFactory;
import rad.RadSQLGrid;
import rad.RadTextField;

public class HtmlComponentFactory extends RadComponentFactory {
	private WebService webService = null;

	public HtmlComponentFactory(WebService webService) {
		this.webService = webService;
	}

	public WebService getWebService() {
		return webService;
	}

	@Override
	public RadTextField createTextField(String name) {
		HtmlTextField tf = new HtmlTextField(name);
		getWebService().addComponent(tf);
		return tf;
	}

	@Override
	public RadTextField createTextField(String name, int size) {
		HtmlTextField tf = new HtmlTextField(name);
		tf.setVisualSize(size);
		tf.setInputSize(size);
		getWebService().addComponent(tf);
		return tf;
	}

	@Override
	public HtmlStaticText createStaticText(String name) {
		HtmlStaticText st = new HtmlStaticText(name);
		getWebService().addComponent(st);
		return st;
	}	
	
	@Override
	public HtmlContainer createContainer(String name) {
		return new HtmlContainer(name);
	}

	@Override
	public HtmlRadioGroup createRadRadioGroup(String name, String[] values, String[] labels) {
		HtmlRadioGroup rg = new HtmlRadioGroup(name, values, labels);
		getWebService().addComponent(rg);
		return rg;
	}

	@Override
	public HtmlComboBox createSQLComboBox(String name, Connection conn, String sql) {
		HtmlComboBox cb = new HtmlComboBox(name);
		RadComponentFactory.FillSqlComboBox(cb, conn, sql);
		getWebService().addComponent(cb);
		return cb;
	}

	@Override
	public RadSQLGrid createRadSQLGrid(String nom, Connection conn, String KeyField, String sql, int hiddenFieldsCount,
			SQLGridCalculatedField[] calculatedFields) {
		HtmlSQLGrid grille;
		grille = new HtmlSQLGrid(nom, conn, KeyField, sql, hiddenFieldsCount, calculatedFields);
		getWebService().addComponent(grille);
		return grille;
	}

	@Override
	public RadButton createButton(String name, String text) {
		HtmlButton btn = new HtmlButton("bt" + name, text);
		getWebService().addComponent(btn);
		return btn;
	}
}
