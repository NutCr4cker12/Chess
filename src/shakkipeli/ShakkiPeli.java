package shakkipeli;

public class ShakkiPeli {

    public static void main(String[] args) {
       /*
	   Koko pelin hallinnointi tapahtuu tekstikayttoliimman alla
	   ja Main-luokka vain kaynnistaa sen
	   */
        TekstiKayttoLiittyma t = new TekstiKayttoLiittyma();
        t.start();
    }
    

    
}
