package puzzle;

import java.io.Serializable;
import java.util.ArrayList;

public class PieceInfo implements Serializable {
	private static final long serialVersionUID = 2L;
	private int offsetX;
	private int offsetY;
	private int rotation =-1;
	private int x=0;
	private int y=0;
	private ArrayList<Integer> neighbors;
	
	public PieceInfo(int offsetX, int offsetY) {
		super();
		this.setOffsetX(offsetX);
		this.setOffsetY(offsetY);
		setNeighbors(new ArrayList<Integer>()); 
	}
	public PieceInfo(int offsetX, int offsetY,int rotation,int x,int y) {
		this.x = x;
		this.y = y;
		this.setOffsetX(offsetX);
		this.setOffsetY(offsetY);
		this.rotation =rotation;
		setNeighbors(new ArrayList<Integer>()); 		
	}
	public int getRotation() {
		return rotation;
	}
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	public ArrayList<Integer> getNeighbors() {
		return neighbors;
	}
	public void setNeighbors(ArrayList<Integer> neighbor) {
		this.neighbors = neighbor;
	}
	public int getOffsetX() {
		return offsetX;
	}
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}
	public int getOffsetY() {
		return offsetY;
	}
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
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
	
	public String getJson() {
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(getOffsetX() + "," + getOffsetY() + ",");
		json.append("[");
		boolean bfirst = true;
		for (int neighbor : getNeighbors()) {
			if (!bfirst) {
				json.append(",");
			}
			bfirst = false;
			json.append(neighbor);
		}
		json.append("],");
		json.append(rotation);
		json.append(",");
		json.append(x);
		json.append(",");
		json.append(y);
		json.append("]");
		return json.toString();
	}
	
}
