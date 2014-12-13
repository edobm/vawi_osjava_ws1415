package osjava.tl3.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import osjava.tl3.logic.io.InputFileHelper;
import osjava.tl3.logic.io.OutputController;
import osjava.tl3.logic.planning.Scheduler;
import osjava.tl3.logic.planning.strategies.Strategy;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.MasterSchedule;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.ScheduleElement;
import osjava.tl3.model.Semester;
import osjava.tl3.model.StudyProgram;
import osjava.tl3.model.controller.DataController;

/**
 *
 * @author
 */
public class SchedulerUI extends JFrame {

    JButton btnOpenProtocol = new JButton("Protokoll anzeigen");
    JButton btnSelectInput = new JButton("Eingabeverzeichnis");
    JButton btnSelectOutput = new JButton("Ausgabeverzeichnis");
    JButton btnGenerateMasterSchedule = new JButton("Plan erzeugen");
    
    JComboBox<String> comboBoxStrategies = new JComboBox<>(new String []{"Kostenoptimiert", "Nur Extern", "Nur Intern"});
    
    JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JSplitPane splitPane = new JSplitPane();
    JScrollPane scrollPaneTree = new JScrollPane();
    JScrollPane scrollPaneTable = new JScrollPane();
    JTree treeMasterSchedule = new JTree();
    
    private DataController dataController;
    private OutputController outputController;
    private Scheduler scheduler;
    private MasterSchedule masterSchedule;

    ScheduleTable scheduleTable = new ScheduleTable();
    
    ScheduleTableModel scheduleTableModel;

    JLabel lSelectNode = new JLabel("Bitte wählen Sie das Objekt in der Baumansicht, dessen Plan Sie einsehen möchten.", SwingConstants.CENTER);

    public SchedulerUI() throws HeadlessException {
        setSize(1200, 900);
        setTitle("VAWi OSJAVA TL3");
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        getContentPane().add(panelTop, BorderLayout.NORTH);
        panelTop.add(btnOpenProtocol);
        panelTop.add(btnSelectInput);
        panelTop.add(btnSelectOutput);
        btnSelectInput.addActionListener(new ActionListener() {
 
            @Override
            public void actionPerformed(ActionEvent e)
            {
              
            }
        });      
        
        panelTop.add(comboBoxStrategies);
        panelTop.add(btnGenerateMasterSchedule);
        btnGenerateMasterSchedule.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                init();
            }
        });
        
        getContentPane().add(splitPane, BorderLayout.CENTER);
        
        splitPane.add(scrollPaneTree, JSplitPane.LEFT);
        scrollPaneTree.setViewportView(treeMasterSchedule);
        
        splitPane.add(scrollPaneTable, JSplitPane.RIGHT);
        scrollPaneTable.setViewportView(lSelectNode);
        
       

        

    }

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
     * @return the dataController
     */
    public DataController getDataController() {
        return dataController;
    }

    /**
     * @param dataController the dataController to set
     */
    public void setDataController(DataController dataController) {
        this.dataController = dataController;
    }

    /**
     * @return the outputController
     */
    public OutputController getOutputController() {
        return outputController;
    }

    /**
     * @param outputController the outputController to set
     */
    public void setOutputController(OutputController outputController) {
        this.outputController = outputController;
    }

    /**
     * @return the scheduler
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * @param scheduler the scheduler to set
     */
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * @return the masterSchedule
     */
    public MasterSchedule getMasterSchedule() {
        return masterSchedule;
    }

    /**
     * @param masterSchedule the masterSchedule to set
     */
    public void setMasterSchedule(MasterSchedule masterSchedule) {
        this.masterSchedule = masterSchedule;
    }

}
