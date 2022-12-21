package rad.swing;

import javax.swing.JLabel;
import javax.swing.JPanel;

import rad.RadComponent;
import rad.RadContainer;

public class SwingContainer extends SwingComponent implements RadContainer  {
	private JPanel content;

	public SwingContainer() {
		super();
		content = new JPanel();
	}

	@Override
	public void addComponent(RadComponent cpn) {	
		content.add(((SwingComponent) cpn).getComponent());
	}

	@Override
	public JPanel getComponent() {		
		return content;
	}

	@Override
	public void addLabelComponent(String label, RadComponent radComponent) {
			SwingComponent comp = (SwingComponent) radComponent;
			content.add(new JLabel(label));
			content.add(comp.getComponent());
	}

	@Override
	public void setVisible(boolean visible) {
		content.setVisible(visible);		
	}

}
