package KNN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class PlusProche extends AlgoClassification {
    public PlusProche(Donnees donneesEntrainement) {
        super(donneesEntrainement);
    }

    @Override
    public int predireEtiquette(Imagette imagette) {

        int distPix = 0;
        int[] distances = new int[this.donnees.tabImagettes.size()];

        for (int i = 0; i < this.donnees.tabImagettes.size(); i++) {

            distPix = 0;

            int[][] image = this.donnees.tabImagettes.get(i).getPixels();
            for (int j = 0; j < image.length; j++) {
                distPix += calculateDistance(imagette.getPixels()[j], image[j]);
            }

            distances[i] = distPix;
        }

        int indexMinDistance = findIndexOfMin(distances);
        System.out.println("l'index de la plus petite distance =  " + indexMinDistance);
        //System.out.println("Index min distance : " + indexMinDistance);
        return donnees.tabImagettes.get(indexMinDistance).getEtiquette();
    }

    private int calculateDistance(int[] img1, int[] img2) {
        int distance = 0;
        for (int i = 0; i < img1.length; i++) {
            distance += Math.abs(img1[i] - img2[i]);
        }
        return distance;
    }

    private int findIndexOfMin(int[] array) {
        int minimum = Arrays.stream(array).min().getAsInt();
        System.out.println(Arrays.toString(array));
        System.out.println("el min : " + minimum);
        System.out.println("en esperant que ça crash pas = " + Arrays.stream(array).min());
        for (int i = 0; i < array.length; i++) {
            if (array[i] == minimum) {
                System.out.println("index : "+i);
                return i;
            }
        }
        return -1;
    }

}
