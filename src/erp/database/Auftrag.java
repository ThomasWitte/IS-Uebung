package erp.database;

import java.sql.Date;

/**
 * 
 * @author Thomas Witte
 *
 */
public class Auftrag {
	private int AuftrNr;
	private int KdNr;
	private Date ErfassungsDatum;
	private boolean LgLieferung;
	private int AuftrStatus;
	
	@Override
	public String toString() {
		return "" + AuftrNr;
	}

	public int getAuftrNr() {
		return AuftrNr;
	}

	public void setAuftrNr(int auftrNr) {
		AuftrNr = auftrNr;
	}

	public int getKdNr() {
		return KdNr;
	}

	public void setKdNr(int kdNr) {
		KdNr = kdNr;
	}

	public Date getErfassungsDatum() {
		return ErfassungsDatum;
	}

	public void setErfassungsDatum(Date erfassungsDatum) {
		ErfassungsDatum = erfassungsDatum;
	}

	public boolean isLgLieferung() {
		return LgLieferung;
	}

	public void setLgLieferung(boolean lgLieferung) {
		LgLieferung = lgLieferung;
	}

	public int getAuftrStatus() {
		return AuftrStatus;
	}

	public void setAuftrStatus(int auftrStatus) {
		AuftrStatus = auftrStatus;
	}
}
