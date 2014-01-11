package fi.utu.harjoitukset.shakkipeli;
/**
 * Shakkipelin nappuloiden yhteinen rajapinta.
 * Oletettavasti tällä tulee olemaan kuusi
 * konkreettia perivää luokkaa, joista kukin
 * kuvaa yhdentyyppistä shakkinappulaa.
 * <p>
 * Nappulat ovat joko mustia tai valkoisia ja
 * niille on sallittu jotkin siirrot vain, jos
 * niitä ei ole vielä siirretty pelissä.
 * Näiden ominaisuuksien havainnointia varten
 * määritellään metodit tässä rajapinnassa.
 * Nappulan <code>toString()</code> -esityksen
 * on tarkoitus kertoa jollain tavalla,
 * yhdellä merkillä, sen tyyppi ja väri.
 * <code>onLaillinen()</code> kertoo onko
 * sille parametreillä ehdotettu siirto
 * sallittu shakin säännöissä. Kukin perivä
 * nappulatyyppi toteuttaa tämän metodin
 * omalla tavallaan.
 */
public interface Nappula {

	/**
	 * Literaaliluokka, jonka on tarkoitus
	 * määrittää eri nappulatyyppien
	 * merkkiesitys shakkilaudan
	 * konsolitulosteessa.
	 * <p>
	 * Huom. <code>Shakkipeli</code> -luokassa
	 * käytetään parametreinä useassa metodissa
	 * ja pelaajilta pyydetyssä syötteessä
	 * omia merkkiesityksiä, jotka eivät
	 * välttämättä ole samoja kuin tässä.
	 * Tämän literaaliluokan on tarkoitus
	 * määrittää vain konsolille piirrettävän
	 * pelilautanäkymän merkkiesitykset,
	 * eli nappuloiden <code>toString()</code>
	 * käyttäytyminen.
	 * <p>
	 * Koska ympäröivä rajapinta kuvaa
	 * shakkinappulan käyttäytymistä pelissä,
	 * oletettavasti erilaisia nappuloita on,
	 * väriä huomioimatta, tasan kuusi
	 * kappaletta ja niiden nimet ovat tämän
	 * literaaliluokan vakiot.
	 */
	public enum MerkkiesitysNappulalle {
		SOTILAS ("S", "s"),
		LAHETTI ("L", "l"),
		RATSU ("R", "r"),
		TORNI ("T", "t"),
		KUNINGATAR ("D", "d"),
		KUNINGAS ("K", "k");
		
		private final String VALK;
		private final String MUST;
		
		MerkkiesitysNappulalle(String VALK, String MUST) {
			this.VALK = VALK;
			this.MUST = MUST;
		}
		
		/**
		 * Antaa merkkiesityksen tämäntyyppiselle
		 * valkoiselle nappulalle.
		 * 
		 * @return merkkiesitys tällaiselle valkoiselle nappulalle
		 */
		public String valkoinen() {
			return VALK;
		}
		
		/**
		 * Antaa merkkiesityksen tämäntyyppiselle
		 * mustalle nappulalle.
		 *
		 * @return merkkiesitys tällaiselle mustalle nappulalle
		 */		
		public String musta() {
			return MUST;
		}
	}

	/**
	 * Kertoo, onko nappula valkoinen
	 *
	 * @return <code>true</code>, jos nappula on valkoisen pelaajan.
	 */
	public boolean onValkoinen();
	
	/** 
	 * Havannoi, onko nappulaa sirretty. Joitakin siirtoja
	 * voi tehdä vain ensimmäisellä kerralla siirrettäessä
	 * nappulaa.
	 * 
	 * @return <code>true</code>, jos nappulaa on sirretty
	 */
	public boolean onSiirretty();
	
	/**
	 * Totuusarvometodi nappulalle sallittujen siirtojen
	 * tarkastamista varten. Palauttaa <code>true</code>,
	 * jos shakin säännöt sallivat siirron tällä
	 * nappulalla.
	 *
	 * @return <code>true</code>, jos ehdotettu siirto on sallittu tälle nappulalle
	 * @param peli <code>Shakkipeli</code>, jossa siirto halutaan tehdä
	 * @param vaakaAlku lähtökohdan vaakakoordinaatti [A-Ha-h]
	 * @param pystyAlku lähtökohdan pystykoordinaatti [1-8]
	 * @param vaakaLoppu maaliruudun vaakakoordinaatti [A-Ha-h]
	 * @param pystyLoppu maaliruudun pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; vaakaAlku == c) && (1 <= pystyAlku <= 8) && EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; vaakaLoppu == c) && (1 <= pystyLoppu <= 8)</code>
	 */
	public boolean onLaillinen(Shakkipeli peli, char vaakaAlku, int pystyAlku,
												char vaakaLoppu, int pystyLoppu);
	
	/**
	 * Shakkinappulan merkkijonoesitys.
	 * <p>
	 * Esityksen tulee olla jokin <code>MerkkiesitysNappulalle</code>
	 * literaaliluokan vakioista siten, että nappulan
	 * <code>onValkoinen()</code> ollessa tosi, käytetään
	 * merkkiesitystä, jonka antaa <code>MerkkiesitysNappulalle</code>
	 * kutakin nappulatyyppiä vastaavan vakion
	 * <code>valkoinen()</code> -metodi, ja toisessa
	 * tapauksessa <code>musta()</code> -metodi.
	 *
	 * @return <code>Nappula</code>:n esitys String:nä
	 */
	public String toString();
}