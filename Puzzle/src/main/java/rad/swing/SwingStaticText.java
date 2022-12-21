package rad.swing;

import javax.swing.JLabel;
import rad.RadStaticText;

public class SwingStaticText extends SwingComponent implements RadStaticText {
	private JLabel jlabel;

	public SwingStaticText() {
		this.jlabel = new JLabel();
	}

	@Override
	public void setValue(String value) {
		jlabel.setText(value);
	}

	@Override
	public void setReadOnly(boolean readonly) {
	}

	@Override
	public String getValue() {
		return jlabel.getText();
	}

	@Override
	public void setVisible(boolean visible) {
		jlabel.setVisible(visible);
	}

	@Override
	public JLabel getComponent() {
		return jlabel;
	}
}
