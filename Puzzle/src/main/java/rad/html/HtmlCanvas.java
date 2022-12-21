package rad.html;

import java.io.PrintWriter;

public class HtmlCanvas extends HtmlComponent {
	private int width;
	private int height;
	private StringBuilder drawImage;
	private StringBuilder drawImageSelected;

	public HtmlCanvas(String name, int width, int height) {
		super(name);
		this.width = width;
		this.height = height;
	}

	@Override
	public String getHtml() {
		return "<canvas " + "name=\"" + getName() + "\" " + "id=\"id" + getName() + "\" " + "width=\"" + width + "\" "
				+ "height=\"" + height + "\" " + htmlExtra() + "></canvas>";
	}

	public void writeResponseDrawImage(PrintWriter out) {
		if (drawImage != null) {
			out.print("[");
			out.print("\"id"+getName()+"\",[");			
			out.print(drawImage);
			if (drawImageSelected != null) {
				out.println(drawImageSelected);
				drawImageSelected = null;
			}			
			out.println("]]]]");
			drawImage = null;
		}
	}
	
	@Override
	public String getResponseFunctions() {
		return null;
//		return "[\"iniCanvas\",\"test\"]";
	}

	public void selectDrawImage(HtmlImage image) {
		if (drawImage == null) {
			drawImage = new StringBuilder();
		} else {
			if (drawImageSelected != null) {
				drawImage.append(drawImageSelected);
				drawImageSelected = null;
			}
			drawImage.append("]],");
		}
		drawImage.append("[\"id");
		drawImage.append(image.getName());
		drawImage.append("\",[");

	}

	public void drawImage(int imageX, int imageY, int imageWidth, int imageHeight, int DestinationX,
			int DestinationY, int DestinationWidth, int DestinationHeight) {
		if (drawImageSelected == null) {
			drawImageSelected = new StringBuilder();
		} else {
			drawImageSelected.append(",");
		}
		drawImageSelected.append("[");
		drawImageSelected.append(imageX);
		drawImageSelected.append(",");
		drawImageSelected.append(imageY);
		drawImageSelected.append(",");
		drawImageSelected.append(imageWidth);
		drawImageSelected.append(",");
		drawImageSelected.append(imageHeight);
		drawImageSelected.append(",");
		drawImageSelected.append(DestinationX);
		drawImageSelected.append(",");
		drawImageSelected.append(DestinationY);
		drawImageSelected.append(",");
		drawImageSelected.append(DestinationWidth);
		drawImageSelected.append(",");
		drawImageSelected.append(DestinationHeight);
		drawImageSelected.append("]");
	}

}
