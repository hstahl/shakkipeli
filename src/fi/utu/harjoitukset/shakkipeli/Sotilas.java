package fi.utu.harjoitukset.shakkipeli;
import java.lang.Math;
/**
 * Sotilas- shakkinappula
 */
public class Sotilas extends AbstraktiNappula {
	
	/**
	 * Uutta sotilasta luodessa kutsuttava konstruktori
	 *
	 * @param onValkoinen Halutaanko luoda valkoinen nappula?
	 * @.post <code>this.onValkoinen() == onValkoinen && this.onSiirretty() == false</code>
	 */
	public Sotilas(boolean onValkoinen) {
		super(MerkkiesitysNappulalle.SOTILAS, onValkoinen);
	}
	
	/**
	 * Konstruktori tiedostosta ladattua peli� varten, jossa
	 * sotilasta on saatettu jo siirt��
	 * 
	 * @param onValkoinen Halutaanko luoda valkoinen nappula?
	 * @param onSiirretty Onko nappulaa siirretty ladattavassa peliss�?
	 * @.post <code>this.onValkoinen() == onValkoinen && this.onSiirretty() == onSiirretty</code>
	 */
	public Sotilas(boolean  onValkoinen, boolean onSiirretty) {
		super(MerkkiesitysNappulalle.SOTILAS, onValkoinen);
		ensimmainenSiirto = !onSiirretty;
	}
	
	/**
	 * Sotilas liikkuu suoraan eteenp�in yhden ruudun, jos edess� ei ole vastustajan
	 * nappulaa, ja etuviistoon sy�dess� nappulan. Poikkeuksia on kaksi: ensimm�isen
	 * kerran siirrett�ess� sotilasta voidaan siirt�� kaksi ruutua eteen, jos
	 * kummassakaan ruudussa ei ole toista nappulaa, ja jos vastustajan sotilas
	 * liikkuu kaksi ruutua eteen tullen sotilaan viereen, se voidaan sy�d� nk.
	 * ohestaly�nti -liikkeell�, jolloin tavalliseen tapaan sirryt��n etuviistoon
	 * vastustajan sotilaan taakse ja poistetaan se pelilaudalta. Jos sotilas kulkee
	 * pelilaudan toiseen p��h�n saakka, se ylennet��n upseeriksi (ratsu, l�hetti,
	 * torni tai kuningatar), mutta t�m� tarkistus tehd��n <code>Shakkipeli</code> -luokassa.
	 *
	 * @return	<code>true</code>, jos ehdotettu siirto on sallittu sotilaalle
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
		boolean result = false;
		if (onValkoinen()) { // valkoisille nappuloille
			if (vaakaAlku == vaakaLoppu) {
				if (pystyLoppu - pystyAlku == 2) {
					// yritet��n siirt�� kaksi ruutua eteenp�in
					if (ensimmainenSiirto &&
						!(peli.onMiehitetty(vaakaLoppu, pystyLoppu - 1) ||
						peli.onMiehitetty(vaakaLoppu, pystyLoppu))) {
						result = true;
					}
				}
				else if (pystyLoppu - pystyAlku == 1) {
					// yritet��n siirt�� yksi ruutu eteenp�in
					if (!(peli.onMiehitetty(vaakaLoppu, pystyLoppu))) {
						result = true;
					}
				}
			}
			else if (Math.abs(vaakaLoppu - vaakaAlku) == 1 && pystyLoppu - pystyAlku == 1) {
				// yritet��n sy�d� nappula
				if (peli.onMiehitetty(vaakaLoppu, pystyLoppu)) {
					if (!(peli.annaNappula(vaakaLoppu, pystyLoppu).onValkoinen())) {
						result = true;
					}
				}
				// jos loppuruutu ei ollut miehitetty, tarkastetaan viel� ohestaly�nti
				else if (peli.annaOhestalyotava() != null) {
					if (peli.annaOhestalyotava().annaEka() == vaakaLoppu &&
						peli.annaOhestalyotava().annaToka() == pystyAlku) {
						result = true;
					}
				}
			}
		}
		else { // mustille nappuloille
			if (vaakaAlku == vaakaLoppu) {
				if (pystyLoppu - pystyAlku == -2) {
					// yritet��n siirt�� kaksi ruutua eteenp�in
					if (ensimmainenSiirto &&
						!(peli.onMiehitetty(vaakaLoppu, pystyLoppu + 1) ||
						peli.onMiehitetty(vaakaLoppu, pystyLoppu))) {
						result = true;
					}
				}
				else if (pystyLoppu - pystyAlku == -1) {
					// yritet��n siirt�� yksi ruutu eteenp�in
					if (!(peli.onMiehitetty(vaakaLoppu, pystyLoppu))) {
						result = true;
					}
				}
			}
			else if (Math.abs(vaakaLoppu - vaakaAlku) == 1 && pystyLoppu - pystyAlku == -1) {
				// yritet��n sy�d� nappula
				if (peli.onMiehitetty(vaakaLoppu, pystyLoppu)) {
					if (peli.annaNappula(vaakaLoppu, pystyLoppu).onValkoinen()) {
						result = true;
					}
				}
				// jos loppuruutu ei ollut miehitetty, tarkastetaan viel� ohestaly�nti
				else if (peli.annaOhestalyotava() != null) {
					if (peli.annaOhestalyotava().annaEka() == vaakaLoppu &&
						peli.annaOhestalyotava().annaToka() == pystyAlku) {
						result = true;
					}
				}
			}
		}
		if (result) ensimmainenSiirto = false;
		return result;
	}
	
	/**
	 * Vertaa t�t� <code>Sotilas</code>:ta toiseen olioon.
	 * Palauttaa <code>true</code>, jos toinen
	 * on samanv�rinen <code>Sotilas</code>
	 * -nappula, t�t� ja toista on tai ei ole
	 * siirretty meneill�� olevassa peliss� ja
	 * molemmilla on sama merkkiesitys.
	 *
	 * @param o t�h�n verrattava olio
	 * @return <code>true</code>, jos t�m� <code>Sotilas</code> on samanlainen kuin verrukki
	 */
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o instanceof Sotilas) {
			return super.equals(o);
		}
		return false;
	}
}