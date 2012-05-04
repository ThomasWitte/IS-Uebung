package erp.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author Thomas Witte
 *
 */
public class AuftragPosAnlegenForm extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTextField teileid = new JTextField();
	private String[] combovalues = {"neutral", "rot", "blau"};
	private JComboBox<String> farbe = new JComboBox<String>(combovalues);
	private JTextField anzahl = new JTextField();
	
	public AuftragPosAnlegenForm(JDialog owner, final DefaultTableModel tmodel) {
		super(owner, true);
		Container content = getContentPane();
		content.setLayout(new BorderLayout());
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(3,2));
		center.add(new JLabel("TeileID:"));
		center.add(teileid);
		center.add(new JLabel("Farbe:"));
		center.add(farbe);
		center.add(new JLabel("Anzahl:"));
		center.add(anzahl);
		
		JButton accept = new JButton("Hinzufügen");
		accept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] data = {teileid.getText(),
								 farbe.getSelectedItem().toString(),
								 anzahl.getText()};
				tmodel.addRow(data);
				dispose();
			}
		});
		JButton cancel = new JButton("Abbruch");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttons.add(accept);
		buttons.add(cancel);
		
		content.add(new JLabel("Neue Auftragsposition"), BorderLayout.PAGE_START);
		content.add(center, BorderLayout.CENTER);
		content.add(buttons, BorderLayout.PAGE_END);
		
		setTitle("Auftragsposition hinzufügen");
		setSize(400, 130);
	}
}
