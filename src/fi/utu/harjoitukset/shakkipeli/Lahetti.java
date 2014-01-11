package fi.utu.harjoitukset.shakkipeli;
import java.lang.Math;
/**
 * Lähetti- shakkinappula
 */
public class Lahetti extends AbstraktiNappula {
	
	/**
	 * Uutta lähettiä luodessa kutsuttava konstruktori
	 *
	 * @param onValkoinen Halutaanko luoda valkoinen nappula?
	 * @.post <code>this.onValkoinen() == onValkoinen</code>
	 */
	public Lahetti(boolean onValkoinen) {
		super(MerkkiesitysNappulalle.LAHETTI, onValkoinen);
	}

	/**
	 * Lähetti liikkuu viistoon kaikkiin suuntiin, mutta ei muiden
	 * nappuloiden ylitse.
	 *
	 * @return <code>true</code>, jos ehdotettu siirto on sallittu lähetille
	 * @param peli <code>Shakkipeli</code>, jossa siirto halutaan tehdä
	 * @param vaakaAlku lähtökohdan vaakakoordinaatti [A-Ha-h]
	 * @param pystyAlku lähtökohdan pystykoordinaatti [1-8]
	 * @param vaakaLoppu maaliruudun vaakakoordinaatti [A-Ha-h]
	 * @param pystyLoppu maaliruudun pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; vaakaAlku == c) && (1 <= pystyAlku <= 8) && EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; vaakaLoppu == c) && (1 <= pystyLoppu <= 8)</code>
	 */
	public boolean onLaillinen(Shakkipeli peli, char vaakaAlku, int pystyAlku,
												char vaakaLoppu, int pystyLoppu) {
		// paikallaan pysyminen ei ole siirto
		if (vaakaAlku == vaakaLoppu || pystyAlku == pystyLoppu) return false;
		// kohderuudussa ei saa olla oma nappula
		if (peli.onMiehitetty(vaakaLoppu, pystyLoppu)) {
			if (peli.annaNappula(vaakaLoppu, pystyLoppu).onValkoinen() == onValkoinen()) {
				return false;
			}
		}
		// sallitut siirrot ovat eteen tai taakse kulmakertoimella 1 tai -1
		if ((vaakaAlku - pystyAlku == vaakaLoppu - pystyLoppu) ||
			(vaakaAlku + pystyAlku == vaakaLoppu + pystyLoppu)) {
			// tarkastetaan, onko matkalla nappuloita viimeistä ruutua lukuunottamatta
			for (int pituus = Math.abs(vaakaLoppu - vaakaAlku) - 1; pituus > 0; pituus--) {
				if (peli.onMiehitetty((vaakaAlku < vaakaLoppu) ? (char)(vaakaAlku + pituus) : (char)(vaakaAlku - pituus),
									(pystyAlku < pystyLoppu) ? pystyAlku + pituus : pystyAlku - pituus)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Vertaa tätä <code>Lahetti</code>:ä toiseen olioon.
	 * Palauttaa <code>true</code>, jos toinen
	 * on samanvärinen <code>Lahetti</code>
	 * -nappula, tätä ja toista on tai ei ole
	 * siirretty meneillää olevassa pelissä ja
	 * molemmilla on sama merkkiesitys.
	 *
	 * @param o tähän verrattava olio
	 * @return <code>true</code>, jos tämä <code>Lahetti</code> on samanlainen kuin verrukki
	 */
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o instanceof Lahetti) {
			return super.equals(o);
		}
		return false;
	}
}