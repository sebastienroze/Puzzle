package rad;

public interface RadComboBox extends DataFieldRAD,RadComponent {
	public void addChangeListener(ListenerRAD al);
	public void addItem(String value, String label);
	public String getDisplayValue();
}
