public class TransferTangenteHyperbolique implements TransferFunction{
    /**
     * Function de transfert
     * @param value entrée
     * @return sortie de la fonction sur l'entrée
     */
    @Override
    public double evaluate(double value) {
        return Math.tanh(value);
    }
    /**
     * Dérivée de la fonction de tranfert
     * @param value entrée
     * @return sortie de la fonction dérivée sur l'entrée
     */
    @Override
    public double evaluateDer(double value) {
        return 1-Math.pow(evaluate(value),2);
    }
}
