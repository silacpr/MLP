package KNN;

import java.util.ArrayList;

public class Donnees {
    public ArrayList<Imagette> tabImagettes;

    public Donnees(){
        this.tabImagettes = new ArrayList<>();
    }

    public Donnees(ArrayList<Imagette> list){
        this.tabImagettes = list;
    }
}
