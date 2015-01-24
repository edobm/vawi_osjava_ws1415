package osjava.tl3.gui.components.fileselection;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;
import javax.swing.JDialog;

/**
 * Mit diesem Dialog werden Eingabedateien für Räume, Kurse, Studiengänge sowie
 * das Ausgabeverzeichnis für den Dateiexport selektiert.
 *
 * @author Meikel Bode
 */
public class InputFileDialog extends JDialog implements ComponentListener {
  
    /**
     * Das JPanel zur Anzeige und Auswahl selektierter Dateien/Verzeichnisse
     */
    private final InputFilePanel inputFilePanel;

    /**
     * Erzeugt eine neue Instanz dieses Dialoges
     *
     * @param owner Die Swing-Componente (Frame) der dieser Dialog gehört
     * @param title Der Titel des Dialoges
     * @param inputFileType Der Typ von Eingabedatein, den dieser Dialog
     * akzeptiert
     */
    public InputFileDialog(Frame owner, String title, InputFileType inputFileType) {
        super(owner, title);
        
        /**
         * Grundlegende Einstellungen festlegen
         */
        setLayout(new BorderLayout());
        setModal(true);
        setSize(640, 480);
        setLocationRelativeTo(null);
        setTitle(title);
        addComponentListener(InputFileDialog.this);

        /**
         * Das InputFilePanel auf den gegeben Dateityp initialisieren und 
         * dem Dialog hinzufügen
         */
        inputFilePanel = new InputFilePanel(inputFileType, this);
        add(inputFilePanel, BorderLayout.CENTER);

    }

    /**
     * Liefert die selektierten Dateien/Verzeichnisse
     *
     * @return Die selektierten Dateien/Verzeichnisse
     */
    public List<InputFileDescriptor> getSelectedFiles() {
        return this.inputFilePanel.getSelectedFiles();
    }

    /**
     * Beim Öffnen des Dialoges prüfen ob Dateien selektiert wurden. 
     * Wenn nicht direkt den Selektionsdialog zeigen.
     *
     * @param event Der Komponentenevent
     */
    @Override
    public void componentShown(ComponentEvent event) {
        if (inputFilePanel.getSelectedFiles().isEmpty()) {
            inputFilePanel.addFileAction();
        }
    }

    /**
     * Reagieren, wenn die Komponente in der Größe verändert wird
     * @param event Der Komponentenevent
     */
    @Override
    public void componentResized(ComponentEvent event) {
        // Nicht verwendet!
    }

    /**
     * Reagieren wenn die Komponente verschoben wird
     * @param event Der Komponentenevent
     */
    @Override
    public void componentMoved(ComponentEvent event) {
        // Nicht verwendet!
    }
    
   /**
     * Reagieren wenn die Komponente versteckt wird
     * @param event Der Komponentenevent
     */
    @Override
    public void componentHidden(ComponentEvent event) {
        // Nicht verwendet!
    }
    
}