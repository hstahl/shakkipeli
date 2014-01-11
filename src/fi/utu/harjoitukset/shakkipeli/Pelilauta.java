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
 * vaakasuunta merkit��n kirjaimin A-H ja
 * pystysuunta numeroin 1-8.
 *
 * @.classInvariant <code>FORALL(i : new char[]{'A','B','C','D','E','F','G','H'}; FORALL(j; 1 <= j <= 8 : annaNappula(i,j) instanceof N || annaNappula(i,j) == null))</code>
 * @.classInvariantPrivate <code>lauta.size() == 8 && FORALL(i; 0 <= i < lauta.size(): lauta.get(i).size == 8)</code>
 */
public class Pelilauta<N> {

	/** Pelilaudan toteutus on kaksiulotteinen taulukko. */
	private ArrayList<ArrayList<N>> lauta;

	/**
	 * Luo uuden tyhj�n shakkilaudan.
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
	 * T�m� metodi antaa viittauksen koordinaateissa olevaan
	 * nappulaan tai <code>null</code>, jos ruutu on tyhj�.
	 *
	 * @return viittaus ruudussa olevaan nappulaan tai <code>null</code>
	 * @param vaaka kysytt�v�n ruudun vaakakoordinaatti '[A-Ha-h]'
	 * @param pysty kysytt�v�n ruudun pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; c == Character.toUpperCase(vaaka)) && (0 <= pysty < 8)</code>
	 */
	public N annaNappula(char vaaka, int pysty) {
		return lauta.get(pysty - 1).get(lueVaakakoordinaatti(vaaka));
	}

	/**
	 * Asettaa uuden nappulan pelilaudalle. Jos annetussa ruudussa
	 * on ennest��n nappula, vanha nappula korvautuu uudella.
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
	 * Muuttaa aakkosten kirjaimen sis�lt�v�n <code>char</code>
	 * parametrin kokonaislukuarvoksi siten, ett� 'a' palauttaa 0,
	 * 'b' palauttaa 1 jne. Metodi palauttaa siis kirjaimen
	 * j�rjestysluvun aakkosissa alkaen nollasta. K�ytt�tarkoitus
	 * on muuntaa tyypillisen shakkilaudan vaakaruutujen merkinn�t
	 * numeerisiksi. Parametri voi olla iso- tai pikkukirjain.
	 *
	 * @param vaakakoordinaatti kirjain, jonka j�rjestysluku halutaan '[A-Za-z]'
	 * @return parametrikirjaimen j�rjestysluvun aakkosissa nollasta alkaen
	 */
	private static int lueVaakakoordinaatti(char vaakakoordinaatti) {
		return Character.getNumericValue(Character.toUpperCase(vaakakoordinaatti))
			 - Character.getNumericValue('A');
	}
	
	/**
	 * Vertaa annettua oliota t�h�n pelilautaan.
	 * Palauttaa <code>true</code>, jos annettu
	 * olio on my�s pelilauta ja laudalla on
	 * tarkalleen sama asetelma nappuloita.
	 *
	 * @param o verrattava olio
	 * @return <code>true</code>, jos annettu olio on syv�sama t�m�n pelilaudan kanssa
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
	 * @return merkkiesitys t�st� pelilaudasta
	 */
	public String toString() {
		String result = "";
		for (ArrayList<N> a : lauta) {
			result += a.toString() + "\n";
		}
		return result;
	}
}