package shakkipeli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class TekstiKayttoLiittyma {

    Lauta lauta;
    Scanner lukija = new Scanner(System.in);
    String p1;
    String p2;
    Vari p1Vari;
    Vari p2Vari;
    Vari vuoro;

    public void start() {
        lukija = new Scanner(System.in);
        //aloitaUusiPeli();

        System.out.println("****************************");
        System.out.println("********** SHAKKI **********");
        System.out.println("****************************");
        System.out.println("");
        while (true) {
            System.out.println("A - Aloittaa uuden pelin");
            System.out.println("J - Jatkaa vanhaa pelia");
            System.out.println("info - Antaa ohjeet milloin vain");
            System.out.println("Lopeta - lopettaa pelin milloin tahansa");
            System.out.println("");
            String vastaus = lukija.nextLine();
            if (vastaus.equals("A")) {
                aloitaUusiPeli();
                break;
            } else if (vastaus.equals("J")) {
                jatkaVanhaaPelia();
                break;
            } else if (vastaus.equals("info")) {
                annaOhjeet();
            } else if (vastaus.equals("Lopeta")) {
                break;
            } else {
                System.out.println("Epäkelpo syöte, yritä uudelleen");
                System.out.println("");
            }
        }
    }

    public void aloitaUusiPeli() {
        System.out.print("Anna ensimmäisen pelaajan nimi: ");
        p1 = lukija.nextLine();
        System.out.print("Anna toisen pelaajan nimi: ");
        p2 = lukija.nextLine();
        while (true) {
            System.out.println("Kumpi aloittaa, " + p1 + " vai " + p2 + " ?");
            String vastaus = lukija.nextLine();
            if (vastaus.equals(p1)) {
                p1Vari = Vari.VALKOINEN;
                p2Vari = Vari.MUSTA;
                break;
            } else if (vastaus.equals(p2)) {
                p2Vari = Vari.VALKOINEN;
                p1Vari = Vari.MUSTA;
                break;
            } else {
                System.out.println("Pelaaja " + vastaus + " ei ole pelaamassa");
            }
        }
        System.out.println("Selva, aloitetaan peli");
        vuoro = Vari.VALKOINEN;
        alustaPeli();
        lauta.tulostaLauta();
        pelaa();

    }

    public void jatkaVanhaaPelia() {
        // luetaan vanha peli, alustetaan pelaajat, lauta ja napit sen mukaan ja jatketaan pelaamista siita
        ArrayList<String> lista = new ArrayList<>();
        try (Scanner lukija = new Scanner(new File("Tallennetut pelit.txt"))) {
            while (lukija.hasNextLine()) {
                lista.add(lukija.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Ei tallennettuja pelejä");
            System.exit(0);
        }
        lauta = new Lauta();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).contains("Pelaaja")) { // haetaan pelaajat ja niiden varit
                if (lista.get(i).contains("Pelaaja1")) { // alustetaan pelaaja1
                    String[] rivi = lista.get(i).split(" ");
                    p1 = rivi[1];
                    p1Vari = annaVari(rivi[rivi.length - 1]);
                } else { // alustetaan pelaaja2
                    String[] rivi = lista.get(i).split(" ");
                    p2 = rivi[1];
                    p2Vari = annaVari(rivi[rivi.length - 1]);
                }
            } else if (lista.get(i).contains("vuorossa on")) { // katotaan kumman vuoroon jai
                if (lista.get(i).contains("VALKOINEN")) {
                    vuoro = Vari.VALKOINEN;
                } else {
                    vuoro = Vari.MUSTA;
                }
            } else if (lista.get(i).contains("Ruutu") && !(lista.get(i).contains("tyhja"))) {
                String[] rivi = lista.get(i).split(" ");
                int x = Integer.parseInt(rivi[1]);
                int y = Integer.parseInt(rivi[2]);
                String s = rivi[3];
                Arvo arvo = annaArvo(rivi[3]);
                Vari vari = annaVari(rivi[4]);
                int siirrot = Integer.parseInt(rivi[5]);
                Nappi n;
                if (s.equals("TORNI")) {
                    n = new Torni(vari);
                } else if (s.equals("LAHETTI")) {
                    n = new Lahetti(vari);
                } else if (s.equals("RATSU")) {
                    n = new Ratsu(vari);
                } else if (s.equals("KUNINGAS")) {
                    n = new Kuningas(vari);
                } else if (s.equals("KUNINGATAR")) {
                    n = new Kuningatar(vari);
                } else {
                    n = new Sotilas(vari);
                }
                n.setSiirrot(n.getSiirrot() - 1);
                lauta.setRuutu(x, y, n);
            }
        } // end of listan parseus
        lauta.tulostaLauta();
        pelaa();
    }

    public void pelaa() {
        while (true) {
            while (true) {
                if (vuoro == Vari.VALKOINEN) {
                    System.out.println(p1 + "n vuoro: (Valkoiset)");
                    String siirto = lukija.nextLine();
                    if (kasitteleSiirto(siirto, Vari.VALKOINEN)) {
                        lauta.tulostaLauta();
                        vuoro = Vari.MUSTA;
                        break;
                    }
                } else {
                    System.out.println(p2 + "n vuoro: (Mustat)");
                    String siirto = lukija.nextLine();
                    if (kasitteleSiirto(siirto, Vari.MUSTA)) {
                        lauta.tulostaLauta();
                        vuoro = Vari.VALKOINEN;
                        break;
                    }
                }
            } // end inner while
        } // end outer while
    }

    public void alustaPeli() {
        this.lauta = new Lauta();
        for (int i = 0; i < 8; i++) { // sotilaat
            lauta.setRuutu(i, 1, new Sotilas(Vari.MUSTA));
            lauta.setRuutu(i, 6, new Sotilas(Vari.VALKOINEN));
        }
        lauta.setRuutu(0, 0, new Torni(Vari.MUSTA));
        lauta.setRuutu(7, 0, new Torni(Vari.MUSTA));
        lauta.setRuutu(0, 7, new Torni(Vari.VALKOINEN));
        lauta.setRuutu(7, 7, new Torni(Vari.VALKOINEN));

        lauta.setRuutu(1, 0, new Ratsu(Vari.MUSTA));
        lauta.setRuutu(6, 0, new Ratsu(Vari.MUSTA));
        lauta.setRuutu(1, 7, new Ratsu(Vari.VALKOINEN));
        lauta.setRuutu(6, 7, new Ratsu(Vari.VALKOINEN));

        lauta.setRuutu(2, 0, new Lahetti(Vari.MUSTA));
        lauta.setRuutu(5, 0, new Lahetti(Vari.MUSTA));
        lauta.setRuutu(2, 7, new Lahetti(Vari.VALKOINEN));
        lauta.setRuutu(5, 7, new Lahetti(Vari.VALKOINEN));

        lauta.setRuutu(3, 0, new Kuningatar(Vari.MUSTA));
        lauta.setRuutu(3, 7, new Kuningatar(Vari.VALKOINEN));
        lauta.setRuutu(4, 0, new Kuningas(Vari.MUSTA));
        lauta.setRuutu(4, 7, new Kuningas(Vari.VALKOINEN));

    }

    private void annaOhjeet() {
        System.out.println("Siirtojen tekemiseen käytetään pitkään merkintätapaan.");
        System.out.println("Siirron komento on muotoa Xa2-a3, missä");
        System.out.println("X = pelinapin arvo (K=kuningas, D=kuningatar, T=torni, L=lähetti, R=ratsu ja S=sotilas)");
        System.out.println("a2 = mistä ruudusta pelinappi siirretään");
        System.out.println("a3 = mihin ruutuun nappula siirretään");
        System.out.println("----");
        System.out.println("Lauta - näyttaa laudan");
        System.out.println("Lopeta - lopettaa pelin milloin tahansa");
        System.out.println("----");
    }

    private boolean kasitteleSiirto(String siirto, Vari pelaajanVari) {
        if (siirto.equals("info")) {
            annaOhjeet();
            return false;
        } else if (siirto.equals("Lopeta") || siirto.equals("lopeta")) {
            lopetaPeli();
            return false;
        } else if (siirto.equals("Lauta")) {
            lauta.tulostaLauta();
            return false;
        }
        if (siirto.length() < 6) {
            System.out.println("Liian lyhyt siirtokomento: " + siirto);
        }
        /* 
            
        parsetaan syotteesta pelinappula, muutetaan se seka Arvokis etta tekstiksi
         */
        String pnappi = siirto.substring(0, 1);
        Arvo annettuArvo = muutaArvo(pnappi);
        String napinTekstiMuoto = annaNappiTekstina(pnappi);

        /*
        parsetaan siirron lahtoruutu
         */
        String kirjain1 = siirto.substring(1, 2);
        int x1 = muutaPystyrivi(kirjain1); // muutetaan Kirjain numeroksi
        if (x1 == -1) {
            System.out.println("Vihreellinen ruudun sarake: " + kirjain1);
            return false;
        }
        int y1;  // parsetaan ja haetaan ruudun vaakarivi
        try {
            y1 = Integer.parseInt(siirto.substring(2, 3));
        } catch (Exception e) {
            System.out.println("Virheellinen ruudun rivisyöte: " + siirto.substring(2, 3));
            return false; // jos valittu vaakarivi on virheellinen, palataan
        }
        if (y1 < 1 || y1 > 8) {
            System.out.println("Virheellinen ruudun rivisyöte: " + y1);
            return false; // jos valittu vaakarivi on virheellinen, palataan
        }

        /*
        parsetaan siirron maaliruutu
         */
        String kirjain2 = siirto.substring(4, 5);
        int x2 = muutaPystyrivi(kirjain2); // muutetaan Kirjain numeroksi
        if (x2 == -1) {
            System.out.println("Vihreellinen ruudun sarake: " + kirjain2);
            return false;
        }
        int y2;  // parsetaan ja haetaan ruudun vaakarivi
        try {
            y2 = Integer.parseInt(siirto.substring(5, 6));
        } catch (Exception e) {
            System.out.println("Virheellinen ruudun rivisyöte: " + siirto.substring(5, 6));
            return false; // jos valittu vaakarivi on virheellinen, palataan
        }
        if (y2 < 1 || y2 > 8) {
            System.out.println("Virheellinen ruudun rivisyöte: " + y2);
            return false; // jos valittu vaakarivi on virheellinen, palataan
        }

        /*
        // tarkastetaan annettu syote luvattomien siirtojen varalta:
         */
        if (lauta.getRuutu(x1, y1 - 1).onkoTyhja()) {           // lahtoruudussa ei ole nappulaa
            System.out.println("Vaitsemassasi ruudussa " + kirjain1 + y1 + " ei ole nappulaa");
            return false;
        } else {
            if (lauta.getRuutu(x1, y1 - 1).getNappi().getVari() != pelaajanVari) { // yrittaa siirtaa vaaran varista nappulaa
                System.out.println("Et voi siirtaa vastustajan nappulaa!");
                return false;
            } else if (lauta.getRuutu(x1, y1 - 1).getNappi().getArvo() != annettuArvo) { // yritti siirtaa vaaranlaista nappulaa
                System.out.println("Valitsemassasi ruudussa ei ole " + napinTekstiMuoto);
                return false;
            } else if (!(lauta.getRuutu(x2, y2 - 1).onkoTyhja()) && lauta.getRuutu(x2, y2 - 1).getNappi().getVari() == pelaajanVari) { // yrittaa siirtaa oman pelaajan paalle
                System.out.println("Et voi syoda omaa pelaajaa!");
                return false;
            }
        }
        if (!(lauta.siirra(x1, y1 - 1, x2, y2 - 1))) {
            System.out.println("Virheellinen siirto!");
            return false;
        }

        return true;
    }

    private int muutaPystyrivi(String kirjain) {
        if (kirjain.equals("A") || kirjain.equals("a")) {
            return 0;
        } else if (kirjain.equals("B") || kirjain.equals("b")) {
            return 1;
        } else if (kirjain.equals("C") || kirjain.equals("c")) {
            return 2;
        } else if (kirjain.equals("D") || kirjain.equals("d")) {
            return 3;
        } else if (kirjain.equals("E") || kirjain.equals("e")) {
            return 4;
        } else if (kirjain.equals("F") || kirjain.equals("f")) {
            return 5;
        } else if (kirjain.equals("G") || kirjain.equals("g")) {
            return 6;
        } else if (kirjain.equals("H") || kirjain.equals("h")) {
            return 7;
        } else {
            return -1;
        }
    }

    private Arvo muutaArvo(String pnappi) {
        if (pnappi.equals("S") || pnappi.equals("s")) {
            return Arvo.SOTILAS;
        } else if (pnappi.equals("T") || pnappi.equals("t")) {
            return Arvo.TORNI;
        } else if (pnappi.equals("R") || pnappi.equals("r")) {
            return Arvo.RATSU;
        } else if (pnappi.equals("L") || pnappi.equals("l")) {
            return Arvo.LAHETTI;
        } else if (pnappi.equals("K") || pnappi.equals("k")) {
            return Arvo.KUNINGAS;
        } else if (pnappi.equals("D") || pnappi.equals("d")) {
            return Arvo.KUNINGATAR;
        } else {
            throw new IllegalArgumentException("Epäkelpo napin syöte");
        }

    }

    private String annaNappiTekstina(String pnappi) {
        if (pnappi.equals("S") || pnappi.equals("s")) {
            return "sotilasta";
        } else if (pnappi.equals("T") || pnappi.equals("t")) {
            return "tornia";
        } else if (pnappi.equals("R") || pnappi.equals("r")) {
            return "ratsua";
        } else if (pnappi.equals("L") || pnappi.equals("l")) {
            return "lahettia";
        } else if (pnappi.equals("K") || pnappi.equals("k")) {
            return "kuningasta";
        } else if (pnappi.equals("D") || pnappi.equals("d")) {
            return "kuningatarta";
        } else {
            throw new IllegalArgumentException("Epäkelpo napin syöte");
        }
    }

    private void lopetaPeli() {
        // lopetetaan peli
        // kysytaan, halutaanko tallentaa peli
        while (true) {
            System.out.println("Lopetetaanko peli? ( Y / N )");
            String vastaus = lukija.nextLine();
            if (vastaus.equals("Y") || vastaus.equals("y")) {
                while (true) {
                    System.out.println("Tallennetaanko peli? ( Y / N )");
                    String v2 = lukija.nextLine();
                    if (v2.equals("Y") || v2.equals("y")) {
                        tallennaPeli();
                        break;
                    } else if (v2.equals("N") || v2.equals("n")) {
                        break;
                    }
                }
                System.exit(0);
                break;
            } else if (vastaus.equals("N") || vastaus.equals("n")) {
                break;
            }
        }

    }

    private void tallennaPeli() {
        File file = new File("Tallenetut pelit.txt");
        try {
            PrintWriter kirjoittaja = new PrintWriter("Tallennetut pelit.txt");
            kirjoittaja.println("Pelaaja1: " + p1 + " Vari: " + p1Vari);
            kirjoittaja.println("Pelaaja2: " + p2 + " Vari: " + p2Vari);
            kirjoittaja.println("Seuraavana vuorossa on: " + vuoro);
            kirjoittaja.println("Laudan tilanne:");
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    String ruutu = "Ruutu " + x + " " + y;
                    if (lauta.getRuutu(x, y).onkoTyhja()) {
                        ruutu += " tyhja";
                    } else {
                        Nappi n = lauta.getRuutu(x, y).getNappi();
                        ruutu += " " + n.getArvo() + " " + n.getVari() + " " + n.getSiirrot();
                    }
                    kirjoittaja.println(ruutu);
                }
            }
            kirjoittaja.println("");
            kirjoittaja.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        }
    }

    private Arvo annaArvo(String s) {
        if (s.equals("TORNI")) {
            return Arvo.TORNI;
        } else if (s.equals("LAHETTI")) {
            return Arvo.LAHETTI;
        } else if (s.equals("RATSU")) {
            return Arvo.RATSU;
        } else if (s.equals("KUNINGAS")) {
            return Arvo.KUNINGAS;
        } else if (s.equals("KUNINGATAR")) {
            return Arvo.KUNINGATAR;
        } else {
            return Arvo.SOTILAS;
        }
    }

    private Vari annaVari(String s) {
        if (s.equals("VALKOINEN")) {
            return Vari.VALKOINEN;
        } else {
            return Vari.MUSTA;
        }
    }

}
