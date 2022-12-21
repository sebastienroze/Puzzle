package puzzle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Preferences implements Serializable {
	private static final long serialVersionUID = 3L;
	protected boolean fullScreen=true;
	protected String color="#DEB887";
	public static Preferences deserizalizeFromFile(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Preferences preferences = (Preferences) ois.readObject();
			ois.close();
			fis.close();
			return preferences;
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
