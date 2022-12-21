package puzzle;

import java.util.ArrayList;
import java.util.LinkedList;

public class RunPuzzleCutter {

	public static void main(String[] args) {
//		test();
		cutPuzzle();
//		testSerialisation();
//		testDeserialisation();
		System.out.println("terminé");
		
	}

	public static void test() {
		String str = "1,2,3";
		str = str.replace(","," ");
		System.out.println(str);
		
	}

	public static void cutPuzzle() {

		new PuzzleCutter("image1.png",
				"puzzle576.png",
				"D:\\java\\eclipse-workspace\\Puzzle\\data\\temp\\");
		
	}

	public static void testSerialisation() {
		PuzzleInfo puzzleInfo = new PuzzleInfo();
		puzzleInfo.nbPieces = 2;
		puzzleInfo.imageWidth = 100;
		puzzleInfo.imageHeight = 200;
		puzzleInfo.piecesInfo = new LinkedList<PieceInfo>();
		PieceInfo pieceInfo = new PieceInfo(1, 2, 3, 4, 5);
		ArrayList<Integer> neighbors = new ArrayList<Integer>();
		neighbors.add(10);
		neighbors.add(11);
		pieceInfo.setNeighbors(neighbors);
		puzzleInfo.piecesInfo.add(pieceInfo);
		puzzleInfo.serizalizeToFile("serial.bin");
	}

	public static void testDeserialisation() {
		PuzzleInfo puzzleInfo = PuzzleInfo
				.deserizalizeFromFile("D:\\java\\eclipse-workspace\\Puzzle\\src\\main\\webapp\\files\\puzzle1\\info.bin");
		System.out.println(puzzleInfo.getJson());
	}

}
