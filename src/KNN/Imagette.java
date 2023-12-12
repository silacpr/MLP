package KNN;

import java.util.ArrayList;

public class Imagette {
    private int[][] pixels;

    private int etiquette;

    public Imagette(){}

    public int[][] getPixels() {
        return pixels;
    }

    public void setPixels(int[][] pixels) {
        this.pixels = pixels;
    }

    public int getEtiquette() {
        return etiquette;
    }

    public void setEtiquette(int etiquette) {
        this.etiquette = etiquette;
    }
}
