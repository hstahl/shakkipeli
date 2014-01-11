package fi.utu.harjoitukset.shakkipeli;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.io.*;
import java.nio.ByteBuffer;
/**
 * Konsolissa pelattava shakkipeli, joka ei tied�
 * shakin s��nn�ist� muuta, kuin nappuloiden
 * sallitut liikkeet. Teko�lyvastustajaa ei ole.
 * <p>
 * Shakkipeli koostuu pelilaudasta, joka on 8x8
 * ruudukko, ja mustista ja valkoisista pelinappuloista.
 * Pelilaudan ruudut numeroidaan kirjain- ja
 * numeroyhdistelm�ll� siten, ett�, pelaajien
 * kuvakulmasta, sarakkeet ovat nimetty A-H ja rivit 1-8.
 * <p>
 * Pelaajat eiv�t voi tehd� laittomia siirtoja
 * lukuunottamatta pelin p��ttymistilanteita, joissa
 * kuningas on uhattuna. Siisp� pelin p��ttyminen
 * ja kuninkaan siirt�minen vaaratilanteeseen ovat
 * pelaajien itsens� vastuulla. Shakin s��nn�t
 * l�ytyv�t vaikkapa
 * <a href=http://fi.wikipedia.org/wiki/Shakki>wikipediasta</a>.
 * <p>
 * Shakkia pelataan komentorivill� vuorotellen
 * sy�tt�en koordinaatteja ensin valiten nappula,
 * jota halutaan siirt�� ja sitten kohderuutu. Peli
 * kertoo, jos siirto onnistui. Jos siirto oli laiton,
 * peli kehottaa yritt�m��n uudestaan.
 * <p>
 * Peliss� on my�s kello, joka n�ytt�� kummankin
 * pelaajan siirtoihin k�ytt�m�n ajan aina vuoron
 * p��tytty�.
 * <p>
 * <h5>K�ytt�</h5>
 * Shakkipeli� pelataan komentorivilt�. Aja t�m�n
 * luokan <code>main()</code> -metodi JVM:ll� komennolla
 * <code>java fi.utu.harjoitukset.shakkipeli.Shakkipeli</code>
 * projektin juurikansiosta.
 * Ohjelma tulostaa ruudulle k�ytt�ohjeita ajettaessa.
 * <p>
 * <h5>Pelin kulku</h5>
 * Shakin pelaaminen alkaa, kun k�ytt�j� sy�tt�� komennon
 * uuden pelin aloittamiseksi tai pelin lataamiseksi
 * tiedostosta. Pelilauta tulostuu n�kyviin ja pelaajat
 * voivat alkaa sy�tt�� koordinaatteja siirtoja varten.
 * Koordinaatit sy�tet��n yksi koordinaattipari kerrallaan
 * v�lil�ynnill� erotettuna. Ensin valitaan siirrett�v�
 * nappula, jonka onnistuessa ohjelma viestitt�� konsoliin.
 * Seuraavaksi valitaan kohderuutu. Jos siirto onnistui,
 * vuoro vaihtuu vastustajalle. Muussa tapauksessa, peli
 * viestii miksi siirto ei onnistunut ja pelaajan tulee
 * aloittaa siirt�minen uudestaan nappulan valitsemisesta.
 * <p>
 * Peli p��ttyy, kun pelaajat itse huomaavat jomman kumman
 * kuninkaan olevan pelastamattomassa pinteess�. Shakkipeli
 * ei itse viestit� tai keskeyt� peli� shakkimatin sattuessa,
 * eik� my�sk��n est� pelaajia siirt�m�st� kuningasta shakkiin,
 * mik� on s��nn�iss� kielletty.
 */
public class Shakkipeli {

	/** Shakkipelin lopettava muuttuja */
	private boolean lopeta;
	/** Onko pelilauta valmiina? */
	private boolean peliAlkanut;
	/** Kertoo kumman pelaajan vuoro */
	private boolean valkoisenVuoro;
	/** Pelin logiikassa k�ytett�v� */
	private boolean pelaaVuoro;
	/** Yksi sotilas kerrallaan voi olla haavoittuvainen
	 * ohestaly�nti siirrolle ja vain yhden vuoron ajan. */
	private Koordinaatit<Character,Integer> ohestalyotava;
	
	/** valkoisen pelaajan ajanottoa varten */
	private long valkoisenKello;
	/** mustan pelaajan ajanottoa varten */
	private long mustanKello;
	/** Vuorojen keston laskemista varten otetaan v�liaika vuoron vaihtuessa. */
	private long valiaika;
	
	/** Kehote pyydett�ess� sy�tett� */
	private static final String KEHOTE = "> ";
	
	/**	Pelilauta */
	private Pelilauta<Nappula> pelilauta;
	
	/** sy�tyj� nappuloita varten */
	private List<Nappula> vainajat;
	
	/**
	 * Shakkipelin main metodi luo ajettaessa Shakkipeli
	 * -olion ja k�ynnist�� pelin.
	 *
	 * @param args komentoriviargumentti ei k�yt�ss�
	 */
	public static void main(String[] args) {
		Shakkipeli p = new Shakkipeli();
		p.aloita();
	}
	
	/**
	 * Shakkipeliolio luodaan ennen pelaamista.
	 */
	public Shakkipeli() {
		lopeta = false;
		peliAlkanut = false;
		valkoisenVuoro = true;
		pelaaVuoro = false;
		ohestalyotava = null;
		valkoisenKello = 0;
		mustanKello = 0;
		valiaika = 0;
		pelilauta = new Pelilauta<Nappula>();
		vainajat = new LinkedList<Nappula>();
	}
	
