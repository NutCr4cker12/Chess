
package shakkipeli;


public class Ruutu {
    private Nappi nappi;
    private Boolean tyhja;
    
    public Ruutu() {
        this.tyhja = true;
    }
    public void setNappi(Nappi nappi) {
        this.tyhja = false;
        this.nappi = nappi;
    }
    public void setNappi() {
        this.tyhja = true;
    }
    public Nappi getNappi() {
        return this.nappi;
    }
    public boolean onkoTyhja() {
        if (this.tyhja) {
            return true;
        } else {
            return false;
        }
    }
    public String tulostaRuutu() {
        if (onkoTyhja()) {
            return "|   ";
        } else {
            return "| " + this.nappi.tulostaNappi() + " ";
        }
    }
    
}
