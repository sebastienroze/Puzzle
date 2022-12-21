package rad.swing;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import rad.ListenerRAD;
import rad.RadButton;

public class SwingButton extends SwingComponent implements RadButton {
	private JButton btn;
	private String confirmation = null;

	public SwingButton(String text) {
		super();
		btn = new JButton(text);
	}

	public RadButton setConfirmation(String confirmation) {
		this.confirmation = confirmation;
		return this;
	}

	private ListenerRAD actionListener = null;

	@Override
	public void setVisible(boolean visible) {
		btn.setVisible(visible);
	}

	@Override
	public RadButton addActionListener(ListenerRAD al) {
		this.actionListener = al;
		btn.addActionListener(e -> {
			boolean confirm = true;
			if (SwingButton.this.confirmation != null) {
				int dialogResult = JOptionPane.showConfirmDialog (null, 
						SwingButton.this.confirmation,"Warning",JOptionPane.YES_NO_OPTION);
				confirm = (dialogResult == JOptionPane.YES_OPTION);
			}
			if (confirm)
				SwingButton.this.performAction();
		});
		return this;		
	}

	@Override
	public JButton getComponent() {
		return btn;
	}

	@Override
	public void setReadOnly(boolean readonly) {
		btn.setEnabled(!readonly);			
	}
	
	@Override
	public void performAction() {
		if (this.actionListener!=null) this.actionListener.actionPerformed(this);		
	}		

}
