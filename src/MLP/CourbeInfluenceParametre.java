package MLP;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CourbeInfluenceParametre {

    public static void tracerCourbe(String titre, String labelX, String labelY, int iterationLength, ArrayList<Double> data, String file) {
        //int iterationLength = nombre de fois que l'on a lancé la fonction d'apprentissage
        //double[] data = taux d'erreur moyen pour chaque itération
        // Tracer la courbe
        plotCourbe(titre, labelX, labelY, iterationLength, data, file);
    }

    public static void plotCourbe(String titre, String labelX, String labelY, int xData, ArrayList<Double> yData, String outputFile) {
        // Créer une série de données
        XYSeries series = new XYSeries("Courbe");

        for (int i = 0; i < xData; i++) {
            series.add(i, yData.get(i));
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
            ChartUtils.saveChartAsPNG(new File("./courbe/"+outputFile), chart, 800, 600);
            System.out.println("Graphique sauvegardé avec succès dans : " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
