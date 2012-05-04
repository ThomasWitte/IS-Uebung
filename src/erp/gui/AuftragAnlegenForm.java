package erp.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.sql.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import erp.database.LegoDB;

/**
 * 
 * @author Thomas Witte
 *
 */
public class AuftragAnlegenForm extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTextField kdnr_field = new JTextField();
	private JTextField kdauftrnr_field = new JTextField();
	private JTextField kddate_field = new JTextField();
	private JButton add_button = new JButton("Auftragspos. hinzufügen");
	private JButton create_button = new JButton("Erstellen");
	private JButton cancel_button = new JButton("Abbruch");
	private DefaultTableModel tmodel = null;

	public AuftragAnlegenForm(JFrame owner) {
		super(owner, true);
		add_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AuftragPosAnlegenForm f = new AuftragPosAnlegenForm(AuftragAnlegenForm.this, tmodel);
				f.setVisible(true);
			}
		});
		create_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int kdnr = Integer.parseInt(kdnr_field.getText());
				String kdauftrnr = kdauftrnr_field.getText();
				try {
					DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
					Date kdauftrdatum = new Date(df.parse(kddate_field.getText()).getTime());

					int rows = tmodel.getRowCount();
					String[] teileID = new String[rows];
					int[] farbe = new int[rows];
					int[] amount = new int[rows];
					for(int i = 0; i < rows; ++i) {
						teileID[i] = tmodel.getValueAt(i, 0).toString();
						switch(tmodel.getValueAt(i, 1).toString()) {
						case "neutral":
							farbe[i] = 0;
							break;
						case "rot":
							farbe[i] = 1;
							break;
						case "blau":
							farbe[i] = 2;
						}
						amount[i] = Integer.parseInt(tmodel.getValueAt(i, 2).toString());
						System.out.println(teileID[i] + ", " + farbe[i] + ", " + amount[i]);
					}

					LegoDB legodb = new LegoDB();
					legodb.createAuftrag(kdnr, kdauftrnr, kdauftrdatum, teileID, farbe, amount);
				} catch (ParseException ex) {
					JOptionPane.showMessageDialog(null, "ungültiges Datumsformat; bitte dd.mm.yy verwenden",
							"Fehler beim Parsen des Datums", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
				dispose();
			}
		});
		cancel_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		Container content = getContentPane();
		content.setLayout(new GridLayout(2,1));
		
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.add(new JLabel("Neuer Auftrag:"), BorderLayout.PAGE_START);
		
		JPanel fields = new JPanel();
		fields.setLayout(new GridLayout(3,2));
		fields.add(new JLabel("Kundennummer:"));
		fields.add(kdnr_field);
		fields.add(new JLabel("Kunden-Auftragsnummer:"));
		fields.add(kdauftrnr_field);
		fields.add(new JLabel("Kunden-Auftragsdatum:"));
		fields.add(kddate_field);
		top.add(fields, BorderLayout.CENTER);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1,5));
		buttons.add(add_button);
		buttons.add(new JPanel());
		buttons.add(new JPanel());
		buttons.add(create_button);
		buttons.add(cancel_button);
		top.add(buttons, BorderLayout.PAGE_END);
		
		String[] colnames = {"TeileID", "Farbe", "Anzahl"};
		tmodel = new DefaultTableModel(colnames, 0);
		JTable table = new JTable(tmodel);
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.add(table.getTableHeader(), BorderLayout.PAGE_START);
		bottom.add(table, BorderLayout.CENTER);
		
		content.add(top, BorderLayout.PAGE_START);
		content.add(bottom, BorderLayout.CENTER);
		setSize(500,250);
		setTitle("Auftrag erstellen");
	}
}
