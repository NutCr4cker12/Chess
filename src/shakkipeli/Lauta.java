
package shakkipeli;


public class Lauta {
    private Ruutu[][] lauta;
    
    public Lauta() {
        this.lauta = new Ruutu[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.lauta[i][j] = new Ruutu();
            }
        }
    }
    public Ruutu getRuutu(int x, int y) {
        return this.lauta[x][y];
    }
    public void setRuutu(int x, int y, Nappi nappi) {
        this.lauta[x][y].setNappi(nappi);
        nappi.setSijainti(x, y);
    }
    public void setRuutuTyhjaksi(int x, int y) {
        this.lauta[x][y].setNappi();
    }
    
    public void tulostaLauta() {
        String[] kirjaimet = {"A", "B", "C", "D", "E", "F", "G", "H"};
        String poikkiviiva = "   ---  --- ---  --- ---  --- ---  ---";
        System.out.println(poikkiviiva);
        for (int y = 0; y < 8; y++) {
            System.out.print((y+1) + " ");
            int tyhjia = 0;
            for (int x = 0; x < 8; x++) {
                if (this.lauta[x][y].onkoTyhja()) {
                    tyhjia++;
                }
                System.out.print(this.lauta[x][y].tulostaRuutu());
                if (tyhjia%3==1) {
                    System.out.print(" ");
                    tyhjia++;
                }
            }
            System.out.println("|");
            System.out.println(poikkiviiva);
        }
        System.out.print("    ");
        for (int i = 0; i < 8; i++) {
            System.out.print(kirjaimet[i] + "   ");
            if (i%2==0) {
                System.out.print(" ");
            }
        }
        System.out.println("");
        System.out.println("");
    }

    public boolean siirra(int x1, int y1, int x2, int y2) {
        Nappi n = getRuutu(x1,y1).getNappi();
        if (!(n.onkoSallittuSiirto(x2, y2))) {
            return false;  
        }                                   // tarkistetaan, ettei pompita omien yli
        if (n.getArvo()==Arvo.LAHETTI || n.getArvo()==Arvo.TORNI || n.getArvo()==Arvo.KUNINGATAR) { // vinottain
            int dx;
            int dy;
            if (x2-x1>0) {
                dx=1;
            } else if (x2-x1<0) {
                dx=-1;
            } else {
                dx = 0;
            }
            if (y2-y1>0) {
                dy=1;
            } else if(y2-y1<0) {
                dy=-1;
            } else {
                dy = 0;
            }
            int pituus;
            if (Math.abs(x2-x1)>Math.abs(y2-y1)) {
                pituus = Math.abs(x2-x1);
            } else {
                pituus = Math.abs(y2-y1);
            }
            for (int i = 1; i < pituus ;i++) {
                if (!(getRuutu(x1+i*dx, y1+i*dy).onkoTyhja())) {
                    return false;
                }
            }
        } else if (n.getArvo()==Arvo.SOTILAS) {
            if (x2!=x1 && getRuutu(x2, y2).onkoTyhja()) {
                System.out.println("Sotilas ei voi liikkua sivuttain syomatta");
                return false;            // jos yrittaa liikkua sivusuunnassa - sen on pakko syoda
            } else if (x2==x1 && !(getRuutu(x2,y2).onkoTyhja())) {
                System.out.println("Sotilas ei voi syoda liikkumalla eteenpain");
                return false;            // jos yrittaa liikkua pystysuunnassa - sen on pakko olla tyhja (e isaa syoda suoraan)
            } else if (x2==x1 && Math.abs(y2-y1)>1) {
                if (getRuutu(x1, y1).getNappi().getVari()==Vari.VALKOINEN) { 
                    if (!(getRuutu(x1, y1-1).onkoTyhja())) {
                        System.out.println("Sotilas ei voi liikkua ensimmaista siirtoa 2 askelta eteenpain");
                        System.out.println("jos edessa on pelinappula");
                        return false;
                    }
                } else {            // Musta sotilas
                    if (!(getRuutu(x1, y1+1).onkoTyhja())) {
                        System.out.println("Sotilas ei voi liikkua ensimmaista siirtoa 2 askelta eteenpain");
                        System.out.println("jos edessa on pelinappula");
                       return false;
                    }
                }
            }
            
        } // Ratsu ja kuningas saa liikkua anycase
        // tee siirto
        setRuutu(x2, y2, n);
        setRuutuTyhjaksi(x1, y1);
        
        return true;
    }
    
}
