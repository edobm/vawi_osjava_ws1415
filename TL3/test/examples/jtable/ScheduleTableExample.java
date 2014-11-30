package examples.jtable;

import javax.swing.JScrollPane;
import osjava.tl3.logic.planning.InputFileHelper;
import osjava.tl3.logic.planning.Scheduler;
import osjava.tl3.logic.planning.strategies.StrategyType;
import osjava.tl3.model.Academic;
import osjava.tl3.model.MasterSchedule;
import osjava.tl3.model.controller.DataController;

/**
 *
 * @author Meikel Bode
 */
public class ScheduleTableExample extends javax.swing.JFrame {
    
    DataController dataController;
    Scheduler scheduler;
    MasterSchedule masterSchedule;
    
    ScheduleTable scheduleTable;
    ScheduleTableModel scheduleTableModel;
    
    
    
    /**
     * Creates new form NewJFrame
     */
    public ScheduleTableExample() {
        
        initComponents();
        
        dataController = new DataController();
        InputFileHelper.loadCourses(dataController);
        InputFileHelper.loadRooms(dataController);
        InputFileHelper.loadStudyPrograms(dataController);
        
        scheduler = new Scheduler();
        scheduler.setDataController(dataController);
        scheduler.setStrategyType(StrategyType.COST_OPTIMIZED);
        scheduler.executeStrategy(null);
        
        masterSchedule = scheduler.getMasterSchedule();
        
        scheduleTable = new ScheduleTable();
        scheduleTableModel = new ScheduleTableModel();
        scheduleTable.setModel(scheduleTableModel);
        
        Academic academic = dataController.getAcademicByName("Hamann");
        
        scheduleTableModel.setSchedule(masterSchedule.getSchedule(academic));
        
        JScrollPane sp = new JScrollPane();
        sp.setViewportView(scheduleTable);
        
        getContentPane().add(sp);
        
        
        setSize(1024, 768);
        setLocationByPlatform(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(ScheduleTableExample.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScheduleTableExample.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScheduleTableExample.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScheduleTableExample.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScheduleTableExample().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
