package KNN;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Etiquettes {
    public ArrayList<Integer> listChiffre;

    public Etiquettes(File f) throws IOException {
        this.listChiffre = new ArrayList<>();

        InputStream inputStream = new FileInputStream(f);
        DataInputStream data = new DataInputStream(inputStream);

        int type = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();
        int nbElem = data.readUnsignedByte() << 24 | data.readUnsignedByte() << 16 | data.readUnsignedByte() << 8 | data.readUnsignedByte();

        /*
        System.out.println(type);
        System.out.println(nbElem);
         */

        for (int i = 0; i < nbElem; i++) {
            this.listChiffre.add(data.readUnsignedByte());
        }
    }

    public ArrayList<Integer> getListChiffre() {
        return listChiffre;
    }


}