	/**
	 * Shakkipelin p��lohko, josta l�ytyy pelin logiikka.
	 * T�t� kutsutaan, kun halutaan aloittaa peli.
	 */
	public void aloita() {
		// siirtoja varten k�ytet��n Koordinaatit olion sis�lt�mi� lukupareja
		Koordinaatit<Character,Integer> koord = new Koordinaatit<Character,Integer>(new Character(' '),new Integer(-1));		
		// Siirtoja varten. Jos jotain on pieless�, pit�isi alkaa ropista IndexOutOfBoundsException:eja.
		char vaakaAlku = ' ', vaakaLoppu = ' ';
		int pystyAlku = -1, pystyLoppu = -1;
		/* Siirtojen koordinaatit annetaan kahdessa askeleessa */ 
		int askel = 0;
		
		System.out.print("Kirjoita konsoliin \"chcp 1257\" saadaksesi ��kk�set n�kyviin\n");
		System.out.print("\n");
		System.out.print("-----SHAKKIPELI-----\n");
		System.out.print("\n");
		System.out.print("KOMENNOT:\n");
		System.out.print("Aloita uusi peli: uusi\n");
		System.out.print("Lataa tallennettu peli tiedostosta: load <tiedosto>\n");
		System.out.print("Tallenna nykyinen peli tiedostoon: save <tiedosto>\n");
		System.out.print("Kuinka pelata: ohje\n");
		System.out.print("Sulje peli: quit\n");

		// main loop
		while (!lopeta) {
			String komento = pyydaSyote();
			try { parsiKomento(komento); } // kokeillaan onko sy�te komento
			catch (TunnistamatonKomentoPoikkeus e) { // jos ei...
				try { // kokeillaan onko sy�te koordinaatit
					koord = parsiKoordinaatit(komento);
					pelaaVuoro = true;
				}
				catch (TunnistamatonKomentoPoikkeus f) {
					System.out.println(e); continue; // ei komento eik� koordinaatti
				}
			} // t�st� eteenp�in vain, jos komento tunnistettiin
			
			if (pelaaVuoro && !peliAlkanut) {
				System.out.println("Aloita uusi peli tai lataa tiedostosta ennen kuin sy�t�t koordinaatteja!");
				pelaaVuoro = false;
			}
			
			// Varsinaisen shakkipelin pelivuoro
			if (pelaaVuoro && peliAlkanut) {
				askel++;
				
				// Valitaan siirrett�v� nappula
				if (askel == 1) {
					vaakaAlku = koord.annaEka().charValue();
					pystyAlku = koord.annaToka().intValue();
					if (onMiehitetty(vaakaAlku,pystyAlku)) {
						if (annaNappula(vaakaAlku,pystyAlku).onValkoinen() == valkoisenVuoro) {
							System.out.println("Nappula valittu!");
						}
						else {
							System.out.println("Et voi siirt�� vastustajan nappulaa!");
							askel = 0;
						}
					}
					else {
						System.out.println("Ruudussa ei ole nappulaa!");
						askel = 0;
					}
				}
				
				// Valitaan mihin ruutuun nappulaa siirret��n
				else if (askel == 2) {
					vaakaLoppu = koord.annaEka().charValue();
					pystyLoppu = koord.annaToka().intValue();
					if (annaNappula(vaakaAlku,pystyAlku).onLaillinen(this, vaakaAlku, pystyAlku, vaakaLoppu, pystyLoppu)) {
						if (onMiehitetty(vaakaLoppu,pystyLoppu)) {
							syoNappula(vaakaLoppu,pystyLoppu);
						}
						siirraNappula(vaakaAlku, pystyAlku, vaakaLoppu, pystyLoppu);
						
						// sotilasnappuloille on v�h�n poikkeuksia
						if (annaNappula(vaakaLoppu, pystyLoppu).getClass().equals(Sotilas.class)) {
							// Kun sotilas p��see pelilaudan p��tyyn, se ylennet��n
							if (pystyLoppu == 1 || pystyLoppu == 8) {
								System.out.println("Sotilas ylennet��n. " +
												   "Valitse haluamasi upseeriarvo: l, r, t tai d " +
												   "(l�hetti, ratsu, torni tai kuningatar).");
								boolean onnistui = false;
								while (!onnistui) {
									try {
										komento = pyydaSyote();
										ylennaSotilas(vaakaLoppu, pystyLoppu, komento);
										onnistui = true;
									}
									catch (TunnistamatonKomentoPoikkeus e) {
										System.out.println("Virheellinen sy�te! Kokeile uudestaan!");
									}
								}
								System.out.println("Ylennys onnistui!");
							}
							// jos liikuttiin kaksi ruutua sotilaalla, merkit��n ohestaly�t�v�ksi
							else if (Math.abs(pystyLoppu - pystyAlku) == 2) {
								ohestalyotava = new Koordinaatit<Character,Integer>(new Character(vaakaLoppu), new Integer(pystyLoppu));
							}
							// Sy�d��n ohestal�ynnille haavoittuvainen nappula
							else if (annaOhestalyotava() != null) {
								if (pystyAlku == annaOhestalyotava().annaToka() &&
									vaakaLoppu == annaOhestalyotava().annaEka()) {
									syoNappula(vaakaLoppu, pystyAlku);
								}
								ohestalyotava = null;
							}
						}
						// jos ei liikutettu sotilasta, yksik��n nappula ei ole en�� ohestaly�t�v�
						else ohestalyotava = null;
						
						// Jos siirto oli linnoitus, siirret��n my�s tornia
						if (annaNappula(vaakaLoppu, pystyLoppu).getClass().equals(Kuningas.class)) {						
							if (pystyAlku == pystyLoppu && Math.abs(vaakaLoppu - vaakaAlku) == 2) {
								int kumpiPuoli = 0;
								if (vaakaAlku < vaakaLoppu) kumpiPuoli = 1;
								else if (vaakaAlku > vaakaLoppu) kumpiPuoli = -2;
								if (onMiehitetty((char)(vaakaLoppu + kumpiPuoli), pystyLoppu) && kumpiPuoli != 0) {
									if (annaNappula((char)(vaakaLoppu + kumpiPuoli), pystyLoppu).getClass().equals(Torni.class)) {
										if (!annaNappula((char)(vaakaLoppu + kumpiPuoli), pystyLoppu).onSiirretty()) {
											siirraNappula((char)(vaakaLoppu + kumpiPuoli), pystyLoppu, (kumpiPuoli > 0) ? (char)(vaakaLoppu - 1): (char)(vaakaLoppu + 1), pystyAlku);
										}
										else System.err.println("Tornia on jo siirretty. Virhe Kuningas luokassa.");
									}
									else System.err.println("Kulmassa oleva nappula ei ole Torni. Virhe Kuningas luokassa.");
								}
								else System.err.println("Kulmassa ei ole nappulaa. Virhe Kuningas luokassa.");
							}
						}					
						askel = 0;
						System.out.println("Siirto onnistui!");
						valkoisenVuoro = valkoisenVuoro ? false : true;
						System.out.println(this); // siirron onnistuttua pit�� p�ivitt�� n�kym�
					}
					else {
						System.out.println("Siirto ei sallittu! Yrit� uudestaan!");
						askel = 0;
					}
				}
				else { // t�nne ei jouduta
					System.err.println("Fatal Error! Askeleen arvo ei sallittu: " + askel);
					System.exit(-1);
				}
				
				// T�m� vuoro on ohi. Seuraavan kerran t�h�n lohkoon
				// tullaan, kun sy�tteeksi saadaan koordinaatit.
				pelaaVuoro = false;
				
				// lopuksi p�ivitet��n vuorossa olleen kello
				if (valkoisenVuoro) valkoisenKello += System.currentTimeMillis() - annaAika();
				else mustanKello += System.currentTimeMillis() - annaAika();
				// aloitetaan seuraavan vuoron ajanotto
				otaAika();
			}	
		}
	}
	
