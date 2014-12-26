package osjava.tl3.logic.io;

import osjava.tl3.model.Schedule;

/**
 * Ein Ausgabeformatierer für Planinstanzen für das CSV-Format
 *
 * @author Fabian Simon
 */
public class CSVOutputFormatter extends OutputFormatter {

    /**
     * Erzeugt für den eigegebenen Plan die CSV Ausgabe
     *
     * @param schedule Der auszugebende Plan
     * @param title Der Titel/Beschriftung des Plans
     * @return Die Repräsentation des Plans im Ausgabeformat
     */
    @Override
    public StringBuilder format(Schedule schedule, String title) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Liefert das Dateinamensuffix für CSV-Dateien
     *
     * @return Das Suffix (csv)
     */
    @Override
    public String getFileNameSuffix() {
        final String suffix = "csv";
        return suffix;
    }

}
