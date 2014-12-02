package examples.jtable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import osjava.tl3.logic.planning.InputFileHelper;
import osjava.tl3.logic.planning.Scheduler;
import osjava.tl3.logic.planning.strategies.StrategyType;
import osjava.tl3.model.Academic;
import osjava.tl3.model.MasterSchedule;
import osjava.tl3.model.Room;
import osjava.tl3.model.Schedule;
import osjava.tl3.model.StudyProgram;
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

    DefaultComboBoxModel<Room> roomCbModel;
    DefaultComboBoxModel<Academic> academicCbModel;
    DefaultComboBoxModel<StudyProgram> studyProgramCbModel;

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

        roomCbModel = new DefaultComboBoxModel<>(dataController.getRooms().toArray(new Room[dataController.getRooms().size()]));
        academicCbModel = new DefaultComboBoxModel<>(dataController.getAcademics().toArray(new Academic[dataController.getAcademics().size()]));
        
        cbSchedule.setModel(roomCbModel);
        cbSchedule.setSelectedIndex(0);
        updateSelection();
        
        JScrollPane sp = new JScrollPane();
        sp.setViewportView(scheduleTable);

        getContentPane().add(sp);

        setSize(1024, 768);
        setLocationByPlatform(true);
    }
    
    private void updateSelection () {
          if (rbRooms.isSelected()) {
            Room room = (Room)cbSchedule.getSelectedItem();

            if (room != null) {
                Schedule schedule = masterSchedule.getSchedule(room);
                scheduleTableModel.setSchedule(schedule);
                scheduleTable.updateUI();
            }
            
        }
        
         if (rbAcademics.isSelected()) {
            Academic academic = (Academic)cbSchedule.getSelectedItem();

            if (academic != null) {
                Schedule schedule = masterSchedule.getSchedule(academic);
                scheduleTableModel.setSchedule(schedule);
                scheduleTable.updateUI();
            }

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        rbRooms = new javax.swing.JRadioButton();
        rbAcademics = new javax.swing.JRadioButton();
        rbStudyPrograms = new javax.swing.JRadioButton();
        btnShow = new javax.swing.JButton();
        cbSchedule = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        buttonGroup1.add(rbRooms);
        rbRooms.setSelected(true);
        rbRooms.setText("Räume");
        rbRooms.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbRoomsItemStateChanged(evt);
            }
        });
        rbRooms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbRoomsActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbAcademics);
        rbAcademics.setText("Dozenten");
        rbAcademics.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbAcademicsItemStateChanged(evt);
            }
        });
        rbAcademics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAcademicsActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbStudyPrograms);
        rbStudyPrograms.setText("Studiengänge");
        rbStudyPrograms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbStudyProgramsActionPerformed(evt);
            }
        });

        btnShow.setText("Zeigen");
        btnShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowActionPerformed(evt);
            }
        });

        cbSchedule.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbSchedule.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbScheduleItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbRooms)
                .addGap(5, 5, 5)
                .addComponent(rbAcademics)
                .addGap(5, 5, 5)
                .addComponent(rbStudyPrograms)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShow)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnShow)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbRooms)
                            .addComponent(rbAcademics)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(rbStudyPrograms)
                                .addComponent(cbSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 4, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rbStudyProgramsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbStudyProgramsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbStudyProgramsActionPerformed

    private void btnShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowActionPerformed

      


    }//GEN-LAST:event_btnShowActionPerformed

    private void rbRoomsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbRoomsItemStateChanged
        if (rbRooms.isSelected()) {
            cbSchedule.setModel(roomCbModel);
            cbSchedule.setSelectedIndex(0);
            updateSelection();
        }
    }//GEN-LAST:event_rbRoomsItemStateChanged

    private void rbRoomsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbRoomsActionPerformed
        
    }//GEN-LAST:event_rbRoomsActionPerformed

    private void rbAcademicsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbAcademicsItemStateChanged
        if (rbAcademics.isSelected()) {
            cbSchedule.setModel(academicCbModel);
            cbSchedule.setSelectedIndex(0);
            updateSelection();
        }
    }//GEN-LAST:event_rbAcademicsItemStateChanged

    private void rbAcademicsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAcademicsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbAcademicsActionPerformed

    private void cbScheduleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbScheduleItemStateChanged
        updateSelection();
    }//GEN-LAST:event_cbScheduleItemStateChanged

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
    private javax.swing.JButton btnShow;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbSchedule;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton rbAcademics;
    private javax.swing.JRadioButton rbRooms;
    private javax.swing.JRadioButton rbStudyPrograms;
    // End of variables declaration//GEN-END:variables
}