	/**
	 * T�t� metodia kutsutaan, kun halutaan tiet��, onko
	 * pelilaudan koordinaateissa nappula.
	 *
	 * @see #annaNappula(char,int)
	 * @return <code>true</code>, jos ruudussa on Nappula
	 * @param vaaka kysytt�v�n ruudun vaakakoordinaatti [A-Ha-h]
	 * @param pysty kysytt�v�n ruudun pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; c == Character.toUpperCase(vaaka)) && (1 <= pysty <= 8)</code>
	 */
	public boolean onMiehitetty(char vaaka, int pysty) {
		return pelilauta.annaNappula(vaaka, pysty) != null;
	}
	
	/**
	 * T�m� metodi antaa viittauksen pelilaudan
	 * koordinaateissa olevaan nappulaan.
	 *
	 * @see #onMiehitetty(char,int)
	 * @return viittaus ruudussa olevaan <code>Nappula</code> -olioon
	 * @param vaaka kysytt�v�n ruudun vaakakoordinaatti [A-Ha-h]
	 * @param pysty kysytt�v�n ruudun pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; c == Character.toUpperCase(vaaka)) && (1 <= pysty <= 8) && onMiehitetty(vaaka,pysty)</code>
	 */
	public Nappula annaNappula(char vaaka, int pysty) {
		return pelilauta.annaNappula(vaaka, pysty);
	}
	
	/**
	 * Palauttaa ohestalyonnille haavoittuvaisen nappulan koordinaatit. Ohestaly�nti
	 * voidaan tehd� vain <code>Sotilas</code> -nappulalle, jota vastustaja siirsi edellisell�
	 * vuorolla kaksi ruutua eteenp�in aloitusruudustaan.
	 *
	 * @return <code>Koordinaatit</code> olio, joka osoittaa oheistalyotavan sotilaan. Jos sellaista ei ole, palautetaan <code>null</code>.
	 */
	public Koordinaatit<Character,Integer> annaOhestalyotava() {
		return ohestalyotava;
	}
	
	/**
	 * T�m� tallentaa v�liajan siirtovuoron vaihtuessa
	 * pelaajien ajanottoa varten.
	 *
	 * @see #annaAika()
	 */
	private void otaAika() {
		valiaika = System.currentTimeMillis();
	}
	
	/**
	 * T�m� antaa tallennetun v�liajan.
	 *
	 * @see #otaAika()
	 * @return talletettu v�liaika millisekunteina
	 */
	private long annaAika() {
		return valiaika;
	}
	
