package rad.html;

import java.util.LinkedList;

import rad.RadComponent;
import rad.RadContainer;

public class HtmlContainer extends HtmlComponent implements RadContainer  {
	private LinkedList<Object> content;

	
	public HtmlContainer(String name) {
		super(name);
		content = new LinkedList<Object>();
	}

	@Override
	public void addComponent(RadComponent cpn) {	
		content.add(cpn);
	}
	
	@Override
	public void addLabelComponent(String label, RadComponent radComponent) {
		HtmlComponent comp = (HtmlComponent) radComponent;
		content.add("<div>");
		content.add("<label for =\"id" + comp.getName() + "\">" + label + "</label>");
		content.add(comp);		
		content.add("</div>");
	}	
	
	
	public void addHtml(String html) {
		content.add(html);
	}

	private String getContent() {
		StringBuilder html = new StringBuilder();
		for (Object object : content) {
			if (object instanceof String) {
				html.append((String)object);				
			}	
			if (object instanceof HtmlComponent) {
				html.append(((HtmlComponent)object).getHtml());
			}
		}		
		return html.toString();		
	}

	@Override
	public String getHtml() {
		return 
				"<div "
				+ "name=\""+getName()+"\" "
				+ "id=\"id"+getName()+"\" "
				+ htmlExtra()
				+ ">"
				+getContent()
				+"</div>";		
	}

}
