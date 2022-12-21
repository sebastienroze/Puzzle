package rad.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;

import rad.ListenerRAD;
import rad.RadComboBox;

public class SwingComboBox extends SwingComponent implements RadComboBox  {
	private HashMap<String, Integer> values;
	private JComboBox<String> cb;
	private ListenerRAD onChangeListener = null;	

	public SwingComboBox() {
		values = new HashMap<String, Integer>();
		cb = new JComboBox<String>();
		cb.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			@Override
			public void paint(Graphics g) {
				setForeground(cb.getForeground());
				super.paint(g);
			}
		});
	}
	
	@Override
	public void addItem(String value, String label) { 
		values.put(value, cb.getItemCount());
		cb.addItem(label);
	}

	@Override
	public void setValue(String value) {
		if (values.containsKey(value)) {
			cb.setSelectedIndex(values.get(value));
		} else {
			cb.setSelectedIndex(-1);
		}
	}

	@Override
	public String getValue() {
		for (Entry<String, Integer> entry : values.entrySet()) {
			if (cb.getSelectedIndex() == entry.getValue()) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	@Override
	public String getDisplayValue() {
		if (cb.getSelectedIndex()==-1) return null;
		return cb.getItemAt(cb.getSelectedIndex());
	}

	@Override
	public void setReadOnly(boolean readonly) {
		cb.setEnabled(!readonly);
	}

	@Override
	public void setVisible(boolean visible) {
		cb.setVisible(visible);		
	}

	@Override
	public Component getComponent() {		
		return cb;
	}

	@Override
	public void addChangeListener(ListenerRAD al) {
		this.onChangeListener = al;
		cb.addActionListener(e -> {
			SwingComboBox.this.onChangeListener.actionPerformed(SwingComboBox.this);
		});				
	}

}
