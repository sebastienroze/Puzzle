package rad.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import rad.ListenerRAD;
import rad.RadCheckBox;

public class SwingCheckBox extends SwingComponent implements RadCheckBox {
	private JCheckBox checkBox ;
	private ListenerRAD onChangeListener = null;	

	public SwingCheckBox() {
		super();
		checkBox = new JCheckBox();
	}

	@Override
	public void setValue(String value) {
		checkBox.setSelected((value!=null) && !"0".equals(value));
	}

	public boolean getBooleanValue() {
		return checkBox.isSelected();
	}
	
	@Override
	public String getValue() {		
		return checkBox.isSelected() ? "1":"0";
	}

	@Override
	public void setReadOnly(boolean readonly) {
		checkBox.setEnabled(! readonly);		
	}

	@Override
	public void setVisible(boolean visible) {
		checkBox.setVisible(visible);		
		
	}

	@Override
	public void addChangeListener(ListenerRAD al) {
		this.onChangeListener = al;
		checkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingCheckBox.this.onChangeListener.actionPerformed(SwingCheckBox.this);
			}
		} ); 
		
	}

	@Override
	public JCheckBox getComponent() {
		return checkBox;
	}

}
