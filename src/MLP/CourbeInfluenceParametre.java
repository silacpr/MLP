package MLP;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;

public class CourbeInfluenceParametre {

    public static void tracerCourbe(int[] layers, double[] tauxErreur,String file) {
        // Exemple d'utilisation pour illustrer l'influence des paramètres
        //int[] layers = {1, 2, 3, 4, 5};  // Valeurs du paramètre X (par exemple, nombre de neurones)
        //double[] tauxErreur = {10, 8, 6, 4, 2};  // Valeurs du taux d'erreur correspondant à chaque paramètre X

        // Tracer la courbe
        plotCourbe("Influence du nombre de neurones", "Nombre de neurones", "Taux d'erreur", layers, tauxErreur, file);
    }

    public static void plotCourbe(String titre, String labelX, String labelY, int[] xData, double[] yData, String outputFile) {
        // Créer une série de données
        XYSeries series = new XYSeries("Courbe");

        for (int i = 0; i < xData.length; i++) {
            series.add(xData[i], yData[i]);
        }

        // Créer une collection de séries de données
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        // Créer le graphique
        JFreeChart chart = ChartFactory.createXYLineChart(
                titre,     // Titre du graphique
                labelX,    // Nom de l'axe X
                labelY,    // Nom de l'axe Y
                dataset    // Données
        );

        // Sauvegarder le graphique dans un fichier
        try {
            ChartUtils.saveChartAsPNG(new File(outputFile), chart, 800, 600);
            System.out.println("Graphique sauvegardé avec succès dans : " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
