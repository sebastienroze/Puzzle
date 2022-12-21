package rad;

public interface RadButton extends RadComponent {
	public RadButton addActionListener(ListenerRAD al);
	public RadButton setConfirmation(String confirmation);
	public void setReadOnly(boolean readonly);
	public void setVisible(boolean visible);
	public void performAction();	
}
