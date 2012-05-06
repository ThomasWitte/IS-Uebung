package erp.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import erp.database.Kunde;
import erp.database.LegoDB;

public class KundeAnlegenForm extends JDialog {
	private static final long serialVersionUID = 1L;
	private Kunde newkd;
	private JButton cancel = new JButton("Abbrechen");
	private JButton create = new JButton("Kunde erstellen");
	private JTextField nameField = new JTextField();
	private JTextField townField = new JTextField();
	private JSlider slider = new JSlider(-2, 2, 0);
	
	KundeAnlegenForm(JDialog owner) {
		super(owner, true);
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LegoDB db = new LegoDB();
				newkd = db.createKunde(
						nameField.getText(),
						townField.getText(),
						slider.getValue());
				setVisible(false);
			}
		});
		
		Container content = getContentPane();
		content.setLayout(new BorderLayout());
		
		JPanel center = new JPanel();
		JPanel buttons = new JPanel();

		center.setLayout(new GridLayout(3,2));
		center.add(new JLabel("Kundenname:"));
		center.add(nameField);
		center.add(new JLabel("Ort:"));
		center.add(townField);
		center.add(new JLabel("Bonität:"));
		slider.setMajorTickSpacing(2);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		center.add(slider);
		
		buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttons.add(cancel);
		buttons.add(create);
		
		content.add(center, BorderLayout.CENTER);
		content.add(buttons, BorderLayout.PAGE_END);
		
		setTitle("Neuen Kunden anlegen");
		setSize(300,200);
	}
	
	Kunde getNewKd() {
		return newkd;
	}
}
