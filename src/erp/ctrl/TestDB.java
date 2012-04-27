package erp.ctrl;

import erp.database.LegoDB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import static java.sql.Types.*;

/**
 * 
 * @author Thomas Witte
 *
 */
public class TestDB {
	public static void main(String[] args) {
		LegoDB legodb = new LegoDB();
		Connection con = legodb.openConnection();

		try (PreparedStatement ps = con
				.prepareStatement("select * from teiletypen")) {
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData md = rs.getMetaData();

			int colcount = md.getColumnCount();
			for(int i = 1; i <= colcount; ++i) {
				System.out.print(md.getColumnName(i) + " | ");
			}
			System.out.println("\n------------------------------------------");
			
			while (rs.next()) {
				for (int i = 1; i <= colcount; ++i) {
					switch (md.getColumnType(i)) {
					case VARCHAR:
						System.out.print(rs.getString(i));
						break;

					case INTEGER:
						System.out.print(rs.getInt(i));
						break;
						
					case BOOLEAN:
						System.out.print(rs.getBoolean(i));
						break;
						
					case DATE:
						System.out.print(rs.getDate(i));
						break;
						
					case DOUBLE:
						System.out.print(rs.getDouble(i));
						break;
					}
					System.out.print(" | ");
				}
				System.out.println();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
