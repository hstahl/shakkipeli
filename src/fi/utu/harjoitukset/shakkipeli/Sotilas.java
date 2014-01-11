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
	 * Konstruktori tiedostosta ladattua peliä varten, jossa
	 * sotilasta on saatettu jo siirtää
	 * 
	 * @param onValkoinen Halutaanko luoda valkoinen nappula?
	 * @param onSiirretty Onko nappulaa siirretty ladattavassa pelissä?
	 * @.post <code>this.onValkoinen() == onValkoinen && this.onSiirretty() == onSiirretty</code>
	 */
	public Sotilas(boolean  onValkoinen, boolean onSiirretty) {
		super(MerkkiesitysNappulalle.SOTILAS, onValkoinen);
		ensimmainenSiirto = !onSiirretty;
	}
	
	/**
	 * Sotilas liikkuu suoraan eteenpäin yhden ruudun, jos edessä ei ole vastustajan
	 * nappulaa, ja etuviistoon syödessä nappulan. Poikkeuksia on kaksi: ensimmäisen
	 * kerran siirrettäessä sotilasta voidaan siirtää kaksi ruutua eteen, jos
	 * kummassakaan ruudussa ei ole toista nappulaa, ja jos vastustajan sotilas
	 * liikkuu kaksi ruutua eteen tullen sotilaan viereen, se voidaan syödä nk.
	 * ohestalyönti -liikkeellä, jolloin tavalliseen tapaan sirrytään etuviistoon
	 * vastustajan sotilaan taakse ja poistetaan se pelilaudalta. Jos sotilas kulkee
	 * pelilaudan toiseen päähän saakka, se ylennetään upseeriksi (ratsu, lähetti,
	 * torni tai kuningatar), mutta tämä tarkistus tehdään <code>Shakkipeli</code> -luokassa.
	 *
	 * @return	<code>true</code>, jos ehdotettu siirto on sallittu sotilaalle
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
		boolean result = false;
		if (onValkoinen()) { // valkoisille nappuloille
			if (vaakaAlku == vaakaLoppu) {
				if (pystyLoppu - pystyAlku == 2) {
					// yritetään siirtää kaksi ruutua eteenpäin
					if (ensimmainenSiirto &&
						!(peli.onMiehitetty(vaakaLoppu, pystyLoppu - 1) ||
						peli.onMiehitetty(vaakaLoppu, pystyLoppu))) {
						result = true;
					}
				}
				else if (pystyLoppu - pystyAlku == 1) {
					// yritetään siirtää yksi ruutu eteenpäin
					if (!(peli.onMiehitetty(vaakaLoppu, pystyLoppu))) {
						result = true;
					}
				}
			}
			else if (Math.abs(vaakaLoppu - vaakaAlku) == 1 && pystyLoppu - pystyAlku == 1) {
				// yritetään syödä nappula
				if (peli.onMiehitetty(vaakaLoppu, pystyLoppu)) {
					if (!(peli.annaNappula(vaakaLoppu, pystyLoppu).onValkoinen())) {
						result = true;
					}
				}
				// jos loppuruutu ei ollut miehitetty, tarkastetaan vielä ohestalyönti
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
					// yritetään siirtää kaksi ruutua eteenpäin
					if (ensimmainenSiirto &&
						!(peli.onMiehitetty(vaakaLoppu, pystyLoppu + 1) ||
						peli.onMiehitetty(vaakaLoppu, pystyLoppu))) {
						result = true;
					}
				}
				else if (pystyLoppu - pystyAlku == -1) {
					// yritetään siirtää yksi ruutu eteenpäin
					if (!(peli.onMiehitetty(vaakaLoppu, pystyLoppu))) {
						result = true;
					}
				}
			}
			else if (Math.abs(vaakaLoppu - vaakaAlku) == 1 && pystyLoppu - pystyAlku == -1) {
				// yritetään syödä nappula
				if (peli.onMiehitetty(vaakaLoppu, pystyLoppu)) {
					if (peli.annaNappula(vaakaLoppu, pystyLoppu).onValkoinen()) {
						result = true;
					}
				}
				// jos loppuruutu ei ollut miehitetty, tarkastetaan vielä ohestalyönti
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
	 * Vertaa tätä <code>Sotilas</code>:ta toiseen olioon.
	 * Palauttaa <code>true</code>, jos toinen
	 * on samanvärinen <code>Sotilas</code>
	 * -nappula, tätä ja toista on tai ei ole
	 * siirretty meneillää olevassa pelissä ja
	 * molemmilla on sama merkkiesitys.
	 *
	 * @param o tähän verrattava olio
	 * @return <code>true</code>, jos tämä <code>Sotilas</code> on samanlainen kuin verrukki
	 */
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o instanceof Sotilas) {
			return super.equals(o);
		}
		return false;
	}
}