package puzzle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

public class PuzzleInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	protected LinkedList<PieceInfo> piecesInfo;
	protected LinkedList<Integer> piecesFusionnees = null;
	protected int imageWidth;
	protected int imageHeight;
	protected int nbPieces;
	protected String imageName;
	protected String maskName;
	protected String antiCache;

	String getJson() {
		StringBuilder json = new StringBuilder();

		if (nbPieces > 0) {
			json.append(",\"nbPieces\":" + nbPieces);
			json.append(",\"antiCache\":\"" + antiCache+"\"");
			json.append(",\"imageWidth\":" + imageWidth);
			json.append(",\"imageHeight\":" + imageHeight);
			json.append(",\"infoPieces\":");
			json.append("[");
			boolean bfirst = true;
			for (PieceInfo pieceInfo : piecesInfo) {
				if (!bfirst) {
					json.append(",");
				}
				bfirst = false;
				json.append(pieceInfo.getJson());
			}
			json.append("]");			
			if (piecesFusionnees != null) {
				json.append(",\"piecesFusionnees\":[" );
				bfirst = true;
				for (Integer piecesFusionnee : piecesFusionnees) {
					if (!bfirst) {
						json.append(",");
					}
					bfirst = false;
					json.append(piecesFusionnee);
				}
				json.append("]");				
			}
		}
		return json.toString();
	}

	public static PuzzleInfo deserizalizeFromFile(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			PuzzleInfo puzzleInfo = (PuzzleInfo) ois.readObject();
			ois.close();
			fis.close();
			return puzzleInfo;
		} catch (IOException ioe) {
//			ioe.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void serizalizeToFile(String filename) {
		try {
			if (nbPieces == 0) {
				nbPieces = piecesInfo.size();
			}
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
