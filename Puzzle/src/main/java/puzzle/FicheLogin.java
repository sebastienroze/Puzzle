package puzzle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Scanner;

import rad.html.HtmlButton;
import rad.html.HtmlContainer;
import rad.html.HtmlTextField;

public class FicheLogin {

	private FicheHtml fiche;
	private String validLogin;

	public FicheLogin(FicheHtml fiche) {
		this.fiche = fiche;
		loadSavePath();
		if (ApplicationParams.getSavePath() == null)
			createPathSetup();
		else
			createContent();
	}

	private void createPathSetup() {
		fiche.ajoutTitre("Puzzle : configuration");
		HtmlTextField saisieChemin = new HtmlTextField("txtPath");
		saisieChemin.setVisualSize(50);
		fiche.ajoutSaisie("Entrez le chemin des donnes (ex :C:\\puzzle_data ou /home/puzzle_data)<br>", saisieChemin);
		fiche.ajoutTexte("<br>");
		fiche.ajoutComposantSaisie(fiche.createBouton("Validate", "Valider").addActionListener(e -> {
			ApplicationParams.setSavePath(saisieChemin.getValue());
			FicheHtml ficheValidPath = fiche.createFiche("ValidPath");
			ficheValidPath.setOnClose(evp->{
				if (ApplicationParams.getSavePath()!=null) {
					FicheHtml ficheLogin = fiche.createFiche("Login");
					new FicheLogin(ficheLogin);
					ficheValidPath.setFichePrecedente(ficheLogin);
				}
			});
			new FicheValidPath(ficheValidPath);
		}));
	}

	private void loadSavePath() {		
		ApplicationParams.setSavePath(System.getenv(ApplicationParams.systemPuzzleHome));
		System.out.println(ApplicationParams.getSavePath());
		if (ApplicationParams.getSavePath() != null) return;
		File myObj = new File(ApplicationParams.configurationFilename);
		try {
			Scanner myReader = new Scanner(myObj);
			if (myReader.hasNextLine()) {
				ApplicationParams.setSavePath(myReader.nextLine());
			}
			myReader.close();
		} catch (FileNotFoundException e) {
		}
	}

	private void createContent() {
		HtmlTextField saisieUtilisateur = new HtmlTextField("txtLogin");
		HtmlTextField saisieMotDePasse = new HtmlTextField("txtMdp");
		HtmlButton btLogin = fiche.createBouton("Login", "Login");
		HtmlContainer container = new HtmlContainer("DivLogin");
		container.addLabelComponent("Utilisateur", saisieUtilisateur);
		container.addLabelComponent("Mot de passe", saisieMotDePasse);
		fiche.ajoutTitre("Puzzle");
		fiche.ajoutComposantWebService(saisieUtilisateur);
		fiche.ajoutComposantWebService(saisieMotDePasse);
		btLogin.addActionListener(e -> {
			FicheLogin.this.validLogin = saisieUtilisateur.getValue();
			if (FicheLogin.this.validadeLogin(saisieUtilisateur.getValue(), saisieMotDePasse.getValue())) {
				FicheHtml ficheMenu = fiche.createFiche("Menu");
				new FicheMenu(ficheMenu, FicheLogin.this.validLogin);
			} else {
				fiche.alerte("Mauvais mot de passe!");
			}

		});
		fiche.ajoutComposantWebService(btLogin);
		container.addHtml("<br>");
		container.addComponent(btLogin);
		fiche.ajoutComposant(container);
	}

	@SuppressWarnings("unchecked")
	private boolean validadeLogin(String login, String mdp) {
		HashMap<String, String> logins = null;
		String filename = ApplicationParams.getSavePath() + "logins.dat";
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object objet = ois.readObject();
			logins = (HashMap<String, String>) objet;
			ois.close();
			fis.close();
		} catch (IOException ioe) {
			logins = new HashMap<String, String>();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logins = new HashMap<String, String>();
		}
		validLogin = toValidLogin(login);
		String mdpAttendu = logins.get(validLogin);
		if (mdp.equals(mdpAttendu)) {
			return true;
		}
		if (mdpAttendu == null) {
			logins.put(validLogin, mdp);
			try {
				FileOutputStream fos = new FileOutputStream(filename);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(logins);
				oos.close();
				fos.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			return true;
		}
		return false;
	}

	private String toValidLogin(String login) {
		StringBuilder validLogin = new StringBuilder();
		login = login.toLowerCase();
		for (int i = 0; i < login.length(); i++) {
			char c = login.charAt(i);
			if ((c >= 'a') && (c <= 'z')) {
				validLogin.append(c);
			}
		}
		return validLogin.toString();
	}

}
