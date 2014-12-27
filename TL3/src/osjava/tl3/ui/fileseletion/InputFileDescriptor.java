package osjava.tl3.ui.fileseletion;

import java.io.File;

/**
 * Stellt eine Eingabedatei eines bestimmten Typs dar
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
     * @return the fileType
     */
    public InputFileType getFileType() {
        return fileType;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    @Override
    public int compareTo(InputFileDescriptor other) {
        return file.getName().compareTo(other.getFile().getName());
    }
    
}