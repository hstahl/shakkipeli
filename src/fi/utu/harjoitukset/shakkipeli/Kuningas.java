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
	 * Konstruktori tiedostosta ladattua peli� varten, jossa
	 * kuningasta on saatettu jo siirt��
	 * 
	 * @param onValkoinen Tuleeko luodun nappulan olla valkoinen?
	 * @param onSiirretty Onko nappulaa siirretty ladattavassa peliss�?
	 * @.post <code>this.onValkoinen() == onValkoinen && this.onSiirretty() == onSiirretty</code>
	 */	
	public Kuningas(boolean onValkoinen, boolean onSiirretty) {
		super(MerkkiesitysNappulalle.KUNINGAS, onValkoinen);
		ensimmainenSiirto = !onSiirretty;
	}
	
	/**
	 * Kuningas siirtyy yhden ruudun kerrallaan mihin hyv�ns�
	 * suuntaan. Poikkeuksena on linnoitus, jossa siirret��n
	 * kaksi ruutua oikealle tai vasemmalle ja siirret��n
	 * valitun kulman torni kuninkaan ylitse. T�m� siirto on
	 * sallittu vain jos kuningasta ja tornia ei ole viel�
	 * siirretty peliss�, eik� tornin ja kuninkaan v�liss� ole
	 * muita nappuloita.
	 *
	 * @return <code>true</code>, jos ehdotettu siirto on sallittu kuninkaalle
	 * @param peli <code>Shakkipeli</code>, jossa siirto halutaan tehd�
	 * @param vaakaAlku l�ht�kohdan vaakakoordinaatti [A-Ha-h]
	 * @param pystyAlku l�ht�kohdan pystykoordinaatti [1-8]
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
		/* Linnoitusta varten tarkastetaan, ett� my�sk��n tornia ei ole viel� siiretty
		   ja ett� v�liss� ei ole nappuloita. onSiirretty() ominaisuuden vaatiminen
		   est�� IndexOutOfBoundsException lent�misen, mutta vain jos ominaisuutta
		   on k�yttetty oikein ja shakin s��nt�jen mukaan. */
		if (pystyAlku == pystyLoppu && Math.abs(vaakaLoppu - vaakaAlku) == 2 && !onSiirretty()) {
			int kumpiPuoli = 0; // siirret��nk� oikean vai vasemman tornin suuntaan?
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
	 * Vertaa t�t� <code>Kuningas</code>:ta toiseen olioon.
	 * Palauttaa <code>true</code>, jos toinen
	 * on samanv�rinen <code>Kuningas</code>
	 * -nappula, t�t� ja toista on tai ei ole
	 * siirretty meneill�� olevassa peliss� ja
	 * molemmilla on sama merkkiesitys.
	 *
	 * @param o t�h�n verrattava olio
	 * @return <code>true</code>, jos t�m� <code>Kuningas</code> on samanlainen kuin verrukki
	 */
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o instanceof Kuningas) {
			return super.equals(o);
		}
		return false;
	}
}