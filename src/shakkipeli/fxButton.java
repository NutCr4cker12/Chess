package shakkipeli;

import javafx.scene.control.Button;

public class fxButton {

    public Button btn;
    public int x;
    public int y;
    public boolean painettu;
    public int style;

    public fxButton(int x, int y) {
        this.btn = new Button();
        this.painettu = false;
        this.x = x;
        this.y = y;
        this.btn.setPrefSize(60, 60);
    }

    public void setColor(int i) {
        if (i == 1) {
            btn.setStyle("-fx-background-color: LightGoldenRodYellow ; -fx-border-color: black; -fx-font-size: 20px");
            this.style = 1;
        } else {
            btn.setStyle("-fx-background-color: Chocolate ; -fx-border-color: black; -fx-font-size: 20px");
            this.style = 0;
        }
    }

    public void setText(String text) {
        this.btn.setText(text);
    }

    public String getText() {
        return this.btn.getText();
    }

    public int[] getCoord() {
        int[] coord = {this.x, this.y};
        return coord;
    }

    public Button getBtn() {
        return this.btn;
    }

    public void setPainettu(boolean a) {
        this.painettu = a;
        korosta();
    }

    public boolean onPainettu() {
        return this.painettu;
    }

    public void showHint() {
        if (this.style == 1) {
            btn.setStyle("-fx-background-color: #ffd699 ; -fx-border-color: red; -fx-font-size: 20px");
        } else {
            btn.setStyle("-fx-background-color: BurlyWood ; -fx-border-color: red; -fx-font-size: 20px");
        }
    }

    public void korosta() {
        if (this.painettu) {
            showHint();
        } else { // ei oo painettu, palautetaa normaaliksi
            setColor(this.style);
        }
    }

    public void setOnAction() {
        this.btn.setOnAction((e) -> {
            this.painettu = !this.painettu;
        });
    }
}
