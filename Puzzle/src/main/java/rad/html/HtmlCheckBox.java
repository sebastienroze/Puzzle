package rad.html;

import rad.ListenerRAD;
import rad.RadCheckBox;

public class HtmlCheckBox extends HtmlComponent implements RadCheckBox {
	private String value;
	private ListenerRAD actionListener = null;

	public HtmlCheckBox(String name) {
		super(name);
		useDataValue = true;
	}

	public HtmlCheckBox addActionListener(ListenerRAD al) {
		this.actionListener = al;
		return this;
	}

	@Override
	protected String HtmlReadOnly() {
		if (readOnly) {
			return "disabled ";
		}
		return "";
	}

	@Override
	public void triggerEvent(String eventName) {
		performAction();
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

	public void setBooleanValue(boolean booleanValue) {
		value =  booleanValue?"1":"0";
	}
	
	@Override
	public boolean getBooleanValue() {
		return (value != null) && !"0".equals(value);
	}

	@Override
	public String getHtml() {
		String onchange = "";
		if (actionListener != null) {
			onchange = getRADEvent("change");
		}
		String checked = "";
		if (getBooleanValue())
			checked = " checked ";
		String dataValue = value;
		if (dataValue==null) dataValue = "0";
		return "<input type=\"checkbox\" " + checked + "name=\"" + getName() + "\" " + "id=\"id" + getName() + "\" "
				+ "data-value=\"" + dataValue + "\" " + HtmlReadOnly() + htmlExtra()
				+ " onChange=\"setDataValueById('" + "id" + getName() + "',Number(this.checked));"+onchange+"\""
				+ ">";
	}

	@Override
	public String getResponseProperties() {
		String rp = super.getResponseProperties();
		StringBuilder response = new StringBuilder();
		if (!defaultReadOnly) response.append(",[\"id" + getName() + "\",\"disabled\"," + readOnly + "]");
		if (!defaultValue) response.append(",[\"id" + getName()+ "\",\"checked\","+getBooleanValue() +"]");
		if (response.length()==0) return rp;
		if (rp==null) {
		response.deleteCharAt(0);
		} else {
			response.insert(0, rp);
		}
		return response.toString();
	}

	public void performAction() {
		if (this.actionListener != null)
			this.actionListener.actionPerformed(this);
	}

	@Override
	public void addChangeListener(ListenerRAD al) {
		this.actionListener = al;
	}

}
