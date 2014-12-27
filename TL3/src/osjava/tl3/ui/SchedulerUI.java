package osjava.tl3.ui;

import osjava.tl3.ui.fileseletion.InputFilePanel;
import osjava.tl3.ui.fileseletion.InputFileType;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import osjava.tl3.logic.io.InputFileHelper;
import osjava.tl3.logic.io.OutputController;
import osjava.tl3.logic.io.OutputFormat;
import osjava.tl3.logic.planning.Scheduler;
import osjava.tl3.logic.planning.strategies.CostOptimizedStrategy;
import osjava.tl3.logic.planning.strategies.Strategy;
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

/**
 * Diese Klasse stellt ein grafisches Benutzer Interface zur Steuerung der
 * Planungslogik bereit.
 *
 * Die GUI stellt Möglichkeiten der Parametrisierung der Planungslogik und der
 * Eingabe und Ausgabe bereit. Weiterhin dient die GUI zu bequemen Auswahl
 * mittels JTree und Darstellung aller im Gesamtplan (MasterSchedule)
 * enthaltenen Plane (Schedule) in tabellarischer Form (JTable) bereit.
 *
 * @author Christian Müller
 */
public class SchedulerUI extends JFrame {

    private JButton btnOpenProtocol = new JButton("Protokoll anzeigen");
    private JButton btnSelectRooms = new JButton("Räume (0)");
    private JButton btnSelectCourses = new JButton("Lehrveranstaltungen (0)");
    private JButton btnSelectStudyPrograms = new JButton("Studiengänge (0)");

    private JButton btnSelectOutput = new JButton("Ausgabeverzeichnis");
    private JButton btnGenerateMasterSchedule = new JButton("Gesamtplan berechnen");
    private JLabel labelOutputDirectory = new JLabel("Verzeichnis: n/a");

    private JLabel labelStrategies = new JLabel("Planungsstrategie:");
    private JComboBox<String> comboBoxStrategies = new JComboBox<>();

    private JLabel labelOutputFormat = new JLabel("Ausgabeformat:");
    private JComboBox<String> comboBoxOutputFormat = new JComboBox<>();

    private JPanel panelTop = new JPanel(new GridLayout(1, 6, 5, 5));
    private JSplitPane splitPane = new JSplitPane();
    private JScrollPane scrollPaneTree = new JScrollPane();
    private JScrollPane scrollPaneTable = new JScrollPane();

    private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    private JPanel panelLogging = new JPanel(new BorderLayout());
    private JScrollPane scrollPaneLogging = new JScrollPane();
    private JTextArea textAreaLog = new JTextArea();

    private JTree treeMasterSchedule = new JTree();

    private DataController dataController;
    private OutputController outputController;
    private Scheduler scheduler;
    private MasterSchedule masterSchedule;

    private ScheduleTable scheduleTable = new ScheduleTable();

    private ScheduleTableModel scheduleTableModel;

    private JLabel lSelectNode = new JLabel("Bitte wählen Sie das Objekt in der Baumansicht, dessen Plan Sie einsehen möchten", SwingConstants.CENTER);

    private JDialog dlgAddRooms;
    private InputFilePanel inputFilePanelRooms;

    private JDialog dlgAddCourses;
    private InputFilePanel inputFilePanelCourses;

    private JDialog dlgAddStudyPrograms;
    private InputFilePanel inputFilePanelStudyPrograms;

    private JDialog dlgAddOutputDirectory;
    private InputFilePanel inputFilePanelOutputDirectory;

    /**
     * Erzeugt eine neue Instanz der Klasse ScheduleGUI und nimmt dabei
     * grundlegende Einstellungen am UI vor.
     *
     * @throws HeadlessException Wird ausgelöst, wenn die unterliegende JVM im
     * headless Modus, läuft und damit eine grafische Benutzeroberfläche nicht
     * erzeugt werden kann.
     */
    public SchedulerUI() throws HeadlessException {
        setSize(1200, 900);
        setTitle("VAWi OSJAVA TL3");
        setLocationRelativeTo(null);
        initializeComponents();
        initializeTreeEvents();
    }

