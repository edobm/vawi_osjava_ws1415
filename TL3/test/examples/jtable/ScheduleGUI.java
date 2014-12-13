package examples.jtable;

import osjava.tl3.ui.ScheduleTableModel;
import osjava.tl3.ui.ScheduleTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import osjava.tl3.logic.io.InputFileHelper;
import osjava.tl3.logic.planning.Scheduler;
import osjava.tl3.logic.planning.strategies.Strategy;
import osjava.tl3.logic.planning.strategies.helpers.StrategyProtocol;
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
 *
 * @author Meikel Bode
 */
public class ScheduleGUI extends javax.swing.JFrame {

    DataController dataController;
    Scheduler scheduler;
    MasterSchedule masterSchedule;

    ScheduleTable scheduleTable;
    ScheduleTableModel scheduleTableModel;

    JLabel lSelectNode;

    /**
     * Creates new form ScheduleGUI
     */
    public ScheduleGUI() {
        initComponents();
        init();

        setSize(1200, 800);
        setLocationRelativeTo(null);

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
        jt.setModel(treeModel);

        scheduleTable = new  ScheduleTable();
        scheduleTableModel = new ScheduleTableModel();
        scheduleTable.setModel(scheduleTableModel);

        lSelectNode = new JLabel("Bitte wählen Sie das Objekt in der Baumansicht, dessen Plan Sie einsehen möchten.", SwingConstants.CENTER);

        spTable.setViewportView(lSelectNode);

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
        lRoomsInternal.setText(String.valueOf(masterSchedule.getRoomCount(RoomType.INTERNAL, false)));
        lRoomsInternalUsed.setText(String.valueOf(masterSchedule.getRoomCount(RoomType.INTERNAL, true)));
        lRoomsExternal.setText(String.valueOf(masterSchedule.getRoomCount(RoomType.EXTERNAL, false)));
        lCoursesScheduledIntern.setText(String.valueOf(masterSchedule.getTotalRoomBlocks(RoomType.INTERNAL)));
        lCoursesScheduledExternal.setText(String.valueOf(masterSchedule.getTotalRoomBlocks(RoomType.EXTERNAL)));
        
        double percentInternal = masterSchedule.getTotalRoomBlocks(RoomType.INTERNAL) * 100 / (masterSchedule.getTotalRoomBlocks(RoomType.INTERNAL) + masterSchedule.getTotalRoomBlocks(RoomType.EXTERNAL));
        lPercentCoursesInternal.setText(String.valueOf(Math.rint(percentInternal)));

        lSeatsInternalBlocked.setText(String.valueOf(masterSchedule.getInternallyScheduledSeats()));
        lSeatsExternal.setText(String.valueOf(masterSchedule.getExternallyScheduledSeats()));

        percentInternal = masterSchedule.getInternallyScheduledSeats() * 100 / (masterSchedule.getInternallyScheduledSeats() + masterSchedule.getExternallyScheduledSeats());
        lPercentInternal.setText(String.valueOf(Math.rint(percentInternal)));

        lBlocksCount.setText(String.valueOf(masterSchedule.getTotalRoomBlocks(RoomType.INTERNAL) + masterSchedule.getTotalRoomBlocks(RoomType.EXTERNAL)));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        protocolDialog = new javax.swing.JDialog();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        panelMasterScheduleStatus = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lRoomsInternal = new javax.swing.JLabel();
        lRoomsExternal = new javax.swing.JLabel();
        lSeatsExternal = new javax.swing.JLabel();
        lBlocksCount = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lSeatsInternalBlocked = new javax.swing.JLabel();
        lCoursesScheduledIntern = new javax.swing.JLabel();
        lCoursesScheduledExternal = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lRoomsInternalUsed = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lPercentInternal = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lPercentCoursesInternal = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        spTree = new javax.swing.JScrollPane();
        jt = new javax.swing.JTree();
        spTable = new javax.swing.JScrollPane();

        protocolDialog.setTitle("Protokoll der Strategieausführung");

        jButton2.setText("Schließen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout protocolDialogLayout = new javax.swing.GroupLayout(protocolDialog.getContentPane());
        protocolDialog.getContentPane().setLayout(protocolDialogLayout);
        protocolDialogLayout.setHorizontalGroup(
            protocolDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(protocolDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(protocolDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, protocolDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        protocolDialogLayout.setVerticalGroup(
            protocolDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, protocolDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(6, 6, 6))
        );

        jLabel1.setText("Räume (intern):");

        jLabel2.setText("Räume (extern):");

        jLabel3.setText("Externe Plätze belegt:");

        jLabel4.setText("Anzahl Termine:");

        lRoomsInternal.setText("jLabel5");

        lRoomsExternal.setText("jLabel6");

        lSeatsExternal.setText("jLabel7");

        lBlocksCount.setText("jLabel8");

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel5.setText("Informationen zum Gesamtplan");

        jLabel6.setText("Interne Plätze belegt:");

        jLabel7.setText("Kurse intern geplant:");

        jLabel8.setText("Kurse extern geplant:");

        lSeatsInternalBlocked.setText("jLabel9");

        lCoursesScheduledIntern.setText("jLabel9");

        lCoursesScheduledExternal.setText("jLabel10");

        jLabel9.setText("ungenutzt:");

        lRoomsInternalUsed.setText("jLabel10");

        jLabel10.setText("Belegungung intern %:");

        lPercentInternal.setText("jLabel11");

        jLabel11.setText("Kurse intern %:");

        lPercentCoursesInternal.setText("jLabel12");

        javax.swing.GroupLayout panelMasterScheduleStatusLayout = new javax.swing.GroupLayout(panelMasterScheduleStatus);
        panelMasterScheduleStatus.setLayout(panelMasterScheduleStatusLayout);
        panelMasterScheduleStatusLayout.setHorizontalGroup(
            panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                .addGap(214, 214, 214)
                .addComponent(jLabel9)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                .addGap(171, 171, 171)
                .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                        .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(26, 26, 26))))
                        .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMasterScheduleStatusLayout.createSequentialGroup()
                                .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lSeatsInternalBlocked)
                                    .addComponent(lSeatsExternal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(481, 481, 481))
                            .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lRoomsInternal, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lRoomsInternalUsed))
                                .addGap(479, 479, 479))))
                    .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMasterScheduleStatusLayout.createSequentialGroup()
                        .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGap(2, 2, 2)
                                .addComponent(lRoomsExternal, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                                .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                                .addGap(163, 163, 163)
                                .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lCoursesScheduledIntern)
                                    .addComponent(lCoursesScheduledExternal))))
                        .addGap(479, 479, 479))
                    .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                        .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(68, 68, 68)
                                .addComponent(lPercentCoursesInternal, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(22, 22, 22)
                                .addComponent(lPercentInternal, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(26, 26, 26)
                                .addComponent(lBlocksCount, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        panelMasterScheduleStatusLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4, jLabel6, jLabel7, jLabel8});

        panelMasterScheduleStatusLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel5, lBlocksCount, lCoursesScheduledExternal, lCoursesScheduledIntern, lRoomsExternal, lRoomsInternal, lRoomsInternalUsed, lSeatsExternal, lSeatsInternalBlocked});

        panelMasterScheduleStatusLayout.setVerticalGroup(
            panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(lRoomsInternal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(lRoomsInternalUsed))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(lRoomsExternal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lCoursesScheduledIntern))
                .addGap(2, 2, 2)
                .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lCoursesScheduledExternal))
                .addGap(3, 3, 3)
                .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(lPercentCoursesInternal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                        .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(lSeatsInternalBlocked))
                        .addGap(5, 5, 5)
                        .addComponent(jLabel3))
                    .addGroup(panelMasterScheduleStatusLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(lSeatsExternal)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lPercentInternal))
                .addGap(25, 25, 25)
                .addGroup(panelMasterScheduleStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(lBlocksCount))
                .addContainerGap(135, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setRollover(true);

        jButton1.setText("Protokoll zeigen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton3.setText("Exportieren");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        jSplitPane1.setDividerLocation(200);

        jt.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jtValueChanged(evt);
            }
        });
        spTree.setViewportView(jt);

        jSplitPane1.setLeftComponent(spTree);
        jSplitPane1.setRightComponent(spTable);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jtValueChanged
        final int timeSlotWidth = 25;

        if (jt.getLastSelectedPathComponent() == null) {
            spTable.setViewportView(lSelectNode);
            return;
        }

        DefaultMutableTreeNode n = (DefaultMutableTreeNode) jt.getLastSelectedPathComponent();

        if (n.getUserObject() instanceof Room) {
            Schedule schedule = masterSchedule.getSchedule((Room) n.getUserObject());
            scheduleTableModel.setSchedule(schedule);
            scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(timeSlotWidth);
            spTable.setViewportView(scheduleTable);
        } else if (n.getUserObject() instanceof Academic) {
            Schedule schedule = masterSchedule.getSchedule((Academic) n.getUserObject());
            scheduleTableModel.setSchedule(schedule);
            scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(timeSlotWidth);
            spTable.setViewportView(scheduleTable);
        } else if (n.getUserObject() instanceof Semester) {
            Semester semester = (Semester) n.getUserObject();
            Schedule schedule = masterSchedule.getSchedule(semester.getStudyProgram(), semester);
            scheduleTableModel.setSchedule(schedule);
            scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(timeSlotWidth);
            spTable.setViewportView(scheduleTable);
        } else if (n.getUserObject() instanceof String && n.getUserObject().toString().equals("Gesamtplan")) {
            spTable.setViewportView(panelMasterScheduleStatus);
        } else {
            spTable.setViewportView(lSelectNode);
        }

    }//GEN-LAST:event_jtValueChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        protocolDialog.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        jTextArea1.setText(StrategyProtocol.getProtocol());
        protocolDialog.setSize(1024, 800);
        protocolDialog.setLocationRelativeTo(null);;
        protocolDialog.setVisible(true);


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScheduleGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScheduleGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScheduleGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScheduleGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScheduleGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTree jt;
    private javax.swing.JLabel lBlocksCount;
    private javax.swing.JLabel lCoursesScheduledExternal;
    private javax.swing.JLabel lCoursesScheduledIntern;
    private javax.swing.JLabel lPercentCoursesInternal;
    private javax.swing.JLabel lPercentInternal;
    private javax.swing.JLabel lRoomsExternal;
    private javax.swing.JLabel lRoomsInternal;
    private javax.swing.JLabel lRoomsInternalUsed;
    private javax.swing.JLabel lSeatsExternal;
    private javax.swing.JLabel lSeatsInternalBlocked;
    private javax.swing.JPanel panelMasterScheduleStatus;
    private javax.swing.JDialog protocolDialog;
    private javax.swing.JScrollPane spTable;
    private javax.swing.JScrollPane spTree;
    // End of variables declaration//GEN-END:variables
}
