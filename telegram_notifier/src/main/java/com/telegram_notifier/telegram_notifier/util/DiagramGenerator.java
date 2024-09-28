package com.telegram_notifier.telegram_notifier.util;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DiagramGenerator {

    public static File generateChart(DefaultPieDataset dataset) {
        try {
            // Formato data in italiano
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.ITALY);
            String timestamp = sdf.format(new Date());

            JFreeChart chart = ChartFactory.createPieChart3D(
                    "Overview - " + timestamp,
                    dataset,
                    true, 
                    true, 
                    false 
            );

            // Imposta PiePlot3D
            PiePlot3D plot = (PiePlot3D) chart.getPlot();
            plot.setShadowXOffset(3);
            plot.setShadowYOffset(3);
            plot.setForegroundAlpha(0.8f);

            // Genera colori casuali unici per le sezioni
            Set<Color> usedColors = new HashSet<>();
            for (Object key : dataset.getKeys()) {
                Color randomColor = generateUniqueRandomColor(usedColors);
                plot.setSectionPaint((Comparable) key, randomColor); // Cast key to Comparable
            }

            // Aggiungi etichette con percentuali
            PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator("{0}: {1} ({2})");
            plot.setLabelGenerator(generator);

            // Crea la cartella se non esiste
            File directory = new File("charts");
            if (!directory.exists()) {
                directory.mkdirs(); // Crea la cartella
            }

            // Genera un timestamp per il nome del file
            String fileTimestamp = new SimpleDateFormat("dd_MM_yyyy_HHmmss").format(new Date());
            File chartFile = new File(directory, "card_counts_chart_" + fileTimestamp + ".png");
            ChartUtils.saveChartAsPNG(chartFile, chart, 800, 600);
            return chartFile;
        } catch (IOException e) {
            log.error("Errore nella generazione del grafico", e);
            return null; // Return null in caso di errore
        }
    }

    // Metodo per generare un colore casuale unico
    private static Color generateUniqueRandomColor(Set<Color> usedColors) {
        Color color;
        do {
            // Genera un colore casuale
            color = new Color((int) (Math.random() * 0x1000000)); // Genera un colore casuale esadecimale
        } while (usedColors.contains(color)); // Ripeti finché il colore è già usato
        usedColors.add(color); // Aggiungi il colore utilizzato al set
        return color;
    }

}
