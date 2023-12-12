package KNN;

import java.util.ArrayList;

public abstract class AlgoClassification {
    public Donnees donnees;

    public AlgoClassification(Donnees d){
        this.donnees = d;
    }

    public abstract int predireEtiquette(Imagette imagette);
}
