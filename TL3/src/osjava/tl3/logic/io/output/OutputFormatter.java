package osjava.tl3.logic.io.output;

import osjava.tl3.model.schedule.ScheduleView;

/**
 * Stelle eine abstrakte Klasse für Ausgabeformatierer bereit
 * @author Fabian Simon
 */
public abstract class OutputFormatter {
    
    /**
     * Formatiert den eingebenen Schedule im spezifischen Format der ausprägung
     * der Klasse
     * @param schedule Der auszugebende Plan
     * @param title Der Titel den der Plan haben soll
     * @return Der StringBuilder der den Text hält
     */
    public abstract StringBuilder format(ScheduleView schedule, String title);
    
    /**
     * Liefert das implementierungsspezifische Filename Suffiy
     * @return Das Suffix
     */
    public abstract String getFileNameSuffix();
    
}
