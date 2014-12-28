package osjava.tl3.ui;

import osjava.tl3.ui.fileseletion.InputFilePanel;
import osjava.tl3.ui.fileseletion.InputFileType;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import osjava.tl3.logic.io.CourseReader;
import osjava.tl3.logic.io.OutputController;
import osjava.tl3.logic.io.OutputFormat;
import osjava.tl3.logic.io.RoomReader;
import osjava.tl3.logic.io.StudyProgramReader;
import osjava.tl3.logic.planning.Scheduler;
import osjava.tl3.logic.planning.strategies.CostOptimizedStrategy;
import osjava.tl3.logic.planning.strategies.StrategyFactory;
import osjava.tl3.Protocol;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.MasterSchedule;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleElement;
import osjava.tl3.model.Semester;
import osjava.tl3.model.StudyProgram;
import osjava.tl3.model.controller.DataController;
import osjava.tl3.ui.fileseletion.InputFileDescriptor;

/**
 * Diese Klasse stellt ein grafisches Benutzer Interface zur Steuerung der
 * Planungslogik bereit.
 *
 * Die GUI stellt Möglichkeiten der Parametrisierung der Planungslogik und der
 * Eingabe und Ausgabe bereit. Weiterhin dient die GUI zu bequemen Auswahl
 * mittels JTree und Darstellung aller im Gesamtplan (MasterSchedule)
 * enthaltenen Plane (Schedule) in tabellarischer Form (JTable) bereit.
 *
 * @author Meikel Bode
 */
public class SchedulerUI extends JFrame {

    /**
     * Swing Komponenten für Fensterstruktur
     */
    private final JPanel panelTopButtons = new JPanel(new GridLayout(1, 6, 5, 5));
    private final JSplitPane splitPane = new JSplitPane();
    private final JScrollPane scrollPaneTree = new JScrollPane();
    private final JScrollPane scrollPaneTable = new JScrollPane();
    private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    private final LoggingPanel loggingPanel = new LoggingPanel();
    private final JTree treeMasterSchedule = new JTree();
    private final JLabel lSelectNode = new JLabel("Bitte wählen Sie das Objekt in der Baumansicht, dessen Plan Sie einsehen möchten", SwingConstants.CENTER);

    /**
     * Swing Tabelle und Model für Plandarstellung
     */
    private final ScheduleTableModel scheduleTableModel = new ScheduleTableModel();
    private final ScheduleTable scheduleTable = new ScheduleTable(scheduleTableModel);

    /**
     * Swing Komponenten für Raumselektion
     */
    private final JButton btnSelectRooms = new JButton("Räume (0)");
    private final JButton btnSelectCourses = new JButton("Lehrveranstaltungen (0)");
    private final JButton btnSelectStudyPrograms = new JButton("Studiengänge (0)");
    private JDialog dlgAddRooms;
    private InputFilePanel inputFilePanelRooms;
    private JDialog dlgAddCourses;
    private InputFilePanel inputFilePanelCourses;
    private JDialog dlgAddStudyPrograms;
    private InputFilePanel inputFilePanelStudyPrograms;

    /**
     * Swing Komponenten für Konfiguration der Planberechnung
     */
    private final JLabel labelStrategies = new JLabel("Planungsstrategie:");
    private final JComboBox<String> comboBoxStrategies = new JComboBox<>();
    private final JLabel labelCosts = new JLabel("Kosten pro externem Platz:");
    private final JFormattedTextField textFieldCosts = new JFormattedTextField(new DecimalFormat("#,##"));

    /**
     * Swing Komponenten für die Ausführung der Planberechnung
     */
    private final JButton btnGenerateMasterSchedule = new JButton("Gesamtplan berechnen");

    /**
     * Swing Komponenten für Konfiguration der Dateiausgabe
     */
    private final JButton btnSelectOutput = new JButton("Ausgabeverzeichnis");
    private final JLabel labelOutputDirectory = new JLabel();
    private final JLabel labelOutputFormat = new JLabel("Ausgabeformat:");
    private final JComboBox<String> comboBoxOutputFormat = new JComboBox<>();
    private JDialog dlgAddOutputDirectory;
    private InputFilePanel inputFilePanelOutputDirectory;

    /**
     * Swing Komponenten für Ausführung der Dateiausgabe
     */
    private final JButton btnOutputExecute = new JButton("Gesamtplan exportieren");

