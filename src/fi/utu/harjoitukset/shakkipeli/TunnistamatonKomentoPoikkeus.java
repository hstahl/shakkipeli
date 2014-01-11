package fi.utu.harjoitukset.shakkipeli;
/**
 * Poikkeustyyppi, joka on tarkoitus heittää ohjelman
 * lukiessa komentoparametriä konsolistasyötteestä ja
 * saatuaan väärin muotoillun tai muuten virheellisen
 * komennon.
 */
public class TunnistamatonKomentoPoikkeus extends Exception {

	private String komento;
	
	/**
	 * Luo <code>TunnistamatonKomentoPoikkeus</code>:ksen ilman kuvaavaa viestiä.
	 */
	public TunnistamatonKomentoPoikkeus() {}

	/**
	 * Luo <code>TunnistamatonKomentoPoikkeus</code>:ksen kuvaavan viestin kanssa.
	 *
	 * @param komento välitettävä virheviesti
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