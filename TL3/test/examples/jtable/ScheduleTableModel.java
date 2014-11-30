package examples.jtable;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import osjava.tl3.model.Day;
import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleCoordinate;
import osjava.tl3.model.ScheduleElement;
import osjava.tl3.model.ScheduleType;
import osjava.tl3.model.TimeSlot;

/**
 *
 * @author Meikel Bode
 */
public class ScheduleTableModel extends DefaultTableModel  {

    private Schedule schedule = new Schedule(ScheduleType.ACADAMIC);

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return 5;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Zeit";
            case 1:
                return "Montag";
            case 2:
                return "Dienstag";
            case 3:
                return "Mittwoch";
            case 4:
                return "Donnerstag";
            case 5:
                return "Freitag";
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return ScheduleElement.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int col) {
        
        if (col == 0) {
            return TimeSlot.valueOf(row);
        } else {

            ScheduleCoordinate scheduleCoordinate = new ScheduleCoordinate(Day.valueOf(col - 1), TimeSlot.valueOf(row));
            
            return schedule.getScheduleElement(scheduleCoordinate);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
       
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        
    }

}
