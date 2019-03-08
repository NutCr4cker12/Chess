
package shakkipeli;


public class Ruutu {
    private Nappi nappi;
    private Boolean tyhja;
    
    public Ruutu() {
        this.tyhja = true;
    }
    /**
     * Asettaa ruutuun napin
     * @param nappi 
     */
    public void setNappi(Nappi nappi) {
        this.tyhja = false;
        this.nappi = nappi;
    }
    /**
     * Asettaa ruudun tyhjäksi
     */
    public void setNappi() {
        this.tyhja = true;
    }
    /**
     * palauttaa ruudussa olevan napin
     * @return 
     */
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
    
}
