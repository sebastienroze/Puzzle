package rad.swing;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import rad.ListenerRAD;
import rad.RadRadioGroup;

public class SwingRadioGroup extends SwingComponent implements RadRadioGroup {
	private ListenerRAD onChangeListener = null;	
	private HashMap<String, JRadioButton> values ; 
	private boolean readOnly;
	private ButtonGroup btG;
	private JPanel panel;
	
	public SwingRadioGroup(String[] values, String[] labels) {
		this.values = new HashMap<String, JRadioButton>();
		this.btG = new ButtonGroup();
		panel = new JPanel();
		for (int i = 0; i < values.length; i++) {
			JRadioButton rb = new JRadioButton(labels[i]);
			AddRadioButton(values[i], rb);
		}		
		readOnly = false;
	}
	
	private void AddRadioButton(String value,JRadioButton rb) {
		values.put(value,rb);
		btG.add(rb);
		panel.add(rb);		
	}	

	@Override
	public void setValue(String value) {
		if (values.containsKey(value)) {
			values.get(value).setSelected(true);
		} else {
			btG.clearSelection();
		}
		if (readOnly) setReadOnly(readOnly);
	}
	
	@Override
	public String getValue() {		
		for (Map.Entry<String, JRadioButton>  entry : values.entrySet()) {
			if (entry.getValue().isSelected()) {
				return entry.getKey();
			}
		}
		return "";
	}

	@Override
	public void setReadOnly(boolean readonly) {		
		this.readOnly = readonly;
		for (Map.Entry<String, JRadioButton>  entry : values.entrySet()) {
			entry.getValue().setEnabled(!readonly || entry.getValue().isSelected());
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		panel.setVisible(visible);
	}

	@Override
	public JPanel getComponent() {
		return panel;
	}

	@Override
	public void addChangeListener(ListenerRAD al) {
		this.onChangeListener = al;
		for (Map.Entry<String, JRadioButton>  entry : values.entrySet()) {
			entry.getValue().addActionListener(e -> {
				SwingRadioGroup.this.onChangeListener.actionPerformed(SwingRadioGroup.this);
			});
		}
	}

}
