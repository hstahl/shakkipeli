package fi.utu.harjoitukset.shakkipeli;
import java.util.ArrayList;
/**
 * Luokka kuvaa shakkilautaa, jossa tyyppi N
 * on jokin toteutus shakkinappulalle.
 * Shakkilauta on 8x8 kokoinen ruudukko,
 * jonka kussakin ruudussa voi olla
 * korkeintaan yksi nappula kerrallaan.
 * <p>
 * Pelilaudan indeksointi on seuraavanlainen:
 * vaakasuunta merkitään kirjaimin A-H ja
 * pystysuunta numeroin 1-8.
 *
 * @.classInvariant <code>FORALL(i : new char[]{'A','B','C','D','E','F','G','H'}; FORALL(j; 1 <= j <= 8 : annaNappula(i,j) instanceof N || annaNappula(i,j) == null))</code>
 * @.classInvariantPrivate <code>lauta.size() == 8 && FORALL(i; 0 <= i < lauta.size(): lauta.get(i).size == 8)</code>
 */
public class Pelilauta<N> {

	/** Pelilaudan toteutus on kaksiulotteinen taulukko. */
	private ArrayList<ArrayList<N>> lauta;

	/**
	 * Luo uuden tyhjän shakkilaudan.
	 */
	public Pelilauta() {
		lauta = new ArrayList<ArrayList<N>>(8);
		// alustetaan pelilauta
		for (int i = 0; i < 8; i++) {
			lauta.add(i, new ArrayList<N>(8));
			for (int j = 0; j < 8; j++) {
				lauta.get(i).add(j, null);
			}
		}
	}

	/**
	 * Tämä metodi antaa viittauksen koordinaateissa olevaan
	 * nappulaan tai <code>null</code>, jos ruutu on tyhjä.
	 *
	 * @return viittaus ruudussa olevaan nappulaan tai <code>null</code>
	 * @param vaaka kysyttävän ruudun vaakakoordinaatti '[A-Ha-h]'
	 * @param pysty kysyttävän ruudun pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; c == Character.toUpperCase(vaaka)) && (0 <= pysty < 8)</code>
	 */
	public N annaNappula(char vaaka, int pysty) {
		return lauta.get(pysty - 1).get(lueVaakakoordinaatti(vaaka));
	}

	/**
	 * Asettaa uuden nappulan pelilaudalle. Jos annetussa ruudussa
	 * on ennestään nappula, vanha nappula korvautuu uudella.
	 *
	 * @param nappula asetettava nappula
	 * @param vaaka kohderuudun vaakakoordinaatti '[A-Ha-h]'
	 * @param pysty kohderuudun pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; c == Character.toUpperCase(vaaka)) && (0 <= pysty < 8)</code>
	 * @.post <code>annaNappula(vaaka, pysty) == nappula</code>
	 */	
	public void asetaNappula(N nappula, char vaaka, int pysty) {
		lauta.get(pysty - 1).set(lueVaakakoordinaatti(vaaka), nappula);
	}
	
	/**
	 * Poistaa osoitetun nappulan pelilaudalta.
	 *
	 * @param vaaka kohderuudun vaakakoordinaatti '[A-Ha-h]'
	 * @param pysty kohderuudun pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; c == Character.toUpperCase(vaaka)) && (0 <= pysty < 8)</code>
	 * @.post <code>annaNappula(vaaka, pysty) == null</code>
	 */
	public void poistaNappula(char vaaka, int pysty) {
		lauta.get(pysty - 1).set(lueVaakakoordinaatti(vaaka), null);
	}
	
	/**
	 * Muuttaa aakkosten kirjaimen sisältävän <code>char</code>
	 * parametrin kokonaislukuarvoksi siten, että 'a' palauttaa 0,
	 * 'b' palauttaa 1 jne. Metodi palauttaa siis kirjaimen
	 * järjestysluvun aakkosissa alkaen nollasta. Käyttötarkoitus
	 * on muuntaa tyypillisen shakkilaudan vaakaruutujen merkinnät
	 * numeerisiksi. Parametri voi olla iso- tai pikkukirjain.
	 *
	 * @param vaakakoordinaatti kirjain, jonka järjestysluku halutaan '[A-Za-z]'
	 * @return parametrikirjaimen järjestysluvun aakkosissa nollasta alkaen
	 */
	private static int lueVaakakoordinaatti(char vaakakoordinaatti) {
		return Character.getNumericValue(Character.toUpperCase(vaakakoordinaatti))
			 - Character.getNumericValue('A');
	}
	
	/**
	 * Vertaa annettua oliota tähän pelilautaan.
	 * Palauttaa <code>true</code>, jos annettu
	 * olio on myös pelilauta ja laudalla on
	 * tarkalleen sama asetelma nappuloita.
	 *
	 * @param o verrattava olio
	 * @return <code>true</code>, jos annettu olio on syväsama tämän pelilaudan kanssa
	 */
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof Pelilauta)) return false;
		else {
			Pelilauta toinen = (Pelilauta)o;
			for(char i : new char[]{'A','B','C','D','E','F','G','H'}) {
				for(int j = 0; j < 8; j++) {
					if (!(annaNappula(i,j).equals(toinen.annaNappula(i,j)))) return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Antaa pelilaudan merkkiesityksen. Esitys
	 * on taulukko pelilaudalla olevista nappuloista.
	 *
	 * @return merkkiesitys tästä pelilaudasta
	 */
	public String toString() {
		String result = "";
		for (ArrayList<N> a : lauta) {
			result += a.toString() + "\n";
		}
		return result;
	}
}