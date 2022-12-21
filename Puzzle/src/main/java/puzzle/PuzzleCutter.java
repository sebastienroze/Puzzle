package puzzle;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class PuzzleCutter {
	private BufferedImage image = null;
	private BufferedImage mask = null;
	private BufferedImage workImage;
	private PuzzleInfo puzzleInfo;
	private LinkedList<PieceToSaw> piecesToSaw;
	private String destinationPath;
	private Graphics2D workGraphics;
	private ArrayList<BufferedImage> pieces;

	public PuzzleCutter(String puzzleFile, String maskFile, String destinationPath) {
		
		this.destinationPath = destinationPath;
		pieces = new ArrayList<BufferedImage>();
		try {
			image = ImageIO.read(new File(ApplicationParams.getImagesPath()+puzzleFile));
			mask = ImageIO.read(new File(ApplicationParams.getMasksPath()+maskFile));
		} catch (IOException e) {
			System.out.println("Image ou masque non trouvé");
			System.out.println(ApplicationParams.getImagesPath()+puzzleFile);
			System.out.println(ApplicationParams.getMasksPath()+maskFile);
		}

		if ((image != null) && (mask != null)) {
			puzzleInfo = new PuzzleInfo();
			puzzleInfo.imageWidth = image.getWidth();
			puzzleInfo.imageHeight = image.getHeight();
			workImage = new BufferedImage(puzzleInfo.imageWidth, puzzleInfo.imageHeight, BufferedImage.TYPE_INT_ARGB);
			workGraphics = workImage.createGraphics();
			piecesToSaw = new LinkedList<PieceToSaw>();
			puzzleInfo.piecesInfo = new LinkedList<PieceInfo>();
			piecesToSaw.add(new PieceToSaw(0, 0, mask.getRGB(0, 0)));
			cutPieces();
			if (destinationPath != null) {
				serializePiecesInfo();
			}
		}
		saveMask();		
	}

	private void serializePiecesInfo() {
		puzzleInfo.serizalizeToFile(destinationPath + "info.bin");
	}

	public void exportPiecesInfo() {
		try {
			PrintWriter writer = new PrintWriter(destinationPath + "info.txt");
			writer.print(puzzleInfo.getJson());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void cutPieces() {
		while (piecesToSaw.size() > 0) {
			PieceToSaw pieceToSaw = piecesToSaw.pollLast();
			cutPiece(pieceToSaw);
//			System.out.println("cutting "+puzzleInfo.piecesInfo.size() );
		}
	}

	private void cutPiece(PieceToSaw pieceToSaw) {
		int rgb = pieceToSaw.getRgb();
		int xMin = pieceToSaw.getX();
		int yMin = pieceToSaw.getY();
		int pixel = mask.getRGB(xMin, yMin);
		ArrayList<Integer> neighbors = pieceToSaw.getNeighbors();
		if (pixel >= 0) {
			for (int neighbor : neighbors) {
				puzzleInfo.piecesInfo.get(neighbor).getNeighbors().add(pixel);
				puzzleInfo.piecesInfo.get(pixel).getNeighbors().add(neighbor);
			}
			return;
		}
		LinkedList<Integer> pixelsToSaw = new LinkedList<Integer>();
		pixelsToSaw.add(xMin);
		pixelsToSaw.add(yMin);
		int xMax = xMin;
		int yMax = yMin;
		workGraphics.setComposite(AlphaComposite.Clear);
		workGraphics.fillRect(0, 0, puzzleInfo.imageWidth, puzzleInfo.imageHeight);
		while (pixelsToSaw.size() > 0) {
			int x = pixelsToSaw.removeFirst();
			int y = pixelsToSaw.removeFirst();
			if ((x >= 0) && (y >= 0) && (x < puzzleInfo.imageWidth) && (y < puzzleInfo.imageHeight)) {
				pixel = mask.getRGB(x, y);
				if (pixel == rgb) {
					mask.setRGB(x, y, puzzleInfo.piecesInfo.size());
					workImage.setRGB(x, y, image.getRGB(x, y));
					if (x < xMin) {
						xMin = x;
					}
					if (x > xMax) {
						xMax = x;
					}
					if (y < yMin) {
						yMin = y;
					}
					if (y > yMax) {
						yMax = y;
					}
					pixelsToSaw.addLast(x + 1);
					pixelsToSaw.addLast(y);
					pixelsToSaw.addLast(x - 1);
					pixelsToSaw.addLast(y);
					pixelsToSaw.addLast(x);
					pixelsToSaw.addLast(y - 1);
					pixelsToSaw.addLast(x);
					pixelsToSaw.addLast(y + 1);
				} else {
					addPieceToSaw(x, y, pixel);
				}
			}
		}
		BufferedImage piece = workImage.getSubimage(xMin, yMin, xMax - xMin + 1, yMax - yMin + 1);
		savePiece(piece);
		PieceInfo pieceInfo = new PieceInfo(xMin, yMin);
		for (int neighbor : neighbors) {
			puzzleInfo.piecesInfo.get(neighbor).getNeighbors().add(puzzleInfo.piecesInfo.size());
			pieceInfo.getNeighbors().add(neighbor);
		}
		puzzleInfo.piecesInfo.add(pieceInfo);
	}

	private void savePiece(BufferedImage piece) {
		if (destinationPath == null) {
			BufferedImage image = new BufferedImage(piece.getWidth(), piece.getHeight(), BufferedImage.TYPE_INT_ARGB);
			image.createGraphics().drawImage(piece, 0, 0, null);
			pieces.add(image);
		} else {
			try {
								File outputfile = new File(destinationPath + puzzleInfo.piecesInfo.size() + ".png");
				ImageIO.write(piece, "png", outputfile);
			} catch (IOException e) {
			}
		}
	}

	private void addPieceToSaw(int x, int y, int pixel) {
		if (pixel >= 0) {
			return;
		}
		for (PieceToSaw pieceToSaw : piecesToSaw) {
			if ((pieceToSaw.getRgb() == pixel) && (pieceToSaw.getNeighbors().contains(puzzleInfo.piecesInfo.size()))) {
				return;
			}
		}
		PieceToSaw pieceToSaw = new PieceToSaw(x, y, pixel);
		pieceToSaw.getNeighbors().add(puzzleInfo.piecesInfo.size());
		piecesToSaw.add(pieceToSaw);
	}

	public void saveMask() {
		if (destinationPath == null) return;		
		try {
			File outputfile = new File(destinationPath + "mask.png");
			ImageIO.write(mask, "png", outputfile);
		} catch (IOException e) {
		}
	}

	public PuzzleInfo getPuzzleInfo() {
		return puzzleInfo;
	}

	public void setPuzzleInfo(PuzzleInfo puzzleInfo) {
		this.puzzleInfo = puzzleInfo;
	}

	public ArrayList<BufferedImage> getPieces() {
		return pieces;
	}

	public void setPieces(ArrayList<BufferedImage> pieces) {
		this.pieces = pieces;
	}

}
