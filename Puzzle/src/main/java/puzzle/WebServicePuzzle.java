package puzzle;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import rad.html.WebService;

public class WebServicePuzzle extends WebService {
	protected PuzzleInfo puzzleInfo = null;
	protected Preferences preferences=null;
	boolean sendPuzzleInfo = false;

	public WebServicePuzzle(String apiUrl, String name) {
		super(apiUrl, name);
	}

	@Override
	public void writeResponses(PrintWriter out) throws IOException {
		super.writeResponses(out);
		if (sendPuzzleInfo) {
			out.print(",\"color\":\"" + preferences.color+"\"");
			out.print(",\"fullScreen\":" + preferences.fullScreen);
			out.print(puzzleInfo.getJson());			
		}
	}

	@Override
	public void setValues(Map<String, String[]> values) {
		super.setValues(values);
		String[] requestPuzzleInfoStr = values.get("requestPuzzleInfo");
		if (requestPuzzleInfoStr != null) {
			sendPuzzleInfo = (Integer.parseInt(requestPuzzleInfoStr[0])>0) ;
		}		
		String[] pimNumeroStr = values.get("pimNumero");
		if (pimNumeroStr != null) {
			int pimNumero = Integer.parseInt(pimNumeroStr[0]);
			int fusionA = Integer.parseInt(values.get("pieceFusionA")[0]);
			int fusionB = Integer.parseInt(values.get("pieceFusionB")[0]);
			if (fusionA >= 0) {
				puzzleInfo.piecesInfo.add(new PieceInfo(0, 0));
				puzzleInfo.piecesFusionnees.add(fusionA);
				puzzleInfo.piecesFusionnees.add(fusionB);
			}
			puzzleInfo.piecesInfo.get(pimNumero).setX(Integer.parseInt(values.get("pimX")[0]));
			puzzleInfo.piecesInfo.get(pimNumero).setY(Integer.parseInt(values.get("pimY")[0]));
			puzzleInfo.piecesInfo.get(pimNumero).setRotation(Integer.parseInt(values.get("pimRotation")[0]));
		} 
	}
}