	/**
	 * Asettaa uuden nappulan pelilaudalle. Jos annetussa ruudussa
	 * on ennest��n nappula, vanha nappula korvautuu uudella.
	 *
	 * @see #asetaSiirrettyNappula(char,int,char,boolean)
	 * @param vaaka kohderuudun vaakakoordinaatti [A-Ha-h]
	 * @param pysty kohderuudun pystykoordinaatti [1-8]
	 * @param tyyppi Haluttu nappulan tyyppi. On oltava jokin seuraavista: s, l, r, t, d, k.
	 * @param onValkoinen Onko asetettava nappula valkoisen pelaajan?
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; c == Character.toUpperCase(vaaka)) && (1 <= pysty <= 8) && EXISTS(char c : new char[]{'s','l','r','t','d','k'}; tyyppi == c)</code>
	 * @.post <code>onMiehitetty(vaaka, pysty) == true</code>
	 */
	private void asetaNappula(char vaaka, int pysty, char tyyppi, boolean onValkoinen) {
		switch (tyyppi) {
			case 's':
				pelilauta.asetaNappula(new Sotilas(onValkoinen), vaaka, pysty);
				break;
			case 'l':
				pelilauta.asetaNappula(new Lahetti(onValkoinen), vaaka, pysty);
				break;
			case 'r':
				pelilauta.asetaNappula(new Ratsu(onValkoinen), vaaka, pysty);
				break;
			case 't':
				pelilauta.asetaNappula(new Torni(onValkoinen), vaaka, pysty);
				break;
			case 'd':
				pelilauta.asetaNappula(new Kuningatar(onValkoinen), vaaka, pysty);
				break;
			case 'k':
				pelilauta.asetaNappula(new Kuningas(onValkoinen), vaaka, pysty);
				break;
			default:
		}
	}
	
	/**
	 * Asettaa uuden siirretyn nappulan pelilaudalle.
	 * Jos annetussa ruudussa on ennest��n nappula,
	 * vanha nappula korvautuu uudella. K�ytet��n
	 * ladatessa peli� tiedostosta.
	 *
	 * @see #load(String)
	 * @see #asetaNappula(char,int,char,boolean)
	 * @param vaaka kohderuudun vaakakoordinaatti [A-Ha-h]
	 * @param pysty kohderuudun pystykoordinaatti [1-8]
	 * @param tyyppi Haluttu nappulan tyyppi. On oltava jokin seuraavista: s, t, k.
	 * @param onValkoinen Onko asetettava nappula valkoisen pelaajan?
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; c == Character.toUpperCase(vaaka)) && (1 <= pysty <= 8) && EXISTS(char c : new char[]{'s','t','k'}; tyyppi == c)</code>
	 * @.post <code>onMiehitetty(vaaka, pysty) && annaNappula(vaaka, pysty).onSiirretty()</code>
	 */	
	private void asetaSiirrettyNappula(char vaaka, int pysty, char tyyppi, boolean onValkoinen) {
		switch (tyyppi) {
			case 's':
				pelilauta.asetaNappula(new Sotilas(onValkoinen, true), vaaka, pysty);
				break;
			case 't':
				pelilauta.asetaNappula(new Torni(onValkoinen, true), vaaka, pysty);
				break;
			case 'k':
				pelilauta.asetaNappula(new Kuningas(onValkoinen, true), vaaka, pysty);
				break;
			default:
		}	
	}
	
	/**
	 * Poistaa koordinaateissa olevan nappulan pelilaudalta
	 * ja siirt�� sen sy�tyjen nappuloiden listaan.
	 * 
	 * @param vaaka sy�t�v�n nappulan vaakakoordinaatti [A-Ha-h]
	 * @param pysty sy�t�v�n nappulan pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; c == Character.toUpperCase(vaaka)) && (1 <= pysty <= 8) && onMiehitetty(vaaka, pysty)</code>
	 * @.post <code>onMiehitetty(vaaka, pysty) == false && vainajat.contains(OLD.annaNappula(vaaka, pysty))</code>
	 */
	private void syoNappula(char vaaka, int pysty) {
		vainajat.add(pelilauta.annaNappula(vaaka, pysty));
		pelilauta.poistaNappula(vaaka, pysty);
	}
	
	/**
	 * Siirt�� nappulan alkuruudusta kohderuutuun. Sy�dess�
	 * vastustajan nappulaa on tarkoitus k�ytt�� syoNappula()
	 * -metodia ensin. Siirron laillisuus tulee tarkastaa
	 * ennen k�ytt�� pelin aikana.
	 *
	 * @see #syoNappula(char,int)
	 * @param vaakaAlku siirrett�v�n nappulan vaakakoordinaatti [A-Ha-h]
	 * @param pystyAlku siirrett�v�n nappulan pystykoordinaatti [1-8]
	 * @param vaakaLoppu kohderuudun vaakakoordinaatti [A-Ha-h]
	 * @param pystyLoppu kohderuudun pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; c == Character.toUpperCase(vaakaAlku)) && (1 <= pystyAlku <= 8) && EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; c == Character.toUpperCase(vaakaLoppu)) && (1 <= pystyLoppu <= 8) && onMiehitetty(vaakaAlku, pystyAlku)</code>
	 * @.post <code>OLD.annaNappula(vaakaAlku, pystyAlku) == annaNappula(vaakaLoppu, pystyLoppu) && onMiehitetty(vaakaAlku, pystyAlku) == false</code>
	 */
	private void siirraNappula(char vaakaAlku, int pystyAlku, char vaakaLoppu, int pystyLoppu) {
		pelilauta.asetaNappula(pelilauta.annaNappula(vaakaAlku, pystyAlku), vaakaLoppu, pystyLoppu);
		pelilauta.poistaNappula(vaakaAlku, pystyAlku);
	}

