package fi.utu.harjoitukset.shakkipeli;
import java.lang.Math;
/**
 * Torni- shakkinappula
 */
public class Torni extends AbstraktiNappula {

	/**
	 * Uutta tornia luodessa kutsuttava konstruktori
	 *
	 * @param onValkoinen Halutaanko luoda valkoinen nappula?
	 * @.post <code>this.onValkoinen() == onValkoinen && this.onSiirretty() == false</code>
	 */	
	public Torni(boolean onValkoinen) {
		super(MerkkiesitysNappulalle.TORNI, onValkoinen);
	}
	
	/**
	 * Konstruktori tiedostosta ladattua peli‰ varten, jossa
	 * tornia on saatettu jo siirt‰‰
	 * 
	 * @param onValkoinen Halutaanko luoda valkoinen nappula?
	 * @param onSiirretty Onko nappulaa siirretty ladattavassa peliss‰?
	 * @.post <code>this.onValkoinen() == onValkoinen && this.onSiirretty() == onSiirretty</code>
	 */
	public Torni(boolean onValkoinen, boolean onSiirretty) {
		super(MerkkiesitysNappulalle.TORNI, onValkoinen);
		ensimmainenSiirto = !onSiirretty;
	}	
	
	/**
	 * Tornia voi siirt‰‰ eteen, taakse ja sivuille, mutta ei muiden
	 * nappuloiden ylitse. Poikkeuksena linnoitus, jossa kuningasta
	 * siirret‰‰n kaksi askelta oikealle tai vasemmalle, jonka
	 * j‰lkeen torni siirret‰‰n laidasta kuninkaan ylitse. T‰m‰
	 * siirto on sallittu vain, jos kuningasta ja tornia ei ole
	 * viel‰ siirretty pelin aikana, kuninkaan ja tornin v‰liss‰
	 * ei ole muita nappuloita, ja kuningas ei ole siirron j‰lkeen
	 * shakissa.
	 *
	 * @return <code>true</code>, jos ehdotettu siirto on sallittu tornille
	 * @param peli <code>Shakkipeli</code>, jossa siirto halutaan tehd‰
	 * @param vaakaAlku l‰htˆkohdan vaakakoordinaatti [A-Ha-h]
	 * @param pystyAlku l‰htˆkohdan pystykoordinaatti [1-8]
	 * @param vaakaLoppu maaliruudun vaakakoordinaatti [A-Ha-h]
	 * @param pystyLoppu maaliruudun pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; vaakaAlku == c) && (1 <= pystyAlku <= 8) && EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; vaakaLoppu == c) && (1 <= pystyLoppu <= 8)</code>
	 * @.post <code>(RESULT && onSiirretty()) || !RESULT</code>
	 */
	public boolean onLaillinen(Shakkipeli peli, char vaakaAlku, int pystyAlku,
												char vaakaLoppu, int pystyLoppu) {
		boolean result = false;
		if (peli.onMiehitetty(vaakaLoppu, pystyLoppu)) {
			// onko viimeisess‰ ruudussa oma nappula?
			if (peli.annaNappula(vaakaLoppu, pystyLoppu).onValkoinen() == onValkoinen()) {
				return false;
			}
		}
		if (vaakaAlku == vaakaLoppu && pystyAlku != pystyLoppu) {
			for (int pituus = Math.abs(pystyLoppu - pystyAlku) - 1; pituus > 0; pituus--) {
				// tarkastetaan, ett‰ kaikki paitsi viimeinen ruutu ovat tyhji‰
				if (peli.onMiehitetty(vaakaAlku, (pystyAlku < pystyLoppu) ? pystyAlku + pituus : pystyAlku - pituus)) {
					return false;
				}
			}
			result = true;
		}
		else if (pystyAlku == pystyLoppu && vaakaAlku != vaakaLoppu) {
			for (int pituus = Math.abs(vaakaLoppu - vaakaAlku) - 1; pituus > 0; pituus--) {
				// tarkastetaan, ett‰ kaiki paitsi viimeinen ruutu ovat tyhji‰
				if (peli.onMiehitetty((vaakaAlku < vaakaLoppu) ? (char)(vaakaAlku + pituus) : (char)(vaakaAlku - pituus), pystyAlku)) {
					return false;
				}
			}
			result = true;
		}
		if (result) ensimmainenSiirto = false;
		return result;
	}
	
	/**
	 * Vertaa t‰t‰ <code>Torni</code>:a toiseen olioon.
	 * Palauttaa <code>true</code>, jos toinen
	 * on samanv‰rinen <code>Torni</code>
	 * -nappula, t‰t‰ ja toista on tai ei ole
	 * siirretty meneill‰‰ olevassa peliss‰ ja
	 * molemmilla on sama merkkiesitys.
	 *
	 * @param o t‰h‰n verrattava olio
	 * @return <code>true</code>, jos t‰m‰ <code>Torni</code> on samanlainen kuin verrukki
	 */
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o instanceof Torni) {
			return super.equals(o);
		}
		return false;
	}
}