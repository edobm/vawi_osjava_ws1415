package osjava.tl3.ui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import javafx.scene.control.SplitPane;
import javax.swing.JFrame;
import osjava.tl3.logic.io.OutputController;
import osjava.tl3.logic.planning.Scheduler;
import osjava.tl3.model.MasterSchedule;
import osjava.tl3.model.controller.DataController;

/**
 *
 * @author 
 */
public class SchedulerUI extends JFrame {
    
    private DataController dataController;
    private OutputController outputController;
    private Scheduler scheduler;
    private MasterSchedule masterSchedule;

    public SchedulerUI() throws HeadlessException {
        setSize(1200, 900);
        setTitle("VAWi OSJAVA TL3");
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        SplitPane splitPane = new SplitPane();
        
        //getContentPane().add(splitPane, BorderLayout.CENTER);
        
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