	/**
	 * Asettaa pelilaudalle nappulat uutta peli� varten
	 * aloituspaikoilleen ja resetoi muuttujat.
	 */
	private void alustaUusiPeli() {
		// tyhjennet��n pelilauta
		pelilauta = new Pelilauta<Nappula>();
		
		// aloitetaan valkoisista torneista
		asetaNappula('A', 1, 't', true);
		asetaNappula('H', 1, 't', true);
		// valkoiset ratsut
		asetaNappula('B', 1, 'r', true);
		asetaNappula('G', 1, 'r', true);
		// valkoiset l�hetit
		asetaNappula('C', 1, 'l', true);
		asetaNappula('F', 1, 'l', true);
		// valkoiset kuninkaalliset
		asetaNappula('D', 1, 'd', true);
		asetaNappula('E', 1, 'k', true);
		// valkoiset sotilaat
		asetaNappula('A', 2, 's', true);
		asetaNappula('B', 2, 's', true);
		asetaNappula('C', 2, 's', true);
		asetaNappula('D', 2, 's', true);
		asetaNappula('E', 2, 's', true);
		asetaNappula('F', 2, 's', true);
		asetaNappula('G', 2, 's', true);
		asetaNappula('H', 2, 's', true);
		
		// mustat tornit
		asetaNappula('A', 8, 't', false);
		asetaNappula('H', 8, 't', false);
		// mustat ratsut
		asetaNappula('B', 8, 'r', false);
		asetaNappula('G', 8, 'r', false);
		// mustat l�hetit
		asetaNappula('C', 8, 'l', false);
		asetaNappula('F', 8, 'l', false);
		// mustat kuninkaalliset
		asetaNappula('D', 8, 'd', false);
		asetaNappula('E', 8, 'k', false);
		// mustat sotilaat
		asetaNappula('A', 7, 's', false);
		asetaNappula('B', 7, 's', false);
		asetaNappula('C', 7, 's', false);
		asetaNappula('D', 7, 's', false);
		asetaNappula('E', 7, 's', false);
		asetaNappula('F', 7, 's', false);
		asetaNappula('G', 7, 's', false);
		asetaNappula('H', 7, 's', false);
		
		// valkoinen aloittaa
		valkoisenVuoro = true;
		// tyhjennet��n sy�tyjen lista
		vainajat = new LinkedList<Nappula>();
		// nollataan kellot
		valkoisenKello = 0;
		mustanKello = 0;
		// tulostetaan pelin�kym�
		System.out.println(this);
		peliAlkanut = true;
		// valkoisen vuoron ajanotto alkaa
		otaAika();
	}
	
	/**
	 * Kirjoittaa komentoriville kehotteen ja
	 * palauttaa k�ytt�j�lt� saadun sy�tteen.
	 *
	 * @return sy�temerkkijono
	 */
	private static String pyydaSyote() {
		System.out.print(KEHOTE);
		Scanner lukija = new Scanner(System.in);
		return lukija.nextLine();
	}
	
	/**
	 * Yritt�� lukea komentoparametrista oikein muotoillun,
	 * ohjelman tunnistaman komennon ja suorittaa sen.
	 * Hyv�ksytt�v�t komennot ovat: <code>exit, quit, uusi, save, load, ohje</code>
	 *
	 * @param komento parsittava komento
	 * @.pre <code>komento != null</code>
	 * @throws TunnistamatonKomentoPoikkeus jos <code>komento</code> oli v��rin muotoiltu
	 */
	private void parsiKomento(String komento) throws TunnistamatonKomentoPoikkeus {
		Scanner pilkkoja = new Scanner(komento);
		String komentosana = pilkkoja.next();
		switch (komentosana.toLowerCase()) {
			case "exit":
			case "quit":
				lopeta = true;
				break;
			case "uusi":
				alustaUusiPeli();
				break;
			case "save":
				if (pilkkoja.hasNext()) {
					if (peliAlkanut) {
						save(pilkkoja.next());
					}
					else throw new TunnistamatonKomentoPoikkeus("Et voi tallentaa ennen kuin peli on alkanut: " + komento);
				}
				else throw new TunnistamatonKomentoPoikkeus("Anna my�s tiedostonimi: " + komento);
				break;
			case "load":
				if (pilkkoja.hasNext()) {
					load(pilkkoja.next());
				}
				else throw new TunnistamatonKomentoPoikkeus("Anna my�s tiedostonimi: " + komento);
				break;
			case "ohje":
				System.out.println("Nappuloita siirret��n valitsemalla ensin nappula");
				System.out.println("pelilaudalta sy�tt�m�ll� vaakakoordinaattikirjain,");
				System.out.println("jonka j�lkeen v�lily�nti ja pystykoordinaatin numero.");
				System.out.println("Sen j�lkeen samalla tavalla sy�tet��n kohderuudun");
				System.out.println("vaaka- ja pystykoordinaatti. Jos siirto on sallittu,");
				System.out.println("valittu nappula siirtyy ja mahdollisesti sy�");
				System.out.println("vastustajan nappulan. Sitten vuoro vaihtuu.");
				System.out.println("Pelaajien itsens� t�ytyy huomata pelin p��ttyminen.\n");
				break;
			default:
				throw new TunnistamatonKomentoPoikkeus(komento);
		}
	}
	
