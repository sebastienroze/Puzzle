package puzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import rad.html.HtmlButton;

public class FicheValidPath {
	private FicheHtml fiche;
	private boolean error = false;

	public FicheValidPath(FicheHtml fiche) {
		this.fiche = fiche;
		createContent();
	}

	private void createContent() {
		HtmlButton btClose = new HtmlButton("close", "Retour");
		btClose.setClassName("btRetour");
		btClose.addActionListener(e -> {
			ApplicationParams.setSavePath(null);
			this.fiche.close();
		});
		fiche.ajoutComposantSaisie(btClose);

		fiche.ajoutTitre("Vérification du chemin :");
		fiche.ajoutTexte("Images:<br>");
		ajouteListe(ApplicationParams.getImagesPath());
		fiche.ajoutTexte("<br>Masques:<br>");
		ajouteListe(ApplicationParams.getMasksPath());
		if (!error) {
			HtmlButton btSave = new HtmlButton("save", "Sauvegarder la configuration");
			btSave.addActionListener(e -> {
				saveDataPath();
				this.fiche.close();
			});
			fiche.ajoutComposantSaisie(btSave);
		}

	}

	private void saveDataPath() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(ApplicationParams.configurationFilename, "UTF-8");
			writer.println(ApplicationParams.getSavePath());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void ajouteListe(String listPath) {
		File folder = new File(listPath);
		File[] listFiles = folder.listFiles();
		if (listFiles == null) {
			fiche.ajoutTexte(listPath + " n'existe pas!");
			error = true;
			return;
		}
		for (final File fileEntry : listFiles) {
			if (!fileEntry.isDirectory()) {
				String fileName = fileEntry.getName();
				fiche.ajoutTexte(fileName + "<br>");
			}
		}
	}
}
