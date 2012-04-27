package erp.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.util.List;

import erp.database.Auftrag;
import erp.database.LegoDB;

/**
 * 
 * @author Thomas Witte
 *
 */
public class AuftragForm extends JFrame {
	private static final long serialVersionUID = 1L;

	private BorderLayout layout = new BorderLayout();
	private JMenuBar menu = new JMenuBar();
	private JMenu menu_datei = new JMenu("Datei");
	private JMenu menu_auftraege = new JMenu("Aufträge");
	private JTabbedPane tab_pane = new JTabbedPane();
	private JTable table = null;
	private DefaultTableModel tmodel = null;

	public AuftragForm() {
		Container content = getContentPane();
		content.setLayout(layout);

		JMenuItem auftrag_erstellen = new JMenuItem("Auftrag erstellen…");
		auftrag_erstellen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AuftragAnlegenForm win = new AuftragAnlegenForm();
				win.setVisible(true);
			}
		});
		menu_auftraege.add(auftrag_erstellen);
		
		JMenuItem beenden = new JMenuItem("Beenden");
		beenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu_datei.add(beenden);
		
		menu.add(menu_datei);
		menu.add(menu_auftraege);
		content.add(menu, BorderLayout.PAGE_START);

		String[] colnames = { "Auftragsnummer", "Kundennummer",
				"Erfassungsdatum", "Auftragsstatus" };
		List<Auftrag> auftraege = (new LegoDB()).readOffeneAuftraege();

		tmodel = new DefaultTableModel(colnames, 0);
		for(Auftrag a : auftraege) {
			String[] rowData = {
					"" + a.getAuftrNr(),
					"" + a.getKdNr(),
					"" + a.getErfassungsDatum(),
					"" + a.getAuftrStatus()};
			tmodel.addRow(rowData);
		}
		table = new JTable(tmodel);
		JTableHeader header = table.getTableHeader();
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(header, BorderLayout.PAGE_START);
		panel.add(table, BorderLayout.CENTER);
		
		tab_pane.addTab("Auftragsliste", panel);
		content.add(tab_pane, BorderLayout.CENTER);
		
		setTitle("Lego Trailer ERP");
	}
}
