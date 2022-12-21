package rad.html;

import rad.ListenerRAD;
import rad.RadTextField;

public class HtmlTextField extends HtmlComponent implements RadTextField {
	protected ListenerRAD onChangeListener = null;
	private String value;
	private String inputSize = null;
	private String visualSize = null;

	public HtmlTextField(String name) {
		super(name);
		useValue = true;
	}

	public void setInputSize(int inputSize) {
		this.inputSize = "maxlength=\"" + inputSize + "\"";
	}

	public void setVisualSize(int visualSize) {
		this.visualSize = "style = \"width:"+visualSize+"em;\"";
	}
	
	public String getVisualSize() {
		if (visualSize == null)
			return "";
		return visualSize;
	}
	
	public String getInputSize() {
		if (inputSize == null)
			return "";
		return inputSize;
	}

	@Override
	public String getHtml() {
		String onchange = "";
		if (onChangeListener != null) {
			onchange = " oninput=\""+getRADEvent("change")+"\"" ;			
		}
		return "<input " + "name = \"" + getName() + "\"" + "id = \"id" + getName() + "\""
		+ "value =\""+HTMLString(value) + "\""
		+getInputSize()+getVisualSize()
				+ HtmlReadOnly() + htmlExtra()+ onchange + ">";
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
	public void triggerEvent(String eventName) {
		onChangeListener.actionPerformed(this);
	}

	public int getIntegerValue() {
		int val = 0;
		try {
			val = Integer.parseInt(value);
		} catch (Exception e) {	}
		return val;

	}

	public long getLongValue() {
		long val = 0;
		try {
			val = Long.parseLong(value);
		} catch (Exception e) {}
		return val;
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
		return rp + "[\"id"+getName()+"\",\"readOnly\","+readOnly+"]";		
	}

	public void addChangeListener(ListenerRAD al) {
		this.onChangeListener = al;
	}

}
