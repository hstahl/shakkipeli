package fi.utu.harjoitukset.shakkipeli;
/** 
 * Kuningatar- shakkinappula
 */
public class Kuningatar extends AbstraktiNappula {
	
	/**
	 * Uutta kuningatarta luodessa kutsuttava konstruktori
	 *
	 * @param onValkoinen Halutaanko luoda valkoinen nappula?
	 * @.post <code>this.onValkoinen() == onValkoinen</code>
	 */
	public Kuningatar(boolean onValkoinen) {
		super(MerkkiesitysNappulalle.KUNINGATAR, onValkoinen);
	}

	/**
	 * Kuningatar liikkuu kuin yhdistetty torni ja l�hetti,
	 * eli viistosuuntiin tai suoraan pysty- tai vaakasuunnassa.
	 *
	 * @return <code>true</code>, jos ehdotettu siirto on sallittu kuningattarelle
	 * @param peli <code>Shakkipeli</code>, jossa siirto halutaan tehd�
	 * @param vaakaAlku l�ht�kohdan vaakakoordinaatti [A-Ha-h]
	 * @param pystyAlku l�ht�kohdan pystykoordinaatti [1-8]
	 * @param vaakaLoppu maaliruudun vaakakoordinaatti [A-Ha-h]
	 * @param pystyLoppu maaliruudun pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; vaakaAlku == c) && (1 <= pystyAlku <= 8) && EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; vaakaLoppu == c) && (1 <= pystyLoppu <= 8)</code>
	 */
	public boolean onLaillinen(Shakkipeli peli, char vaakaAlku, int pystyAlku,
												char vaakaLoppu, int pystyLoppu) {
		// kohderuudussa ei saa olla oma nappula
		if (peli.onMiehitetty(vaakaLoppu, pystyLoppu)) {
			if (peli.annaNappula(vaakaLoppu, pystyLoppu).onValkoinen() == onValkoinen()) {
				return false;
			}
		}
		// pystysuunnassa liikkuminen
		if (vaakaAlku == vaakaLoppu && pystyAlku != pystyLoppu) {
			for (int pituus = Math.abs(pystyLoppu - pystyAlku) - 1; pituus > 0; pituus--) {
				// tarkastetaan, ett� kaikki paitsi viimeinen ruutu ovat tyhji�
				if (peli.onMiehitetty(vaakaAlku, (pystyAlku < pystyLoppu) ? pystyAlku + pituus : pystyAlku - pituus)) {
					return false;
				}
			}
			return true;
		}
		// vaakasuunnassa liikkuminen
		else if (pystyAlku == pystyLoppu && vaakaAlku != vaakaLoppu) {
			for (int pituus = Math.abs(vaakaLoppu - vaakaAlku) - 1; pituus > 0; pituus--) {
				// tarkastetaan, ett� kaiki paitsi viimeinen ruutu ovat tyhji�
				if (peli.onMiehitetty((vaakaAlku < vaakaLoppu) ? (char)(vaakaAlku + pituus) : (char)(vaakaAlku - pituus), pystyAlku)) {
					return false;
				}
				
			}
			return true;
		}
		// viistoon liikkuminen
		else if ((vaakaAlku - pystyAlku == vaakaLoppu - pystyLoppu) ||
				 (vaakaAlku + pystyAlku == vaakaLoppu + pystyLoppu)) {
			// tarkastetaan, onko matkalla nappuloita viimeist� ruutua lukuunottamatta
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
	 * Vertaa t�t� <code>Kuningatar</code>:ta toiseen olioon.
	 * Palauttaa <code>true</code>, jos toinen
	 * on samanv�rinen <code>Kuningatar</code>
	 * -nappula, t�t� ja toista on tai ei ole
	 * siirretty meneill�� olevassa peliss� ja
	 * molemmilla on sama merkkiesitys.
	 *
	 * @param o t�h�n verrattava olio
	 * @return <code>true</code>, jos t�m� <code>Kuningatar</code> on samanlainen kuin verrukki
	 */
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o instanceof Kuningatar) {
			return super.equals(o);
		}
		return false;
	}
}