package osjava.tl3.ui.components.fileselection;

import java.io.File;

/**
 * Stellt eine Eingabedatei eines bestimmten Typs dar
 * 
 * @author Meikel Bode
 */
public class InputFileDescriptor implements Comparable<InputFileDescriptor>{
    
    /**
     * Der Typ der Datei
     */
    private final InputFileType fileType;
    
    /**
     * Die Datei
     */
    private final File file;

    /**
     * Erzeugt eine neue Instanz des angebenen Eingabedateityps
     * @param fileType Der Typ der Datei
     * @param file  Die Datei
     */
    public InputFileDescriptor(InputFileType fileType, File file) {
        this.fileType = fileType;
        this.file = file;
    }

    /**
     * Liefert den Typ der Datei
     * @return Der Typ der Datei
     */
    public InputFileType getFileType() {
        return fileType;
    }

    /**
     * Liefert die Datei
     * @return Die Datei
     */
    public File getFile() {
        return file;
    }

    /**
     * Vergleicht einen InputFileDescriptor mit einem anderen auf Basis
     * des absoluten Dateipfades
     * @param other Die andere Instanz
     * @return Das Vergleichsergebnis
     */
    @Override
    public int compareTo(InputFileDescriptor other) {
        return file.getName().compareTo(other.getFile().getName());
    }
    
}