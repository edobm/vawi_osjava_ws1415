package osjava.tl3.ui.fileseletion;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 * Ein Tablemodel für die Aufnahme von Eingabedateien und Ausgabeverzeichnissen
 * in Form von Instanzen der Klasse InputFileDescriptor
 * 
 * @author Meikel Bode
 */
public class InputFileTableModel extends DefaultTableModel {

    /**
     * Die Liste der Dateien/Verzeichnisse
     */
    private final ArrayList<InputFileDescriptor> inputFiles;

    /**
     * Erzeugt eine neue Instanz des TableModels
     */
    public InputFileTableModel() {
        this.inputFiles = new ArrayList<>();
    }

    /**
     * Löscht alle Inputfiles
     */
    public void clear() {
        inputFiles.clear();
        fireTableDataChanged();
    }
    
    /**
     * Eine Datei hinzufügen
     *
     * @param inputFile Die Datei, die hinzugefügt werden soll
     */
    public void addInputFile(InputFileDescriptor inputFile) {

        for (InputFileDescriptor ifd : inputFiles) {
            if (ifd.getFile().equals(inputFile.getFile())) {
                System.out.println("Datei bereits hinzugefügt: " + inputFile.getFile().toString());
                return;
            }
        }
        inputFiles.add(inputFile);
        fireTableDataChanged();
    }

    public void removeSelectedFiles(int[] selectedFiles) {
        ArrayList<InputFileDescriptor> files = new ArrayList<>(selectedFiles.length);
        for (int idx : selectedFiles) {
            files.add(inputFiles.get(idx));
        }
        
        inputFiles.removeAll(files);
        fireTableDataChanged();
    }

    /**
     * Entfern die gewünschte Datei
     *
     * @param inputFile Die Datei die entfernt werden soll
     */
    public void removeInputFile(InputFileDescriptor inputFile) {
        inputFiles.remove(inputFile);
        fireTableDataChanged();
    }

    /**
     * Liefert die Anzahl der Inputdateien
     *
     * @return Die Anzahl der Inputdateien
     */
    @Override
    public int getRowCount() {
        if (inputFiles == null || inputFiles.isEmpty()) {
            return 0;
        } else {
            return inputFiles.size();
        }
    }

    /**
     * Dateiname
     *
     * @return Die Anzahl der Spalten
     */
    @Override
    public int getColumnCount() {
        return 1;
    }

    /**
     * Der Bezeichner der gegebenen Spalte
     *
     * @param columnIndex Index der Spalte
     * @return Der Bezeichner der Spalte mit dem gegebenen Index
     */
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
            default:
                return "Datei/Verzeichnis";
        }
    }

    /**
     * Die Klasse des Werts der Spalte mit dem gegebenen Index ist immer
     * InputFileDescriptor
     *
     * @param columnIndex Der Index
     * @return Die Klasse des Werts am gegebenen Index
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return InputFileDescriptor.class;
    }

    /**
     * Ob die Zelle am Zeilen- und Spaltenindex editiertbar ist. Ist immer false
     *
     * @param rowIndex Der Index der Zeile
     * @param columnIndex der Index der Spalte
     * @return Ob editierbar oder nicht. Immer False!
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Liefert den Wert an der Position Zeile und Spalte
     *
     * @param row Die Zeile
     * @param col Die Spalte
     * @return Der Wert des Models an den gegeben Indices
     */
    @Override
    public Object getValueAt(int row, int col) {

        if (getInputFiles().isEmpty() || row >= getInputFiles().size()) {
            return "";
        } else {
            return getInputFiles().get(row).getFile();
        }
    }

    /**
     * Liefert die Eingabedateien
     *
     * @return Die Eingabedateien
     */
    public ArrayList<InputFileDescriptor> getInputFiles() {
        return inputFiles;
    }
}