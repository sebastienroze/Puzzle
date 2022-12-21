package rad;

public interface RadContainer extends RadComponent {
	public void addComponent(RadComponent cpn);
	public void addLabelComponent(String label, RadComponent radComponent) ;	
}
