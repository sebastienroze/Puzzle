package rad.html;

import rad.ListenerRAD;
import rad.RadRadioGroup;

public class HtmlRadioGroup extends HtmlComponent implements RadRadioGroup {
	private ListenerRAD onChangeListener = null;
	private String value = null;
	private String[] values;
	private String[] labels;

	public HtmlRadioGroup(String nomChamp, String[] values, String[] labels) {
		super(nomChamp);
		useDataValue = true;
		this.values = values;
		this.labels = labels;
	}

	@Override
	public String getHtml() {
		StringBuilder html = new StringBuilder();
		String onchange = "";
		if (onChangeListener != null) {
			onchange = getRADEvent("change");			
		}
		html.append("<div id=\"id" + getName() + "\" data-value=\""+value+"\">");
		for (int i = 0; i < values.length; i++) {
			String selected = "";
			if (values[i].equals(getValue()) ) {
				selected = "checked ";
			} else {
				if (readOnly) {
					selected = "disabled ";					
				}
			}
			html.append("<label for=\"id" + getName() + ":" + i + "\">" + labels[i] + "</label>");
			html.append("<input type=\"radio\"" +selected+ " value=\"" + values[i] + "\"" + " name=\"" + getName() + "\""
					+ " onChange=\"setDataValueById('" + "id" + getName() + "','" + values[i] + "');"+onchange+"\"" + " id=\"id"
					+ getName() + ':' + i + "\"" + ">");
		}
		html.append("</div>");
		return html.toString();

	}

	@Override
	public void setValue(String value) {
		super.setValue(value);
		this.value = value;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public String getResponseProperties() {
		String rp = super.getResponseProperties();
		
		boolean disabled = false;
		StringBuilder response = new StringBuilder();
	
		for (int i = 0; i < values.length; i++) {
			disabled = false;
			if (values[i].equals(value)) {
				if (!defaultValue) response.append(",[\"id" + getName() + ':' + i + "\",\"checked\",true]");
			} else {
				if (!defaultValue) response.append(",[\"id" + getName() + ':' + i + "\",\"checked\",false]");
				if (isReadOnly()) disabled = true;				
			}
			if (!defaultReadOnly || !defaultValue) response.append(",[\"id" + getName() + ':' + i + "\",\"disabled\"," + disabled + "]");
		}
		if (response.length()==0) return rp;
		if (rp==null) {
		response.deleteCharAt(0);
		} else {
			response.insert(0, rp);
		}
		return response.toString();
	}
	
	@Override
	public void addChangeListener(ListenerRAD al) {
		this.onChangeListener = al;
	}

	@Override
	public void triggerEvent(String eventName) {
		onChangeListener.actionPerformed(this);
	}

}
