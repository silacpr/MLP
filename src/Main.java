import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String [] args) {
        double[][] tableET = {
                {-1,1,-1},
                {-1,-1,-1},
                {1,-1,-1},
                {1,1,1}
        };
        double[][] tableOU = {
                {-1,1,1},
                {-1,-1,-1},
                {1,-1,1},
                {1,1,1}
        };

        double[][] tableXOR = {
                {-1,1,1},
                {-1,-1,-1},
                {1,-1,1},
                {1,1,-1}
        };

        System.out.println(tableXOR.length);
        System.out.println(tableXOR[0].length);
        TransferSigmoide transferSigmoide = new TransferSigmoide();
        TransferTangenteHyperbolique transferTangenteHyperbolique = new TransferTangenteHyperbolique();
        MLP mlp = new MLP(new int[]{2,4},0.01,transferSigmoide);

        for (int i = 0; i < tableXOR.length; i++) {
            double[] resultats = mlp.execute(tableXOR[i]);
            System.out.println(Arrays.toString(resultats));
        }





    }

    public static double signeSomme(double[] perceptron, double[] exemples){
        double somme = 0;
        for (int i = 0 ; i < perceptron.length - 1 ; i++) {
            somme += perceptron[i] * exemples[i];
        }
        somme -= perceptron[perceptron.length - 1];

        if (somme < 0) {
            somme = -1;
        } else {
            somme = 1;
        }
        return somme;
    }

    public static double[] appPerceptron(double[] perceptron, double [] exemple,
                                         double sortie, double pas) {
	/*
	   mise a jour des poids du perceptron
	   suivant la regle d'apprentissage
	   du perceptron
	*/
        double signe = signeSomme(perceptron,exemple);
        //affichePoids(perceptron);
        if (signe != exemple[exemple.length-1]) {

            /* poids */
            for (int i = 0; i < perceptron.length - 1; i++) {
                perceptron[i] = perceptron[i] + pas * exemple[i] * (sortie - signe);
            }
            /* seuil */
            perceptron[perceptron.length - 1] = perceptron[perceptron.length - 1] + pas * -1 * (sortie - signe);

        }
        return perceptron;
    }
}
