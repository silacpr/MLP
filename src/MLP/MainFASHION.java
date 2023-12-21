package MLP;

import KNN.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MainFASHION {

    static File ftest_img = new File("./FASHION/train-images-idx3-ubyte");
    static File ft10k_img = new File("./FASHION/t10k-images-idx3-ubyte");


    static File ftest_etiquette = new File("./FASHION/train-labels-idx1-ubyte");
    static File ft10k_etiquette= new File("./FASHION/t10k-labels-idx1-ubyte");

    static double[][] exemplesTest;
    static double[][] exemplesReels;

    public static void main(String [] args) {
         try {
             int diviseur = 100;
            exemplesTest = enregistrerImages(ftest_img, ftest_etiquette,diviseur);
            exemplesReels = enregistrerImages(ft10k_img, ft10k_etiquette, diviseur);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }

        TransferFunction transferFunction = new TransferSigmoide();
        ArrayList<Double> tauxErreurs = new ArrayList<>();
        MLP mlp = initialiserMLPEnFonctionDeLaTransferFunction(transferFunction);

        System.out.println("Calcul sur les images :");
        calcul(exemplesTest,exemplesReels, mlp, tauxErreurs);
        calcul(exemplesTest,exemplesReels, mlp, tauxErreurs);
    }

    public static MLP initialiserMLPEnFonctionDeLaTransferFunction(TransferFunction transferFunction){
        MLP mlp = null;
        if (transferFunction.getClass() == TransferSigmoide.class) {
            mlp = new MLP(new int[]{784,10}, 0.1, transferFunction);
        }else if ( transferFunction.getClass() == TransferTangenteHyperbolique.class) {
            mlp = new MLP(new int[]{784,10}, 0.01, transferFunction);
        }
        return mlp;
    }

    public static void calcul(double[][] tableTest,double[][] tableReel, MLP mlp, ArrayList<Double> resultats){

        int max_apprentissage = 100;

        ArrayList<Boolean> test = new ArrayList<>();

        for(int i = 0; i<tableTest.length; i++) test.add(false);

        //boolean[] res = new boolean[resultats.length];

        //boucle d'apprentissage
        while (test.contains(false)){

            //Entrainement avec les données de tests
            for (int i = 0; i < tableTest.length; i++) {
                double[] input = Arrays.copyOf(tableTest[i],tableTest[i].length-10);
                double[] out = new double[]{
                        tableTest[i][tableTest[i].length-10],
                        tableTest[i][tableTest[i].length-9],
                        tableTest[i][tableTest[i].length-8],
                        tableTest[i][tableTest[i].length-7],
                        tableTest[i][tableTest[i].length-6],
                        tableTest[i][tableTest[i].length-5],
                        tableTest[i][tableTest[i].length-4],
                        tableTest[i][tableTest[i].length-3],
                        tableTest[i][tableTest[i].length-2],
                        tableTest[i][tableTest[i].length-1],
                };
                resultats.add(mlp.backPropagate(input,out));
            }

            // teste du MPL sur les données réelles
            test = testMLP(tableTest,mlp,test);

            max_apprentissage--;
            if (max_apprentissage<=0){
                break;
            }
        }

        test = testMLP(tableReel,mlp,test);

        CourbeInfluenceParametre.tracerCourbe(
                "Fonction Sigmoïde"
                ,"Itérations",
                "Taux d'erreur",
                resultats.size(),resultats,
                "fonctionHyperboliqueInfluence50N50C_fashion.png");
        //System.out.println("Erreur = "+Arrays.toString(resultats));
        System.out.println("Nombre d'itération : "+(100-max_apprentissage));
        System.out.println(test);
        System.out.println(statistiques(test)*100+ " % de réussite !");
    }

    public static ArrayList<Boolean> testMLP(double[][] table,MLP mlp, ArrayList<Boolean> test){
        double[] sortie = new double[table.length];
        for (int i = 0; i < table.length; i++) {
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
            test.set(i,chiffre == sortie[i]);
        }
        return test;
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
        return chiffrePredit;
    }

    public static double[][] enregistrerImages(File file, File etiquette,int diviseur) throws IOException {
        File f = file;
        InputStream inputStream = new FileInputStream(f);
        DataInputStream data = new DataInputStream(inputStream);

        int type = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();
        int nbImages = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();
        int nbLignes = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();
        int nbCol = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();

        double[][] images = new double[nbImages / diviseur][nbLignes*nbCol+10];

        Etiquettes etiquettes = new Etiquettes(etiquette);
        ArrayList<Integer> listE = etiquettes.getListChiffre();

        double max = 256.0;
        System.out.println("eh oh = " +nbImages/diviseur);

        for (int i = 0; i < nbImages/diviseur; i++) {
            int index = 0;
            for (int row = 0; row < nbLignes; row++) {
                for (int col = 0; col < nbCol; col++) {
                    int valeur = data.readUnsignedByte();
                    images[i][index] = valeur/max;

                    index++;
                }
            }
            images[i] = ajouterDesire(images[i],listE.get(i));
        }
        return images;
    }

    public static double[] ajouterDesire(double[] ex,int chiffre){
        ArrayList<Integer> listDes = new ArrayList<>();

        for (int i = 0; i <= 9; i++) {
            if (i==chiffre){
                listDes.add(1);
            }else {
                listDes.add(0);
            }
        }

        for (int k = ex.length-10; k < ex.length; k++) {
            ex[k] = listDes.get(k-ex.length+10);
        }

        //System.out.println(listDes.toString());
        return ex;
    }

    public static double statistiques(ArrayList<Boolean> res){
        int cpt = 0;
        for (boolean trouve : res) {
            if (trouve){
                cpt++;
            }
        }
        return (double) cpt /res.size();
    }



}