    /**
     * UI Komponenten initialisieren
     */
    private void initializeComponents() {

        setLayout(new BorderLayout());

        getContentPane().add(panelTop, BorderLayout.NORTH);

        JPanel fileButtons = new JPanel(new GridLayout(4, 1));
        fileButtons.setBorder(new TitledBorder("Schritt 1: Dateien wählen"));

        fileButtons.add(btnSelectRooms);
        fileButtons.add(btnSelectCourses);
        fileButtons.add(btnSelectStudyPrograms);

        panelTop.add(fileButtons);
        btnSelectRooms.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dlgAddRooms.setModal(true);
                dlgAddRooms.setVisible(true);
                btnSelectRooms.setText("Räume (" + inputFilePanelRooms.getInputFiles().size() + ")");
            }
        });
        btnSelectCourses.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dlgAddCourses.setModal(true);
                dlgAddCourses.setVisible(true);
                btnSelectCourses.setText("Lehrveranstaltungen (" + inputFilePanelCourses.getInputFiles().size() + ")");
            }
        });
        btnSelectStudyPrograms.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dlgAddStudyPrograms.setModal(true);
                dlgAddStudyPrograms.setVisible(true);
                btnSelectStudyPrograms.setText("Studiengänge (" + inputFilePanelStudyPrograms.getInputFiles().size() + ")");
            }
        });

        ComboBoxElementModel cbxModelStrategies = new ComboBoxElementModel();
        cbxModelStrategies.addElement(new ComboxBoxElement("Kostenoptimiert", CostOptimizedStrategy.class));
        comboBoxStrategies.setModel(cbxModelStrategies);
        JPanel panelConfiguration = new JPanel(new GridLayout(4, 1));
        panelConfiguration.setBorder(new TitledBorder("Schritt 2: Einstellungen"));
        panelConfiguration.add(labelStrategies);
        panelConfiguration.add(comboBoxStrategies);
        panelTop.add(panelConfiguration);

        JPanel panelExecution = new JPanel(new GridLayout(4, 1));
        panelExecution.setBorder(new TitledBorder("Schritt 3: Ausführung"));
        panelExecution.add(btnGenerateMasterSchedule);
        panelTop.add(panelExecution);

        ComboBoxElementModel cbxModelOutputFormats = new ComboBoxElementModel();
        cbxModelOutputFormats.addElement(new ComboxBoxElement("CSV-Text", OutputFormat.CSV_TEXT));
        cbxModelOutputFormats.addElement(new ComboxBoxElement("HTML", OutputFormat.HTML));
        comboBoxOutputFormat.setModel(cbxModelOutputFormats);
        JPanel panelOutput = new JPanel(new GridLayout(4, 1));
        panelOutput.setBorder(new TitledBorder("Schritt 4: Ausgabe"));

        panelOutput.add(btnSelectOutput);
        panelOutput.add(labelOutputDirectory);

        panelOutput.add(labelOutputFormat);
        panelOutput.add(comboBoxOutputFormat);
        panelTop.add(panelOutput);
        btnSelectOutput.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dlgAddOutputDirectory.setModal(true);
                dlgAddOutputDirectory.setVisible(true);
                if (inputFilePanelOutputDirectory.getInputFiles().size() == 1) {
                    labelOutputDirectory.setText("Ausgabe: " + inputFilePanelOutputDirectory.getInputFiles().get(0).getFile().toString());
                }
            }
        });

        btnGenerateMasterSchedule.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                init();
            }
        });

        getContentPane().add(splitPane, BorderLayout.CENTER);

        splitPane.setDividerLocation(200);

        splitPane.add(scrollPaneTree, JSplitPane.LEFT);
        scrollPaneTree.setViewportView(treeMasterSchedule);

        splitPane.add(scrollPaneTable, JSplitPane.RIGHT);
        scrollPaneTable.setViewportView(lSelectNode);

        getContentPane().add(tabbedPane, BorderLayout.SOUTH);

        scrollPaneLogging.setViewportView(textAreaLog);
        panelLogging.add(scrollPaneLogging, BorderLayout.CENTER);
        panelLogging.setSize(100, 280);
        tabbedPane.add("Ausgabe", panelLogging);
        tabbedPane.setPreferredSize(new Dimension(100, 250));
        tabbedPane.setMinimumSize(new Dimension(100, 250));

        dlgAddRooms = new JDialog(this);
        dlgAddRooms.setLayout(new BorderLayout());
        dlgAddRooms.setSize(640, 480);
        dlgAddRooms.setLocationRelativeTo(null);
        dlgAddRooms.setTitle("Raumdateien für Planerstellung");
        inputFilePanelRooms = new InputFilePanel(InputFileType.ROOM_FILE, dlgAddRooms);
        dlgAddRooms.getContentPane().add(inputFilePanelRooms, BorderLayout.CENTER);

        dlgAddCourses = new JDialog(this);
        dlgAddCourses.setLayout(new BorderLayout());
        dlgAddCourses.setSize(640, 480);
        dlgAddCourses.setLocationRelativeTo(null);
        dlgAddCourses.setTitle("Kursdateien für Planerstellung");
        inputFilePanelCourses = new InputFilePanel(InputFileType.COURSE_FILE, dlgAddCourses);
        dlgAddCourses.getContentPane().add(inputFilePanelCourses, BorderLayout.CENTER);

        dlgAddStudyPrograms = new JDialog(this);
        dlgAddStudyPrograms.setLayout(new BorderLayout());
        dlgAddStudyPrograms.setSize(640, 480);
        dlgAddStudyPrograms.setLocationRelativeTo(null);
        dlgAddStudyPrograms.setTitle("Studiengangsdateien für Planerstellung");
        inputFilePanelStudyPrograms = new InputFilePanel(InputFileType.ROOM_FILE, dlgAddStudyPrograms);
        dlgAddStudyPrograms.getContentPane().add(inputFilePanelStudyPrograms, BorderLayout.CENTER);

        dlgAddOutputDirectory = new JDialog(this);
        dlgAddOutputDirectory.setLayout(new BorderLayout());
        dlgAddOutputDirectory.setSize(640, 480);
        dlgAddOutputDirectory.setLocationRelativeTo(null);
        dlgAddOutputDirectory.setTitle("Ausgabeverzeichnisse für Plandateien");
        inputFilePanelOutputDirectory = new InputFilePanel(InputFileType.OUTPUT_DIRECTORY, dlgAddOutputDirectory);
        dlgAddOutputDirectory.getContentPane().add(inputFilePanelOutputDirectory, BorderLayout.CENTER);

        treeMasterSchedule.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Gesamtplan")));
    }

    /**
     * Events und Aktionen für den Navigationsbaum erzeugen und binden.
     */
    private void initializeTreeEvents() {

        treeMasterSchedule.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                final int timeSlotWidth = 25;

                if (treeMasterSchedule.getLastSelectedPathComponent() == null) {
                    scrollPaneTable.setViewportView(lSelectNode);
                    return;
                }

                DefaultMutableTreeNode n = (DefaultMutableTreeNode) treeMasterSchedule.getLastSelectedPathComponent();

                if (n.getUserObject() instanceof Room) {
                    Schedule schedule = masterSchedule.getSchedule((Room) n.getUserObject());
                    scheduleTableModel.setSchedule(schedule);
                    scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(timeSlotWidth);
                    scrollPaneTable.setViewportView(scheduleTable);
                } else if (n.getUserObject() instanceof Academic) {
                    Schedule schedule = masterSchedule.getSchedule((Academic) n.getUserObject());
                    scheduleTableModel.setSchedule(schedule);
                    scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(timeSlotWidth);
                    scrollPaneTable.setViewportView(scheduleTable);
                } else if (n.getUserObject() instanceof Semester) {
                    Semester semester = (Semester) n.getUserObject();
                    Schedule schedule = masterSchedule.getSchedule(semester.getStudyProgram(), semester);
                    scheduleTableModel.setSchedule(schedule);
                    scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(timeSlotWidth);
                    scrollPaneTable.setViewportView(scheduleTable);
                } else if (n.getUserObject() instanceof String && n.getUserObject().toString().equals("Gesamtplan")) {
                    // scrollPaneTable.setViewportView(panelMasterScheduleStatus);
                } else {
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
    private void init() {

        dataController = new DataController();
        InputFileHelper.loadCourses(dataController);
        InputFileHelper.loadRooms(dataController);
        InputFileHelper.loadStudyPrograms(dataController);

        scheduler = new Scheduler();
        scheduler.setDataController(dataController);
        scheduler.setStrategy(Strategy.getStrategyInstanceByClassName("CostOptimizedStrategy"));
        scheduler.executeStrategy(null);

        masterSchedule = scheduler.getMasterSchedule();

        initMasterScheduleInfos(masterSchedule);

        DefaultTreeModel treeModel = new DefaultTreeModel(buildTreeModel());

        treeMasterSchedule.setModel(treeModel);

        scheduleTableModel = new ScheduleTableModel();
        scheduleTable.setModel(scheduleTableModel);

    }

    /**
     * Erzeugt auf Basis des Gesamtplanes (MasterSchedule) das TreeModel
     * mithilfe von verknüpften DefaultMutableTreeNodes
     */
    private TreeNode buildTreeModel() {
        DefaultMutableTreeNode rMasterSchedule = new DefaultMutableTreeNode("Gesamtplan");
        DefaultMutableTreeNode rRooms = new DefaultMutableTreeNode("Raumpläne");
        rMasterSchedule.add(rRooms);
        DefaultMutableTreeNode rRoomsInternal = new DefaultMutableTreeNode("Interne Räume");
        List<Room> rooms = masterSchedule.getRooms(RoomType.INTERNAL);
        Collections.sort(rooms);
        for (Room room : rooms) {
            rRoomsInternal.add(new DefaultMutableTreeNode(room));
        }
        rRooms.add(rRoomsInternal);

        DefaultMutableTreeNode rRoomsExternal = new DefaultMutableTreeNode("Externe Räume");
        rooms = masterSchedule.getRooms(RoomType.EXTERNAL);
        Collections.sort(rooms);
        for (Room room : rooms) {
            rRoomsExternal.add(new DefaultMutableTreeNode(room));
        }
        rRooms.add(rRoomsExternal);

        DefaultMutableTreeNode rAcademics = new DefaultMutableTreeNode("Dozentenpläne");
        List<Academic> academics = dataController.getAcademics();
        Collections.sort(academics);
        for (Academic academic : academics) {
            rAcademics.add(new DefaultMutableTreeNode(academic));
        }
        rMasterSchedule.add(rAcademics);

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
         * Pläne pro Kurs ausgeben
         */
        DefaultMutableTreeNode rCourses = new DefaultMutableTreeNode("Pläne pro Kurs");
        rMasterSchedule.add(rCourses);

        List<Course> courses = dataController.getCourses();
        Collections.sort(courses);
        for (Course course : courses) {
            DefaultMutableTreeNode rCourse = new DefaultMutableTreeNode(course);
            rCourses.add(rCourse);

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

        return rMasterSchedule;
    }

    /**
     * Erzeugt mithilfe des Gesamtplans eine Ausgabe der wichtigsten Rahmendaten
     * zum Plan
     *
     * @param masterSchedule Der MasterSchedule des Rahmendaten ausgegeben
     * werden sollen
     */
    private void initMasterScheduleInfos(MasterSchedule masterSchedule) {
//        lRoomsInternal.setText(String.valueOf(masterSchedule.getRoomCount(RoomType.INTERNAL, false)));
//        lRoomsInternalUsed.setText(String.valueOf(masterSchedule.getRoomCount(RoomType.INTERNAL, true)));
//        lRoomsExternal.setText(String.valueOf(masterSchedule.getRoomCount(RoomType.EXTERNAL, false)));
//        lCoursesScheduledIntern.setText(String.valueOf(masterSchedule.getTotalRoomBlocks(RoomType.INTERNAL)));
//        lCoursesScheduledExternal.setText(String.valueOf(masterSchedule.getTotalRoomBlocks(RoomType.EXTERNAL)));
//
//        double percentInternal = masterSchedule.getTotalRoomBlocks(RoomType.INTERNAL) * 100 / (masterSchedule.getTotalRoomBlocks(RoomType.INTERNAL) + masterSchedule.getTotalRoomBlocks(RoomType.EXTERNAL));
//        lPercentCoursesInternal.setText(String.valueOf(Math.rint(percentInternal)));
//
//        lSeatsInternalBlocked.setText(String.valueOf(masterSchedule.getInternallyScheduledSeats()));
//        lSeatsExternal.setText(String.valueOf(masterSchedule.getExternallyScheduledSeats()));
//
//        percentInternal = masterSchedule.getInternallyScheduledSeats() * 100 / (masterSchedule.getInternallyScheduledSeats() + masterSchedule.getExternallyScheduledSeats());
//        lPercentInternal.setText(String.valueOf(Math.rint(percentInternal)));
//
//        lBlocksCount.setText(String.valueOf(masterSchedule.getTotalRoomBlocks(RoomType.INTERNAL) + masterSchedule.getTotalRoomBlocks(RoomType.EXTERNAL)));
    }

    /**
     * Liefert den DatenController
     *
     * @return the dataController
     */
    public DataController getDataController() {
        return dataController;
    }

    /**
     * Setzt den DatenController
     *
     * @param dataController the dataController to set
     */
    public void setDataController(DataController dataController) {
        this.dataController = dataController;
    }

    /**
     * Liefert den OutputController
     *
     * @return the outputController
     */
    public OutputController getOutputController() {
        return outputController;
    }

    /**
     * Setzt den OutputController
     *
     * @param outputController the outputController to set
     */
    public void setOutputController(OutputController outputController) {
        this.outputController = outputController;
    }

    /**
     * Liefert den Scheduler
     *
     * @return the scheduler
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * Setzt den Scheduler
     *
     * @param scheduler the scheduler to set
     */
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Liefert den MasterSchedule
     *
     * @return the masterSchedule
     */
    public MasterSchedule getMasterSchedule() {
        return masterSchedule;
    }

    /**
     * Setzt den MasterSchedule
     *
     * @param masterSchedule the masterSchedule to set
     */
    public void setMasterSchedule(MasterSchedule masterSchedule) {
        this.masterSchedule = masterSchedule;
    }

}
