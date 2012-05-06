package erp.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class LogWindow extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTextArea text = new JTextArea();
	
	public LogWindow(JFrame parent, boolean modal) {
		super(parent, modal);
		getContentPane().add(text);
		setSize(500, 300);
	}
	
	public void setText(String t) {
		text.setText(t);
	}
}
