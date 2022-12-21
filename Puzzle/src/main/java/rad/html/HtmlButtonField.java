package rad.html;

import rad.RadButtonField;

public class HtmlButtonField extends HtmlButton implements RadButtonField {
	private String value;

	public HtmlButtonField(String name, String text) {
		super(name, text);
	}

	@Override
	public void setValue(String value) {
		if ("".equals(value)) this.value=null;
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}
}
