package shakkipeli;

public class Kuningas extends Nappi {

    private final Arvo arvo = Arvo.KUNINGAS;

    public Kuningas(Vari vari) {
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
        if (Math.abs(x - sijainti[0]) <= 1 && Math.abs(y - sijainti[1]) <= 1) {
            return true;
        }
        System.out.println("Kuningas ei voi liikkua noin");
        return false;
    }

    public Arvo getArvo() {
        return this.arvo;
    }

    public String tulostaNappi() {
        if (getVari() == Vari.VALKOINEN) {
            return "\u2654";
        } else {
            return "\u265A";
        }
    }

}
