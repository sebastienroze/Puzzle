package puzzle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import rad.RadComponent;

public class ImageInfo {
	public int width;
	public int height;
	public RadComponent image;
	public String filename;
	public int numero;
	public ImageInfo(String path,String fileName, RadComponent image,int numero) {
		super();
		try {
			this.filename = fileName;		
			this.numero = numero;
			BufferedImage img = ImageIO.read(new File(path+fileName));
			width = img.getWidth();
			height = img.getHeight();
		} catch (IOException e) {
			System.out.println("ImageInfo : Image inconnue : "+path+fileName);
		}
		this.image = image;
	}
	
}
