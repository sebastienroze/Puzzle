package rad.html;

import rad.ListenerRAD;

public class HtmlImage extends HtmlComponent {
private String src;
private ListenerRAD actionListener = null;


	public HtmlImage(String name, String src) {
		super(name);
		this.src = src;
	}
	
	@Override
	public String getHtml(){
		
// 		+ " onclick=\""+confirm+getRADEvent("click")+"\"" 			

		return
				"<img "
				+ "name=\""+getName()+"\" "
				+ "id=\"id"+getName()+"\" "
				+ "src=\""+src+"\" "
				+ getOnclick ()
				+ htmlExtra()				
				+ ">";
	}
	private String getOnclick() {
		if (actionListener!=null) {
			return " onclick=\""+getRADEvent("click")+"\"" ;					
		}
		return "";
	}

	public HtmlImage addOnClick(ListenerRAD al) {
			this.actionListener = al;
			return this;
	}
	
	@Override
	public void triggerEvent(String eventName) {
		if (this.actionListener!=null) this.actionListener.actionPerformed(this);		
	}
}
