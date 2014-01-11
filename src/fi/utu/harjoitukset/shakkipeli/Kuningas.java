package fi.utu.harjoitukset.shakkipeli;
import java.lang.Math;
/**
 * Kuningas- shakkinappula
 */
public class Kuningas extends AbstraktiNappula {
	
	/**
	 * Uutta kuningasta luodessa kutsuttava konstruktori
	 *
	 * @param onValkoinen Tuleeko luodun nappulan olla valkoinen?
	 * @.post <code>this.onValkoinen() == onValkoinen && this.onSiirretty() == false</code>
	 */
	public Kuningas(boolean onValkoinen) {
		super(MerkkiesitysNappulalle.KUNINGAS, onValkoinen);
	}
	
	/**
	 * Konstruktori tiedostosta ladattua peliä varten, jossa
	 * kuningasta on saatettu jo siirtää
	 * 
	 * @param onValkoinen Tuleeko luodun nappulan olla valkoinen?
	 * @param onSiirretty Onko nappulaa siirretty ladattavassa pelissä?
	 * @.post <code>this.onValkoinen() == onValkoinen && this.onSiirretty() == onSiirretty</code>
	 */	
	public Kuningas(boolean onValkoinen, boolean onSiirretty) {
		super(MerkkiesitysNappulalle.KUNINGAS, onValkoinen);
		ensimmainenSiirto = !onSiirretty;
	}
	
	/**
	 * Kuningas siirtyy yhden ruudun kerrallaan mihin hyvänsä
	 * suuntaan. Poikkeuksena on linnoitus, jossa siirretään
	 * kaksi ruutua oikealle tai vasemmalle ja siirretään
	 * valitun kulman torni kuninkaan ylitse. Tämä siirto on
	 * sallittu vain jos kuningasta ja tornia ei ole vielä
	 * siirretty pelissä, eikä tornin ja kuninkaan välissä ole
	 * muita nappuloita.
	 *
	 * @return <code>true</code>, jos ehdotettu siirto on sallittu kuninkaalle
	 * @param peli <code>Shakkipeli</code>, jossa siirto halutaan tehdä
	 * @param vaakaAlku lähtökohdan vaakakoordinaatti [A-Ha-h]
	 * @param pystyAlku lähtökohdan pystykoordinaatti [1-8]
	 * @param vaakaLoppu maaliruudun vaakakoordinaatti [A-Ha-h]
	 * @param pystyLoppu maaliruudun pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; vaakaAlku == c) && (1 <= pystyAlku <= 8) && EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; vaakaLoppu == c) && (1 <= pystyLoppu <= 8)</code>
	 * @.post <code>(RESULT && onSiirretty()) || !RESULT</code>
	 */
	public boolean onLaillinen(Shakkipeli peli, char vaakaAlku, int pystyAlku,
												char vaakaLoppu, int pystyLoppu) {
		// kohderuudussa ei saa olla oma nappula
		if (peli.onMiehitetty(vaakaLoppu, pystyLoppu)) {
			if (peli.annaNappula(vaakaLoppu, pystyLoppu).onValkoinen() == onValkoinen()) {
				return false;
			}
		}
		if (Math.abs(vaakaAlku - vaakaLoppu) <= 1 && Math.abs(pystyAlku - pystyLoppu) <= 1) {
			ensimmainenSiirto = false;
			return true;
		}
		/* Linnoitusta varten tarkastetaan, että myöskään tornia ei ole vielä siiretty
		   ja että välissä ei ole nappuloita. onSiirretty() ominaisuuden vaatiminen
		   estää IndexOutOfBoundsException lentämisen, mutta vain jos ominaisuutta
		   on käyttetty oikein ja shakin sääntöjen mukaan. */
		if (pystyAlku == pystyLoppu && Math.abs(vaakaLoppu - vaakaAlku) == 2 && !onSiirretty()) {
			int kumpiPuoli = 0; // siirretäänkö oikean vai vasemman tornin suuntaan?
			if (vaakaAlku < vaakaLoppu && !peli.onMiehitetty((char)(vaakaLoppu - 1), pystyLoppu)) kumpiPuoli = 1;
			else if (vaakaAlku > vaakaLoppu && !peli.onMiehitetty((char)(vaakaLoppu - 1), pystyLoppu)
					&& !peli.onMiehitetty((char)(vaakaLoppu + 1), pystyLoppu)) kumpiPuoli = -2;
			else return false;
			if (peli.onMiehitetty((char)(vaakaLoppu + kumpiPuoli), pystyLoppu)) {
				if (peli.annaNappula((char)(vaakaLoppu + kumpiPuoli), pystyLoppu).getClass().equals(Torni.class)) {
					if (!peli.annaNappula((char)(vaakaLoppu + kumpiPuoli), pystyLoppu).onSiirretty()) {
						ensimmainenSiirto = false;
						return true;
					}
				}
			}			
		}
		return false;
	}
	
	/**
	 * Vertaa tätä <code>Kuningas</code>:ta toiseen olioon.
	 * Palauttaa <code>true</code>, jos toinen
	 * on samanvärinen <code>Kuningas</code>
	 * -nappula, tätä ja toista on tai ei ole
	 * siirretty meneillää olevassa pelissä ja
	 * molemmilla on sama merkkiesitys.
	 *
	 * @param o tähän verrattava olio
	 * @return <code>true</code>, jos tämä <code>Kuningas</code> on samanlainen kuin verrukki
	 */
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o instanceof Kuningas) {
			return super.equals(o);
		}
		return false;
	}
}