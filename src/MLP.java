import java.util.Arrays;

public class MLP  {
    protected double	        fLearningRate = 0.6;
    protected Layer[]		fLayers;
    protected TransferFunction 	fTransferFunction;

    /**
     * @param layers Nb neurones par couches ->
     *               taille du tableau = nb de couches cachées ;
     *               valeurs des indexs = nombre de neurones dans la couche à l'indice correspondant
     * @param learningRate tx d'apprentissage
     * @param fun Function de transfert
     */

    public MLP(int[] layers, double learningRate, TransferFunction fun) {
        fLearningRate = learningRate;
        fTransferFunction = fun;

        fLayers = new Layer[layers.length];
        for(int i = 0; i < layers.length; i++) {
            if(i != 0) {
                // new Layer(nombre de neurons, taille de la couche précédente)
                fLayers[i] = new Layer(layers[i], layers[i - 1]);
            } else {
                //couche initiale / première couche
                //new Layer(nombre de neurones, taille de la couche précédente : ici aucune donc 0)
                fLayers[i] = new Layer(layers[i], 0);
            }
        }
    }

    /**
     * Réponse à une entrée
     *
     * @param input l'entrée testée
     * @return résultat de l'exécution
     */
    public double[] execute(double[] input) {
        // indices de parcours pour les boucles for
        int i, j, k;
        // valeur calculée de la sortie
        double new_value;

        //output : tableau de double -> taille = nombre de neurones de la dernière couche du réseau
        double output[] = new double[fLayers[fLayers.length - 1].Length];

        // input en entrée du réseau
        // le nombre de neurones de la 1ère couche doit correspondre au nombre de valeurs contenues dans input
        // exemple : pour 3 valeurs dans input il faut 3 neurones dans la 1ère couche
        // exemple 2 : si le nombre de valeurs dans input est supérieur au nombre de neurones
        //              alors toutes les input ne seront pas prises en compte en entrée du réseau
        for(i = 0; i < fLayers[0].Length; i++) {
            fLayers[0].Neurons[i].Value = input[i];
        }

        // calculs couches cachées et sortie
        // on calcule pour toutes les couches cachées sauf la 1ère
        // pour chaque couche :
        for(k = 1; k < fLayers.length; k++) {
            // pour tous les neurones dans la couche à l'indice k :
            for(i = 0; i < fLayers[k].Length; i++) {
                // on initialise la nouvelle valeur à 0.0
                new_value = 0.0;
                // pour tous les neurones de la couche précédente :
                for(j = 0; j < fLayers[k-1].Length; j++)
                    // on augmente la valeur du neurone actuel avec les (poids * la valeur du neurone de la couche
                    // précédente à l'indice j)
                    new_value += fLayers[k].Neurons[i].Weights[j] * fLayers[k - 1].Neurons[j].Value;

                //on enlève à la valeur du neurone actuel son Bias (?)
                new_value -= fLayers[k].Neurons[i].Bias;
                //on remplace la valeur du neurone actuel par le résultat de son évaluation par la fct de transfert
                fLayers[k].Neurons[i].Value = fTransferFunction.evaluate(new_value);
            }
        }

        // Renvoyer sortie
        // pour tous les neurones de la dernière couche :
        for(i = 0; i < fLayers[fLayers.length-1].Length; i++) {
            // on ajoute au tableau ouput la valeur du neurone à l'indice i
            output[i] = fLayers[fLayers.length-1].Neurons[i].Value;
        }
        return output;
    }

    /**
     * Rétropropagation
     * @param input    L'entrée courante
     * @param output   Sortie souhaitée (apprentissage supervisé !)
     * @return Error différence entre la sortie calculée et la sortie souhaitée
     */

    public double backPropagate(double[] input, double[] output) {
        //initialise new_output avec les valeurs de sortie des neurones de la dernière couche pour les valeurs d'input
        // new_output doit être de la même taille q'output
        double new_output[] = execute(input);
        //System.out.println(Arrays.toString(new_output));
        double error;
        int i, j, k;


        // Erreur de sortie
        // pour tous les neurones de la dernière couche / le nombre de sorties
        for(i = 0; i < fLayers[fLayers.length - 1].Length; i++) {
            // l'erreur = la valeur de la sortie désirée - la sortie constatée
            error = output[i] - new_output[i];
            // le Delta de neurone à l'indice i = erreur calculé * l'évaluation de la dérivée
            fLayers[fLayers.length-1].Neurons[i].Delta = error * fTransferFunction.evaluateDer(new_output[i]);
        }

        for(k = fLayers.length - 2; k >= 0; k--) {
            // Calcul de l'erreur courante pour les couches cachées
            // et mise à jour des Delta de chaque neurone
            for(i = 0; i < fLayers[k].Length; i++) {
                error = 0.0;
                for(j = 0; j < fLayers[k+1].Length; j++)
                    error += fLayers[k+1].Neurons[j].Delta * fLayers[k+1].Neurons[j].Weights[i];
                fLayers[k].Neurons[i].Delta = error * fTransferFunction.evaluateDer(fLayers[k].Neurons[i].Value);
            }
            // Mise à jour des poids de la couche suivante
            for(i = 0; i < fLayers[k+1].Length; i++) {
                for(j = 0; j < fLayers[k].Length; j++)
                    fLayers[k+1].Neurons[i].Weights[j] += fLearningRate * fLayers[k+1].Neurons[i].Delta *
                            fLayers[k].Neurons[j].Value;
                fLayers[k+1].Neurons[i].Bias -= fLearningRate * fLayers[k+1].Neurons[i].Delta;
            }
        }

        // Calcul de l'erreur
        error = 0.0;
        for(i = 0; i < output.length; i++) {
            error += Math.abs(new_output[i] - output[i]);
        }
        error = error / output.length;
        return error;
    }

    /**
     * @return LearningRate
     */
    public double getLearningRate() {
        return fLearningRate;
    }

    /**
     * maj LearningRate
     *
     * @param rate nouveau LearningRate
     */
    public void	setLearningRate(double rate) {
        fLearningRate = rate;
    }

    /**
     * maj fonction de tranfert
     *
     * @param fun nouvelle fonction de tranfert
     */
    public void setTransferFunction(TransferFunction fun) {
        fTransferFunction = fun;
    }

    /**
     * @return Taille couche d'entrée
     */
    public int getInputLayerSize() {
        return fLayers[0].Length;
    }

    /**
     * @return Taille couche de sortie
     */
    public int getOutputLayerSize() {
        return fLayers[fLayers.length - 1].Length;
    }
}