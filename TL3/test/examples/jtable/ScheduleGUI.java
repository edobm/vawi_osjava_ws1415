package examples.jtable;

import java.util.Collections;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import osjava.tl3.logic.planning.InputFileHelper;
import osjava.tl3.logic.planning.Scheduler;
import osjava.tl3.logic.planning.strategies.StrategyType;
import osjava.tl3.model.Academic;
import osjava.tl3.model.MasterSchedule;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.Schedule;
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
        scheduler.setStrategyType(StrategyType.COST_OPTIMIZED);
        scheduler.executeStrategy(null);

        masterSchedule = scheduler.getMasterSchedule();

        DefaultTreeModel treeModel = new DefaultTreeModel(buildTreeModel());
        jt.setModel(treeModel);

        scheduleTable = new ScheduleTable();
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
        for (Room room : masterSchedule.getRooms(RoomType.INTERNAL)) {
            rRoomsInternal.add(new DefaultMutableTreeNode(room));
        }
        rRooms.add(rRoomsInternal);
        
        DefaultMutableTreeNode rRoomsExternal = new DefaultMutableTreeNode("Externe Räume");
        for (Room room : masterSchedule.getRooms(RoomType.EXTERNAL)) {
            rRoomsExternal.add(new DefaultMutableTreeNode(room));
        }
        rRooms.add(rRoomsExternal);
       

        DefaultMutableTreeNode rAcademics = new DefaultMutableTreeNode("Dozentenpläne");
        for (Academic academic : dataController.getAcademics()) {
            rAcademics.add(new DefaultMutableTreeNode(academic));
        }
        rMasterSchedule.add(rAcademics);

        DefaultMutableTreeNode rStudyPrograms = new DefaultMutableTreeNode("Studiengangspläne");
        for (StudyProgram studyProgram : dataController.getStudyPrograms()) {
            DefaultMutableTreeNode rStudyProgram = new DefaultMutableTreeNode(studyProgram);
            rStudyPrograms.add(rStudyProgram);
            for (Semester semester : studyProgram.getSemesters()) {
                rStudyProgram.add(new DefaultMutableTreeNode(semester));
            }
        }
        rMasterSchedule.add(rStudyPrograms);

        return rMasterSchedule;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        spTree = new javax.swing.JScrollPane();
        jt = new javax.swing.JTree();
        spTable = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 738, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

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

        DefaultMutableTreeNode n = (DefaultMutableTreeNode) jt.getLastSelectedPathComponent();

        if (n.getUserObject() instanceof Room) {
            Schedule schedule = masterSchedule.getSchedule((Room) n.getUserObject());
            scheduleTableModel.setSchedule(schedule);
            spTable.setViewportView(scheduleTable);
        } else if (n.getUserObject() instanceof Academic) {
            Schedule schedule = masterSchedule.getSchedule((Academic) n.getUserObject());
            scheduleTableModel.setSchedule(schedule);
            spTable.setViewportView(scheduleTable);
        } else if (n.getUserObject() instanceof Semester) {
            Semester semester = (Semester) n.getUserObject();
            Schedule schedule = masterSchedule.getSchedule(semester.getStudyProgram(), semester);
            scheduleTableModel.setSchedule(schedule);
            spTable.setViewportView(scheduleTable);
        } else {
            spTable.setViewportView(lSelectNode);
        }

    }//GEN-LAST:event_jtValueChanged

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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTree jt;
    private javax.swing.JScrollPane spTable;
    private javax.swing.JScrollPane spTree;
    // End of variables declaration//GEN-END:variables
}