package rad.html;

public class HtmlDateField extends HtmlTextField {

	public HtmlDateField(String name) {
		super(name);
	}

	@Override
	public String getHtml() {
		String onchange = "";
		if (onChangeListener != null) {
			onchange = " oninput=\""+getRADEvent("change")+"\"" ;			
		}
		return "<input "+"type=\"date\"" + "name = \"" + getName() + "\"" + "id = \"id" + getName() + "\""
		+ "value =\""+HTMLString(getValue()) + "\""
		+getInputSize()+getVisualSize()
				+ HtmlReadOnly() + htmlExtra()+ onchange + ">";
	}
}
