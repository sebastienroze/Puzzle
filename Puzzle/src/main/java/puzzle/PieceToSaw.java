package puzzle;

import java.util.ArrayList;

public class PieceToSaw {
	private int x;
	private int y;
	private int rgb;
	private ArrayList<Integer> neighbors;
	public PieceToSaw(int x, int y, int rgb) {
		super();
		this.setX(x);
		this.setY(y);
		this.setRgb(rgb);
		this.setNeighbors(new ArrayList<Integer>());
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getRgb() {
		return rgb;
	}
	public void setRgb(int rgb) {
		this.rgb = rgb;
	}
	public ArrayList<Integer> getNeighbors() {
		return neighbors;
	}
	public void setNeighbors(ArrayList<Integer> neighbors) {
		this.neighbors = neighbors;
	}

}
