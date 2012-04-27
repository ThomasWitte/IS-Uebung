package erp.ctrl;

import javax.swing.JFrame;

import erp.gui.AuftragForm;

/**
 * 
 * @author Thomas Witte
 *
 */
public class LegoERPMain {
	public static void main(String[] args) {
		AuftragForm win = new AuftragForm();
		win.setSize(640, 480);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setVisible(true);
	}
}
