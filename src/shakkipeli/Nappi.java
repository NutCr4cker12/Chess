
package shakkipeli;


public abstract class Nappi {
    private int Xsijainti;
    private int Ysijainti;
    private Vari vari;
    private int siirtoja;
    
    public Nappi(Vari vari) {
        this.vari = vari;
        this.siirtoja = -1;
    }
    public void setSijainti(int x, int y) {
        this.Xsijainti = x;
        this.Ysijainti = y;
        this.siirtoja++;
    }
    public int[] getSijainti() {
        int[] sijainti = {this.Xsijainti, this.Ysijainti};
        return sijainti;
    }
    public Vari getVari() {
        return this.vari;
    }
    public void setSiirto() {
        this.siirtoja++;
    }
    public int getSiirrot() {
        return this.siirtoja;
    }
    public void setSiirrot(int x) {
        this.siirtoja = x;
    }
    public abstract Arvo getArvo();
    public abstract String tulostaNappi();
    public abstract boolean onkoSallittuSiirto(int x, int y);
    
}
