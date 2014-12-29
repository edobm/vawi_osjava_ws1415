package osjava.tl3.ui.components.schedule;

import javax.swing.table.DefaultTableModel;
import osjava.tl3.model.Day;
import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleCoordinate;
import osjava.tl3.model.ScheduleElement;
import osjava.tl3.model.ScheduleType;
import osjava.tl3.model.TimeSlot;

/**
 * Ein auf Instanzen der Klasse Schedule spezialisiertes TableModel.
 * Diese Klasse erweiter die Klasse DefaultTableModel
 * 
 * @author Meikel Bode
 */
public class ScheduleTableModel extends DefaultTableModel  {

    /**
     * Die Planinstanz auf der dieses TableModel operiert
     */
    private Schedule schedule = new Schedule(ScheduleType.ACADAMIC);

    /**
     * Setzt die Instanz der Klasse Schedule, auf der dieses TableModel
     * operieren soll
     * @param schedule 
     */
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        fireTableDataChanged();
    }

    /**
     * Liefert die Zeilen des Models
     * Dieser Wert ist immer 5!
     * Zeilen: 0800-1000, 1000-1200, 1200-1400, 1400-1600 und 1600-1800
     * @return Die Anzahl der Zeilen
     */
    @Override
    public int getRowCount() {
        return 5;
    }

    /**
     * Liefert die Spaten des Models
     * Dieser Wert ist immer 6!
     * Spalten: Zeitraum, Montag, Dienstag, Mittwoch, Donnerstag, Freitag
     * @return Die Anzahl der Spalten
     */
    @Override
    public int getColumnCount() {
        return 6;
    }

    /**
     * Der Bezeichner der gegebenen Spalte
     * @param columnIndex Index der Spalte
     * @return Der Bezeichner der Spalte mit dem gegebenen Index
     */
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

    /**
     * Die Klasse des Werts der Spalte mit dem gegebenen Index
     * Ist immer ScheduleElement
     * @param columnIndex Der Index
     * @return Die Klasse des Werts am gegebenen Index
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return ScheduleElement.class;
    }

    /**
     * Ob die Zelle am Zeilen- und Spaltenindex editiertbar ist.
     * Ist immer false
     * @param rowIndex Der Index der Zeile
     * @param columnIndex der Index der Spalte
     * @return Ob editierbar oder nicht. Immer False!
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Liefert den Wert an der Position Zeile und Spalte
     * @param row Die Zeile
     * @param col Die Spalte
     * @return Der Wert des Models an den gegeben Indices
     */
    @Override
    public Object getValueAt(int row, int col) {
        
        if (col == 0) {
            return TimeSlot.valueOf(row);
        } else {

            ScheduleCoordinate scheduleCoordinate = new ScheduleCoordinate(Day.valueOf(col - 1), TimeSlot.valueOf(row));
            
            return schedule.getScheduleElement(scheduleCoordinate);
        }
    }

}