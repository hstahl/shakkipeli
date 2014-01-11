package fi.utu.harjoitukset.shakkipeli;
/**
 * Kaksiulotteisen koordinaatiston pistettä
 * kuvaava luokka. Koordinaatisto voi olla
 * mielivaltainen.
 */
public class Koordinaatit<X,Y> {
	
	/** ensimmäinen (vaaka) koordinaatti */
	protected final X eka;
	/** toinen (pysty) koordinaatti */
	protected final Y toka;
	
	/**
	 * @param eka ensimmäinen koordinaatti
	 * @param toka toinen koordinaatti
	 * @.pre <code>eka != null && toka != null</code>
	 * @.post <code>annaEka() == eka && annaToka() == toka</code>
	 */
	public Koordinaatit(X eka, Y toka) {
		this.eka = eka;
		this.toka = toka;
	}
	
	/**
	 * Palauttaa ensimmäisen (vaaka) koordinaatin
	 *
	 * @return ensimmäinen koordinaatti
	 */
	public X annaEka() {
		return eka;
	}
	
	/**
	 * Palauttaa toisen (pysty) koordinaatin
	 *
	 * @return toinen koordinaatti
	 */
	public Y annaToka() {
		return toka;
	}
	
	/**
	 * Vertaa toista oliota tähän <code>
	 * Koordinaatit</code> -olioon. Palauttaa
	 * <code>true</code>, jos toinen olio on
	 * samanlainen, kuin tämä.
	 *
	 * @return	<code>true</code>, jos tämä <code>Koordinaatit</code> ja <code>toinen</code> ovat syväsamoja
	 */
	public boolean equals(Object toinen) {
		if (this == toinen) return true;
		if (!(toinen instanceof Koordinaatit)) return false;
		Koordinaatit t = (Koordinaatit)toinen;
		return annaEka().equals(t.annaEka()) && annaToka().equals(t.annaToka()); 
	}
	
	/**
	 * Koordinaattien merkkijonoesitys
	 *
	 * @return koordinaatit merkkijonona
	 */
	public String toString() {
		return "(" + eka + ", " + toka + ")";
	}
}