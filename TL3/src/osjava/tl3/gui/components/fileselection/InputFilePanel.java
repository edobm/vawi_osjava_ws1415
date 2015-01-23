package osjava.tl3.gui.components.fileselection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import osjava.tl3.logging.Protocol;

/**
 * UI Element zur anzeige eine Tabelle und Buttons zur Aufnahme von selektierten
 * Eingabedateien und Ausgabeverzeichnis
 *
 * @author Meikel Bode
 */
public class InputFilePanel extends JPanel {

    /**
     * Der akzeptierte Dateityp dieser Instanz. Der Wert bestimmt den
     * Laufzeitmodus!
     */
    private final InputFileType acceptedInputFileType;

    /**
     * Die Dialoginstanz, die dieses InputFilePanel anzeigt
     */
    private final JDialog parentDialog;

    /**
     * TableModel, Table und ScrollPane zur Aufnahme der Dateien und
     * Verzeichnisse
     */
    private InputFileTableModel inputFileTableModel;
    private JTable inputFileTable;
    private JScrollPane scrollPane;

    /**
     * Button Leiste und Buttons
     */
    private JPanel buttonPanelFileButtons;
    private JButton btnAddFile;
    private JButton btnRemoveFile;
    private JButton btnOk;

    /**
     * Der FileChooser für die Auswahl der Dateien und Verzeichnisse
     */
    private JFileChooser fileChooser;

    /**
     * Erzeugt eine neue Instanz mit dem gegebenen Dateityp und dem anzeigenden
     * Dialog
     *
     * @param acceptedType
     * @param parentDialog
     */
    public InputFilePanel(InputFileType acceptedType, JDialog parentDialog) {
        this.acceptedInputFileType = acceptedType;
        this.parentDialog = parentDialog;
        initLayout();
    }

    /**
     * Initialisiert das Layout
     */
    private void initLayout() {
        setLayout(new BorderLayout());

        /**
         * ScrollPane erzeugen
         */
        scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        /**
         * Tabellenmodel und Tabelle erzeugen und der ScrollPane hinzufügen
         */
        inputFileTableModel = new InputFileTableModel(acceptedInputFileType);
        inputFileTable = new JTable(inputFileTableModel);
        scrollPane.setViewportView(inputFileTable);

        /**
         * FileChooser erzeugen und initialisieren
         */
        fileChooser = new JFileChooser();
        if (acceptedInputFileType != InputFileType.OUTPUT_DIRECTORY) {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(true);
        } else {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
        }

        switch (acceptedInputFileType) {
            case COURSE_FILE:
                fileChooser.setDialogTitle("Lehrveranstaltungsdateien auswählen");
                break;
            case ROOM_FILE:
                fileChooser.setDialogTitle("Raumdateien auswählen");
                break;
            case STUDYPROGRAM_FILE:
                fileChooser.setDialogTitle("Studiengangsdateien auswählen");
                break;
            case OUTPUT_DIRECTORY:
                fileChooser.setDialogTitle("Ausgabeverzeichnis auswählen");
                break;
        }

        /**
         * Buttonpanel und Buttons erzeugen und dem Layout hinzufügen
         */
        buttonPanelFileButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(buttonPanelFileButtons, BorderLayout.SOUTH);

        btnAddFile = new JButton("Element hinzufügen");
        buttonPanelFileButtons.add(btnAddFile);
        btnAddFile.addActionListener((ActionEvent e) -> {
            addFileAction();
        });

        btnRemoveFile = new JButton("Auswahl entfernen");
        buttonPanelFileButtons.add(btnRemoveFile);
        btnRemoveFile.addActionListener((ActionEvent e) -> {
            removeFileAction();
        });

        btnOk = new JButton("OK");
        buttonPanelFileButtons.add(btnOk);
        btnOk.addActionListener((ActionEvent e) -> {
            okAction();
        });
    }

    /**
     * Fügt Dateien der Liste hinzu
     */
    protected void addFileAction() {

        /**
         * Dateiselektionsdialog zeigen
         */
        int result = fileChooser.showOpenDialog(this);

        /**
         * Dialogrückgabe behandeln
         */
        if (result == JFileChooser.APPROVE_OPTION) {
            
            /**
             * Je nach akzeptiertem Eingabedateityp reagieren
             */
            InputFileDescriptor fileDescriptor;
            if (acceptedInputFileType == InputFileType.OUTPUT_DIRECTORY) {
                /**
                 * Modus: Verzeichnis wählen
                 */
                inputFileTableModel.clear();
                File directory = fileChooser.getSelectedFile();
                fileDescriptor = new InputFileDescriptor(acceptedInputFileType, directory.exists() ? directory : directory.getParentFile());
                inputFileTableModel.addInputFile(fileDescriptor);
                Protocol.log("Ausgabeverzeichnis selektiert: " + fileDescriptor.getFile().getAbsolutePath());
            } else {
                /**
                 * Modus: Daitei(n) wählen
                 */
                for (File file : fileChooser.getSelectedFiles()) {
                    fileDescriptor = new InputFileDescriptor(acceptedInputFileType, file);
                    inputFileTableModel.addInputFile(fileDescriptor);
                    Protocol.log("Eingabedatei [Typ = " + acceptedInputFileType+"] selektiert: " + fileDescriptor.getFile().getName());
                }
            }
        }
    }

    /**
     * Entfernt die in der Tabelle selektierten Datein aus der Liste
     */
    private void removeFileAction() {
        inputFileTableModel.removeSelectedFiles(inputFileTable.getSelectedRows());
    }

    /**
     * Schließt den Dialog
     */
    private void okAction() {
        parentDialog.setVisible(false);
    }

    /**
     * Liefert den von diesem InputFilePanel akzeptierten Dateityp
     *
     * @return Der akzeptierte Dateityp
     */
    public InputFileType getAcceptedInputFileType() {
        return acceptedInputFileType;
    }

    /**
     * Liefert die Eingabedateien
     *
     * @return Die Dateien
     */
    public ArrayList<InputFileDescriptor> getSelectedFiles() {
        return inputFileTableModel.getSelectedFiles();
    }

}