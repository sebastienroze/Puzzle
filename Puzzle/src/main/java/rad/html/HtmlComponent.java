package rad.html;

import java.util.regex.Matcher;

import rad.RadComponent;

public abstract class HtmlComponent implements RadComponent {
	protected String name;
	private String className =null;
	protected boolean visible = true;
	protected boolean readOnly = false;
	protected boolean defaultValue = false;
	protected boolean defaultClassName = false;
	protected boolean defaultReadOnly = false;
	protected boolean useDataValue = false;
	protected boolean useValue = false;
	protected String radInit;

	private WebService webService = null;

	public HtmlComponent(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {		
		return className;
	}

	public void setClassName(String className) {
		defaultClassName = false;
		this.className = className;		
	}

	public String getRadInit() {
		return radInit;
	}

	public void setRadInit(String radInit) {
		this.radInit = radInit;
	}

	public WebService getWebService() {
		return webService;
	}

	public void setWebService(WebService webService) {
		this.webService = webService;
	}

	public void setValue(String value) {
		defaultValue = false;
	}

	public String getValue() {
		return null;
	}

	public void setDefault() {
		defaultValue = true;
		defaultReadOnly = true;
		defaultClassName = true;
	}

	public void setReadOnly(boolean readonly) {
		defaultReadOnly = false;
		this.readOnly = readonly;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setVisible(boolean visible) {
		defaultClassName = false;
		this.visible = visible;
	}

	/*
	 * public String getRequestField() { if (getWebService() != null) return
	 * getWebService().getRequestField(); return "[],[]"; }
	 */
	public String getRADEvent(String eventName) {
		if (webService == null)
			return "";
		return "RADEvent('" + getName() + "','" + getWebService().getApiUrl() + "','" + webService.getName() + "','"
				+ eventName + "')";
	}

	public String getResponseFunctions() {
		return null;
	}

	public String getResponseProperties() {
		String responseProperties = null;
		if (!defaultValue && useValue)
			responseProperties= "[\"id" + getName() + "\",\"value\"," + JSONString(getValue()) + "]";

		if (!defaultClassName) {
			String classeNameIncludingVisible = getClasseNameIncludingVisible(); 
			if (classeNameIncludingVisible==null) {
				classeNameIncludingVisible = "\"\"";		
			}
			String responseClassName = "[\"id" + getName() + "\",\"className\"," + classeNameIncludingVisible+ "]";		
			if (responseProperties==null) return responseClassName;
			return responseClassName+","+responseClassName;			
		}
		return responseProperties;
	}

	public String getResponseDataValues() {
		if (defaultValue || !useDataValue)
			return null;
		return "[\"id" + getName() + "\"," + JSONString(getValue()) + "]";
	}

	public void triggerEvent(String eventName) {
	}

	public abstract String getHtml();

	static public String HTMLString(String value) {
		if (value == null) {
			return "";
		}
		return value.replaceAll("<", "&lt;");
	}

	static public String JSONString(String value) {
		if (value == null) {
			return "\"\"";
		}
		return "\"" + value.replaceAll("\"", Matcher.quoteReplacement("\\\"")).replaceAll("\n",
				Matcher.quoteReplacement("\\n")) + "\"";
	}

	protected String HtmlReadOnly() {
		if (readOnly) {
			return "readonly ";
		}
		return "";
	}

	protected String htmlInit() {
		if (radInit != null) {
			return "RADinit=\"" + radInit + "\" ";
		}
		return "";
	}

	protected String htmlExtra() {
		return htmlInit() + htmlClassName() + htmlServiceValue() + htmlServiceDataValue();
	}

	protected String htmlClassName() {
		String htmlClass = getClasseNameIncludingVisible();
		if (htmlClass == null) return ""; 
		return "class = " + htmlClass + " ";
	}

	private String getClasseNameIncludingVisible() {
		if (visible) {
			if (className==null) return null;
		    return "\""+ className +"\"";		
		}
		if (className==null) return "\"hidden\"";
	    return "\"hidden "+ className +"\"";		
	}
	
	protected String htmlServiceValue() {
		if (useValue && getWebService() != null)
			return "RADWebServiceValue" + getWebService().getName() + " ";
		return "";
	}

	protected String htmlServiceDataValue() {
		if (useDataValue && getWebService() != null)
			return "RADWebServiceDataValue" + getWebService().getName() + " ";
		return "";
	}

}
