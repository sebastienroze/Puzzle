package rad.html;

import java.util.ArrayList;

import rad.ListenerRAD;
import rad.RadComboBox;

public class HtmlComboBox  extends HtmlComponent implements RadComboBox  {
	private ListenerRAD onChangeListener = null;	
	private String value;
	private ArrayList<String> values;
	private ArrayList<String> labels;

	public HtmlComboBox(String name) {
		super(name);
		useValue = true;
		values = new ArrayList<String>();
		labels = new ArrayList<String>();		
	}

	@Override
	public void addChangeListener(ListenerRAD al) {
		this.onChangeListener = al;		
	}
	@Override
	public String HtmlReadOnly() {
		if (readOnly) {
			return "disabled ";
		}
		return "";
	}

	@Override
	public String getHtml() {
		StringBuilder html = new StringBuilder();
		String onchange = "";
		if (onChangeListener != null) {
			onchange = " onChange=\""+getRADEvent("change")+"\"" ;			
		}
		html.append("<select "+HtmlReadOnly()+"id=\"id" + getName() + "\"");
		html.append(onchange);		
		html.append(">");
		for (int i = 0; i < values.size(); i++) {
			String selected = "";
			if (values.get(i).equals(getValue()) ) {
				selected = " selected";				
			}
			html.append("<option"+selected);
			html.append(selected);
			html.append(" value =\"");
			html.append(values.get(i));
			html.append("\">");
			html.append(HTMLString(labels.get(i)));
			html.append("</option>");			
		}		
		html.append("</select>");
		
		return html.toString();
	}
	
	public void addItem(String value, String label) {
		values.add(value);
		labels.add(label);
		
	}
	
	@Override
	public void setValue(String value) {
		super.setValue(value);
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}
	
	@Override
	public String getDisplayValue() {
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) == value) return labels.get(i);
			
		}
		return null;
	}
	
	
	@Override
	public void triggerEvent(String eventName) {
		onChangeListener.actionPerformed(this);
	}

	@Override
	public String getResponseProperties() {
		String rp = super.getResponseProperties();
		if (defaultReadOnly)
			return rp;
		if (rp != null) {
			rp += ",";
		} else {
			rp = "";
		}
		return rp + "[\"id"+getName()+"\",\"disabled\","+readOnly+"]";		
	}	
}
