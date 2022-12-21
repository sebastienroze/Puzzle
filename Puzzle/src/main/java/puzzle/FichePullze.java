package puzzle;

import rad.html.HtmlButton;
import rad.html.HtmlContainer;
import rad.html.HtmlStaticText;

public class FichePullze {
	protected FicheHtml fiche;
	private String saveFilename;
	public FichePullze(FicheHtml fiche, String saveFilename,String prefFilename) {
		this.fiche = fiche;
		fiche.webService.sendPuzzleInfo=true;
		this.saveFilename = saveFilename;
		createContent();
		fiche.webService.preferences = Preferences.deserizalizeFromFile(ApplicationParams.getSavePath() +prefFilename);		
		if (fiche.webService.preferences==null) {
			fiche.webService.preferences = new Preferences();
		}
	}

	private void createContent() {
		HtmlButton btClose = new HtmlButton("close", "Retour");
		btClose.setClassName("btRetour");		
		fiche.ajoutComposantSaisie(btClose);
		btClose.addActionListener(e->{
			this.saveInfo();
			this.fiche.close();
		});
		HtmlContainer divDisplay = new HtmlContainer("display");
		
		HtmlStaticText texteLabelNbPiece = new HtmlStaticText("texteLabelNbPiece");
		texteLabelNbPiece.setValue("Nombre de pièces :");		
		HtmlStaticText texteNbPiece = new HtmlStaticText("texteNbPiece");
		texteNbPiece.setValue("(Lecture en cours)");
		divDisplay.addComponent(texteLabelNbPiece);
		divDisplay.addComponent(texteNbPiece);
		fiche.ajoutComposant(divDisplay);
		HtmlButton btLoad = new HtmlButton("load", "Lire");
		btLoad.setRadInit("initialisation");
		btLoad.setClassName("hidden");
		fiche.ajoutComposantSaisie(btLoad);
		fiche.ajoutTexte("<svg id=\"idrotation\" style=\"display:none;position:absolute;left:0;top:0\" height=\"100\" width=\"100\">\r\n"
				+ "  <circle cx=\"50\" cy=\"50\" r=\"40\" stroke=\"black\" stroke-width=\"20\" fill=\"none\" />\r\n"
				+ "  <circle cx=\"50\" cy=\"50\" r=\"40\" stroke=\"red\" stroke-width=\"10\" fill=\"none\" />");
	}
	
	public void saveInfo() {
		fiche.webService.puzzleInfo.serizalizeToFile(ApplicationParams.getSavePath() +saveFilename);
	}

	public String getSaveFilename() {
		return saveFilename;
	}

	public void setSaveFilename(String saveFilename) {
		this.saveFilename = saveFilename;
	}
}
