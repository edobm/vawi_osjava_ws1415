package osjava.tl3.ui;


import java.awt.Color;
import javax.swing.JTable;
import osjava.tl3.model.ScheduleElement;
import osjava.tl3.model.TimeSlot;

/**
 *
 * @author Meikel Bode
 */
public class ScheduleTable extends JTable {

    public ScheduleTable() {
        super();
        setRowHeight(90);
        setDefaultRenderer(ScheduleElement.class, new ScheduleTableCellRenderer());
        setDefaultRenderer(TimeSlot.class, new ScheduleTableCellRenderer());
        setGridColor(Color.LIGHT_GRAY);
        
        setColumnSelectionAllowed(false);
        setCellSelectionEnabled(false);
       
    }
    
    
}
