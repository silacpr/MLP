import java.lang.invoke.TypeDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    static final double[][] tableET = {
            {-1,1,-1},
            {-1,-1,-1},
            {1,-1,-1},
            {1,1,1}
    };
    static final double[][] tableOU = {
            {-1,1,1},
            {-1,-1,-1},
            {1,-1,1},
            {1,1,1}
    };

    static final double[][] tableXOR = {
            {-1,1,1},
            {-1,-1,-1},
            {1,-1,1},
            {1,1,-1}
    };

    public static void main(String [] args) {
        TransferFunction transferFunction = new TransferSigmoide();
        System.out.println("Calcul sur la table XOR : ");
        Main.calcul(tableXOR, transferFunction);

        System.out.println("\nCalcul sur la table ET : ");
        Main.calcul(tableET, transferFunction);

        System.out.println("\nCalcul sur la table OU : ");
        Main.calcul(tableOU, transferFunction);
    }

    public static void calcul(double[][] table, TransferFunction transferFunction){

        MLP mlp = initialiserMLPEnFonctionDeLaTransferFunction(transferFunction, table);

        double[] resultats = new double[table.length];
        int max_apprentissage = 1000000;

        ArrayList<Boolean> test = new ArrayList<>();
        test.add(false);test.add(false);test.add(false);test.add(false);



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
                //System.out.println(Arrays.toString(prediction));

                // Appliquer une fonction de seuil pour obtenir des valeurs binaires

                sortie[i] = testSortie(transferFunction,table,sortie[i],prediction);

                //System.out.println("Entrée : " + Arrays.toString(input) + ", Prédiction : " + sortie[i]);
            }

            for (int i = 0; i < sortie.length; i++) {
                //res[i] = table[i][table[i].length-1] == sortie[i];
                //test.clear();
                test.set(i,table[i][table[i].length-1] == sortie[i]);
            }
            //System.out.println(Arrays.toString(res));

            max_apprentissage--;
            if (max_apprentissage<=0){
                break;
            }
        }

        for (int i = 0; i < table.length; i++) {
            double[] prediction = mlp.execute(Arrays.copyOf(table[i],2));
            System.out.println(Arrays.toString(prediction));
        }

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
            if (table == tableET) {
                mlp = new MLP(new int[]{2,1}, 0.3, transferFunction);
            } else if (table == tableOU) {
                mlp = new MLP(new int[]{2,1}, 0.3, transferFunction);
            } else if (table == tableXOR) {
                mlp = new MLP(new int[]{2,3,3,1}, 1, transferFunction);
            }


        }else if ( transferFunction.getClass() == TransferTangenteHyperbolique.class) {
            if (table == tableET) {
                mlp = new MLP(new int[]{2,1}, 0.01, transferFunction);
            } else if (table == tableOU) {
                mlp = new MLP(new int[]{2,1}, 0.01, transferFunction);
            } else if (table == tableXOR) {
                mlp = new MLP(new int[]{2,3,3,1}, 1, transferFunction);
                //mlp = new MLP(new int[]{2,2,1}, 0.1, transferFunction);
            }
        }
        return mlp;
    }

    public static double testSortie(TransferFunction transferFunction, double[][] table, double sortie, double[] prediction){
        if (transferFunction.getClass() == TransferSigmoide.class) {
            if (table == tableXOR) {
                sortie = (prediction[0] > 0.9) ? 1.0 : -1.0;
            } else {
                sortie = (prediction[0] > 0.1) ? 1.0 : -1.0;
            }
        }else if ( transferFunction.getClass() == TransferTangenteHyperbolique.class) {
                sortie = (prediction[0] > 0) ? 1.0 : -1.0;
        }
        return sortie;
    }

//    public static double signeSomme(double[] perceptron, double[] exemples){
//        double somme = 0;
//        for (int i = 0 ; i < perceptron.length - 1 ; i++) {
//            somme += perceptron[i] * exemples[i];
//        }
//        somme -= perceptron[perceptron.length - 1];
//
//        if (somme < 0) {
//            somme = -1;
//        } else {
//            somme = 1;
//        }
//        return somme;
//    }
//
//    public static double[] appPerceptron(double[] perceptron, double [] exemple,
//                                         double sortie, double pas) {
//	/*
//	   mise a jour des poids du perceptron
//	   suivant la regle d'apprentissage
//	   du perceptron
//	*/
//        double signe = signeSomme(perceptron,exemple);
//        //affichePoids(perceptron);
//        if (signe != exemple[exemple.length-1]) {
//
//            /* poids */
//            for (int i = 0; i < perceptron.length - 1; i++) {
//                perceptron[i] = perceptron[i] + pas * exemple[i] * (sortie - signe);
//            }
//            /* seuil */
//            perceptron[perceptron.length - 1] = perceptron[perceptron.length - 1] + pas * -1 * (sortie - signe);
//
//        }
//        return perceptron;
//    }
}