	/**
	 * Yritt�� lukea komentomerkkijonosta oikein muotoillun
	 * koordinaattiparin ja palauttaa sen <code>Koordinaatit</code>
	 * -oliona, joka sis�lt�� koordinaattiparin. Ensimm�inen on
	 * vaaka- ja toinen pystykoordinaatti. Parametrin
	 * <code>komento</code> tulee olla muotoa "[A-Ha-h] [1-8]".
	 *
	 * @return luetut koordinaatit <code>Koordinaatit</code> -oliona
	 * @param komento parsittava komento
	 * @.pre <code>komento != null</code>
	 * @.post <code>(RESULT.annaEka().toString().matches("[A-Ha-h]")) && (1 <= RESULT.annaToka() <= 8)</code>
	 * @throws TunnistamatonKomentoPoikkeus jos komento on v��rin muotoiltu tai koordinaatit eiv�t ole validit
	 */
	private Koordinaatit<Character,Integer> parsiKoordinaatit(String komento) throws TunnistamatonKomentoPoikkeus {
		Character eka;
		Integer toka;
		Scanner lukija = new Scanner(komento);
		String luettu = "";
		if (lukija.hasNext()) luettu = lukija.next();
		else throw new TunnistamatonKomentoPoikkeus(komento);
		if (luettu.matches("[A-Ha-h]")) eka = new Character(luettu.charAt(0));
		else throw new TunnistamatonKomentoPoikkeus(komento);
		if (lukija.hasNext()) luettu = lukija.next();
		else throw new TunnistamatonKomentoPoikkeus(komento);
		try { toka = Integer.parseInt(luettu); }
		catch (NumberFormatException e) { throw new TunnistamatonKomentoPoikkeus(komento); }
		if (toka < 1 || toka > 8) throw new TunnistamatonKomentoPoikkeus(komento);
		return new Koordinaatit<Character,Integer>(eka,toka);
	}
	
	/**
	 * Muuttaa pelilaudan p��tyyn p��sseen sotilasnappulan
	 * <code>komento</code>- parametrin m��r��m�ksi upseeriksi.
	 *
	 * @param vaaka ylennett�v�n sotilasnappulan vaakakoordinaatti [A-Ha-h]
	 * @param pysty ylennett�v�n sotilasnappulan pystykoordinaatti [1-8]
	 * @param komento Halutun ylennetyn nappulan tyyppi. Tunnistettuja komentoja ovat l, r, t ja d.
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; c == Character.toUpperCase(vaaka)) && (1 <= pysty <= 8) && onMiehitetty(vaaka, pysty) && annaNappula(vaaka, pysty).getClass().equals(Sotilas.class) && EXISTS(s : new String[]{"l","r","t","d"}; komento.toLowerCase().equals(s))</code>
	 * @throws TunnistamatonKomentoPoikkeus jos komento on odottamaton tai <code>null</code>.
	 */
	private void ylennaSotilas(char vaaka, int pysty, String komento) throws TunnistamatonKomentoPoikkeus {
		switch (komento.toLowerCase().trim()) {
			case "l":
			case "r":
			case "d":
				asetaNappula(vaaka, pysty, komento.charAt(0), valkoisenVuoro);
				break;
			case "t":
				asetaSiirrettyNappula(vaaka, pysty, komento.charAt(0), valkoisenVuoro);
				break;
			default:
				throw new TunnistamatonKomentoPoikkeus(komento);
		}
	}
	
