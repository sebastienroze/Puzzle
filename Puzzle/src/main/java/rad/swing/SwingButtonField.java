package rad.swing;

import rad.RadButtonField;

public class SwingButtonField extends SwingButton implements RadButtonField {
	private String value;
	
	public SwingButtonField(String text) {
		super(text);
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
