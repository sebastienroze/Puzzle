package rad.html;

import rad.ListenerRAD;
import rad.RadButton;

public class HtmlButton extends HtmlComponent implements RadButton {
private String text;
private ListenerRAD actionListener = null;
private String confirmation = null;

	public HtmlButton(String name, String text) {
		super(name);
		this.text = text;
	}
	
	public HtmlButton setConfirmation(String confirmation) {
		this.confirmation = confirmation;
		return this;
	}
	
	public HtmlButton addActionListener(ListenerRAD al) {
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
	public String getHtml(){
		String confirm = "";
		if (confirmation != null) {
			confirm = "if (confirm('"+confirmation +"')) ";
		}
		return
				"<input type=\"button\" "
				+ "name=\""+getName()+"\" "
				+ "id=\"id"+getName()+"\" "
				+ "value=\""+text+"\" "
				+ HtmlReadOnly()+htmlExtra()
		 		+ " onclick=\""+confirm+getRADEvent("click")+"\"" 			
								
				+ ">";
	}
	@Override
	public String getResponseProperties() {		
		String rp = super.getResponseProperties();
		if (!defaultReadOnly)   {
			if (rp==null) rp = ""; else rp = rp+",";
			rp= rp+"[\"id"+getName()+"\",\"disabled\","+readOnly+"]";
		} 
		return rp;
	}

	@Override
	public void performAction() {
		if (this.actionListener!=null) this.actionListener.actionPerformed(this);		
	}	
}
