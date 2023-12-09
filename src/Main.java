import java.util.ArrayList;
import java.util.Arrays;

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
        System.out.println("Calcul sur la table XOR : ");
        Main.calcul(tableXOR);
        System.out.println("\nCalcul sur la table ET : ");
        Main.calcul(tableET);
        System.out.println("\nCalcul sur la table OU : ");
        Main.calcul(tableOU);
    }

    public static void calcul(double[][] table){
        TransferSigmoide transferSigmoide = new TransferSigmoide();
        TransferTangenteHyperbolique transferTangenteHyperbolique = new TransferTangenteHyperbolique();

        // Attention : le nombre de neurones de le 1ère couche ne doit pas dépasser le nombre de valeur dans input donc
        //              ici nos input sont les lignes des tables - la dernière valeur à chaque fois
        // Attention 2 : le nombre de neurones dans la dernière couche indique le nombre d'output que l'on veut.
        MLP mlp = new MLP(new int[]{2,1},0.03,transferSigmoide);

        double[] resultats = new double[table.length];
        int nombre_apprentissage = 100;
        for (int j = 0; j < nombre_apprentissage; j++) {
            //System.out.println("\nItération numéro : "+j);
            for (int i = 0; i < table.length; i++) {
                double[] input = Arrays.copyOf(table[i],2);
                double[] out = new double[]{table[i][table[i].length-1]};
                resultats[i] = mlp.backPropagate(input,out);
            }
        }

        if (Arrays.deepEquals(table, tableXOR)){
            boolean[] res = new boolean[resultats.length];
            for (int i = 0; i < resultats.length; i++) {
                if (resultats[i] < 1) {
                    resultats[i] = 1;
                }else{
                    resultats[i] = -1;
                }
            }
            //System.out.println(Arrays.toString(resultats));


            for (int i = 0; i < resultats.length; i++) {
                res[i] = table[i][table[i].length-1] == resultats[i];
            }
            System.out.println(Arrays.toString(res));
        }else{
            // teste du MPL sur la table
            for (int i = 0; i < table.length; i++) {
                double[] input = Arrays.copyOf(table[i],2);
                double[] prediction = mlp.execute(Arrays.copyOf(table[i],2));
                //System.out.println(Arrays.toString(prediction));

                // Appliquer une fonction de seuil pour obtenir des valeurs binaires
                for (int j = 0; j < prediction.length; j++) {
                    prediction[j] = (prediction[j] > 0.1) ? 1.0 : 0.0;
                }

                System.out.println("Entrée : " + Arrays.toString(input) + ", Prédiction : " + Arrays.toString(prediction));
            }
        }


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
