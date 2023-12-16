package MLP;

import KNN.*;
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
            enregistrerImages(ftest_img, ftest_etiquette,1);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }


        TransferFunction transferFunction = new TransferSigmoide();

        System.out.println("Calcoul sur les images :");
        calcul(exemples, transferFunction);

        //System.out.println(Arrays.deepToString(exemples));

    }

    public static MLP initialiserMLPEnFonctionDeLaTransferFunction(TransferFunction transferFunction, double[][] table){
        MLP mlp = null;
        if (transferFunction.getClass() == TransferSigmoide.class) {
            // Attention : le nombre de neurones de le 1ère couche ne doit pas dépasser le nombre de valeur dans input donc
            //              ici nos input sont les lignes des tables - la dernière valeur à chaque fois
            // Attention 2 : le nombre de neurones dans la dernière couche indique le nombre d'output que l'on veut.

            mlp = new MLP(new int[]{784,10}, 0.1, transferFunction);

            /*
            if (table != null) {
                mlp = new MLP(new int[]{2,3,3,1}, 1, transferFunction);
            }
            else {
                mlp = new MLP(new int[]{2,1}, 0.3, transferFunction);
            }

             */

        }else if ( transferFunction.getClass() == TransferTangenteHyperbolique.class) {

            mlp = new MLP(new int[]{784,10}, 0.01, transferFunction);
            /*
            if (table != null) {
                mlp = new MLP(new int[]{2,3,3,1}, 1, transferFunction);
            }
            else mlp = new MLP(new int[]{2,1}, 0.01, transferFunction);

             */
        }
        return mlp;
    }

    public static void calcul(double[][] table, TransferFunction transferFunction){
        MLP mlp = initialiserMLPEnFonctionDeLaTransferFunction(transferFunction, table);

        double[] resultats = new double[table.length];
        int max_apprentissage = 100;

        ArrayList<Boolean> test = new ArrayList<>();

        for(int i = 0; i<table.length; i++) test.add(false);

        //boolean[] res = new boolean[resultats.length];

        //boucle d'apprentissage
        while (test.contains(false)){
            for (int i = 0; i < table.length; i++) {
                double[] input = Arrays.copyOf(table[i],table[i].length-10);
                double[] out = new double[]{
                        table[i][table[i].length-10],
                        table[i][table[i].length-9],
                        table[i][table[i].length-8],
                        table[i][table[i].length-7],
                        table[i][table[i].length-6],
                        table[i][table[i].length-5],
                        table[i][table[i].length-4],
                        table[i][table[i].length-3],
                        table[i][table[i].length-2],
                        table[i][table[i].length-1],
                };
                resultats[i] = mlp.backPropagate(input,out);
            }

            // teste du MPL sur la table
            double[] sortie = new double[table.length];
            for (int i = 0; i < table.length; i++) {
                //double[] input = Arrays.copyOf(table[i],table[i].length-1);
                double[] prediction = mlp.execute(Arrays.copyOf(table[i],table[i].length-1));
                sortie[i] = testSortie(prediction);
            }

            for (int i = 0; i < sortie.length; i++) {
                int chiffre = 11;
                for (int j = table[i].length-11; j < table[i].length ; j++) {
                    if (table[i][j]==1){
                        chiffre = j - table[i].length+10;
                        break;
                    }
                }
                //System.out.println(chiffre + " == "+sortie[i]);
                boolean res = chiffre == sortie[i];

                test.set(i,res);
            }

            max_apprentissage--;
            if (max_apprentissage<=0){
                break;
            }
        }

        /*
        for (int i = 0; i < table.length; i++) {
            double[] prediction = mlp.execute(Arrays.copyOf(table[i],table[i].length-1));
            System.out.println("Prédiction = "+Arrays.toString(prediction));
        }*/

        //CourbeInfluenceParametre.tracerCourbe(new int[]{2,3,3,1},resultats,"file"+cpt+".png");
        //cpt++;
        //System.out.println("Erreur = "+Arrays.toString(resultats));
        System.out.println("Nombre d'itération : "+(1000-max_apprentissage));
        System.out.println(test);
        System.out.println(statistiques(test)*100+ " % de réussite !");
    }

    public static double testSortie(double[] prediction){
        int chiffrePredit = 0;
        double max = 0;
        for (int i = 0; i < prediction.length; i++) {
            if (prediction[i]>max){
                max = prediction[i];
                chiffrePredit = i;
            }
        }
        //System.out.println(chiffrePredit);
        return chiffrePredit;
    }

    public static void enregistrerImages(File file, File etiquette,int diviseur) throws IOException {
        File f = file;
        InputStream inputStream = new FileInputStream(f);
        DataInputStream data = new DataInputStream(inputStream);

        int type = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();
        int nbImages = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();
        int nbLignes = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();
        int nbCol = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();

        exemples = new double[nbImages / diviseur][nbLignes*nbCol+10];

        Etiquettes etiquettes = new Etiquettes(etiquette);
        ArrayList<Integer> listE = etiquettes.getListChiffre();

        double max = 256.0;

        System.out.println("eh oh = " +nbImages/diviseur);

        for (int i = 0; i < nbImages/diviseur; i++) {

            int index = 0;
            for (int row = 0; row < nbLignes; row++) {
                for (int col = 0; col < nbCol; col++) {
                    int valeur = data.readUnsignedByte();
                    if (max<valeur) max = valeur;
                    exemples[i][index] = valeur/max;

                    index++;
                }
            }
            //exemples[i][index] = listE.get(i) / max;
            exemples[i] = ajouterDesire(exemples[i],listE.get(i));
            /*
            for (int j = 0; j < 10; j++) {
                System.out.println(exemples[i][exemples[i].length-10+j]+" -> chiffre : "+listE.get(i));
            }*/
        }
        //System.out.println(Arrays.deepToString(exemples));
        //System.out.println("le MAAAAX : " +max);
    }

    public static double[] ajouterDesire(double[] ex,int chiffre){
        ArrayList<Integer> listDes = new ArrayList<>();

        for (int i = 0; i <= chiffre; i++) {
            if (i==chiffre){
                listDes.add(1);
            }else {
                listDes.add(0);
            }
        }

        for (int j = 9; j > chiffre ; j--) {
            listDes.add(0);
        }

        for (int k = ex.length-10; k < ex.length; k++) {
            ex[k] = listDes.get(k-ex.length+10);
        }

        //System.out.println(listDes.toString());
        return ex;
    }

    public static double statistiques(ArrayList<Boolean> res){
        int cpt = 0;
        int cptTotal = 0;
        for (boolean trouve : res) {
            if (trouve){
                cpt++;
            }
            cptTotal++;
        }
        return (double) cpt /cptTotal;
    }



}
