
package shakkipeli;


public abstract class Nappi {
    private int Xsijainti;
    private int Ysijainti;
    private Vari vari;
    
    public Nappi(Vari vari) {
        this.vari = vari;
    }
    public void setSijainti(int x, int y) {
        this.Xsijainti = x;
        this.Ysijainti = y;
    }
    public int[] getSijainti() {
        int[] sijainti = {this.Xsijainti, this.Ysijainti};
        return sijainti;
    }
    public Vari getVari() {
        return this.vari;
    }
    public abstract Arvo getArvo();
    public abstract String tulostaNappi();
    public abstract boolean onkoSallittuSiirto(int x, int y);
    
}
