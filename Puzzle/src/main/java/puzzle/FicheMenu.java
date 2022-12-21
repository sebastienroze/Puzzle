package puzzle;

import rad.html.HtmlButton;
import rad.html.HtmlContainer;
public class FicheMenu {
	protected String loginName ;
	protected String puzzleColor ;	
	protected FicheHtml fiche;
	
	public FicheMenu(FicheHtml fiche,String loginName) {
		this.loginName = loginName;		
		this.fiche = fiche;
		fiche.webService.sendPuzzleInfo=false;
		createContent();
	}
	
	private void createContent() {
		HtmlButton btClose = new HtmlButton("close", "Déconnecter");
		btClose.setClassName("btRetour");
		HtmlContainer container= new HtmlContainer("DivMenu");
		fiche.ajoutComposantSaisie(btClose);
		fiche.ajoutTitre("Puzzle !");		
		btClose.addActionListener(e->{
			this.fiche.close();
		});
		HtmlButton btLoad = new HtmlButton("load", "Charger une sauvegarde");
		fiche.ajoutComposantWebService(btLoad);
		container.addComponent(btLoad);
		
		String saveFilename =  "save."+loginName+".bin";
		String prefFilename =  "pref."+loginName+".bin";
		
		btLoad.addActionListener(e->{
			PuzzleInfo puzzleInfo = PuzzleInfo.deserizalizeFromFile(ApplicationParams.getSavePath() +saveFilename);		
			if (puzzleInfo==null) {
				fiche.alerte("Pas de sauvegarde !");
				return;
			}
			PuzzleCutter puzzleCutter = new PuzzleCutter(puzzleInfo.imageName, puzzleInfo.maskName,null);
			FicheHtml fichePuzzle = fiche.createFiche("Puzzle");
			new FichePullze(fichePuzzle,saveFilename,prefFilename);				
			fichePuzzle.webService.puzzleInfo = puzzleInfo;
			fichePuzzle.setPieces(puzzleCutter.getPieces());				
		});
		
		HtmlButton btPuzzle = new HtmlButton("puzzle", "Nouveau puzzle");
		fiche.ajoutComposantWebService(btPuzzle);
		container.addComponent(btPuzzle);
		btPuzzle.addActionListener(e->{
			FicheHtml fiche = this.fiche.createFiche("NouveauPuzzle");
			new FicheNouveauPullze(fiche,saveFilename,prefFilename);			
		});
		
		
		HtmlButton btPref = new HtmlButton("pref", "Préférences");
		fiche.ajoutComposantWebService(btPref);
		container.addComponent(btPref);
		btPref.addActionListener(e->{
			FicheHtml fiche = this.fiche.createFiche("Preferences");
			new FichePreferences(fiche,loginName);			
		});
		
		fiche.ajoutComposant(container);		
	}
}
