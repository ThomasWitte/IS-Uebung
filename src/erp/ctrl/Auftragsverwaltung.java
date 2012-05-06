package erp.ctrl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import erp.database.LegoDB;

public class Auftragsverwaltung {
	private static Connection con = (new LegoDB()).openConnection();
	
	public static int auftragBearbeiten(int auftrnr) {
		//Lieferung aus Lager möglich?
		try (PreparedStatement ps = con.prepareStatement(
				"select * " +
				"from teile natural join auftragsposds " +
				"where auftrnr = ? and bestand-reserviert-anzvonkundebestellt < 0")) {
			ps.setInt(1, auftrnr);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				//Auftrag kann nicht aus Lager bedient werden
				return auftragsPosReservieren(auftrnr);
				
			} else {
				//Auftrag kann aus Lager bedient werden oder besitzt keine Auftragspositionen
				return auftragAusbuchen(auftrnr);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private static int auftragAusbuchen(int auftrnr)
			throws SQLException {
		PreparedStatement ps = con.prepareStatement(
				"select * from auftragsposds where auftrnr = ?");
		ps.setInt(1, auftrnr);
		ResultSet rs = ps.executeQuery();
		
		//Bestand verringern
		while(rs.next()) {
			ps = con.prepareStatement(
					"update teile " +
					"set bestand = (" +
							"select bestand-anzvonkundebestellt as bestand " +
							"from teile natural join auftragsposds " +
							"where auftrnr = ? and teileid = ? and farbe = ?) " +
					"where teileid = ? and farbe = ?");
			ps.setInt(1, auftrnr);
			ps.setString(2, rs.getString("teileid"));
			ps.setInt(3, rs.getInt("farbe"));
			ps.setString(4, rs.getString("teileid"));
			ps.setInt(5, rs.getInt("farbe"));
			ps.executeUpdate();
		}
		
		ps = con.prepareStatement(
				"update auftragsds " +
				"set auftrstatus = 3, lglieferung = 1 " +
				"where auftrnr = ?");
		ps.setInt(1, auftrnr);
		ps.executeUpdate();
		return 3;
	}
	
	private static int auftragsPosReservieren(int auftrnr)
			throws SQLException {
		PreparedStatement ps = con.prepareStatement(
				"select * from auftragsposds where auftrnr = ?");
		ps.setInt(1, auftrnr);
		ResultSet rs = ps.executeQuery();
		int count = 0;
		
		while(rs.next()) {
			//vorrätige Teile reservieren und Fertigungsauftrag eintragen
			ps = con.prepareStatement(
					"update auftragsposds " +
					"set anzfuerkundereserviert = (" +
							"select case when bestand-reserviert > anzvonkundebestellt " +
									"then anzvonkundebestellt " +
									"else bestand-reserviert end " +
							"from teile natural join auftragsposds " +
							"where auftrnr = ? and teileid = ? and farbe = ?) " +
					"where auftrnr = ? and teileid = ? and farbe = ?");
			ps.setInt(1, auftrnr);
			ps.setString(2, rs.getString("teileid"));
			ps.setInt(3, rs.getInt("farbe"));
			ps.setInt(4, auftrnr);
			ps.setString(5, rs.getString("teileid"));
			ps.setInt(6, rs.getInt("farbe"));
			ps.executeUpdate();
			
			ps = con.prepareStatement(
					"update auftragsposds " +
					"set anznochzufertigen = anzvonkundebestellt-anzfuerkundereserviert, fertigungsstatus = 0 " +
					"where anzfuerkundereserviert is not null and anzvonkundebestellt > anzfuerkundereserviert");
			count += (ps.executeUpdate() > 0 ? 1 : 0);
			
			ps = con.prepareStatement(
					"update teile " +
					"set reserviert = (" +
							"select case when bestand > reserviert+anzvonkundebestellt " +
									"then reserviert+anzvonkundebestellt " +
									"else bestand end as reserviert " +
							"from teile natural join auftragsposds " +
							"where auftrnr = ? and teileid = ? and farbe = ?) " +
					"where teileid = ? and farbe = ?");
			ps.setInt(1, auftrnr);
			ps.setString(2, rs.getString("teileid"));
			ps.setInt(3, rs.getInt("farbe"));
			ps.setString(4, rs.getString("teileid"));
			ps.setInt(5, rs.getInt("farbe"));
			ps.executeUpdate();
		}
	
		//Auftragsstatus aktualisieren
		ps = con.prepareStatement(
				"update auftragsds " +
				"set auftrstatus = 1, lglieferung = 0, auftranfertigung = ?, auftrposinarbeit = ?" +
				"where auftrnr = ?");
		ps.setDate(1, new Date(System.currentTimeMillis()));
		ps.setInt(2, (count > 0 ? count : null));
		ps.setInt(3, auftrnr);
		ps.executeUpdate();
		
		return 1;
	}
}
