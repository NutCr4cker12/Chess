package shakkipeli;

public class Sotilas extends Nappi {

    private Arvo arvo = Arvo.SOTILAS;

    public Sotilas(Vari vari) {
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
        if (getVari() == Vari.MUSTA) { // Mustat liikkuu alaspain
            if (sijainti[1] == 1) { // Jos musta = rivilla 1 -> ei oo liikkunu
                if (y - sijainti[1] == 2 && dx == 0) { // eka siirtoo saa olla 2x "eteenpain"
                    return true;
                }
            }
            if (y - sijainti[1] == 1 && dx <= 1) { // saa syoda sivuttain
                return true;
            }
        } else { // Valkoiset liikkuu ylospain
            if (sijainti[1] == 6) { // Jos valkone = rivilla 6 -> ei oo liikkunu
                if (sijainti[1] - y == 2 && dx == 0) { // eka siirtoo saa olla 2x "eteenpain"
                    return true;
                }
            }
            if (sijainti[1] - y == 1 && dx <= 1) { // saa syoda sivuttain
                return true;
            }
        }
        return false;
    }

    public Arvo getArvo() {
        return this.arvo;
    }

    public String tulostaNappi() {
        if (getVari() == Vari.VALKOINEN) {
            return "\u2659";
        } else {
            return "\u265F";
        }
    }

}