	/**
	 * Tallentaa pelitilanteen tiedostoon.
	 *
	 * @see #load(String)
	 * @param tiedostonimi haluttu tallennustiedoston nimi
	 * @.pre <code>tiedostonimi != null</code>
	 */
	private void save(String tiedostonimi) {
		try {
			FileOutputStream out = new FileOutputStream(new File(tiedostonimi));
			// aluksi kirjoitetaan peliruudun tilanne
			for (char vaaka : new char[]{'A','B','C','D','E','F','G','H'}) {
				for (int pysty = 1; pysty <= 8; pysty++) {
					/* jokaista nappulaa kohden kirjoitetaan yksi tavu,
					 * jossa eniten merkitsev� bitti merkkaa onValkoinen
					 * -ominaisuutta, eli kaikki 128:aa suuremmat arvot
					 * ovat valkoisia nappuloita.
					 * 1xxx xxxx valkoinen
					 * 0xxx xxxx musta
					 *
					 * Kolme v�hiten merkitsev��
					 * bitti� m��r��v�t nappulan tyypin seuraavasti:
					 * 001 sotilas
					 * 010 l�hetti
					 * 011 Ratsu
					 * 100 Torni
					 * 101 Kuningatar
					 * 110 Kuningas
					 *
					 * Nelj�s bitti merkitsee joidenkin nappuloiden
					 * onSiirretty() ominaisuutta.
					 * 1xxx onSiirretty()
					 * 0xxx !onSiirretty() 
					 */
					int val = 0;
					if (onMiehitetty(vaaka, pysty)) {
						if (annaNappula(vaaka, pysty).onValkoinen()) {
							val += 128;
						}
						switch (annaNappula(vaaka, pysty).getClass().getSimpleName()) {
							case "Sotilas":
								val += 1;
								break;
							case "Lahetti":
								val += 2;
								break;
							case "Ratsu":
								val += 3;
								break;
							case "Torni":
								val += 4;
								break;
							case "Kuningatar":
								val += 5;
								break;
							case "Kuningas":
								val += 6;
								break;
							default:
						}
						if (annaNappula(vaaka, pysty).onSiirretty()) val += 8;
					}
					// tyhj��n ruutuun kirjoitetaan nolla
					else val = 0;
					out.write(val);
				}
			}
			// k�ytet��n v�limerkkin� tavua 0xff
			out.write(0xff);
			// seuraavaksi sy�tyjen nappuloiden lista
			for (Nappula n : vainajat) {
				int val = 0;
				if (n.onValkoinen()) val += 128;
				switch (n.getClass().getSimpleName()) {
					case "Sotilas":
						val += 1;
						break;
					case "Lahetti":
						val += 2;
						break;
					case "Ratsu":
						val += 3;
						break;
					case "Torni":
						val += 4;
						break;
					case "Kuningatar":
						val += 5;
						break;
					case "Kuningas":
						val += 6;
						break;
					default:		
				}
				out.write(val);
			}
			out.write(0xff); // vainajien lista p��ttyi
			
			// seuraavaksi ohestaly�t�v�n koordinaatit
			if (ohestalyotava != null) {
				// ByteBuffer luokan array() palauttaa Byte taulukon, joka voidaan antaa sellaisenaan kirjoitettavaksi
				out.write(ByteBuffer.allocate(2).putChar(ohestalyotava.annaEka().charValue()).array());
				out.write(ByteBuffer.allocate(4).putInt(ohestalyotava.annaToka().intValue()).array());
				out.write(0xff);
			}
			else out.write(0xff); // k�ytet��n merkkaamaan ohestaly�nti� null:ksi
			
			// seuraavaksi pelaajien kellot
			out.write(ByteBuffer.allocate(8).putLong(valkoisenKello).array());
			out.write(ByteBuffer.allocate(8).putLong(mustanKello).array());
			
			// lopuksi viel� talletetaan kumman vuoroon j��tiin
			out.write(valkoisenVuoro ? 1 : 0);
			out.flush();
			out.close();
		}
		catch (FileNotFoundException fnfe) {
			System.err.println("Kohde on kansio tai muokkaaminen ei sallittu: " + fnfe);
		}
		catch (NullPointerException nulle) {
			// t�t� ei pit�isi tapahtua, koska tiedostonimen t�ytyisi olla null
			// ja t�m� tuottaisi poikkeuksen jo parsiKomento metodissa
			System.err.println("Et antanut tiedostonime�: " + nulle);
		}
		catch (IOException IOe) {
			System.err.println("Kirjoitus ep�onnistui: " + IOe);
		}
	}
	
	/**
	 * Lataa pelitilanteen tiedostosta.
	 *
	 * @see #save(String)
	 * @see #asetaSiirrettyNappula(char,int,char,boolean)
	 * @param tiedostonimi tallennetun pelin tiedostostonimi
	 * @.pre <code>tiedostonimi != null</code>
	 */
	private void load(String tiedostonimi) {
		try {
			FileInputStream in = new FileInputStream(new File(tiedostonimi));
			LinkedList<Integer> luetut = new LinkedList<Integer>();
			// koska javan tavut ovat kahden komplementteja, k�ytet��n AND operaatiota unsigned arvon saamiseksi
			while (in.available() > 0) {
				luetut.add(new Integer(in.read()) & 0xff);
			}
			int luettu = 0;
			// ensiksi luetaan pelilaudan nappulat
			pelilauta = new Pelilauta<Nappula>();
			boolean asetaValkoinen = false;
			boolean asetaSiirretty = false;
			char vaaka = 'A';
			int pysty = 1;
			luettu = luetut.removeFirst();
			while (luettu != 0xff) {
				if (luettu > 128) {
					asetaValkoinen = true;
					luettu -= 128;
				}
				else asetaValkoinen = false;
				if (luettu > 8) {
					asetaSiirretty = true;
					luettu -= 8;
				}
				else asetaSiirretty = false;
				switch (luettu) {
					case 0: break; // ruudussa ei ole nappulaa, eik� haluta ajaa default -lohkoa
					case 1:
						if (asetaSiirretty) asetaSiirrettyNappula(vaaka, pysty, 's', asetaValkoinen);
						else asetaNappula(vaaka, pysty, 's', asetaValkoinen);
						break;
					case 2:
						asetaNappula(vaaka, pysty, 'l', asetaValkoinen);
						break;
					case 3:
						asetaNappula(vaaka, pysty, 'r', asetaValkoinen);
						break;
					case 4:
						if (asetaSiirretty) asetaSiirrettyNappula(vaaka, pysty, 't', asetaValkoinen);
						else asetaNappula(vaaka, pysty, 't', asetaValkoinen);
						break;
					case 5:
						asetaNappula(vaaka, pysty, 'd', asetaValkoinen);
						break;
					case 6:
						if (asetaSiirretty) asetaSiirrettyNappula(vaaka, pysty, 'k', asetaValkoinen);
						else asetaNappula(vaaka, pysty, 'k', asetaValkoinen);
						break;
					default:
						System.out.println("Virhe lukiessa tiedostosta pelilautaa: " + luettu);
				}
				if (pysty < 8) pysty++;
				else {pysty = 1; vaaka++;}
				luettu = luetut.removeFirst();
			} // while

			// seuraavaksi luetaan sy�tyjen nappuloiden lista
			vainajat = new LinkedList<Nappula>();
			luettu = luetut.removeFirst();
			while (luettu != 0xff) {
				if (luettu > 128) {
					asetaValkoinen = true;
					luettu -= 128;
				}
				else asetaValkoinen = false;
				switch (luettu) {
					case 1:
						vainajat.add(new Sotilas(asetaValkoinen));
						break;
					case 2:
						vainajat.add(new Lahetti(asetaValkoinen));
						break;
					case 3:
						vainajat.add(new Ratsu(asetaValkoinen));
						break;
					case 4:
						vainajat.add(new Torni(asetaValkoinen));
						break;
					case 5:
						vainajat.add(new Kuningatar(asetaValkoinen));
						break;
					case 6:
						vainajat.add(new Kuningas(asetaValkoinen));
						break;
					default:
						System.out.println("Virhe lukiessa tiedostosta sy�tyj� nappuloita: " + luettu);				
				}
				luettu = luetut.removeFirst();
			}
			// seuraavaksi ohestaly�t�v�n koordinaatit
			luettu = luetut.removeFirst();
			if (luettu != 0xff) {
				int val = 0;
				// char on 2 tavua
				for (int i = 0; i < 2; i++) {
					val = val << 8;
					val = val + luettu;
					luettu = luetut.removeFirst();
				}
				char c = (char)val;
				val = 0;
				// int on 4 tavua
				for (int i = 0; i < 4; i++) {
					val = val << 8;
					val = val + luettu;
					luettu = luetut.removeFirst();
				}
				ohestalyotava = new Koordinaatit<Character,Integer>(new Character(c), new Integer(val));
			}
			else ohestalyotava = null;
			
			valkoisenKello = 0;
			mustanKello = 0;
			// muutetaan seuraavat 8 tavua yhdeksi long:ksi
			for (int i = 0; i < 8; i++) {
				valkoisenKello <<= 8;
				valkoisenKello += luetut.removeFirst();
			}
			
			// muutetaan seuraavat 8 tavua yhdeksi long:ksi
			for (int i = 0; i < 8; i++) {
				mustanKello <<= 8;
				mustanKello += luetut.removeFirst();
			}
			
			valkoisenVuoro = (luetut.removeFirst() == 0) ? false : true;
			
			in.close();
			
			// peli voi jatkua
			System.out.println(this);
			otaAika();
			peliAlkanut = true;
		}
		catch (FileNotFoundException fnfe) {
			System.err.println("Tiedostoa ei l�ytynyt");
		}
		catch (IOException IOe) {
			System.err.println("Tiedostoa ei voitu lukea: " + IOe);
		}
		catch (NoSuchElementException noee) {
			System.err.println("EOF ladattaessa peli� tiedostosta");
		}
	}
		
