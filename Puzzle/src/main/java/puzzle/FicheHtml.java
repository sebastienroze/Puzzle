package puzzle;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;
import rad.ListenerRAD;
import rad.html.HtmlButton;
import rad.html.HtmlCanvas;
import rad.html.HtmlComponent;
import rad.html.HtmlContainer;
import rad.html.HtmlKeyboard;

public class FicheHtml {
	protected HtmlCanvas canvas;
	protected HtmlCanvas zoneclick;
	protected HtmlContainer spritesContainer;
	private FicheHtml fichePrecedente;
	private FicheHtml ficheSuivante;
	protected WebServicePuzzle webService;
	public HtmlKeyboard htmlKeyboard;
	private LinkedList<Object> content;
	private ArrayList<BufferedImage> pieces;
	private ListenerRAD onClose = null;

	public FicheHtml getFicheSuivante() {
		return ficheSuivante;
	}

	public void setFicheSuivante(FicheHtml ficheSuivante) {
		this.ficheSuivante = ficheSuivante;
	}

	public FicheHtml getFichePrecedente() {
		return fichePrecedente;
	}

	public void setFichePrecedente(FicheHtml fichePrecedente) {
		this.fichePrecedente = fichePrecedente;
	}

	public FicheHtml(String apiURL, String name) {
		webService = new WebServicePuzzle(apiURL, name);
		content = new LinkedList<Object>();
	}

	public boolean storeSession() {
		return (fichePrecedente != null);
	}

	protected FicheHtml createFiche(String name) {
		ficheSuivante = new FicheHtml(webService.getApiUrl(), name);
		ficheSuivante.fichePrecedente = this;
		return ficheSuivante;
	}

	protected void close() {
		if (onClose != null) {
			onClose.actionPerformed(null);
		}
		ficheSuivante = fichePrecedente;
		ficheSuivante.ficheSuivante = null;
	}

	protected HtmlButton createBouton(String nom, String caption) {
		HtmlButton btn = new HtmlButton("bt" + nom, caption);
		webService.addComponent(btn);
		return btn;
	}

	protected void ajoutComposantSaisie(HtmlComponent comp) {
		webService.addComponent(comp);
		content.add(comp);
	}

	protected void ajoutComposant(HtmlComponent comp) {
		content.add(comp);
	}

	protected void ajoutComposantWebService(HtmlComponent comp) {
		webService.addComponent(comp);
	}

	protected void ajoutSaisie(String label, HtmlComponent component) {
		webService.addComponent(component);
		content.add("<label for =\"id" + component.getName() + "\">" + label + "</label>");
		content.add(component);
	}

	protected void alerte(String alert) {
		if (alert==null) return;
		webService.addAlert(alert);
	}

	protected void ajoutTitre(String titre) {
		content.add("<h1>" + titre + "</h1>");
	}

	public void ajoutTexte(String texte) {
		content.add(texte);
	}

	public String getContent() {
		StringBuilder fiche = new StringBuilder();
		for (Object object : content) {
			if (object instanceof String) {
				fiche.append((String) object);
			}
			if (object instanceof HtmlComponent) {
				fiche.append(((HtmlComponent) object).getHtml());
			}
		}
		return fiche.toString();
	}

	public FicheHtml setValuesAndWriteResponse(Map<String, String[]> values, HttpServletResponse response)
			throws IOException {
		webService.setValues(values);
		if (ficheSuivante == null) {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print("{");
			webService.writeResponses(out);
			out.println("}");
		}
		return ficheSuivante;
	}

	public ArrayList<BufferedImage> getPieces() {
		return pieces;
	}

	public void setPieces(ArrayList<BufferedImage> pieces) {
		this.pieces = pieces;
	}

	public void setOnClose(ListenerRAD onClose) {
		this.onClose = onClose;
	}
}
