package KNN;

import MLP.TransferSigmoide;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws IOException {

        //Sauvegarder les train images

        File ftest_img = new File("./MNIST/train-images-idx3-ubyte");
        File ft10k_img = new File("./MNIST/t10k-images.idx3-ubyte");


        File ftest_etiquette = new File("./MNIST/train-labels.idx1-ubyte");
        File ft10k_etiquette= new File("./MNIST/t10k-labels.idx1-ubyte");


        Donnees dtest = enregistrerImages(ftest_img, ftest_etiquette);
        Donnees d2 = enregistrerImages(ft10k_img, ft10k_etiquette);

        AlgoClassification algoClassification = new PlusProche(d2);
        Statistiques statistiques = new Statistiques(algoClassification,dtest);

        System.out.println("Statistiques : "+statistiques.stats);
    }

    public static Donnees enregistrerImages(File file, File etiquette) throws IOException {
        File f = file;
        InputStream inputStream = new FileInputStream(f);
        DataInputStream data = new DataInputStream(inputStream);

        int type = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();
        int nbImages = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();
        int nbLignes = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();
        int nbCol = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();

        //System.out.println(type);
        //System.out.println(nbImages);
        //System.out.println(nbLignes);
        //System.out.println(nbCol);

        ArrayList<Imagette> imagettes = new ArrayList<>();
        for (int i = 0; i < nbImages / 10; i++) {
            Imagette imagette = new Imagette();
            int[][] pixels = new int[nbLignes][nbCol];
            for (int row = 0; row < nbLignes; row++) {
                for (int col = 0; col < nbCol; col++) {
                    pixels[col][row] = data.readUnsignedByte();
                }
            }
            imagette.setPixels(pixels);
            imagettes.add(imagette);
        }

        //Sauvegarder les étiquettes
        Etiquettes etiquettes = new Etiquettes(etiquette);
        ArrayList<Integer> listE = etiquettes.getListChiffre();

        //System.out.println("le size = " + listE.size());
        //System.out.println("Premier chiffre : " + listE.get(0));
        //System.out.println("Dernier chiffre : " + listE.get(listE.size()-1));
        //System.out.println("taille imagette = " + imagettes.size());
        for (int i = 0; i < listE.size() / 10; i++) {
            imagettes.get(i).setEtiquette(listE.get(i));
        }

        return new Donnees(imagettes);
    }
}
