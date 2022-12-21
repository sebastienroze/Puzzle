package rad;

import java.awt.FlowLayout;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.JPanel;

import rad.html.HtmlContainer;
import rad.swing.SwingContainer;

public abstract class SQLGridSearch {
	public String padFieldName=null;	
	public RadSQLGrid grille;
	public RadComponentFactory compFact;
	private ListenerRAD searchListener=null;

	public abstract void init();

	public abstract String getFilter(ArrayList<String> values);

	public abstract RadContainer getContainer();
	
	private SQLGridSearch(String padFieldName, RadComponentFactory componentFactory) {
		compFact = componentFactory;
		this.padFieldName = padFieldName;
		init();
	}
	public String getPadFieldName() {
		if (padFieldName==null) return "";
		return padFieldName;
	}

	public SQLGridSearch(RadComponentFactory componentFactory) {
		this("",componentFactory);
	}

	public void search() {
		if (searchListener != null) {
			searchListener.actionPerformed(grille);
		}
		if (grille!=null) grille.filter();		
	}

	public RadContainer createContainer(String nom) {
		RadContainer container = compFact.createContainer(getPadFieldName() +nom);
		if (container instanceof HtmlContainer) {
			((HtmlContainer) container).setClassName("recherche");
		}
		if (container instanceof SwingContainer) {
			((JPanel) ((SwingContainer) container).getComponent()).setLayout(new FlowLayout(FlowLayout.LEFT));
		}
		return container;
	}

	public RadTextField createTextField(String nomChamp) {
		RadTextField tf = compFact.createTextField(getPadFieldName() +nomChamp);
		tf.addChangeListener(new ListenerRAD() {
			@Override
			public void actionPerformed(RadComponent cpn) {
				SQLGridSearch.this.search();
			}
		});
		tf.setValue("");
		return tf;
	}

	public RadRadioGroup createRadioGroup(String name, String[] values, String[] labels) {
		RadRadioGroup rg = compFact.createRadRadioGroup(getPadFieldName() +name, values, labels);
		rg.addChangeListener(new ListenerRAD() {
			@Override
			public void actionPerformed(RadComponent cpn) {
				SQLGridSearch.this.search();
			}
		});
		return rg;
	}

	public RadComboBox createSQLComboBox(String name, Connection conn, String sql) {
		RadComboBox cb = compFact.createSQLComboBox(getPadFieldName() +name, conn, sql);
		cb.addChangeListener(new ListenerRAD() {
			@Override
			public void actionPerformed(RadComponent cpn) {
				SQLGridSearch.this.search();
			}
		});
		return cb;
	}

	public SQLGridSearch addSearchListener(ListenerRAD al) {
		this.searchListener = al;
		return this;
	}

}
