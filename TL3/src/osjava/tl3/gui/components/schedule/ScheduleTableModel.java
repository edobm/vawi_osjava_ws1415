package osjava.tl3.gui.components.schedule;

import javax.swing.table.DefaultTableModel;
import osjava.tl3.model.schedule.Day;
import osjava.tl3.model.schedule.ScheduleCoordinate;
import osjava.tl3.model.schedule.TimeSlot;
import osjava.tl3.model.schedule.ScheduleElementViewWrapper;
import osjava.tl3.model.schedule.ScheduleView;
import osjava.tl3.model.schedule.ScheduleViewUnspecific;

/**
 * Ein auf Instanzen der Klasse Schedule spezialisiertes TableModel. Diese
 * Klasse erweitert die Klasse DefaultTableModel
 *
 * @author Meikel Bode
 *
 * @see DefaultTableModel
 */
public class ScheduleTableModel extends DefaultTableModel {

    /**
     * Die Planinstanz auf der dieses TableModel operiert. Da zum Zeitpunkt der
     * Initialisierung kein Plan existiert wird das Modell mit einer
     * spezifischen ScheduleView Variante intialisiert.
     */
    private ScheduleView scheduleView = new ScheduleViewUnspecific();

    /**
     * Setzt die Instanz der Klasse Schedule, auf der dieses TableModel
     * operieren soll
     *
     * @param scheduleView Die Plansicht die verwendet werden soll
     */
    public void setSchedule(ScheduleView scheduleView) {
        this.scheduleView = scheduleView;
        fireTableDataChanged();
    }

    /**
     * Liefert die Zeilen des Models Dieser Wert ist immer 5! Zeilen: 0800-1000,
     * 1000-1200, 1200-1400, 1400-1600 und 1600-1800
     *
     * @return Die Anzahl der Zeilen
     *
     * @see DefaultTableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return 5;
    }

    /**
     * Liefert die Spaten des Models Dieser Wert ist immer 6! Spalten: Zeitraum,
     * Montag, Dienstag, Mittwoch, Donnerstag, Freitag
     *
     * @return Die Anzahl der Spalten
     *
     * @see DefaultTableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return 6;
    }

    /**
     * Der Bezeichner der gegebenen Spalte
     *
     * @param columnIndex Index der Spalte
     * @return Der Bezeichner der Spalte mit dem gegebenen Index
     *
     * @see DefaultTableModel#getColumnName(int)
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
            default:
                return "";
        }

    }

    /**
     * Die Klasse des Werts der Spalte mit dem gegebenen Index Ist immer
     * ScheduleElementImpl
     *
     * @param columnIndex Der Index
     * @return Die Klasse des Werts am gegebenen Index
     *
     * @see DefaultTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return ScheduleElementViewWrapper.class;
    }

    /**
     * Ob die Zelle am Zeilen- und Spaltenindex editiertbar ist. Ist immer false
     *
     * @param rowIndex Der Index der Zeile
     * @param columnIndex der Index der Spalte
     * @return Ob editierbar oder nicht. Immer False!
     * 
     * @see DefaultTableModel#isCellEditable(int, int) 
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Liefert den Wert an der Position Zeile und Spalte
     *
     * @param row Die Zeile
     * @param col Die Spalte
     * @return Der Wert des Models an den gegeben Indices
     * 
     * @see DefaultTableModel#getValueAt(int, int) 
     */
    @Override
    public Object getValueAt(int row, int col) {

        /**
         * Bei Spaltenindex == 0 immer TimeSlot. Der jeweilige Zeilenindex
         * wird dabei einfach in seine TimeSlot-Enum Entsprechung umgesetzt.
         */
        if (col == 0) {
            return TimeSlot.valueOf(row);
        } 
        /**
         * Bei Spaltenindex != 0 handelt es sich immer um eine Plankoordinate.
         * Für den Zugriff muss zunächst eine Plankoodinate für die Kombination
         * aus Spalte und Zeile erzeugt werden. Dazu wird einfach über die
         * Enums Day und TimeSplot der passende Wert erzeugt und damit der
         * die Plansicht abgefragt.
         */
        else {
            ScheduleCoordinate scheduleCoordinate = new ScheduleCoordinate(Day.valueOf(col - 1), TimeSlot.valueOf(row));
            return scheduleView.getScheduleElement(scheduleCoordinate);
        }
    }

}