	/**
	 * Palauttaa merkkiesityksen shakkilaudasta
	 * ja pelitilanteesta. Windows konsolin
	 * rajoitteiden takia k�ytet��n nappuloiden
	 * merkint�in� iso- ja pikkukirjaimia.
	 * <p>
	 * Milt� uuden pelin pit�isi n�ytt��:
	 * <p><code>&nbsp;
	 *	  A B C D E F G H<br />
	 *	8|t|r|l|d|k|l|r|t|<br />
	 *	7|s|s|s|s|s|s|s|s|  0:00<br />
	 *	6| | | | | | | | |<br />
	 *	5| | | | | | | | |<br />
	 *	4| | | | | | | | |<br />
	 *	3| | | | | | | | |<br />
	 *	2|S|S|S|S|S|S|S|S|  0:00<br />
	 *	1|T|R|L|D|K|L|R|T|</code>
	 *
	 * @return merkkijonopiirros pelitilanteesta
	 */
	public String toString() {
		String pelinakyma = "";
		
		// peliruudukon tulostus nappuloineen
		pelinakyma += "\n  A B C D E F G H\n";
		int rivi = 1;
		for (rivi = 8; rivi >= 1; rivi--) {
			pelinakyma += (rivi) + "|";
			for (char sarake : new char[]{'A','B','C','D','E','F','G','H'}) {
				pelinakyma += onMiehitetty(sarake,rivi) ? annaNappula(sarake,rivi).toString() : " ";
				pelinakyma += "|";
			}
			// Kellot riveille 2 ja 7
			if (rivi == 7) pelinakyma += "  " + sekuntikello(mustanKello);
			if (rivi == 2) pelinakyma += "  " + sekuntikello(valkoisenKello);

			pelinakyma += "\n";
		}
		
		pelinakyma += "\n";
		
		// Syodyt nappulat
		pelinakyma += "Sy�dyt: ";
		for (Nappula n : vainajat) {
			pelinakyma += n.toString() + " ";
		}
		pelinakyma += "\n\n";
		// kumman vuoro?
		pelinakyma += valkoisenVuoro ? "Valkoisen siirto" : "Mustan siirto";		
		return pelinakyma;
	}
	
	/**
	 * Palauttaa parametrin� saadun millisekuntim��r�n sekunteina
	 * ja minuutteina esitettyn� muodossa "m:ss".
	 *
	 * @param millisek millisekunnit, jotka halutaan esitt�� sekuntikellona
	 * @return sekuntikellon <code>String</code> esitys
	 */
	public static String sekuntikello(long millisek) {
		return String.format("%d:%d%d", TimeUnit.MILLISECONDS.toMinutes(millisek), // minuutit
										TimeUnit.MILLISECONDS.toSeconds(millisek) / 10 - // kymmenet sekunnit miinus minuutit
										TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisek)) / 10,
										TimeUnit.MILLISECONDS.toSeconds(millisek) - // sekunnit miinus kymmenet sekunnit
										TimeUnit.MILLISECONDS.toSeconds(millisek) / 10 * 10); // py�ristet��n jakamalla ja kertomalla
	}
}