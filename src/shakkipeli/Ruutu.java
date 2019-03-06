
package shakkipeli;


public class Ruutu {
    private Nappi nappi;
    private Boolean tyhja;
    
    public Ruutu() {
        this.tyhja = true;
    }
    public String annaRuutu() {
        if (onkoTyhja()) {
            return " ";
        } else {
            return this.nappi.tulostaNappi();
        }
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
    /**
     * 
     * @return true if ruutu is empty
     */
    public boolean onkoTyhja() {
        return this.tyhja;
    }
    public String tulostaRuutu() {
        if (onkoTyhja()) {
            return "|   ";
        } else {
            return "| " + this.nappi.tulostaNappi() + " ";
        }
    }
    
}
