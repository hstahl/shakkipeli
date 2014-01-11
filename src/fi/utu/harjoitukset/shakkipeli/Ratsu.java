package fi.utu.harjoitukset.shakkipeli;
import java.lang.Math;
/**
 * Ratsu- shakkinappula
 */
public class Ratsu extends AbstraktiNappula {
	
	/**
	 * Uutta ratsua luodessa kutsuttava konstruktori
	 *
	 * @param onValkoinen Halutaanko luoda valkoinen nappula?
	 * @.post <code>this.onValkoinen() == onValkoinen</code>
	 */
	public Ratsu(boolean onValkoinen) {
		super(MerkkiesitysNappulalle.RATSU, onValkoinen);
	}

	/**
	 * Ratsua voi siirtää ensin kaksi ruutua vaaka- tai pystyakselin
	 * suunnassa ja sitten yhden ruudun sivuun valitusta suunnasta.
	 * Maalipiste siis voi olla jokin kahdeksasta ruudusta ratsun
	 * ympärillä. Ratsu voi myös hyppiä muiden nappuloiden ylitse.
	 *
	 * @return <code>true</code>, jos ehdotettu siirto on sallittu ratsulle
	 * @param peli <code>Shakkipeli</code>, jossa siirto halutaan tehdä
	 * @param vaakaAlku lähtökohdan vaakakoordinaatti [A-Ha-h]
	 * @param pystyAlku lähtökohdan pystykoordinaatti [1-8]
	 * @param vaakaLoppu maaliruudun vaakakoordinaatti [A-Ha-h]
	 * @param pystyLoppu maaliruudun pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; vaakaAlku == c) && (1 <= pystyAlku <= 8) && EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; vaakaLoppu == c) && (1 <= pystyLoppu <= 8)</code>
	 */
	public boolean onLaillinen(Shakkipeli peli, char vaakaAlku, int pystyAlku,
												char vaakaLoppu, int pystyLoppu) {
		// maaliruudussa ei saa olla omaa nappulaa
		if (peli.onMiehitetty(vaakaLoppu, pystyLoppu)) {
			if (peli.annaNappula(vaakaLoppu, pystyLoppu).onValkoinen() == onValkoinen()) {
				return false;
			}
		}
		if ((Math.abs(vaakaLoppu - vaakaAlku) == 2 && Math.abs(pystyLoppu - pystyAlku) == 1) ||
			(Math.abs(pystyLoppu - pystyAlku) == 2 && Math.abs(vaakaLoppu - vaakaAlku) == 1)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Vertaa tätä <code>Ratsu</code>:a toiseen olioon.
	 * Palauttaa <code>true</code>, jos toinen
	 * on samanvärinen <code>Ratsu</code>
	 * -nappula, tätä ja toista on tai ei ole
	 * siirretty meneillää olevassa pelissä ja
	 * molemmilla on sama merkkiesitys.
	 *
	 * @param o tähän verrattava olio
	 * @return <code>true</code>, jos tämä <code>Ratsu</code> on samanlainen kuin verrukki
	 */
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o instanceof Ratsu) {
			return super.equals(o);
		}
		return false;
	}	
}