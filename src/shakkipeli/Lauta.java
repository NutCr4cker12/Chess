package shakkipeli;

public class Lauta {

    private Ruutu[][] lauta;

    /**
     * Alustaa uuden, 8*8 laudan tyhjillä ruuduilla
     */
    public Lauta() {
        this.lauta = new Ruutu[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.lauta[i][j] = new Ruutu();
            }
        }
    }
    
    /**
     * 
     * @param x - koordinaatti
     * @param y - koordinaatti
     * @return ruutu kyseisessä koordinaatissa
     */
    public Ruutu getRuutu(int x, int y) {
        return this.lauta[x][y];
    }

    /**
     * Asettaa ruutuun x,y pelinappulan nappi
     * @param x - koordinaatti
     * @param y - koordinaatti
     * @param nappi 
     */
    public void setRuutu(int x, int y, Nappi nappi) {
        this.lauta[x][y].setNappi(nappi);
        nappi.setSijainti(x, y);
    }

    /**
     * Asettaa ruudun x, y tyhjäksi
     * @param x - koordinaatti
     * @param y  - koordinaatti
     */
    public void setRuutuTyhjaksi(int x, int y) {
        this.lauta[x][y].setNappi();
    }
    
    /**
     * Tarkistaa, onko annettu siirto sallittu siirto ja
     * suorittaa siiroon ja palauttaa true, jos kyllä
     * ei suorita siirtoa ja palauttaa false, jos ei
     * @param x1 - siirrettävän napin x-koordinaatti
     * @param y1 - siirrettävän napin y-koordinaatti
     * @param x2 - ruudun x-koordinaatti, mihin nappi siirretään
     * @param y2 - ruudun y-koordinaatti, mihin nappi siirretään
     * @return true jos siirto suoritettiin, false muutoin
     */
    public boolean siirra(int x1, int y1, int x2, int y2) {
        Nappi n = getRuutu(x1, y1).getNappi();
        if (voikoSiirtaa(x1, y1, x2, y2) && eiMattia(x1, y1, x2, y2)) {
            // tee siirto
            setRuutu(x2, y2, n);
            setRuutuTyhjaksi(x1, y1);
            return true;
        } else {
            // ei pystyny siirtaa, oma edessa
            // tai MattiTilanne ja siirto jattaisi kuninkaan syotavaksi
            return false;
        }
    }

    /**
     * Tarkistaa ensin, voiko valittu pelinappula liikkua pyydetyllä tavalla
     * Tarkistaa seuraavaksi, ettei yritetä siirtää oman pelaajan päälle
     * Tarkistaa seuraavaksi, ettei yritetä "hyppiä" omien pelaajien yli (pl. ratsu)
     * Tarkistaa seuraavaksi sotilaan erikoisliikkeet (syönti sallittu vain sivuttain)
     * @param x1 - siirrettävän napin x-koordinaatti
     * @param y1 - siirrettävän napin y-koordinaatti
     * @param x2 - ruudun x-koordinaatti, mihin nappi siirretään
     * @param y2 - ruudun y-koordinaatti, mihin nappi siirretään
     * @return true jos siirto on sallittu, false jos ei
     */
    public boolean voikoSiirtaa(int x1, int y1, int x2, int y2) {
        Nappi n = getRuutu(x1, y1).getNappi();
        if (!(n.onkoSallittuSiirto(x2, y2))) {
            return false;
        }                                   // tarkistetaan, ettei pompita omien yli
        if (!(getRuutu(x2,y2).onkoTyhja()) && getRuutu(x2, y2).getNappi().getVari() == n.getVari()) { // ei voi siirtaa oman paalle
            return false;
        }
        if (n.getArvo() == Arvo.LAHETTI || n.getArvo() == Arvo.TORNI || n.getArvo() == Arvo.KUNINGATAR) { // vinottain
            int dx;
            int dy;
            if (x2 - x1 > 0) {
                dx = 1;
            } else if (x2 - x1 < 0) {
                dx = -1;
            } else {
                dx = 0;
            }
            if (y2 - y1 > 0) {
                dy = 1;
            } else if (y2 - y1 < 0) {
                dy = -1;
            } else {
                dy = 0;
            }
            int pituus;
            if (Math.abs(x2 - x1) > Math.abs(y2 - y1)) {
                pituus = Math.abs(x2 - x1);
            } else {
                pituus = Math.abs(y2 - y1);
            }
            for (int i = 1; i < pituus; i++) {
                if (!(getRuutu(x1 + i * dx, y1 + i * dy).onkoTyhja())) {
                    return false;
                }
            }
        } else if (n.getArvo() == Arvo.SOTILAS) {
            if (x2 != x1 && getRuutu(x2, y2).onkoTyhja()) {
                return false;            // jos yrittaa liikkua sivusuunnassa - sen on pakko syoda
            } else if (x2 == x1 && !(getRuutu(x2, y2).onkoTyhja())) {
                return false;            // jos yrittaa liikkua pystysuunnassa - sen on pakko olla tyhja (e isaa syoda suoraan)
            } else if (x2 == x1 && Math.abs(y2 - y1) > 1) {
                if (getRuutu(x1, y1).getNappi().getVari() == Vari.VALKOINEN) {
                    if (!(getRuutu(x1, y1 - 1).onkoTyhja())) {
                        return false;
                    }
                } else {            // Musta sotilas
                    if (!(getRuutu(x1, y1 + 1).onkoTyhja())) {
                        return false;
                    }
                }
            }

        } // Ratsu ja kuningas saa liikkua anycase
        return true;
    }

    /**
     * Tarkistaa, ettei valittu siirto aiheuta siirtoa suorittavalle pelaaja mattitilannetta
     * Ts. ei voi siirtää esimerkiksi ratsua pois kuninkaan edestä, jos seuraavalla siirrolla kuningas voitaisiin syödä
     * @param x1 - siirrettävän napin x-koordinaatti
     * @param y1 - siirrettävän napin y-koordinaatti
     * @param x2 - ruudun x-koordinaatti, mihin nappi siirretään
     * @param y2 - ruudun y-koordinaatti, mihin nappi siirretään
     * @return true jos siirto ei synnytä mattitilannetta, false muutoin
     */
    public boolean eiMattia(int x1, int y1, int x2, int y2) {
        Nappi n = getRuutu(x1, y1).getNappi();
        Nappi kopionappi = getRuutu(x1, y1).getNappi();
        Nappi kopionappi2 = null;
        if (!(getRuutu(x2, y2).onkoTyhja())) {
            kopionappi2 = getRuutu(x2, y2).getNappi();
        }
        setRuutu(x2, y2, kopionappi);
        setRuutuTyhjaksi(x1, y1);
        if (onkoMattiTilanne(n.getVari())) {
            setRuutu(x1, y1, kopionappi);
            if (kopionappi2 != null) {
                setRuutu(x2, y2, kopionappi2);
            } else {
                setRuutuTyhjaksi(x2, y2);
            }
            return false;
        }
        setRuutu(x1, y1, kopionappi);
        if (kopionappi2 != null) {
            setRuutu(x2, y2, kopionappi2);
        } else {
            setRuutuTyhjaksi(x2, y2);
        }
        return true;
    }

    /**
     * Tarkistaa onko pelaajalla mattitilanne päällä,
     * eli kuningas tulee saada turvattua seuraavalla siirrolla
     * @param vari - vuorossa olevan pelaajan väri
     * @return true jos on mattitilanne, false jos ei
     */
    public boolean onkoMattiTilanne(Vari vari) {
        int x1 = -1;
        int y1 = -1;
        /*
        * Kunikaan sijainti
         */
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (!(getRuutu(x, y).onkoTyhja())) {
                    if (getRuutu(x, y).getNappi().getArvo() == Arvo.KUNINGAS && getRuutu(x, y).getNappi().getVari() == vari) {
                        x1 = x;
                        y1 = y;
                    }
                }
            }
        }
        /*
        * Haetaan, pystyyko toisen varinen syomaan kuninkaan seuraavalla siirrolla
         */
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (!(getRuutu(x, y).onkoTyhja())) {
                    if (getRuutu(x, y).getNappi().getVari() != vari) {
                        if (voikoSiirtaa(x, y, x1, y1)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    /**
     * Tarkistaa, onko tilanne ShakkiMatti
     * @param vari = siirtovuorossa olevan pelaajan vari
     * @return true jos tilanne siirtovuorossa oleva pelaaja ei voi suorittaa ainuttakaan laillista siirtoa, false muutoin
     */
    public boolean shakkiMatti(Vari vari) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (!(getRuutu(x, y).onkoTyhja()) && getRuutu(x,y).getNappi().getVari()==vari) {
                    for (int x2 = 0; x2 < 8; x2++) {
                        for (int y2 = 0; y2 < 8; y2++) {
                            if (voikoSiirtaa(x, y, x2, y2) && eiMattia(x, y, x2, y2)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    public int[] sotilasPaadyssa() {
        int[] xy = null;
        for (int i = 0; i < 8; i++) {
            //if ()
        }
        return xy;
    }
}
