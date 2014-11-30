package examples.jtable;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import osjava.tl3.model.ScheduleElement;
import osjava.tl3.model.TimeSlot;

/**
 *
 * @author Meikel Bode
 */
public class ScheduleTableCellRenderer extends DefaultTableCellRenderer {
    
    public ScheduleTableCellRenderer() {
        super();
        setBorder(new LineBorder(Color.GRAY, 1));
        setBackground(Color.WHITE);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column) {
        if (value instanceof ScheduleElement) {
            ScheduleElement scheduleElement = (ScheduleElement) value;
            
            if (!scheduleElement.isBlocked()) {
                setText("");
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("<html><body>");
                sb.append("<b>").append(scheduleElement.getCourse().getName()).append("</b><br>");
                sb.append("Raum: ").append(scheduleElement.getRoom().getName()).append("<br>");
                sb.append("Dozent: ").append(scheduleElement.getCourse().getAcademic().getName());
                sb.append("</body></html>");
                setText(sb.toString());
                setBackground(Color.LIGHT_GRAY);
            }
        } else if (value instanceof TimeSlot) {
            setText(value.toString());
        } else {
            setText("");
        }
        return this;
    }
}
