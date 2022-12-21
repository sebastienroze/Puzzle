package puzzle;

import rad.html.HtmlButton;
import rad.html.HtmlColorPicker;
import rad.html.HtmlContainer;
import rad.html.HtmlCheckBox;

public class FichePreferences {
	protected FicheHtml fiche;
	protected String loginName ;
	protected Preferences preferences;
	
	public FichePreferences(FicheHtml fiche,String loginName) {
		this.loginName = loginName;
		this.fiche = fiche;
		fiche.webService.sendPuzzleInfo=false;
		createContent();
	}
	
	private void createContent() {
		HtmlButton btClose = new HtmlButton("retour", "Retour");
		btClose.setClassName("btRetour");
		HtmlContainer container= new HtmlContainer("DivMenu");
		fiche.ajoutComposantSaisie(btClose);
		fiche.ajoutTitre("Préférences");		
		btClose.addActionListener(e->{
			this.fiche.close();
		});
		
		String saveFilename =  "pref."+loginName+".bin";
		preferences = Preferences.deserizalizeFromFile(ApplicationParams.getSavePath() +saveFilename);		
		if (preferences==null) {
			preferences = new Preferences();
		}
		HtmlColorPicker btColor = new HtmlColorPicker("color");
		btColor.setValue(preferences.color);
		fiche.ajoutComposantWebService(btColor);
		container.addLabelComponent("Couleur de fond de jeu",btColor);		
		HtmlCheckBox cbFullScreen = new HtmlCheckBox("fullscreen");
		cbFullScreen.setBooleanValue(preferences.fullScreen);
		fiche.ajoutComposantWebService(cbFullScreen);
		container.addLabelComponent("Mode plein écran",cbFullScreen);		
		HtmlButton btSave = new HtmlButton("save", "Sauvegarder");
		fiche.ajoutComposantWebService(btSave);
		container.addComponent(btSave);
		btSave.addActionListener(e->{
			preferences.color = btColor.getValue();
			preferences.fullScreen = cbFullScreen.getBooleanValue();
			preferences.serizalizeToFile(ApplicationParams.getSavePath() +saveFilename);
			this.fiche.close();
		});
		fiche.ajoutComposant(container);		
	}

}
