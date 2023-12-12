package KNN;

public class Statistiques {
    public double stats;

    public Statistiques(AlgoClassification algo, Donnees donnees){
        int cpt = 0;
        int cptTotal = 0;
        for (Imagette i : donnees.tabImagettes) {
            int chiffre = algo.predireEtiquette(i);
            System.out.println(chiffre);
            if (chiffre==i.getEtiquette()){
                cpt++;
            }
            cptTotal++;
        }
        this.stats = (double) cpt /cptTotal;
    }

}
