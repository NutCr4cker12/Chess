package shakkipeli;

public class ShakkiPeli {

    public static void main(String[] args) {
       /*
	*   Koko pelin hallinnointi tapahtuu tekstikayttoliimman alla
	*  ja Main-luokka vain kaynnistaa sen
        *
        * Pelin tallennukseen pitaa viela lisata siirtojen maara ja korjata sen
        * hakeminen jatkettaessa vanhaa, tallennettua pelia
	*/
        TekstiKayttoLiittyma t = new TekstiKayttoLiittyma();
        t.start();
    }
    

    
}
