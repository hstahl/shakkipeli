package fi.utu.harjoitukset.shakkipeli;
/**
 * Poikkeustyyppi, joka on tarkoitus heitt�� ohjelman
 * lukiessa komentoparametri� konsolistasy�tteest� ja
 * saatuaan v��rin muotoillun tai muuten virheellisen
 * komennon.
 */
public class TunnistamatonKomentoPoikkeus extends Exception {

	private String komento;
	
	/**
	 * Luo <code>TunnistamatonKomentoPoikkeus</code>:ksen ilman kuvaavaa viesti�.
	 */
	public TunnistamatonKomentoPoikkeus() {}

	/**
	 * Luo <code>TunnistamatonKomentoPoikkeus</code>:ksen kuvaavan viestin kanssa.
	 *
	 * @param komento v�litett�v� virheviesti
	 */
	public TunnistamatonKomentoPoikkeus(String komento) {
		super(komento);
		this.komento = komento;
	}

	/**
	 * Poikkeusta kuvaava merkkijonoesitys
	 * 
	 * @return poikkeuksen merkkijonoesitys
	 */
	public String toString() {
		return "Tunnistamaton komento: " + komento;
	}
}