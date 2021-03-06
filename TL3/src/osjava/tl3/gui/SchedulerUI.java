package osjava.tl3.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import osjava.tl3.gui.components.combobox.ComboBoxElementModel;
import osjava.tl3.gui.components.combobox.ComboxBoxElement;
import osjava.tl3.gui.components.fileselection.InputFileDescriptor;
import osjava.tl3.gui.components.fileselection.InputFileDialog;
import osjava.tl3.gui.components.fileselection.InputFileType;
import osjava.tl3.gui.components.logging.LoggingPanel;
import osjava.tl3.gui.components.schedule.ScheduleTable;
import osjava.tl3.gui.components.schedule.ScheduleTableModel;
import osjava.tl3.logging.Protocol;
import osjava.tl3.logic.io.input.CourseReader;
import osjava.tl3.logic.io.input.RoomReader;
import osjava.tl3.logic.io.input.StudyProgramReader;
import osjava.tl3.logic.io.output.OutputController;
import osjava.tl3.logic.io.output.OutputFormat;
import osjava.tl3.logic.planning.Scheduler;
import osjava.tl3.logic.planning.strategies.CostOptimizedStrategy;
import osjava.tl3.logic.planning.strategies.StrategyFactory;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.Semester;
import osjava.tl3.model.StudyProgram;
import osjava.tl3.model.controller.DataController;
import osjava.tl3.model.schedule.Schedule;
import osjava.tl3.model.schedule.ScheduleAppointment;
import osjava.tl3.model.schedule.ScheduleElement;
import osjava.tl3.model.schedule.ScheduleView;
import osjava.tl3.model.schedule.ScheduleViewAcademic;
import osjava.tl3.model.schedule.ScheduleViewCourse;
import osjava.tl3.model.schedule.ScheduleViewMasterPlan;
import osjava.tl3.model.schedule.ScheduleViewRoom;
import osjava.tl3.model.schedule.ScheduleViewSemester;
import osjava.tl3.model.schedule.ScheduleViewStudyProgram;

