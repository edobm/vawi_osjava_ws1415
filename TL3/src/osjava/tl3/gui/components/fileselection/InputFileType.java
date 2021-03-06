package osjava.tl3.gui.components.fileselection;

/**
 * Stellt eine zu importierende Dateitype oder ein Ausgabeverzeichnis dar
 * 
 * @author Meikel Bode
 */
public enum InputFileType {
    
    /**
     * Eine Datei die Kurse enthält
     */
    COURSE_FILE,
    
    /**
     * Eine Datei die Räume enthält
     */
    ROOM_FILE,
    
    /**
     * Eine Datei die einen Studiengang enthält
     */
    STUDYPROGRAM_FILE,
    
    /**
     * Ein Verzeichnis, das für die Dateiausgabe genutzt werden soll
     */
    OUTPUT_DIRECTORY
    
}