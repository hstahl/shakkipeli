package fi.utu.harjoitukset.shakkipeli;
/**
 * Shakkipelin abstrakti pelinappulaluokka, jossa annetaan toteutus niille
 * <code>Nappula</code>:n ominaisuuksille, jotka ovat yhteisi� kaikille
 * rajapinnan m��ritelm�n mukaisille shakkinappuloille.
 */
public abstract class AbstraktiNappula implements Nappula {

	/** Merkkiesitys nappulalle pelilaudalla. */
	protected final String MERKKIESITYS;
	/** Onko t�m� nappula valkoisen? */
	protected final boolean onValkoinen;
	/** jotkut siirrot ovat sallittuja vain ensimm�ist� kertaa siirrett�ess� */
	protected boolean ensimmainenSiirto;

	/**
	 * <code>AbstraktiNappula</code>:n aliluokista kutsuttava konstruktori. Asettaa
	 * kaikille nappuloille yhteiset luokkamuuttujat.
	 *
	 * @param merkkiesitys <code>Nappula</code>:n merkkiesitys, joka on jokin <code>Nappula.MerkkiesitysNappulalle</code> literaaliluokan vakioista
	 * @param onValkoinen Onko t�m� nappula valkoisen pelaajan?
	 * @.pre <code>valkoinen != null && musta != null</code>
	 * @.post <code>onValkoinen() == onValkoinen && toString().equals(merkkiesitys) && onSiirretty() == false</code>
	 */
	protected AbstraktiNappula(MerkkiesitysNappulalle merkkiesitys, boolean onValkoinen) {
		MERKKIESITYS = onValkoinen ? merkkiesitys.valkoinen() : merkkiesitys.musta();
		this.onValkoinen = onValkoinen;
		ensimmainenSiirto = true;
	}
	
	/**
	 * Kertoo, onko nappula valkoinen
	 *
	 * @return <code>true</code>, jos nappula on valkoisen pelaajan.
	 */
	public boolean onValkoinen() {
		return onValkoinen;
	}
	
	/** 
	 * Havannoi, onko nappulaa sirretty. Joitakin siirtoja
	 * voi tehd� vain ensimm�isell� kerralla siirrett�ess�
	 * nappulaa.
	 * 
	 * @return <code>true</code>, jos nappulaa on sirretty
	 */
	public boolean onSiirretty() {
		return !ensimmainenSiirto;
	}
	
	/**
	 * Abstrakti totuusarvometodi nappulalle sallittujen siirtojen
	 * tarkastamista varten.
	 *
	 * @return <code>true</code>, jos ehdotettu siirto on sallittu t�lle nappulalle
	 * @param peli <code>Shakkipeli</code>, jossa siirto halutaan tehd�
	 * @param vaakaAlku l�ht�kohdan vaakakoordinaatti [A-Ha-h]
	 * @param pystyAlku l�ht�kohdan pystykoordinaatti [1-8]
	 * @param vaakaLoppu maaliruudun vaakakoordinaatti [A-Ha-h]
	 * @param pystyLoppu maaliruudun pystykoordinaatti [1-8]
	 * @.pre <code>EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; vaakaAlku == c) && (1 <= pystyAlku <= 8) && EXISTS(char c : new char[]{'A','B','C','D','E','F','G','H'}; vaakaLoppu == c) && (1 <= pystyLoppu <= 8)</code>
	 */
	public abstract boolean onLaillinen(Shakkipeli peli, char vaakaAlku, int pystyAlku,
														 char vaakaLoppu, int pystyLoppu);
	
	/**
	 * Vertaa oliota tahan abstraktiin nappulaan.
	 * Palauttaa <code>true</code>, jos olio on
	 * my�s abstrakti shakkinappula, se on
	 * samanv�rinen kuin t�m�, sill� on sama
	 * merkkiesitys ja molempia on tai ei ole
	 * siirretty meneill��n olevassa shakkipeliss�.
	 *
	 * @param o t�h�n verrattava olio
	 * @return <code>true</code>, jos verrattava olio on samanlainen nappula
	 */
	public boolean equals(Object o) {
		if (!(o instanceof AbstraktiNappula)) return false;
		AbstraktiNappula temp = (AbstraktiNappula) o;
		return (temp.onValkoinen() == onValkoinen())
			&& (temp.onSiirretty() == onSiirretty())
			&& (temp.toString().equals(toString()));
	}
	
	/**
	 * Shakkinappulan merkkijonoesitys
	 *
	 * @return <code>Nappula</code>:n esitys String:n�
	 */
	public String toString() {
		return MERKKIESITYS;
	}
}