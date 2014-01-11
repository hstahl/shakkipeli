package fi.utu.harjoitukset.shakkipeli;
/**
 * Kaksiulotteisen koordinaatiston pistett�
 * kuvaava luokka. Koordinaatisto voi olla
 * mielivaltainen.
 */
public class Koordinaatit<X,Y> {
	
	/** ensimm�inen (vaaka) koordinaatti */
	protected final X eka;
	/** toinen (pysty) koordinaatti */
	protected final Y toka;
	
	/**
	 * @param eka ensimm�inen koordinaatti
	 * @param toka toinen koordinaatti
	 * @.pre <code>eka != null && toka != null</code>
	 * @.post <code>annaEka() == eka && annaToka() == toka</code>
	 */
	public Koordinaatit(X eka, Y toka) {
		this.eka = eka;
		this.toka = toka;
	}
	
	/**
	 * Palauttaa ensimm�isen (vaaka) koordinaatin
	 *
	 * @return ensimm�inen koordinaatti
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
	 * Vertaa toista oliota t�h�n <code>
	 * Koordinaatit</code> -olioon. Palauttaa
	 * <code>true</code>, jos toinen olio on
	 * samanlainen, kuin t�m�.
	 *
	 * @return	<code>true</code>, jos t�m� <code>Koordinaatit</code> ja <code>toinen</code> ovat syv�samoja
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