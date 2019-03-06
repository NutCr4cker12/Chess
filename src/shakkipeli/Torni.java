package shakkipeli;

public class Torni extends Nappi {

    private final Arvo arvo = Arvo.TORNI;

    public Torni(Vari vari) {
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
        if (x == sijainti[0] || y == sijainti[1]) { // vaaka- tai pystysuoraan
            return true;
        }
        return false;
    }

    public Arvo getArvo() {
        return this.arvo;
    }

    public String tulostaNappi() {
        if (getVari() == Vari.VALKOINEN) {
            return "\u2656";
        } else {
            return "\u265C";
        }
    }

}
