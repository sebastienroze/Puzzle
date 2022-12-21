package puzzle;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ApplicationParams {
	static private String dataPath = null;
	static public final String configurationFilename = "puzzle.path.txt";
	static public final String systemPuzzleHome = "PUZZLE_HOME";
	public static String getImagesPath() {
		return dataPath + "images/";
	}
	public static String getMasksPath() {
		return dataPath + "masks/";
	}

	public static String getSavePath() {
		return dataPath ;
	}

	public static void setSavePath(String dataPath) {
		if (dataPath==null) ApplicationParams.dataPath = null; 
		else ApplicationParams.dataPath= dataPath+"/";
	}
	
	public static BufferedImage loadResizedImage(String fileName) throws IOException {
		BufferedImage img = ImageIO.read(new File(fileName));
		int newW = 400;				
	    int w = img.getWidth();  
	    int h = img.getHeight();
	    int newH = (h*newW)/w;
	    BufferedImage dimg = new BufferedImage(newW, newH, img.getType());  
	    Graphics2D g = dimg.createGraphics();  
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	    RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
	    g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
	    g.dispose();  
	    return dimg;
	}
	
	public static String antiCachePiece(int image, int mask) {
		return  (""+(image*100+mask+10000)).substring(1);		
	}
	public static int removeAntiCachePiece(String pathInfo) {
		return  Integer.parseInt(pathInfo.substring(5));			
	}
	
}