/**
 * Diese Klasse stellt ein grafisches Benutzer Interface zur Steuerung der
 * Planungslogik bereit.
 *
 * Die GUI stellt Möglichkeiten der Parametrisierung der Planungslogik und der
 * Eingabe und Ausgabe bereit. Weiterhin dient die GUI zur bequemen Auswahl
 * mittels JTree und Darstellung aller im Gesamtplan (Schedule)
 * enthaltenen Pläne in tabellarischer Form (JTable) bereit.
 * Die Klasse operiert dabei jedoch nicht direkt auf dem Plan sondern nutzt zu
 * diesem Zweck spezifische Sichten auf den Plan, die jeweils spezifisch sind
 * für Räume, Dozenten, Studiengänge und Fachsemester.
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
    private final JLabel lSelectNode = new JLabel("Bitte wählen Sie das Objekt in der Baumansicht, dessen Terminplan Sie einsehen möchten", SwingConstants.CENTER);

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
    private InputFileDialog dialogAddRooms;
    private InputFileDialog dialogAddCourses;
    private InputFileDialog dialogAddStudyPrograms;

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
    private InputFileDialog dialogAddOutputDirectory;

    /**
     * Swing Komponenten für Ausführung der Dateiausgabe
     */
    private final JButton btnOutputExecute = new JButton("Gesamtplan exportieren");

    /**
     * Instanz des DataControllers für Zugriff auf Model Daten
     */
    private DataController dataController;

    /**
     * Instanz des Planerstellungslogik
     */
    private Scheduler scheduler;

    /**
     * Instanz des Gesamtplans
     */
    private Schedule schedule;

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
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1200, 900);
        setLocationRelativeTo(null);

        /**
         * Grundlegende Fensterevents registrieren
         */
        initializeWindowEvents();

        /**
         * Swing Komponenten erzeugen und konfigurieren
         */
        initializeComponents();

        /**
         * Events für den Planstrukturbaum registrieren
         */
        initializeTreeEvents();

        /**
         * Meldung ausgeben
         */
        Protocol.log("GUI-Modus initialisiert.");
        Protocol.log("Bitte führen Sie die Schritte 1 bis 5 durch und nehmen Sie dabei die gewünschten Einstellungen vor.");
        
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
         * Buttonleiste oben im Hauptfenster platzieren
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
                dialogAddRooms.setVisible(true);
                btnSelectRooms.setText("Räume (" + dialogAddRooms.getSelectedFiles().size() + ")");
            }
        });

        /**
         * Button für Kursselektion
         */
        fileButtons.add(btnSelectCourses);
        btnSelectCourses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogAddCourses.setVisible(true);
                btnSelectCourses.setText("Lehrveranstaltungen (" + dialogAddCourses.getSelectedFiles().size() + ")");
            }
        });

        /**
         * Button für Studiengangselektion
         */
        fileButtons.add(btnSelectStudyPrograms);
        btnSelectStudyPrograms.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogAddStudyPrograms.setVisible(true);
                btnSelectStudyPrograms.setText("Studiengänge (" + dialogAddStudyPrograms.getSelectedFiles().size() + ")");
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
                dialogAddOutputDirectory.setVisible(true);
                if (dialogAddOutputDirectory.getSelectedFiles().size() == 1) {
                    labelOutputDirectory.setText(dialogAddOutputDirectory.getSelectedFiles().get(0).getFile().toString());
                    btnSelectOutput.setToolTipText(dialogAddOutputDirectory.getSelectedFiles().get(0).getFile().toString());
                }
            }
        });

        /**
         * Swing Komponenten für die Ausführung des Dateiexports
         */
        JPanel panelOutputExecute = new JPanel(new GridLayout(4, 1));
        panelTopButtons.add(panelOutputExecute);
        panelOutputExecute.setBorder(new TitledBorder("Schritt 5: Export ausführen"));
        panelOutputExecute.add(btnOutputExecute);

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
        getContentPane().add(tabbedPane, BorderLayout.SOUTH);
        tabbedPane.add("Ausgabe", loggingPanel);
        tabbedPane.setPreferredSize(new Dimension(100, 250));
        tabbedPane.setMinimumSize(new Dimension(100, 250));

        /**
         * Dialoge für Selektion von Räumen, Kursen, Studiengängen und
         * Ausgabeverzeichnis erzeugen
         */
        dialogAddRooms = new InputFileDialog(this, "Raumdateien für Planerstellung", InputFileType.ROOM_FILE);
        dialogAddCourses = new InputFileDialog(this, "Kursdateien für Planerstellung", InputFileType.COURSE_FILE);
        dialogAddStudyPrograms = new InputFileDialog(this, "Studiengangsdateien für Planerstellung", InputFileType.STUDYPROGRAM_FILE);
        dialogAddOutputDirectory = new InputFileDialog(this, "Ausgabeverzeichnisse für Plandateien", InputFileType.OUTPUT_DIRECTORY);

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
                 if (selectedNode.getUserObject() instanceof Schedule) {
                     /**
                     * Knoten hält eine Instanz von Schedule: Sicht für Gesamtplan erzeugen und an Tabellenmodell übergeben und
                     * Tabelle anzeigen
                     */
                    ScheduleView scheduleView = new ScheduleViewMasterPlan(schedule);
                    scheduleTableModel.setSchedule(scheduleView);
                    scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(timeSlotWidth);
                    scrollPaneTable.setViewportView(scheduleTable);

                 }
                 else if (selectedNode.getUserObject() instanceof Room) {

                    /**
                     * Knoten hält eine Instanz von Room: Passenden Plan aus dem
                     * Masterschedule holen und an Tabellenmodell übergeben und
                     * Tabelle anzeigen
                     */
                    ScheduleView scheduleView = new ScheduleViewRoom((Room) selectedNode.getUserObject(), schedule);
                    scheduleTableModel.setSchedule(scheduleView);
                    scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(timeSlotWidth);
                    scrollPaneTable.setViewportView(scheduleTable);

                } else if (selectedNode.getUserObject() instanceof Academic) {

                    /**
                     * Knoten hält eine Instanz von Academic: Passenden Plan aus
                     * dem Masterschedule holen und an Tabellenmodell übergeben
                     * und Tabelle anzeigen
                     */
                    ScheduleView scheduleView = new ScheduleViewAcademic((Academic) selectedNode.getUserObject(), schedule);
                    scheduleTableModel.setSchedule(scheduleView);
                    scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(timeSlotWidth);
                    scrollPaneTable.setViewportView(scheduleTable);

                } else if (selectedNode.getUserObject() instanceof StudyProgram) {

                    /**
                     * Knoten hält eine Instanz von Semester: Passenden Plan aus
                     * dem Masterschedule holen und an Tabellenmodell übergeben
                     * und Tabelle anzeigen
                     */
                    ScheduleView scheduleView = new ScheduleViewStudyProgram((StudyProgram) selectedNode.getUserObject(), schedule);
                    scheduleTableModel.setSchedule(scheduleView);
                    scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(timeSlotWidth);
                    scrollPaneTable.setViewportView(scheduleTable);
                } else if (selectedNode.getUserObject() instanceof Semester) {

                    /**
                     * Knoten hält eine Instanz von Semester: Passenden Plan aus
                     * dem Masterschedule holen und an Tabellenmodell übergeben
                     * und Tabelle anzeigen
                     */
                    ScheduleView scheduleView = new ScheduleViewSemester((Semester) selectedNode.getUserObject(), schedule);
                    scheduleTableModel.setSchedule(scheduleView);
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
     * Initialisiert die Datenhaltung und erzeugt den Gesamtplan das TreeModel
     * sowie das TableModel für die tabellarische Plandarstellung
     */
    private void executeScheduler() {

        Protocol.log("Gesamtplanberechnung gestartet");

        /**
         * Prüfen ob alle notwendigen Einstellungen vorgenommen wurden
         */
        if (dialogAddCourses.getSelectedFiles().isEmpty()
                || dialogAddStudyPrograms.getSelectedFiles().isEmpty()) {

            /**
             * Im Fehlerfalls Meldung ausgeben und Folgeverarbeitung abbrechen
             */
            JOptionPane.showMessageDialog(this, "Wählen Sie jeweils mindestens eine Datei mit Lehrveranstaltungen\n"
                    + "sowie eine mit Studiengangsdaten aus.\n"
                    + "Wird keine Raumdatei selektiert, werden alle Lehrveranstaltungen extern eingeplant.",
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
        for (InputFileDescriptor file : dialogAddRooms.getSelectedFiles()) {
            roomReader.readRooms(file.getFile().toString(), dataController);
        }
        Protocol.log("Raumdateien selektiert: " + dialogAddRooms.getSelectedFiles().size());
        Protocol.log("Räume eingelesen: " + dataController.getRooms().size());

        CourseReader courseReader = new CourseReader();
        for (InputFileDescriptor file : dialogAddCourses.getSelectedFiles()) {
            courseReader.readCourses(file.getFile().toString(), dataController);
        }
        Protocol.log("Kursdateien selektiert: " + dialogAddCourses.getSelectedFiles().size());
        Protocol.log("Kurse eingelesen: " + dataController.getCourses().size());

        StudyProgramReader studyProgramReader = new StudyProgramReader();
        for (InputFileDescriptor file : dialogAddStudyPrograms.getSelectedFiles()) {
            studyProgramReader.readStudyPrograms(file.getFile().toString(), dataController);
        }
        Protocol.log("Studiengangsdateien selektiert: " + dialogAddStudyPrograms.getSelectedFiles().size());
        Protocol.log("Studiengänge eingelesen: " + dataController.getStudyPrograms().size());

        /**
         * Prüfen ob mindestens 1 Kurs, 1 Raum und ein Studiengang in dem
         * DataController geladen wurden
         */
        if (dataController.getCourses().isEmpty()
                || dataController.getStudyPrograms().isEmpty()) {

            /**
             * Im Fehlerfalls Meldung ausgeben und Folgeverarbeitung abbrechen
             */
            JOptionPane.showMessageDialog(this, "Aus den selektierten Eingabedateien konnten\n"
                    + "nicht alle revanten Daten gelesen werden. Es muss mindestens jeweils\n"
                    + "ein Kurs und ein Studiengang geladen worden sein!\n"
                    + "Werden keine Räume eingelesen werden alle Veranstaltungen extern eingeplant.",
                    "Bitte alle notwendingen Dateien einlesen", JOptionPane.INFORMATION_MESSAGE);
            Protocol.log("Gesamtplanberechnung abgebrochen wegen Validierungsfehlern");
            return;
        }

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
        scheduler.executeStrategy();
        schedule = scheduler.getSchedule();

        /**
         * Modell für Planstrukturbaum auf Basis des Gesamtplans erzeugen und
         * der JTree Komponente zuweisen
         */
        DefaultTreeModel treeModel = new DefaultTreeModel(buildTreeModel());
        treeMasterSchedule.setModel(treeModel);

        /**
         * Statistiken ausgeben. Ermitteln welche Räume nicht belegt wurden
         */
        List<Room> unusedRooms = new ArrayList<>(dataController.getRooms());
        unusedRooms.removeAll(schedule.getPlannedRooms());

        Protocol.log("Räume insgesamt: " + dataController.getRooms().size());
        Protocol.log("Räume intern: " + dataController.getRooms(RoomType.INTERNAL).size());
        Protocol.log("Räume extern: " + dataController.getRooms(RoomType.EXTERNAL).size());
        Protocol.log("Räume ohne Termin: " + unusedRooms);
        Protocol.log("Anzahl Termine: " + schedule.getAppointmentCount());
        Protocol.log("Sitzplätze benötigt: " + (int) (schedule.getStudentsCount(RoomType.INTERNAL) + schedule.getStudentsCount(RoomType.EXTERNAL)));
        Protocol.log("Sitzplätze intern besetzt: " + schedule.getStudentsCount(RoomType.INTERNAL));
        Protocol.log("Sitzplätze extern besetzt: " + schedule.getStudentsCount(RoomType.EXTERNAL));
        Protocol.log("Gesamtkosten: " + (int) (schedule.getStudentsCount(RoomType.EXTERNAL) * Integer.parseInt(textFieldCosts.getText())) + " EUR");

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
        if (schedule == null) {

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
        if (dialogAddOutputDirectory.getSelectedFiles().isEmpty()) {

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
        String outputPath = dialogAddOutputDirectory.getSelectedFiles().get(0).getFile().toString();
        Protocol.log("Dateiexport Ausgabeverzeichnis: " + outputPath);

        /**
         * Instanz des OutputControllers erzeugen und alle Pläne des Gesamtplans
         * im angegebenen Ausgabeformat in das Ausgabeverzeichnis exportieren
         */
        outputController = new OutputController();
        List<ScheduleView> views = schedule.getAllScheduleViews(dataController.getRooms(), dataController.getAcademics(), dataController.getStudyPrograms());
        outputController.outputSchedules(views, outputFormat, outputPath);
        Protocol.log("Dateiexport abgeschlossen");

    }

    /**
     * Erzeugt auf Basis des Gesamtplanes das TreeModel
     * mithilfe von verknüpften DefaultMutableTreeNodes
     */
    private TreeNode buildTreeModel() {

        /**
         * Root-Knoten erzeugen und Gesamtplan anhängen
         */
        DefaultMutableTreeNode rMasterSchedule = new DefaultMutableTreeNode("Plansichten");
        rMasterSchedule.add(new DefaultMutableTreeNode(schedule));

        /**
         * Knoten für Raumpläne erzeugen und an Root-Knoten anhängen
         */
        DefaultMutableTreeNode rRooms = new DefaultMutableTreeNode("Raumpläne");
        rMasterSchedule.add(rRooms);

        /**
         * Knoten für Pläne interner Räume erzeugen und an Raumknoten anhängen
         */
        DefaultMutableTreeNode rRoomsInternal = new DefaultMutableTreeNode("Interne Räume");
        List<Room> rooms = dataController.getRooms(RoomType.INTERNAL);
        Collections.sort(rooms);
        for (Room room : rooms) {
            rRoomsInternal.add(new DefaultMutableTreeNode(room));
        }
        rRooms.add(rRoomsInternal);

        /**
         * Knoten für Pläne externer Räume erzeugen und an Raumknoten anhängen
         */
        DefaultMutableTreeNode rRoomsExternal = new DefaultMutableTreeNode("Externe Räume");
        rooms = dataController.getRooms(RoomType.EXTERNAL);
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
             * Plansicht für aktuellen Kurs erzeugen
             */
            ScheduleViewCourse scheduleView = new ScheduleViewCourse(course, schedule);

            /**
             * Knoten für Raumpläne erzeugen in denen der Kurs vorkommt
             */
            DefaultMutableTreeNode rCourseRooms = new DefaultMutableTreeNode("Räume");
            rCourse.add(rCourseRooms);
            List<Room> relevantRooms = new ArrayList<>();

            for (ScheduleElement element : scheduleView.getScheduleElements()) {
               for (ScheduleAppointment appointment : element.getAppointments()) {
                   relevantRooms.add(appointment.getRoom());
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

            for (ScheduleElement element : scheduleView.getScheduleElements()) {
               for (ScheduleAppointment appointment : element.getAppointments()) {
                   relevantAcademics.add(appointment.getCourse().getAcademic());
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

            for (StudyProgram studyProgram : dataController.getStudyPrograms()) {
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

        /**
         * Window Adapter registrieren
         */
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                /**
                 * Entscheidungsdialog anzeigen
                 */
                int result = JOptionPane.showConfirmDialog(SchedulerUI.this, "Wirklich beenden?",
                        "Programm beenden", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                /**
                 * Fenster schließen wenn der Benutzer sich für die Ja-Option
                 * entscheidet
                 */
                if (result == JOptionPane.YES_OPTION) {
                    SchedulerUI.this.setVisible(false);
                    System.exit(0);
                }
            }
        });

    }

}