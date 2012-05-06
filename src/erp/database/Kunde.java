package erp.database;

/**
 * 
 * @author Thomas Witte
 *
 */
public class Kunde {
	private int kdnr;
	private String name;
	private String stadt;
	private int bonitaet;
	
	Kunde(int kdnr, String name, String stadt, int bonitaet) {
		this.kdnr = kdnr;
		this.name = name;
		this.stadt = stadt;
		this.bonitaet = bonitaet;
	}
	
	public int getKdnr() {
		return kdnr;
	}
	public void setKdnr(int kdnr) {
		this.kdnr = kdnr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStadt() {
		return stadt;
	}
	public void setStadt(String stadt) {
		this.stadt = stadt;
	}
	public int getBonitaet() {
		return bonitaet;
	}
	public void setBonitaet(int bonitaet) {
		this.bonitaet = bonitaet;
	}
	
	public String toString() {
		return name + " [" + kdnr + "]";
	}
}
