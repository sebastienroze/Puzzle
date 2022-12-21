package rad.swing;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import rad.ListenerRAD;
import rad.RadTextField;

public class SwingTextField extends SwingComponent implements RadTextField {
	private JTextField txtField ;
	private ListenerRAD onChangeListener = null;	
	
	public SwingTextField(int size) {		
		this.txtField = new JTextField(size);
	}

	@Override
	public void setValue(String value) {
		txtField.setText(value);		
	}

	@Override
	public void setReadOnly(boolean readonly) {
		txtField.setEditable(! readonly);		
	}

	@Override
	public String getValue() {		
		return txtField.getText();
	}

	@Override
	public void setVisible(boolean visible) {
		txtField.setVisible(visible);		
	}

	@Override
	public void addChangeListener(ListenerRAD al) {
		this.onChangeListener = al;
		txtField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				SwingTextField.this.onChangeListener.actionPerformed(SwingTextField.this);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingTextField.this.onChangeListener.actionPerformed(SwingTextField.this);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				SwingTextField.this.onChangeListener.actionPerformed(SwingTextField.this);
			}
		});		
		
	}

	@Override
	public JTextField getComponent() {
		return txtField;
	}
}
