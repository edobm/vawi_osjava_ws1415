package osjava.tl3.ui.components.fileselection;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JDialog;

/**
 * Mit diesem Dialog werden Eingabedateien für Räume, Kurse, Studiengänge
 * sowie das Ausgabeverzeichnis für den Dateiexport selektiert.
 * 
 * @author Meikel Bode
 */
public class InputFileDialog extends JDialog {
    
    /**
     * Das JPanel zur Anzeige und Auswahl selektierter Dateien/Verzeichnisse
     */
    private final InputFilePanel inputFilePanel;
    
    /**
     * Erzeugt eine neue Instanz dieses Dialoges
     * @param owner Die Swing-Componente (Frame) der dieser Dialog gehört
     * @param title Der Titel des Dialoges
     * @param inputFileType Der Typ von Eingabedatein, den dieser Dialog akzeptiert
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
        
        /**
         * Das InputFilePanel initialisieren und dem Dialog hinzufügen
         */
        inputFilePanel = new InputFilePanel(inputFileType, this);
        add(inputFilePanel, BorderLayout.CENTER);
        
        /**
         * Window events abfangen.
         * Wenn der Dialog sichtbar wird und bisher keine Dateien oder
         * Verzeichnisse selektiert wurden direkt den FileChooser anzeigen
         */
        addWindowListener(new WindowAdapter() {

            /**
             * Beim Öffnen des Dialoges prüfen ob Dateien selektiert wurden.
             * Wenn nicht, direkt den Selektionsdialog zeigen
             * @param e Der Fensterevent
             */
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                if (inputFilePanel.getSelectedFiles().isEmpty()) {
                    inputFilePanel.addFileAction();
                }
            }
            
        });
    }
    
    /**
     * Liefert die selektierten Dateien/Verzeichnisse
     * @return Die selektierten Dateien/Verzeichnisse
     */
    public List<InputFileDescriptor> getSelectedFiles() {
        return this.inputFilePanel.getSelectedFiles();
    }
}