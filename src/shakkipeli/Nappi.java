
package shakkipeli;


public abstract class Nappi {
    private int Xsijainti;
    private int Ysijainti;
    private Vari vari;
    
    public Nappi(Vari vari) {
        this.vari = vari;
    }
    /**
     * Asettaan napin sijainnin vastaamaan shakkilaudan sijaintia
     * @param x - sijainti laudalla (x-koordinaatti)
     * @param y - sijainti laudalla (y-koordinaatti)
     */
    public void setSijainti(int x, int y) {
        this.Xsijainti = x;
        this.Ysijainti = y;
    }
    /**
     * Palauttaa napin sijainnin laudalla
     * @return napin sijainti laudalla
     */
    public int[] getSijainti() {
        int[] sijainti = {this.Xsijainti, this.Ysijainti};
        return sijainti;
    }
    /**
     * 
     * @return napin vari
     */
    public Vari getVari() {
        return this.vari;
    }
    /**
     * @return napin arvo (enum Arvo)
     */
    public abstract Arvo getArvo();
    /**
     * @return nappi tekstimuodossa
     */
    public abstract String tulostaNappi();
    /**
     * Tarkistaa, voiko nappi liikkua kysytyllä tavalla
     * @param x - x-koordinatti sille, mihin nappia siirretään
     * @param y - x-koordinatti sille, mihin nappia siirretään
     * @return true jos nappi voi liikkua, false jos ei
     */
    public abstract boolean onkoSallittuSiirto(int x, int y);
    
}
