package rad.html;

import java.util.Map;
import java.util.StringTokenizer;

public class HtmlKeyboard extends HtmlComponent {
	private StringTokenizer keyBuffer;
	public HtmlKeyboard(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getHtml() {
		return null;
	}
	@Override
	public void setValue(String value) {
		super.setValue(value);
		this.keyBuffer =  new StringTokenizer(value);
	}

	public boolean hasNext() {
		if (keyBuffer==null) return false;
		return keyBuffer.hasMoreTokens();
	}
	
	public int nextKey() {		
		return Integer.parseInt(keyBuffer.nextToken());
	}

	public void setValues(Map<String, String[]> values) {
		setValue(values.get("RADKeyValues")[0]);		
	}
	
}
