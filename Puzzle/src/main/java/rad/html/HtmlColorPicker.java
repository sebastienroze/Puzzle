package rad.html;

import rad.ListenerRAD;

public class HtmlColorPicker extends HtmlComponent {
	private String value;
	private ListenerRAD actionListener = null;

	public HtmlColorPicker(String name) {
		super(name);
		useValue = true;
	}

	public HtmlColorPicker addActionListener(ListenerRAD al) {
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

	@Override
	public String getHtml() {
		String onchange = "";
		if (actionListener != null) {
			onchange = " oninput=\"" + getRADEvent("change") + "\"";
		}

		return "<input type=\"color\" " + "name=\"" + getName() + "\" " + "id=\"id" + getName() + "\" " + "value=\""
				+ getValue() + "\" " + HtmlReadOnly() + htmlExtra() + onchange + ">";
	}

	@Override
	public String getResponseProperties() {
		String rp = super.getResponseProperties();
		if (!defaultReadOnly) {
			if (rp == null)
				rp = "";
			else
				rp = rp + ",";
			rp = rp + "[\"id" + getName() + "\",\"disabled\"," + readOnly + "]";
		}
		return rp;
	}

	public void performAction() {
		if (this.actionListener != null)
			this.actionListener.actionPerformed(this);
	}
}