    /**
     * Instanz des DataControllers für Zugriff auf Entitäten
     */
    private DataController dataController;

    /**
     * Instanz des Planerstellungslogik
     */
    private Scheduler scheduler;

    /**
     * Instanz des Gesamtplans
     */
    private MasterSchedule masterSchedule;

    /**
     * Instanz des OutputControllers für die Dateiausgabe der erzeugten
     * Plandanten
     */
    private OutputController outputController;

    /**
     * Erzeugt eine neue Instanz der Klasse ScheduleGUI und nimmt dabei
     * grundlegende Einstellungen am UI vor.
     *
     * @throws HeadlessException Wird ausgelöst, wenn die unterliegende JVM im
     * headless Modus, läuft und damit eine grafische Benutzeroberfläche nicht
     * erzeugt werden kann.
     */
    public SchedulerUI() throws HeadlessException {

        /**
         * Grundlegende Fensterkonfiguration vornehmen
         */
        setTitle("Schedule Planner: VAWi WS14/15 OSJAVA TL3 (Gruppe 1: Christoph Lurz, Christian Müller, Fabian Simon, Meikel Bode)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 900);
        setLocationRelativeTo(null);

        /**
         * Grundlegende Fensterevents registrieren
         */
        initializeWindowEvents();

        /**
         * Swing Komponenten erzeugen und Konfigurieren
         */
        initializeComponents();

        /**
         * Events für den Planstrukturbaum registrieren
         */
        initializeTreeEvents();

        /**
         * Meldung ausgeben
         */
        Protocol.log("Schedule Planner: VAWi WS14/15 OSJAVA TL3 (Gruppe 1: Christoph Lurz, Christian Müller, Fabian Simon, Meikel Bode)");
        Protocol.log("Programm initialisiert");
    }

    /**
     * UI Komponenten initialisieren
     */
    private void initializeComponents() {

        /**
         * Grundlegendes Fensterlayout festlegen auf BorderLayout
         */
        setLayout(new BorderLayout());

        /**
         * Buttonleiste oben platzieren
         */
        getContentPane().add(panelTopButtons, BorderLayout.NORTH);

        /**
         * Swing Komponenten für die Dateiauswahl
         */
        JPanel fileButtons = new JPanel(new GridLayout(4, 1));
        panelTopButtons.add(fileButtons);
        fileButtons.setBorder(new TitledBorder("Schritt 1: Dateien wählen"));

        /**
         * Button für Raumselektion
         */
        fileButtons.add(btnSelectRooms);
        btnSelectRooms.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dlgAddRooms.setModal(true);
                dlgAddRooms.setVisible(true);
                btnSelectRooms.setText("Räume (" + inputFilePanelRooms.getInputFiles().size() + ")");
            }
        });

        /**
         * Button für Kursselektion
         */
        fileButtons.add(btnSelectCourses);
        btnSelectCourses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dlgAddCourses.setModal(true);
                dlgAddCourses.setVisible(true);
                btnSelectCourses.setText("Lehrveranstaltungen (" + inputFilePanelCourses.getInputFiles().size() + ")");
            }
        });

        /**
         * Button für Studiengangselektion
         */
        fileButtons.add(btnSelectStudyPrograms);
        btnSelectStudyPrograms.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dlgAddStudyPrograms.setModal(true);
                dlgAddStudyPrograms.setVisible(true);
                btnSelectStudyPrograms.setText("Studiengänge (" + inputFilePanelStudyPrograms.getInputFiles().size() + ")");
            }
        });

        /**
         * Swing Komponenten für die Konfiguration der Planerstellung
         */
        JPanel panelConfiguration = new JPanel(new GridLayout(4, 1));
        panelTopButtons.add(panelConfiguration);
        panelConfiguration.setBorder(new TitledBorder("Schritt 2: Planung konfigurieren"));
        panelConfiguration.add(labelStrategies);
        panelConfiguration.add(comboBoxStrategies);
        ComboBoxElementModel cbxModelStrategies = new ComboBoxElementModel();
        cbxModelStrategies.addElement(new ComboxBoxElement("Kostenoptimiert", CostOptimizedStrategy.class));
        comboBoxStrategies.setModel(cbxModelStrategies);
        panelConfiguration.add(labelCosts);
        panelConfiguration.add(textFieldCosts);
        textFieldCosts.setHorizontalAlignment(JFormattedTextField.RIGHT);
        textFieldCosts.setValue(10);

        /**
         * Swing Komponenten für die Ausführung der Planerstellung
         */
        JPanel panelExecution = new JPanel(new GridLayout(4, 1));
        panelExecution.setBorder(new TitledBorder("Schritt 3: Planung ausführen"));
        panelExecution.add(btnGenerateMasterSchedule);
        panelTopButtons.add(panelExecution);

        btnGenerateMasterSchedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeScheduler();
            }
        });

        /**
         * Swing Komponenten für die Konfiguration des Dateiexports
         */
        JPanel panelOutput = new JPanel(new GridLayout(4, 1));
        panelTopButtons.add(panelOutput);
        panelOutput.setBorder(new TitledBorder("Schritt 4: Export konfigurieren"));
        panelOutput.add(btnSelectOutput);
        panelOutput.add(labelOutputDirectory);
        panelOutput.add(labelOutputFormat);
        panelOutput.add(comboBoxOutputFormat);
        ComboBoxElementModel cbxModelOutputFormats = new ComboBoxElementModel();
        cbxModelOutputFormats.addElement(new ComboxBoxElement("CSV-Text", OutputFormat.CSV_TEXT));
        cbxModelOutputFormats.addElement(new ComboxBoxElement("HTML", OutputFormat.HTML));
        comboBoxOutputFormat.setModel(cbxModelOutputFormats);

        btnSelectOutput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dlgAddOutputDirectory.setModal(true);
                dlgAddOutputDirectory.setVisible(true);
                if (inputFilePanelOutputDirectory.getInputFiles().size() == 1) {
                    labelOutputDirectory.setText(inputFilePanelOutputDirectory.getInputFiles().get(0).getFile().toString());
                }
            }
        });

        /**
         * Swing Komponenten für die Ausführung des Dateiexports
         */
        JPanel panelOutputExecute = new JPanel(new GridLayout(4, 1));
        panelOutputExecute.setBorder(new TitledBorder("Schritt 5: Export ausführen"));
        panelOutputExecute.add(btnOutputExecute);
        panelTopButtons.add(panelOutputExecute);

        btnOutputExecute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeExport();
            }
        });

        /**
         * Splitpane platzieren und konfigurieren
         */
        getContentPane().add(splitPane, BorderLayout.CENTER);
        splitPane.setDividerLocation(200);

        /**
         * Baum für Planstruktur in ScrollPane platzieren und konfigurieren
         */
        splitPane.add(scrollPaneTree, JSplitPane.LEFT);
        scrollPaneTree.setViewportView(treeMasterSchedule);
        treeMasterSchedule.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Gesamtplan")));

        /**
         * Tabelle für Plandarstellung in ScrollPane platzieren
         */
        splitPane.add(scrollPaneTable, JSplitPane.RIGHT);
        scrollPaneTable.setViewportView(lSelectNode);

        /**
         * LogginPanel konfigurieren und in TabbedPane platzieren
         */
        Protocol.getInstance().addObserver(loggingPanel);
        getContentPane().add(tabbedPane, BorderLayout.SOUTH);
        tabbedPane.add("Ausgabe", loggingPanel);
        tabbedPane.setPreferredSize(new Dimension(100, 250));
        tabbedPane.setMinimumSize(new Dimension(100, 250));

        /**
         * Dialog für Raumdateiselektion erzeugen und konfigurieren
         */
        dlgAddRooms = new JDialog(this);
        dlgAddRooms.setModal(true);
        dlgAddRooms.setLayout(new BorderLayout());
        dlgAddRooms.setSize(640, 480);
        dlgAddRooms.setLocationRelativeTo(null);
        dlgAddRooms.setTitle("Raumdateien für Planerstellung");
        inputFilePanelRooms = new InputFilePanel(InputFileType.ROOM_FILE, dlgAddRooms);
        dlgAddRooms.getContentPane().add(inputFilePanelRooms, BorderLayout.CENTER);

        /**
         * Dialog für Kursdateiselektion erzeugen und konfigurieren
         */
        dlgAddCourses = new JDialog(this);
        dlgAddCourses.setModal(true);
        dlgAddCourses.setLayout(new BorderLayout());
        dlgAddCourses.setSize(640, 480);
        dlgAddCourses.setLocationRelativeTo(null);
        dlgAddCourses.setTitle("Kursdateien für Planerstellung");
        inputFilePanelCourses = new InputFilePanel(InputFileType.COURSE_FILE, dlgAddCourses);
        dlgAddCourses.getContentPane().add(inputFilePanelCourses, BorderLayout.CENTER);

        /**
         * Dialog für Studiengangsdateiselektion erzeugen und konfigurieren
         */
        dlgAddStudyPrograms = new JDialog(this);
        dlgAddStudyPrograms.setModal(true);
        dlgAddStudyPrograms.setLayout(new BorderLayout());
        dlgAddStudyPrograms.setSize(640, 480);
        dlgAddStudyPrograms.setLocationRelativeTo(null);
        dlgAddStudyPrograms.setTitle("Studiengangsdateien für Planerstellung");
        inputFilePanelStudyPrograms = new InputFilePanel(InputFileType.ROOM_FILE, dlgAddStudyPrograms);
        dlgAddStudyPrograms.getContentPane().add(inputFilePanelStudyPrograms, BorderLayout.CENTER);

        /**
         * Dialog für Ausgabeverzeichnisselektion erzeugen und konfigurieren
         */
        dlgAddOutputDirectory = new JDialog(this);
        dlgAddOutputDirectory.setModal(true);
        dlgAddOutputDirectory.setLayout(new BorderLayout());
        dlgAddOutputDirectory.setSize(640, 480);
        dlgAddOutputDirectory.setLocationRelativeTo(null);
        dlgAddOutputDirectory.setTitle("Ausgabeverzeichnisse für Plandateien");
        inputFilePanelOutputDirectory = new InputFilePanel(InputFileType.OUTPUT_DIRECTORY, dlgAddOutputDirectory);
        dlgAddOutputDirectory.getContentPane().add(inputFilePanelOutputDirectory, BorderLayout.CENTER);

    }

    /**
     * Events und Aktionen für den Navigationsbaum erzeugen und binden.
     */
    private void initializeTreeEvents() {

        /**
         * Listener für Selektionen auf dem Planstrukturbaum registrieren
         */
        treeMasterSchedule.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                final int timeSlotWidth = 25;

                /**
                 * Wurde ein Knoten ohne direkten Planbezug selektiert statt der
                 * Tabelle ein Label mit dem Hinweis zur Selektion eines
                 * Baumknotens anzeigen und Folgenverarbeite abbrechen
                 */
                if (treeMasterSchedule.getLastSelectedPathComponent() == null) {
                    scrollPaneTable.setViewportView(lSelectNode);
                    return;
                }

                /**
                 * Selektierten Knoten holen
                 */
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeMasterSchedule.getLastSelectedPathComponent();

                /**
                 * Spezifische Behandlung des Knotens für Benutzerobjekt
                 * festlegen
                 */
                if (selectedNode.getUserObject() instanceof Room) {

                    /**
                     * Knoten hält eine Instanz von Room: Passenden Plan aus dem
                     * Masterschedule holen und an Tabellenmodell übergen und
                     * Tabelle anzeigen
                     */
                    Schedule schedule = masterSchedule.getSchedule((Room) selectedNode.getUserObject());
                    scheduleTableModel.setSchedule(schedule);
                    scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(timeSlotWidth);
                    scrollPaneTable.setViewportView(scheduleTable);

                } else if (selectedNode.getUserObject() instanceof Academic) {

                    /**
                     * Knoten hält eine Instanz von Academic: Passenden Plan aus
                     * dem Masterschedule holen und an Tabellenmodell übergen
                     * und Tabelle anzeigen
                     */
                    Schedule schedule = masterSchedule.getSchedule((Academic) selectedNode.getUserObject());
                    scheduleTableModel.setSchedule(schedule);
                    scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(timeSlotWidth);
                    scrollPaneTable.setViewportView(scheduleTable);

                } else if (selectedNode.getUserObject() instanceof Semester) {

                    /**
                     * Knoten hält eine Instanz von Semester: Passenden Plan aus
                     * dem Masterschedule holen und an Tabellenmodell übergen
                     * und Tabelle anzeigen
                     */
                    Semester semester = (Semester) selectedNode.getUserObject();
                    Schedule schedule = masterSchedule.getSchedule(semester.getStudyProgram(), semester);
                    scheduleTableModel.setSchedule(schedule);
                    scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(timeSlotWidth);
                    scrollPaneTable.setViewportView(scheduleTable);

                } else {

                    /**
                     * Statt Tabelle ein Label mit dem Hinweis zur Selektion
                     * eines Baumknotens anzeigen
                     */
                    scrollPaneTable.setViewportView(lSelectNode);
                }

            }
        });
    }

    /**
     * Initialisiert die Datenhaltung und erzeugt den Gesamtplan
     * (MasterSchedule) und erzeugt das TreeModel und sowie das TableModel für
     * die tabellarische Plandarstellung
     */
    private void executeScheduler() {

        Protocol.log("Gesamtplanberechnung gestartet");

        /**
         * Prüfen ob alle notwendigen Einstellungen vorgenommen wurden
         */
        if (inputFilePanelRooms.getInputFiles().isEmpty()
                || inputFilePanelCourses.getInputFiles().isEmpty()
                || inputFilePanelStudyPrograms.getInputFiles().isEmpty()) {

            /**
             * Im Fehlerfalls Meldung ausgeben und Folgeverarbeitung abbrechen
             */
            JOptionPane.showMessageDialog(this, "Wählen Sie jeweils mindestens eine Datei mit Räumen, eine mit "
                    + "\nLehrveranstaltungen sowie eine mit Studiengangsdaten aus.",
                    "Bitte Dateien wählen!", JOptionPane.INFORMATION_MESSAGE);
            Protocol.log("Gesamtplanberechnung abgebrochen wegen Validierungsfehlern");
            return;
        }

        /**
         * DataController erzeugen
         */
        dataController = new DataController();

        /**
         * Raumdateien, Kursdateien und Studiengangsdateien einlesen
         */
        RoomReader roomReader = new RoomReader();
        for (InputFileDescriptor file : inputFilePanelRooms.getInputFiles()) {
            roomReader.readRooms(file.getFile().toString(), dataController);
        }
        Protocol.log("Raumdateien eingelesen: " + inputFilePanelRooms.getInputFiles().size());
        Protocol.log("Räume insgesamt: " + dataController.getRooms().size());

        CourseReader courseReader = new CourseReader();
        for (InputFileDescriptor file : inputFilePanelCourses.getInputFiles()) {
            courseReader.readCourses(file.getFile().toString(), dataController);
        }
        Protocol.log("Kursdateien eingelesen: " + inputFilePanelCourses.getInputFiles().size());
        Protocol.log("Kurse insgesamt: " + dataController.getCourses().size());

        StudyProgramReader studyProgramReader = new StudyProgramReader();
        for (InputFileDescriptor file : inputFilePanelStudyPrograms.getInputFiles()) {
            studyProgramReader.readStudyPrograms(file.getFile().toString(), dataController);
        }
        Protocol.log("Studiengangsdateien eingelesen: " + inputFilePanelStudyPrograms.getInputFiles().size());
        Protocol.log("Studiengänge insgesamt: " + dataController.getStudyPrograms().size());

        /**
         * Selektierte Planungsstrategie holen
         */
        String strategyName = ((ComboxBoxElement<Class>) comboBoxStrategies.getSelectedItem()).getElement().getSimpleName();

        /**
         * Scheduler erzeugen und konfigurieren
         */
        scheduler = new Scheduler();
        scheduler.setDataController(dataController);
        scheduler.setStrategy(StrategyFactory.getInstanceByClassName(strategyName));

        /**
         * Selektierte Planungsstrategie auf Basis der Eingabedaten ausführen
         * und erzeugten Gesamtplan holen
         */
        scheduler.executeStrategy(null);
        masterSchedule = scheduler.getMasterSchedule();

        /**
         * Modell für Planstrukturbaum auf Basis des Gesamtplans erzeugen und
         * der JTree Komponente zuweisen
         */
        DefaultTreeModel treeModel = new DefaultTreeModel(buildTreeModel());
        treeMasterSchedule.setModel(treeModel);

        Protocol.log("Gesamtplanberechnung beendet");

    }

    /**
     * Exportiert auf Basis der Export-Einstellungen die aktuell ausgewählten
     * Pläne
     */
    private void executeExport() {
        
        Protocol.log("Dateiexport gestartet");
        
        /**
         * Prüfen ob ein Gesamtplan erzeugt wurde
         */
        if (masterSchedule == null) {

            /**
             * Im Fehlerfall Meldung ausgeben und Folgeverarbeitung abbrechen
             */
            JOptionPane.showMessageDialog(this, "Erzeugen Sie zunächst den Gesamtplan",
                    "Bitte Gesamtplan erzeugen", JOptionPane.INFORMATION_MESSAGE);
            Protocol.log("Dateiexport abgebrochen wegen fehlendem Gesamtplan");
            return;
        }

        /**
         * Prüfen ob ein Ausgabeverzeichnis selektiert wurde
         */
        if (inputFilePanelOutputDirectory.getInputFiles().isEmpty()) {

            /**
             * Im Fehlerfall Meldung ausgeben und Folgeverarbeitung abbrechen
             */
            JOptionPane.showMessageDialog(this, "Wählen Sie zunächst das Ausgabeverzeichnis aus",
                    "Bitte Ausgabeverzeichnis wählen", JOptionPane.INFORMATION_MESSAGE);
            Protocol.log("Dateiexport abgebrochen wegen nicht selektiertem Ausgabeverzeichnis");
            return;
        }

        /**
         * Selektiertes Ausgabeformat holen
         */
        OutputFormat outputFormat = ((ComboxBoxElement<OutputFormat>) comboBoxOutputFormat.getSelectedItem()).getElement();
        Protocol.log("Dateiexport Ausgabeformat: " + outputFormat);
        
        /**
         * Selektiertes Ausgabeverzeichnis holen
         */
        String outputPath = inputFilePanelOutputDirectory.getInputFiles().get(0).getFile().toString();
        Protocol.log("Dateiexport Ausgabeverzeichnis: " + outputPath);
        
        /**
         * Instanz des OutputControllers erzeugen und alle Pläne des Gesamtplans
         * im angegebenen Ausgabeformat in das Ausgabeverzeichnis exportieren
         */
        outputController = new OutputController();
        outputController.outputSchedules(masterSchedule.getAllSchedules(), outputFormat, outputPath);
        Protocol.log("Dateiexport abgeschlossen");
        
    }

    /**
     * Erzeugt auf Basis des Gesamtplanes (MasterSchedule) das TreeModel
     * mithilfe von verknüpften DefaultMutableTreeNodes
     */
    private TreeNode buildTreeModel() {

        /**
         * Root-Knoten erzeugen
         */
        DefaultMutableTreeNode rMasterSchedule = new DefaultMutableTreeNode("Gesamtplan");

        /**
         * Knoten für Raumpläne erzeugen und an Root-Knoten anhängen
         */
        DefaultMutableTreeNode rRooms = new DefaultMutableTreeNode("Raumpläne");
        rMasterSchedule.add(rRooms);

        /**
         * Knoten für Pläne interner Räume erzeugen und an Raumknoten anhängen
         */
        DefaultMutableTreeNode rRoomsInternal = new DefaultMutableTreeNode("Interne Räume");
        List<Room> rooms = masterSchedule.getRooms(RoomType.INTERNAL);
        Collections.sort(rooms);
        for (Room room : rooms) {
            rRoomsInternal.add(new DefaultMutableTreeNode(room));
        }
        rRooms.add(rRoomsInternal);

        /**
         * Knoten für Pläne externer Räume erzeugen und an Raumknoten anhängen
         */
        DefaultMutableTreeNode rRoomsExternal = new DefaultMutableTreeNode("Externe Räume");
        rooms = masterSchedule.getRooms(RoomType.EXTERNAL);
        Collections.sort(rooms);
        for (Room room : rooms) {
            rRoomsExternal.add(new DefaultMutableTreeNode(room));
        }
        rRooms.add(rRoomsExternal);

        /**
         * Knoten für Dozentenpläne erzeugen und an Root-Knoten anhängen
         */
        DefaultMutableTreeNode rAcademics = new DefaultMutableTreeNode("Dozentenpläne");
        List<Academic> academics = dataController.getAcademics();
        Collections.sort(academics);
        for (Academic academic : academics) {
            rAcademics.add(new DefaultMutableTreeNode(academic));
        }
        rMasterSchedule.add(rAcademics);

        /**
         * Knoten für Studiengangspläne und Fachsemester erzeugen und an
         * Root-Knoten anhängen
         */
        DefaultMutableTreeNode rStudyPrograms = new DefaultMutableTreeNode("Studiengangspläne");
        List<StudyProgram> studyPrograms = dataController.getStudyPrograms();
        Collections.sort(studyPrograms);
        for (StudyProgram studyProgram : studyPrograms) {
            DefaultMutableTreeNode rStudyProgram = new DefaultMutableTreeNode(studyProgram);
            rStudyPrograms.add(rStudyProgram);
            for (Semester semester : studyProgram.getSemesters()) {
                rStudyProgram.add(new DefaultMutableTreeNode(semester));
            }
        }
        rMasterSchedule.add(rStudyPrograms);

        /**
         * Knoten für alle Pläne in denen ein Kurs vorkommt erzeugen
         */
        DefaultMutableTreeNode rCourses = new DefaultMutableTreeNode("Pläne pro Kurs");
        rMasterSchedule.add(rCourses);
        List<Course> courses = dataController.getCourses();
        Collections.sort(courses);

        /**
         * Alle Kurse durchlaufen
         */
        for (Course course : courses) {

            /**
             * Knoten für Kurs erzeugen
             */
            DefaultMutableTreeNode rCourse = new DefaultMutableTreeNode(course);
            rCourses.add(rCourse);

            /**
             * Knoten für Raumpläne erzeugen in denen der Kurs vorkommt
             */
            DefaultMutableTreeNode rCourseRooms = new DefaultMutableTreeNode("Räume");
            rCourse.add(rCourseRooms);
            List<Room> relevantRooms = new ArrayList<>();

            Iterator<Room> roomSchedules = masterSchedule.getRoomSchedules().keySet().iterator();
            while (roomSchedules.hasNext()) {
                Room room = roomSchedules.next();
                for (ScheduleElement scheduleElement : masterSchedule.getSchedule(room).getScheduleElements()) {
                    if (scheduleElement.isBlocked() && scheduleElement.getCourse().equals(course)) {
                        relevantRooms.add(room);
                    }
                }
            }
            Collections.sort(relevantRooms);
            for (Room room : relevantRooms) {
                rCourseRooms.add(new DefaultMutableTreeNode(room));
            }

            /**
             * Knoten für Dozentenpläne erzeugen in denen der Kurs vorkommt
             */
            DefaultMutableTreeNode rCourseAcadmic = new DefaultMutableTreeNode("Dozent");
            rCourse.add(rCourseAcadmic);
            List<Academic> relevantAcademics = new ArrayList<>();

            Iterator<Academic> academicSchedules = masterSchedule.getAcadademicSchedules().keySet().iterator();
            while (academicSchedules.hasNext()) {
                Academic academic = academicSchedules.next();
                for (ScheduleElement scheduleElement : masterSchedule.getSchedule(academic).getScheduleElements()) {
                    if (scheduleElement.isBlocked() && scheduleElement.getCourse().equals(course)
                            && scheduleElement.getCourse().getAcademic().equals(academic)) {
                        relevantAcademics.add(academic);
                    }
                }
            }
            Collections.sort(relevantAcademics);
            for (Academic academic : relevantAcademics) {
                rCourseAcadmic.add(new DefaultMutableTreeNode(academic));
            }

            /**
             * Knoten für Studiengangspläne und Fachsemester erzeugen in denen
             * der Kurs vorkommt
             */
            DefaultMutableTreeNode rCourseStudyPrograms = new DefaultMutableTreeNode("Studiengänge");
            rCourse.add(rCourseStudyPrograms);
            List<StudyProgram> relevantStudyPrograms = new ArrayList<>();

            Iterator<StudyProgram> studyProgrammSchedules = masterSchedule.getStudyProgramSchedules().keySet().iterator();
            while (studyProgrammSchedules.hasNext()) {
                StudyProgram studyProgram = studyProgrammSchedules.next();

                if (studyProgram.containsCourse(course)) {
                    relevantStudyPrograms.add(studyProgram);
                }

            }
            Collections.sort(relevantStudyPrograms);
            for (StudyProgram studyProgram : relevantStudyPrograms) {
                DefaultMutableTreeNode rCourseStudyProgram = new DefaultMutableTreeNode(studyProgram);
                rCourseStudyPrograms.add(rCourseStudyProgram);
                for (Semester semester : studyProgram.getSemesters()) {
                    if (semester.getCourses().contains(course)) {
                        rCourseStudyProgram.add(new DefaultMutableTreeNode(semester));
                    }
                }

            }

        }

        /**
         * Das erzeugte Tree Modell zurückgeben
         */
        return rMasterSchedule;
    }

    /**
     * Registiert grundlengde Handler für Fenster Events
     */
    private void initializeWindowEvents() {
        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(SchedulerUI.this, "Wirklich beenden?",
                        "Programm beenden", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                if (result == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

}