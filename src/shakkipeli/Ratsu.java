package shakkipeli;

public class Ratsu extends Nappi {

    private final Arvo arvo = Arvo.RATSU;

    public Ratsu(Vari vari) {
        super(vari);
    }

    public boolean onkoSallittuSiirto(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) { // pysyyko laudalla
            return false;
        }
        int[] sijainti = getSijainti();
        if (x == sijainti[0] && y == sijainti[1]) { // ei saa "liikkua" vain paikoillaan
            return false;
        }
        int dx = Math.abs(x - sijainti[0]);
        int dy = Math.abs(y - sijainti[1]);
        if ((dx == 2 && dy == 1) || (dx == 1 && dy == 2)) {
            return true;
        }
        return false;
    }

    public Arvo getArvo() {
        return this.arvo;
    }

    public String tulostaNappi() {
        if (getVari() == Vari.VALKOINEN) {
            return "\u2658";
        } else {
            return "\u265E";
        }
    }

}
