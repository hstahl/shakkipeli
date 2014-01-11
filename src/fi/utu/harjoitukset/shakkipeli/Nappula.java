package fi.utu.harjoitukset.shakkipeli;
/**
 * Shakkipelin nappuloiden yhteinen rajapinta.
 * Oletettavasti t�ll� tulee olemaan kuusi
 * konkreettia periv�� luokkaa, joista kukin
 * kuvaa yhdentyyppist� shakkinappulaa.
 * <p>
 * Nappulat ovat joko mustia tai valkoisia ja
 * niille on sallittu jotkin siirrot vain, jos
 * niit� ei ole viel� siirretty peliss�.
 * N�iden ominaisuuksien havainnointia varten
 * m��ritell��n metodit t�ss� rajapinnassa.
 * Nappulan <code>toString()</code> -esityksen
 * on tarkoitus kertoa jollain tavalla,
 * yhdell� merkill�, sen tyyppi ja v�ri.
 * <code>onLaillinen()</code> kertoo onko
 * sille parametreill� ehdotettu siirto
 * sallittu shakin s��nn�iss�. Kukin periv�
 * nappulatyyppi toteuttaa t�m�n metodin
 * omalla tavallaan.
 */
public interface Nappula {

	/**
	 * Literaaliluokka, jonka on tarkoitus
	 * m��ritt�� eri nappulatyyppien
	 * merkkiesitys shakkilaudan
	 * konsolitulosteessa.
	 * <p>
	 * Huom. <code>Shakkipeli</code> -luokassa
	 * k�ytet��n parametrein� useassa metodissa
	 * ja pelaajilta pyydetyss� sy�tteess�
	 * omia merkkiesityksi�, jotka eiv�t
	 * v�ltt�m�tt� ole samoja kuin t�ss�.
	 * T�m�n literaaliluokan on tarkoitus
	 * m��ritt�� vain konsolille piirrett�v�n
	 * pelilautan�kym�n merkkiesitykset,
	 * eli nappuloiden <code>toString()</code>
	 * k�ytt�ytyminen.
	 * <p>
	 * Koska ymp�r�iv� rajapinta kuvaa
	 * shakkinappulan k�ytt�ytymist� peliss�,
	 * oletettavasti erilaisia nappuloita on,
	 * v�ri� huomioimatta, tasan kuusi
	 * kappaletta ja niiden nimet ovat t�m�n
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
		 * Antaa merkkiesityksen t�m�ntyyppiselle
		 * valkoiselle nappulalle.
		 * 
		 * @return merkkiesitys t�llaiselle valkoiselle nappulalle
		 */
		public String valkoinen() {
			return VALK;
		}
		
		/**
		 * Antaa merkkiesityksen t�m�ntyyppiselle
		 * mustalle nappulalle.
		 *
		 * @return merkkiesitys t�llaiselle mustalle nappulalle
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
	 * voi tehd� vain ensimm�isell� kerralla siirrett�ess�
	 * nappulaa.
	 * 
	 * @return <code>true</code>, jos nappulaa on sirretty
	 */
	public boolean onSiirretty();
	
	/**
	 * Totuusarvometodi nappulalle sallittujen siirtojen
	 * tarkastamista varten. Palauttaa <code>true</code>,
	 * jos shakin s��nn�t sallivat siirron t�ll�
	 * nappulalla.
	 *
	 * @return <code>true</code>, jos ehdotettu siirto on sallittu t�lle nappulalle
	 * @param peli <code>Shakkipeli</code>, jossa siirto halutaan tehd�
	 * @param vaakaAlku l�ht�kohdan vaakakoordinaatti [A-Ha-h]
	 * @param pystyAlku l�ht�kohdan pystykoordinaatti [1-8]
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
	 * literaaliluokan vakioista siten, ett� nappulan
	 * <code>onValkoinen()</code> ollessa tosi, k�ytet��n
	 * merkkiesityst�, jonka antaa <code>MerkkiesitysNappulalle</code>
	 * kutakin nappulatyyppi� vastaavan vakion
	 * <code>valkoinen()</code> -metodi, ja toisessa
	 * tapauksessa <code>musta()</code> -metodi.
	 *
	 * @return <code>Nappula</code>:n esitys String:n�
	 */
	public String toString();
}