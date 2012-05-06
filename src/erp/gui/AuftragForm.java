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

import erp.ctrl.Auftragsverwaltung;
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
				AuftragAnlegenForm win = new AuftragAnlegenForm(AuftragForm.this);
				win.setVisible(true);
				updateTable();
			}
		});
		menu_auftraege.add(auftrag_erstellen);
		
		JMenuItem auftrag_bearbeiten = new JMenuItem("Offene Aufträge bearbeiten");
		auftrag_bearbeiten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<Auftrag> auftraege = (new LegoDB()).readOffeneAuftraege(0);
				String resulttext = "Es wurden " + auftraege.size() + " Aufträge bearbeitet.\n\n";
				String[] stext = {"unbearbeitet", "Fertigungsauftrag", "Fertigung beendet", "Lagerlieferung", "Rechnungsschreibung", "abgeschlossen"};
				
				for(Auftrag a : auftraege) {
					int result = Auftragsverwaltung.auftragBearbeiten(a.getAuftrNr());
					if(result == 0)
						resulttext = resulttext + "FEHLER: ";
					
					resulttext = resulttext + "Auftragsnr: " + a.getAuftrNr() + 
							", alter Status: " + a.getAuftrStatus() + " (" + stext[a.getAuftrStatus()] + ")" +
							", neuer Status: " + result + " (" + stext[result] + ")\n";
				}
				
				updateTable();
				LogWindow win = new LogWindow(AuftragForm.this, false);
				win.setText(resulttext);
				win.setVisible(true);
			}
		});
		menu_auftraege.add(auftrag_bearbeiten);
		
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
		tmodel = new DefaultTableModel(colnames, 0);

		updateTable();
		table = new JTable(tmodel);
		JTableHeader header = table.getTableHeader();
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(header, BorderLayout.PAGE_START);
		panel.add(table, BorderLayout.CENTER);
		
		tab_pane.addTab("Auftragsliste", panel);
		content.add(tab_pane, BorderLayout.CENTER);
		
		setTitle("Lego Trailer ERP");
	}
	
	private void updateTable() {
		List<Auftrag> auftraege = (new LegoDB()).readOffeneAuftraege(3);

		tmodel.setRowCount(0);
		for(Auftrag a : auftraege) {
			String[] rowData = {
					"" + a.getAuftrNr(),
					"" + a.getKdNr(),
					"" + a.getErfassungsDatum(),
					"" + a.getAuftrStatus()};
			tmodel.addRow(rowData);
		}
	}
}
