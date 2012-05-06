package erp.database;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;
import java.util.LinkedList;

import javax.swing.JOptionPane;

/**
 * 
 * @author Thomas Witte
 *
 */
public class LegoDB {
	public Connection openConnection() {
		String url = "jdbc:derby://localhost:1527/dbs";
		String driver = "org.apache.derby.jdbc.ClientDriver";
		Connection con = null;

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, driver + " konnte nicht gefunden werden",
					"Fehler beim Verbinden mit der Datenbank", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}

		try {
			con = DriverManager.getConnection(url);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Läuft die Datenbank " + url + " ?", 
					"Fehler beim Verbinden mit der Datenbank", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}
		return con;
	}

	public List<Auftrag> readOffeneAuftraege(int maxstatus) {
		Connection con = openConnection();
		List<Auftrag> ret = new LinkedList<Auftrag>();

		try (PreparedStatement ps = con.prepareStatement(
				"select * " +
				"from auftragsds " +
				"where auftrstatus <= ?")) {
			ps.setInt(1, maxstatus);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Auftrag a = new Auftrag();
				a.setAuftrNr(rs.getInt("auftrnr"));
				a.setAuftrStatus(rs.getInt("auftrstatus"));
				a.setErfassungsDatum(rs.getDate("erfassungsdatum"));
				a.setKdNr(rs.getInt("kdnr"));
				a.setLgLieferung(rs.getInt("lglieferung") == 1);
				ret.add(a);
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "konnte offene Aufträge nicht laden", 
					"Fehler beim Datenbankzugriff", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}

		return ret;
	}

	public List<Kunde> readKunden() {
		Connection con = openConnection();
		List<Kunde> ret = new LinkedList<Kunde>();
		
		try (PreparedStatement ps = con.prepareStatement(
				"select * from kunden")) {
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Kunde k = new Kunde(
						rs.getInt("kdnr"),
						rs.getString("kdname"),
						rs.getString("kdstadt"),
						rs.getInt("bonitaet"));
				ret.add(k);
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "konnte Kunden nicht laden",
					"Fehler beim Datenbankzugriff", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}
		
		return ret;
	}
	
	public Kunde createKunde(String name, String town, int bonitaet) {
		Connection con = openConnection();
		int kdnr = 0;
		
		//nächste Kundennummer
		try (PreparedStatement ps = con.prepareStatement("select max(KdNr) as kdnr from kunden")) {
			ResultSet rs = ps.executeQuery();
			rs.next();
			kdnr = rs.getInt("kdnr") + 1;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Konnte keine neue Kundennummer generieren",
					"Fehler beim Datenbankzugriff", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}
		
		//Kunde einfügen
		try (PreparedStatement ps = con.prepareStatement("insert into kunden values(?,?,?,?)")) {
			ps.setInt(1, kdnr);
			ps.setString(2, name);
			ps.setString(3, town);
			ps.setInt(4, bonitaet);
			ps.executeUpdate();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Konnte Kunde nicht anlegen",
					"Fehler beim Datenbankzugriff",	JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}
		
		return new Kunde(kdnr, name, town, bonitaet);
	}
	
	public void createAuftrag(int KdNr, String kdauftrnr, Date kdauftrdatum,
			String[] teileID, int[] farbe, int[] amount) {
		Connection con = openConnection();
		int auftrnr = 0;
		
		// nächste Auftragsnummer feststellen
		try (PreparedStatement ps = con.prepareStatement("select max(AuftrNr) as AuftrNr from auftragsds")) {
			ResultSet rs = ps.executeQuery();
			rs.next();
			auftrnr = rs.getInt("AuftrNr") + 1;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Konnte keine neue Auftragsnummer generieren",
					"Fehler beim Datenbankzugriff", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}

		// Auftrag einfügen
		try (PreparedStatement ps = con.prepareStatement("insert into auftragsds values(?,?,?,?,?, null, 0, null, null, null, null, null, 0, null, null)")) {
			ps.setInt(1, auftrnr);
			ps.setInt(2, KdNr);
			ps.setString(3, kdauftrnr);
			ps.setDate(4, kdauftrdatum);
			ps.setDate(5, new Date(System.currentTimeMillis()));
			ps.executeUpdate();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Konnte Auftrag nicht anlegen; existiert die Kundennummer?",
					"Fehler beim Datenbankzugriff",	JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}

		// Auftragspositionen einfügen
		try (PreparedStatement ps = con.prepareStatement("insert into auftragsposds values(?,?,?,?,null,?,null,null,null,null,null,null,0)")) {
			for (int i = 0; i < teileID.length; ++i) {
				ps.setInt(1, auftrnr);
				ps.setInt(2, i + 1);
				ps.setString(3, teileID[i]);
				ps.setInt(4, farbe[i]);
				ps.setInt(5, amount[i]);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, 
					"Konnte Auftragsposition nicht anlegen; existieren alle Teile in den gewählten Farben?",
					"Fehler beim Datenbankzugriff", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
