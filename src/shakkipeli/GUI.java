package shakkipeli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author User
 */
public class GUI extends Application {

    private Lauta lauta;
    private BorderPane asettelu;
    private Label infolabel;
    private Label errorlabel;
    private GridPane pane;
    private BorderPane nappiPane;
    private Button avustinBtn;
    private Button tallennaJaLopetaBtn;
    private Label avustinLabel;
    private GridPane avustinPane;
    private String origStyle;
    private VBox labelbox;

    private ArrayList<fxButton> lista = new ArrayList<>();
    private int valintoja;
    private int xEka;
    private int xToka;
    private int yEka;
    private int yToka;

    private String pelaaja1;
    private String pelaaja2;
    private Vari p1Vari;
    private Vari p2Vari;
    private Vari vuoro;

    private String nakyma;
    private Scene scene;

    private boolean avustin;
    private boolean shakkimatti;
    private boolean guiAlustettu;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage ikkuna) throws Exception {
        ikkuna.setTitle("Shakki");

        ikkuna.setScene(alkuNakyma());
        ikkuna.setResizable(false);

        new AnimationTimer() {

            @Override
            public void handle(long time) {
                if (nakyma.equals("alkunakyma")) {
                    ikkuna.setScene(alkuNakyma());
                } else if (nakyma.equals("pelaa")) {
                    ikkuna.setScene(scene);
                    pelaa();
                } else if (nakyma.equals("pelaajien valinta")) {
                    ikkuna.setScene(annaPelaajat());
                } else if (nakyma.equals("lataa")) {
                    ikkuna.setScene(vanhatPelitNakyma());
                }
            }
        }.start();

        ikkuna.show();
    }

    private void alustaGUI() {
        asettelu = new BorderPane();

        infolabel = new Label(pelaaja1 + "n (Valkoinen) vuoro");
        infolabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px");

        errorlabel = new Label();
        errorlabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px");

        labelbox = new VBox();
        labelbox.getChildren().addAll(infolabel, errorlabel);
        labelbox.setAlignment(Pos.CENTER);

        asettelu.setTop(labelbox);
        pane = new GridPane();

        if (vuoro == null) {
            vuoro = Vari.VALKOINEN;
        }

        for (int i = 0; i < 8; i++) {
            Label l = new Label(Integer.toString(8 - i));
            l.setStyle("-fx-font-weight: bold; -fx-font-size: 16px");
            pane.add(l, 0, i);
        }
        String[] columns = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (int i = 1; i < 9; i++) {
            Label l = new Label("      " + columns[i - 1]);
            l.setStyle("-fx-font-weight: bold; -fx-font-size: 16px");
            pane.add(l, i, 9);
        }

        int laskuri = 0;
        for (int x = 1; x < 9; x++) {
            laskuri++;
            for (int y = 0; y < 8; y++) {
                fxButton btn = new fxButton(x - 1, y);
                if (!(lauta.getRuutu(x - 1, y).onkoTyhja())) {
                    btn.setText(lauta.getRuutu(x - 1, y).getNappi().tulostaNappi());
                } else {
                    btn.setText(" ");
                }
                if (laskuri % 2 == 0) {     // joka toinen nappi tumma, joka toine vaalee
                    btn.setColor(1);
                } else {
                    btn.setColor(0);
                }
                btn.setOnAction();
                pane.add(btn.getBtn(), x, y);
                lista.add(btn);
                laskuri++;
            }
        }

        nappiPane = new BorderPane();

        tallennaJaLopetaBtn = new Button("Tallenna ja Lopeta");
        avustinBtn = new Button();
        avustinLabel = new Label("Avustin (Off)");
        avustinPane = new GridPane();

        avustinPane.add(avustinBtn, 0, 0);
        avustinPane.add(avustinLabel, 1, 0);

        nappiPane.setLeft(avustinPane);
        nappiPane.setCenter(tallennaJaLopetaBtn);

        asettelu.setBottom(nappiPane);
        asettelu.setCenter(pane);

        origStyle = avustinBtn.getStyle();

        tallennaJaLopetaBtn.setOnAction((e) -> {
            tallennaPeli();
        });
        avustinBtn.setOnAction((e) -> {
            avustin = !avustin;
            if (avustin) {
                avustinLabel.setText("Avustin (On) ");
                avustinBtn.setStyle("-fx-background-color: #b3e0ff ;");
            } else {
                avustinLabel.setText("Avustin (Off)");
                avustinBtn.setStyle(origStyle);
            }
        });
        scene = new Scene(asettelu);
        guiAlustettu = true;

    }

    private void alustaUusiPeli() {
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

    /**
     *
     * @param x1 = siirrettavan nappulan x-koordinaatti
     * @param y1 = siirrettavan nappulan y-koordinaatti
     * @param x2 = x-koordinaatti, mihin ruutuun nappula siirretaan
     * @param y2 = y-koordinaatti, mihin ruutuun nappula siirretaan
     * @param pelaajanVari = Kumman pelaajan vuoro
     * @return True jos oli sallittu siirto, false muutoin
     */
    private boolean kasitteleSiirto(int x1, int y1, int x2, int y2, Vari pelaajanVari) {
        if (lauta.getRuutu(x1, y1).onkoTyhja()) {           // lahtoruudussa ei ole nappulaa
            return false;
        } else {
            if (lauta.getRuutu(x1, y1).getNappi().getVari() != pelaajanVari) { // yrittaa siirtaa vaaran varista nappulaa
                return false;
            } else if (!(lauta.getRuutu(x2, y2).onkoTyhja()) && lauta.getRuutu(x2, y2).getNappi().getVari() == pelaajanVari) { // yrittaa siirtaa oman pelaajan paalle
                errorlabel.setText("Et voi syoda omaa pelaajaa!");
                return false;
            }
        }
        if (!(lauta.siirra(x1, y1, x2, y2))) {
            errorlabel.setText("Virheellinen siirto");
            return false;
        }
        siirra(x1, y1, x2, y2);
        errorlabel.setText("");
        return true;
    }

    /**
     * Asettaa kaikki tyhjat GUI-ruudut painamattomiksi
     */
    private void asetaKaikkiTyhjatPainamattomiksi() {
        for (fxButton btn : lista) {
            int x = btn.getCoord()[0];
            int y = btn.getCoord()[1];
            if (lauta.getRuutu(x, y).onkoTyhja()) {
                btn.setPainettu(false);
            }
        }
    }

    /**
     * Asettaa kaikki GUI-ruudut painamattomiksi
     */
    private void asetaKaikkiPainamattomiksi() {
        for (fxButton btn : lista) {
            btn.setPainettu(false);
        }
    }

    /**
     *
     * @param xEka = siirrettavan nappulan x-koordinaati
     * @param yEka = siirrettavan nappulan y-koordinaatti
     * @param xToka = x-koordinaatti, mihin ruutuun nappula siirretaan
     * @param yToka = y-koordinaatti, mihin ruutuun nappula siirretaan
     */
    private void siirra(int xEka, int yEka, int xToka, int yToka) {
        fxButton btn1 = lista.get(0);
        fxButton btn2 = lista.get(0);
        for (fxButton btn : lista) {
            int x = btn.getCoord()[0];
            int y = btn.getCoord()[1];
            if (x == xEka && y == yEka) {
                btn1 = btn;
            } else if (x == xToka && y == yToka) {
                btn2 = btn;
            }
        }
        btn2.setText(btn1.getText());
        btn1.setText((" "));
    }

    private void vaihdaPelaajaa() {
        if (vuoro == Vari.VALKOINEN) {
            vuoro = Vari.MUSTA;
        } else {
            vuoro = Vari.VALKOINEN;
        }
    }

    private Scene alkuNakyma() {
        File imageFile = new File("shakki.png");
        String fileLocation = imageFile.toURI().toString();
        Image image = new Image(fileLocation);
        ImageView imgview = new ImageView(image);
        BorderPane pane = new BorderPane();
        pane.setTop(imgview);

        Label l = new Label("   ");

        Button btn = new Button("Pelaa");
        Button vanha = new Button("Jatka vanhaa peliä");

        vanha.setOnAction((e) -> {
            nakyma = "lataa";
        });
        btn.setOnAction((e) -> {
            nakyma = "pelaajien valinta";
        });
        HBox box = new HBox();
        box.getChildren().addAll(btn, l, vanha);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10);
        pane.setCenter(box);

        nakyma = "alku";

        Scene scene = new Scene(pane);
        return scene;
    }

    private Scene vanhatPelitNakyma() {
        nakyma = "";
        GridPane pane = new GridPane();
        Button btn = new Button("Takaisin");
        Label label = new Label("Mitä peliä haluat jatkaa?");
        Label nimiLabel = new Label("Pelin nimi");
        Label pelaajat1Label = new Label("Pelaaja 1  ");
        Label pelaajat2Label = new Label("Pelaaja 2");

        pane.add(btn, 0, 0);
        pane.add(label, 1, 1);
        pane.add(nimiLabel, 1, 2);
        pane.add(pelaajat1Label, 2, 2);
        pane.add(pelaajat2Label, 3, 2);

        btn.setOnAction((e) -> {
            nakyma = "alkunakyma";
        });

        ArrayList<String> lista = getPelit();
        ArrayList<String> nimiLista = new ArrayList<>(); // nimiLista on muodossa "PelinNimi pelaaja1 pelaaja2 indeksi"
        for (int i = 0; i < lista.size(); i++) { // haetaan pelien ja niiden pelaajien nimet tallennetuista peleista
            if (lista.get(i).contains("NIMI: ")) {
                String pelitiedot = lista.get(i).split(" ")[1];
                pelitiedot += " " + lista.get(i + 1).split(" ")[1]; // pelaaja1:n nimi
                pelitiedot += " " + lista.get(i + 2).split(" ")[1]; // pelaaja2:n nimi
                pelitiedot += " " + i;
                nimiLista.add(pelitiedot);
            }
        }
        for (int i = 0; i < nimiLista.size(); i++) {
            Button valitseBtn = new Button("Jatka");
            Label peli = new Label(nimiLista.get(i).split(" ")[0]);
            Label p1Label = new Label(nimiLista.get(i).split(" ")[1]);
            Label p2Label = new Label(nimiLista.get(i).split(" ")[2]);
            pane.add(valitseBtn, 0, 3 + i);
            pane.add(peli, 1, 3 + i);
            pane.add(p1Label, 2, 3 + i);
            pane.add(p2Label, 3, 3 + i);
            int ind = Integer.parseInt(nimiLista.get(i).split(" ")[3]);
            valitseBtn.setOnAction((e) -> {
                ArrayList<String> jatkoLista = new ArrayList<>();
                for (int j = ind; j < ind + 70; j++) {
                    jatkoLista.add(lista.get(j));
                }
                jatkaVanhaaPelia(jatkoLista);
                nakyma = "pelaa";
            });
        }

        return new Scene(pane);
    }

    private ArrayList<String> getPelit() {
        ArrayList<String> lista = new ArrayList<>();
        try (Scanner lukija = new Scanner(new File("Tallennetut pelit.txt"))) {
            while (lukija.hasNextLine()) {
                lista.add(lukija.nextLine());
            }
        } catch (Exception e) {
        }
        return lista;
    }

    private Scene annaPelaajat() {
        GridPane pane = new GridPane();

        Label label = new Label("Anna pelaajien nimet:");

        Label pelaaja1Label = new Label("Valkoinen : ");
        Label pelaaja2Label = new Label("Musta : ");
        Label pelaaja1Label2 = new Label("(aloittaa pelin)");

        TextField pelaaja1Text = new TextField("");
        TextField pelaaja2Text = new TextField("");

        pelaaja1Text.setPrefWidth(100);
        pelaaja1Text.setPrefWidth(50);

        Label iLabel = new Label("");

        Button pelaa = new Button("Pelaa");
        Button takaisin = new Button("Takaisin");
        pelaa.setOnAction((e) -> {
            if (pelaaja1Text.getText() == null || pelaaja1Text.getText().equals("") || pelaaja2Text.getText() == null || pelaaja2Text.getText().equals("")) {
                iLabel.setText("Syota molemmille pelaajille nimet!");
            } else {
                pelaaja1 = pelaaja1Text.getText();
                pelaaja2 = pelaaja2Text.getText();
                p1Vari = Vari.VALKOINEN;
                p2Vari = Vari.MUSTA;
                alustaUusiPeli();
                nakyma = "pelaa";
            }
        });
        takaisin.setOnAction((e) -> {
            nakyma = "alkunakyma";
        });
        int rivi = 0;
        pane.add(takaisin, 0, rivi);
        rivi++;
        pane.add(label, 1, rivi);
        rivi++;
        pane.add(pelaaja1Label, 0, rivi);
        pane.add(pelaaja1Text, 1, rivi);
        pane.add(pelaaja1Label2, 2, rivi);
        rivi++;
        pane.add(pelaaja2Label, 0, rivi);
        pane.add(pelaaja2Text, 1, rivi);
        rivi++;
        pane.add(pelaa, 1, rivi);
        pane.add(iLabel, 1, rivi + 1);

        nakyma = "alku";

        Scene scene = new Scene(pane);
        return scene;
    }

    private void tallennaPeli() {
        Stage ikkuna2 = new Stage();
        ikkuna2.setResizable(false);
        ikkuna2.setTitle("Tallenna peli");
        GridPane pane = new GridPane();

        TextField tf = new TextField();
        Label iLabel = new Label("Tallenna nimellä : ");
        Label eLabel = new Label("");

        Button tallennaBtn = new Button("Tallenna");
        Button takaisin = new Button("Takaisin");
        Button lopeta = new Button("Lopeta");

        tallennaBtn.setOnAction((e) -> {
            if (tf.getText() == null || tf.getText().equals("")) {
                eLabel.setText("Anna ensin nimi");
            } else if (onJoSamanNiminen(tf.getText())) {
                eLabel.setText("Saman niminen peli on jo tallennettuna");
            } else {
                tallenna(tf.getText());
                eLabel.setText("Peli tallennettu!");
                pane.add(new Label(tf.getText()), 1, 1);
                tf.setVisible(false);
                tallennaBtn.setVisible(false);

            }
        });
        takaisin.setOnAction((e) -> {
            ikkuna2.close();
        });

        lopeta.setOnAction((e) -> {
            System.exit(0);
        });

        pane.add(takaisin, 0, 0);

        pane.add(iLabel, 0, 1);
        pane.add(tf, 1, 1);

        pane.add(tallennaBtn, 0, 2);
        pane.add(lopeta, 1, 2);

        BorderPane asettelu = new BorderPane();
        asettelu.setTop(pane);
        asettelu.setBottom(eLabel);

        Scene scene = new Scene(asettelu);

        ikkuna2.setScene(scene);
        ikkuna2.show();
    }

    private void tallenna(String nimi) {
        try {
            ArrayList<String> vanhatPelit = getPelit();
            PrintWriter kirjoittaja = new PrintWriter("Tallennetut pelit.txt");
            for (String rivi : vanhatPelit) {
                kirjoittaja.println(rivi);
            }

            kirjoittaja.println("NIMI: " + nimi);

            kirjoittaja.println("Pelaaja1: " + pelaaja1 + " Vari: " + p1Vari);
            kirjoittaja.println("Pelaaja2: " + pelaaja2 + " Vari: " + p2Vari);
            kirjoittaja.println("Seuraavana vuorossa on: " + vuoro);
            kirjoittaja.println("Laudan tilanne:");
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    String ruutu = "Ruutu " + x + " " + y;
                    if (lauta.getRuutu(x, y).onkoTyhja()) {
                        ruutu += " tyhja";
                    } else {
                        Nappi n = lauta.getRuutu(x, y).getNappi();
                        ruutu += " " + n.getArvo() + " " + n.getVari();
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

    private boolean onJoSamanNiminen(String nimi) {
        ArrayList<String> lista = new ArrayList<>();
        try (Scanner lukija = new Scanner(new File("Tallennetut pelit.txt"))) {
            while (lukija.hasNextLine()) {
                lista.add(lukija.nextLine());
            }
        } catch (Exception e) {
        }
        for (String rivi : lista) {
            if (rivi.contains("NIMI: ")) {
                String pelinNimi = rivi.split(" ")[1];
                if (nimi.equals(pelinNimi)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void jatkaVanhaaPelia(ArrayList<String> lista) {
        lauta = new Lauta();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).contains("Pelaaja")) { // haetaan pelaajat ja niiden varit
                if (lista.get(i).contains("Pelaaja1")) { // alustetaan pelaaja1
                    String[] rivi = lista.get(i).split(" ");
                    pelaaja1 = rivi[1];
                    p1Vari = annaVari(rivi[rivi.length - 1]);
                } else { // alustetaan pelaaja2
                    String[] rivi = lista.get(i).split(" ");
                    pelaaja2 = rivi[1];
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
                String arvo = rivi[3];
                Vari vari = annaVari(rivi[4]);
                Nappi n;
                if (arvo.equals("TORNI")) {
                    n = new Torni(vari);
                } else if (arvo.equals("LAHETTI")) {
                    n = new Lahetti(vari);
                } else if (arvo.equals("RATSU")) {
                    n = new Ratsu(vari);
                } else if (arvo.equals("KUNINGAS")) {
                    n = new Kuningas(vari);
                } else if (arvo.equals("KUNINGATAR")) {
                    n = new Kuningatar(vari);
                } else {
                    n = new Sotilas(vari);
                }
                lauta.setRuutu(x, y, n);
            }
        } // end of listan parseus
        nakyma = "pelaa";
    }

    private Vari annaVari(String s) {
        if (s.equals("VALKOINEN")) {
            return Vari.VALKOINEN;
        } else {
            return Vari.MUSTA;
        }
    }

    private void korostaKaikkiMahdollisetSiirrot(int x1, int y1) {
        for (fxButton btn : lista) {
            int[] xy = btn.getCoord();
            if (lauta.voikoSiirtaa(x1, y1, xy[0], xy[1]) && lauta.eiMattia(x1, y1, xy[0], xy[1])) {
                if (!(!(lauta.getRuutu(xy[0], xy[1]).onkoTyhja()) && lauta.getRuutu(xy[0], xy[1]).getNappi().getVari() == vuoro)) {
                    btn.showHint();
                }
            }
        }

    }

    private void onnittelut(String voittaja) {
        Stage ikkuna2 = new Stage();
        ikkuna2.setTitle("ShakkiMatti!");
        ikkuna2.setResizable(false);

        BorderPane pane = new BorderPane();
        Label iLabel = new Label("Onneksi olkoon " + voittaja + "!");
        pane.setTop(iLabel);
        
        Button lopeta = new Button("Lopeta");
        pane.setCenter(lopeta);
        lopeta.setOnAction((e) -> {
            System.exit(0);
        });

        ikkuna2.setScene(new Scene(pane));
        ikkuna2.show();
    }

    private void pelaa() {
        if (!guiAlustettu) {
            alustaGUI();
        }
        if (lauta.shakkiMatti(vuoro)) {
            String voittaja;
            if (vuoro == p1Vari) {
                voittaja = pelaaja2;
            } else {
                voittaja = pelaaja1;
            }
            infolabel.setText(voittaja + " VOITTI ");
            errorlabel.setText("!!! SHAKKIMATTI !!!");
            if (!shakkimatti) {
                shakkimatti = true;
                onnittelut(voittaja);
            }
            return;
        }
        String svari = "Valkoinen";
        if (vuoro == Vari.MUSTA) {
            svari = "Musta";
        }
        if (vuoro == p1Vari) {
            infolabel.setText(pelaaja1 + "n (" + svari + ") vuoro");
        } else {
            infolabel.setText(pelaaja2 + "n (" + svari + ") vuoro");
        }

        for (fxButton btn : lista) {
            int x = btn.getCoord()[0];
            int y = btn.getCoord()[1];
            if (lauta.onkoMattiTilanne(vuoro) && !(infolabel.getText().contains("   -  MATTI"))) {
                infolabel.setText(infolabel.getText() + "   -  MATTI");
            }
            if (valintoja == 0) { // ensimmaisen valinnan on pakko olla jokin oma nappi
                if (btn.onPainettu()) {
                    if (!(lauta.getRuutu(x, y).onkoTyhja()) && lauta.getRuutu(x, y).getNappi().getVari() == vuoro) {
                        btn.korosta();
                        valintoja++;
                        asetaKaikkiTyhjatPainamattomiksi();
                        xEka = x;
                        yEka = y;
                        if (avustin) {
                            korostaKaikkiMahdollisetSiirrot(x, y);
                        }
                        return;
                    }
                }
            } else if (valintoja == 1) {
                if (btn.onPainettu()) {
                    if (lauta.getRuutu(x, y).onkoTyhja() || lauta.getRuutu(x, y).getNappi().getVari() != vuoro) {
                        xToka = x;
                        yToka = y;
                        if (kasitteleSiirto(xEka, yEka, xToka, yToka, vuoro)) {
                            valintoja = 0;
                            vaihdaPelaajaa();
                            xEka = -1;
                            yEka = -1;
                            asetaKaikkiPainamattomiksi();
                        } else {
                            btn.setPainettu(false);
                        }

                        xToka = -1;
                        yToka = -1;
                    }
                } else if (!(btn.onPainettu())) {
                    if (x == xEka && y == yEka) { // valittua nappia painettu uudestaa, "undo" sen valinta
                        btn.korosta();
                        asetaKaikkiPainamattomiksi();
                        valintoja = 0;
                    }
                }
            }
        } // end for - btn listan lapikaynti
    }
}
