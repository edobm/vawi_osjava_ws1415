package osjava.tl3.logic.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import osjava.tl3.model.Schedule;

/**
 * Diese Klasse stellt die Dateischreiboperationen bereit.
 *
 * @author Fabian Simon
 * @version 1.0
 */
public abstract class OutputFileWriter {

    /**
     * Formatierung von Zeitstempeln im Dateinamen
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");

    /**
     * Liefert das primäre Namenselement für den gegebenen Plan
     *
     * @param schedule Der Plan für den das primäre Namenselement ermittelt
     * werden soll
     * @return Das primäre Namenselement
     */
    public abstract String getPrimaryNameElement(Schedule schedule);

    /**
     * Schreibt eine Planinstanz im angebenen Ausgabeformat in den angegebenen
     * Ausgabepfad
     *
     * @param schedule Der Plan
     * @param outputFormat Das Ausgabeformat
     * @param outputPath Der Ausgabepfad
     */
    public abstract void writeSchedule(Schedule schedule, OutputFormat outputFormat, String outputPath);

    /**
     * Schreibt eine Planinstanz im angegebenen Ausgabeformat in den angebenen
     * Ausgabepfad
     *
     * @param schedule Der Plan
     * @param outputFormat Das Ausgabeformat
     * @param outputPath Der Ausgabepfad
     * @param fileNamePrefix Das Prefix das am Anfang des Dateinamens ausgegeben
     * werden soll
     * @param title Der Bezeichner für den Plan
     */
    protected void writeSchedule(Schedule schedule, OutputFormat outputFormat, String outputPath, String fileNamePrefix, String title) {

        /**
         * Wenn Scheduleinstanz null ist Fehler werfen
         */
        if (schedule == null) {
            throw new IllegalArgumentException("Given instance of Schedule must not be null!");
        }

        /**
         * Formatter Instance erzeugen
         */
        OutputFormatter formatter = OutputFormatterFactory.getInstance(outputFormat);

        /**
         * Schedule in Ausgabeformat umsetzen
         */
        StringBuilder sb = formatter.format(schedule, title);

        /**
         * Dateinamen erzeugen
         */
        StringBuilder fileName = new StringBuilder();
        fileName.append(outputPath).append(File.separator).append(fileNamePrefix);
        fileName.append(getFileName(getPrimaryNameElement(schedule), formatter.getFileNameSuffix()));

        /**
         * Datei ausgeben
         */
        writeFile(sb.toString(), fileName.toString());

    }

    /**
     * Erzeugt den Dateinamen für den gegeben Plan. Die Erzeugung ist
     * Implmentierungsspezifisch
     *
     * @param primaryNameElement Das primäre Namenselement auf dessen Basis der
     * Dateiname erzeugt werden soll
     * @param fileNameSuffix Das Suffix der zu erzeugenden Datei
     * @return Der Dateiname
     */
    public static String getFileName(String primaryNameElement, String fileNameSuffix) {
        StringBuilder sb = new StringBuilder();

        primaryNameElement = primaryNameElement.trim();
        primaryNameElement = primaryNameElement.replaceAll(" ", "_");

        sb.append(primaryNameElement);

        sb.append("_");

        sb.append(DATE_FORMAT.format(new Date()));

        sb.append(".");

        sb.append(fileNameSuffix);

        return sb.toString();
    }

    /**
     * Diese Methode erwartet als Eingabewert eine Liste von Strings. Diese wird
     * abgearbeitet und pro String wird eine Zeile in die Zieldatei für die
     * Ausgabe der Daten geschrieben. Dabei entsteht je nach Format der Strings
     * eine CSV- bzw. HTML-Datei
     *
     * @param output Die Liste aus Strings, welche in die Datei geschrieben
     * werden sollen
     * @param outputPath Der Ausgabepfad
     */
    public void writeFile(String output, String outputPath) {

        FileOutputStream fos = null;
        OutputStreamWriter osw = null;

        try {
            fos = new FileOutputStream(outputPath);
            osw = new OutputStreamWriter(fos, Charset.forName("UTF-8").newEncoder());
            osw.append(output);
            osw.flush();
            fos.flush();
            
        } catch (IOException ex) {
            Logger.getLogger(OutputFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                osw.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }

        }
    }

}
