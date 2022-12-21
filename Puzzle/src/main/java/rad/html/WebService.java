package rad.html;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class WebService {
	private HashMap<String, HtmlComponent> components;
	private StringBuilder alerts = null;
	private String apiUrl;
	private String name;

	public WebService(String apiUrl, String name) {
		super();
		this.components = new HashMap<String, HtmlComponent>();
		this.apiUrl = apiUrl;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addAlert(String alert) {
		if (this.alerts == null) {
			this.alerts = new StringBuilder();
		} else {
			this.alerts.append(",");
		}
		this.alerts.append(HtmlComponent.JSONString(alert));
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public void addComponent(HtmlComponent component) {
		if (component.getWebService() == null) {
			component.setName(name + "_" + component.getName());
			components.put(component.getName(), component);
			component.setWebService(this);
		} 
	}

	public void setDefault() {
		for (Map.Entry<String, HtmlComponent> entry : components.entrySet()) {
			entry.getValue().setDefault();
		}
	}

	public void setValues(Map<String, String[]> values) {
		HtmlComponent eventCpn = null;
		String eventName = null;
		for (Map.Entry<String, String[]> entry : values.entrySet()) {
			if (entry.getKey().equals("RADEvent")) {
				eventCpn = components.get(entry.getValue()[0]);
			} else if (entry.getKey().equals("RADEventName")) {
				eventName = entry.getValue()[0];
			} else {
				HtmlComponent cpn = components.get(entry.getKey());
				if (cpn != null) {
					cpn.setValue(entry.getValue()[0]);
				}
			}
		}
		setDefault();
		if (eventCpn != null) {
			eventCpn.triggerEvent(eventName);
		}
	}

	public void writeResponses(PrintWriter out) throws IOException {
		writeResponseProperties(out);
		out.print(",");
		writeResponseDataValues(out);
		out.print(",");
		writeResponseAlerts(out);
		out.print(",");
		writeResponseFunctions(out);
	}

	public void writeResponseFunctions(PrintWriter out) {
		boolean first = true;
		out.print("\"functions\":[");
		for (Map.Entry<String, HtmlComponent> entry : components.entrySet()) {
			HtmlComponent cpn = entry.getValue();
			String cpnResp = cpn.getResponseFunctions();
			if (cpnResp != null) {
				if (!first) {
					out.print(",");
				}
				first = false;
				out.print(cpnResp);
			}
		}
		out.println("]");
	}

	public void writeResponseProperties(PrintWriter out) {
		boolean first = true;
		out.print("\"properties\":[");
		for (Map.Entry<String, HtmlComponent> entry : components.entrySet()) {
			HtmlComponent cpn = entry.getValue();			
			String cpnResp = cpn.getResponseProperties();
			if (cpnResp != null) {
				if (!first) {
					out.print(",");
				}
				first = false;
				out.print(cpnResp);
			}
		}
		out.println("]");
	}

	public void writeResponseDataValues(PrintWriter out) {
		boolean first = true;
		out.print("\"datavalues\":[");
		for (Map.Entry<String, HtmlComponent> entry : components.entrySet()) {
			HtmlComponent cpn = entry.getValue();
			String cpnResp = cpn.getResponseDataValues();
			if (cpnResp != null) {
				if (!first) {
					out.print(",");
				}
				first = false;
				out.print(cpnResp);
			}
		}
		out.println("]");
	}

	public void writeResponseAlerts(PrintWriter out) {
		out.print("\"alerts\":[");
		if (alerts != null) {
			out.print(alerts);
		}
		out.println("]");
	}
	/*
	 * public void outFields() { System.out.println(getName() + ": contenu ");
	 * 
	 * for (Map.Entry<String, HtmlComponent> entry : components.entrySet()) {
	 * HtmlComponent cpn = entry.getValue(); System.out.println(cpn.getName() +
	 * ":"+cpn.getValue()); } System.out.println(getName() + "------");
	 * 
	 * }
	 */

}
