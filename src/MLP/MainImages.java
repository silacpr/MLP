package MLP;

import KNN.Donnees;
import KNN.Etiquettes;
import KNN.Imagette;
import KNN.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainImages {

    static File ftest_img = new File("./MNIST/train-images-idx3-ubyte");
    static File ft10k_img = new File("./MNIST/t10k-images.idx3-ubyte");


    static File ftest_etiquette = new File("./MNIST/train-labels.idx1-ubyte");
    static File ft10k_etiquette= new File("./MNIST/t10k-labels.idx1-ubyte");

    static double[][] exemples;

    static int cpt = 0;


    public static void main(String [] args) {
        //784 entrées : 28 x 28 pixels
        //10 sorties : 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 -> 10 neurones à normaliser :D


        try {
            enregistrerImages(ftest_img, ftest_etiquette,100);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }


        TransferFunction transferFunction = new TransferTangenteHyperbolique();

        System.out.println("Calcoul sur les images :");
        calcul(exemples, transferFunction);




        //System.out.println(Arrays.deepToString(exemples));

    }

    public static void calcul(double[][] table, TransferFunction transferFunction){
        MLP mlp = initialiserMLPEnFonctionDeLaTransferFunction(transferFunction, table);

        double[] resultats = new double[table.length];
        int max_apprentissage = 1000000;

        ArrayList<Boolean> test = new ArrayList<>();

        for(int i = 0; i<table.length; i++) test.add(false);

        //boolean[] res = new boolean[resultats.length];

        //boucle d'apprentissage
        while (test.contains(false)){
            for (int i = 0; i < table.length; i++) {
                double[] input = Arrays.copyOf(table[i],2);
                double[] out = new double[]{table[i][table[i].length-1]};
                resultats[i] = mlp.backPropagate(input,out);
            }

            // teste du MPL sur la table
            double[] sortie = new double[table.length];
            for (int i = 0; i < table.length; i++) {
                double[] input = Arrays.copyOf(table[i],2);
                double[] prediction = mlp.execute(Arrays.copyOf(table[i],2));

                // Appliquer une fonction de seuil pour obtenir des valeurs binaires
                sortie[i] = testSortie(transferFunction,table,sortie[i],prediction);
            }

            for (int i = 0; i < sortie.length; i++) {
                //res[i] = table[i][table[i].length-1] == sortie[i];
                //test.clear();
                test.set(i,table[i][table[i].length-1] == sortie[i]);
            }

            max_apprentissage--;
            if (max_apprentissage<=0){
                break;
            }
        }

        for (int i = 0; i < table.length; i++) {
            double[] prediction = mlp.execute(Arrays.copyOf(table[i],2));
            System.out.println(Arrays.toString(prediction));
        }

        //CourbeInfluenceParametre.tracerCourbe(new int[]{2,3,3,1},resultats,"file"+cpt+".png");
        //cpt++;
        System.out.println(Arrays.toString(resultats));
        System.out.println("Nombre d'itération : "+(1000000-max_apprentissage));
        System.out.println(test);
    }

    public static MLP initialiserMLPEnFonctionDeLaTransferFunction(TransferFunction transferFunction, double[][] table){
        MLP mlp = null;
        if (transferFunction.getClass() == TransferSigmoide.class) {
            // Attention : le nombre de neurones de le 1ère couche ne doit pas dépasser le nombre de valeur dans input donc
            //              ici nos input sont les lignes des tables - la dernière valeur à chaque fois
            // Attention 2 : le nombre de neurones dans la dernière couche indique le nombre d'output que l'on veut.

            if (table != null) {
                mlp = new MLP(new int[]{2,3,3,1}, 1, transferFunction);
            }
            else {
                mlp = new MLP(new int[]{2,1}, 0.3, transferFunction);
            }

        }else if ( transferFunction.getClass() == TransferTangenteHyperbolique.class) {
            if (table != null) {
                mlp = new MLP(new int[]{2,3,3,1}, 1, transferFunction);
            }
            else mlp = new MLP(new int[]{2,1}, 0.01, transferFunction);
        }
        return mlp;
    }

    public static double testSortie(TransferFunction transferFunction, double[][] table, double sortie, double[] prediction){
        if (transferFunction.getClass() == TransferSigmoide.class) {
           sortie = (prediction[0] > 0.1) ? 1.0 : -1.0;
        }else if ( transferFunction.getClass() == TransferTangenteHyperbolique.class) {
            sortie = (prediction[0] > 0) ? 1.0 : -1.0;
        }
        return sortie;
    }

    public static void enregistrerImages(File file, File etiquette,int diviseur) throws IOException {


        File f = file;
        InputStream inputStream = new FileInputStream(f);
        DataInputStream data = new DataInputStream(inputStream);
        System.out.println("test");

        int type = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();
        int nbImages = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();
        int nbLignes = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();
        int nbCol = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();

        exemples = new double[nbImages / diviseur][nbLignes*nbCol+1];

        Etiquettes etiquettes = new Etiquettes(etiquette);
        ArrayList<Integer> listE = etiquettes.getListChiffre();

        double max = 256.0;

        System.out.println("eh oh = " +nbImages/diviseur);

        for (int i = 0; i < nbImages/diviseur; i++) {
            //System.out.println("Ah");
            int index = 0;
            for (int row = 0; row < nbLignes; row++) {
                for (int col = 0; col < nbCol; col++) {
                    int valeur = data.readUnsignedByte();
                    if (max<valeur) max = valeur;
                    exemples[i][index] = valeur/max;

                    index++;
                }
            }
           exemples[i][index] = listE.get(i) / max;
        }
        System.out.println(Arrays.deepToString(exemples));
        System.out.println("le MAAAAX : " +max);


    }



}
