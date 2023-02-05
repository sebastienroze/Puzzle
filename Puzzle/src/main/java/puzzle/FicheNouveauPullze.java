package puzzle;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import rad.ListenerRAD;
import rad.RadComponent;
import rad.html.HtmlButton;
import rad.html.HtmlComponent;
import rad.html.HtmlContainer;
import rad.html.HtmlImage;
import rad.html.HtmlStaticText;

public class FicheNouveauPullze {
	protected FicheHtml fiche;
	private String saveFilename;
	private String prefFilename;
	private ArrayList<ImageInfo> images;
	private ArrayList<ImageInfo> masks;
	private ArrayList<HtmlContainer> containers;
	private HtmlStaticText titre;
	private ImageInfo selectedImage;
	public static final String classImage = "img-with-text";
	
	public FicheNouveauPullze(FicheHtml fiche, String saveFilename,String prefFilename) {
		this.fiche = fiche;
		this.saveFilename = saveFilename;
		this.prefFilename = prefFilename;
		images = new ArrayList<ImageInfo>();
		masks = new ArrayList<ImageInfo>();
		containers = new ArrayList<HtmlContainer>();		
		createContent();
	}

	private void createContent() {
		HtmlButton btClose = new HtmlButton("close", "Retour");
		btClose.setClassName("btRetour");		
		btClose.addActionListener(e -> {
			this.fiche.close();
		});
		fiche.ajoutComposantSaisie(btClose);
		fiche.ajoutTitre("Nouveau puzzle");
		fiche.ajoutTexte("<br>");
		titre = new HtmlStaticText("titre");
		titre.setValue("Choisir une image");
		fiche.ajoutComposantSaisie(titre);
		fiche.ajoutTexte("<br>");
		
		ajoutImages("puzzle","Images/",ApplicationParams.getImagesPath(),images,true,e -> {
			selectImage(e);
		});
		ajoutImages("mask","Masks/",ApplicationParams.getMasksPath(),masks,false,e -> {
			selectMask(e);
		});
	}

	private void ajoutImages(String name, String url, String imagesPath,ArrayList<ImageInfo> collection,boolean visible,ListenerRAD onClick) {
		File folder = new File(imagesPath);
		int i = 0;
		for (final File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory()) {
				String fileName = fileEntry.getName();
				HtmlImage image = new HtmlImage(name + i++, url + fileName);
				ImageInfo imageInfo = new ImageInfo(imagesPath,fileName, image,i);
				collection.add(imageInfo);
				image.addOnClick(onClick);
				image.setClassName("choixImage");
				image.setVisible(visible);
				if (visible) {
					fiche.ajoutComposantWebService(image);
					HtmlContainer container = new HtmlContainer("div"+name + i++);
					container.addComponent(image);
					container.addHtml("<br>Taille " + imageInfo.width + " x " + imageInfo.height);
					container.setClassName(classImage);
					fiche.ajoutComposantSaisie(container); 
					containers.add(container);					
				} else {
					fiche.ajoutComposantSaisie(image);
				}
			}
		}
	}

	private void selectImage(RadComponent image) {
		int width = 0;
		int height = 0;

		for (ImageInfo imageInfo : images) {
			if (imageInfo.image == image) {
				width = imageInfo.width;
				height = imageInfo.height;
				selectedImage = imageInfo;				
			}
		}
		for (HtmlContainer container : containers) {
			container.setVisible(false);
			container.setClassName(null);
		}		

		for (ImageInfo imageInfo : masks) {
			((HtmlComponent) imageInfo.image).setVisible((width == imageInfo.width) && (height == imageInfo.height));
		}
		titre.setValue("Choisir une découpe");		
	}

	private void selectMask(RadComponent image) {
		ImageInfo selectedMask=null;
		int width = 0;
		int height = 0;		
		for (ImageInfo imageInfo : masks) {
			if (imageInfo.image == image) {
				selectedMask = imageInfo;
				width = imageInfo.width;
				height = imageInfo.height;				
			}
		}		
		PuzzleCutter puzzleCutter = new PuzzleCutter(selectedImage.filename, selectedMask.filename,null);
		FicheHtml fichePuzzle = fiche.createFiche("Puzzle");
		new FichePullze(fichePuzzle,saveFilename,prefFilename);				
		fichePuzzle.webService.puzzleInfo = puzzleCutter.getPuzzleInfo();
		fichePuzzle.webService.puzzleInfo.piecesFusionnees =  new LinkedList<Integer>();
		fichePuzzle.webService.puzzleInfo.nbPieces = puzzleCutter.getPieces().size();
		fichePuzzle.webService.puzzleInfo.imageName = selectedImage.filename;
		fichePuzzle.webService.puzzleInfo.maskName = selectedMask.filename;		
		fichePuzzle.webService.puzzleInfo.imageWidth = width;
		fichePuzzle.webService.puzzleInfo.imageHeight = height;		
		fichePuzzle.webService.puzzleInfo.antiCache = ApplicationParams.antiCachePiece(selectedImage.numero, selectedMask.numero);		
		fichePuzzle.setPieces(puzzleCutter.getPieces());
		fichePuzzle.alerte(puzzleCutter.getWarning());
		fichePuzzle.setFichePrecedente(fiche.getFichePrecedente());
	}

	public void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				System.out.println(fileEntry.getName());
			}
		}
	}
}
