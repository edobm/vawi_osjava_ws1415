package osjava.tl3.ui;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import osjava.tl3.model.ScheduleElement;
import osjava.tl3.model.TimeSlot;

/**
 * Implement eine JTable für die spezfischen Anforderungen zur Anzeige eines
 * Planes (Schedule)
 *
 * @author Meikel Bode
 */
public class ScheduleTable extends JTable {

    /**
     * Erzeugt eine neue Instanz von ScheduleTable und setzt grundlegende
     * Einstellungen. Weiterhin wird zur Erzeugung einer stundenplanartigen
     * Ausgabe Elemente (ScheduleElement) der jeweils zugeordneten Instanz der
     * Klasse Schedule (Plan), sowie der Instanzen von TimeSlot, der
     * spezialisierte Renderer ScheduleTableCellRenderer zugewiesen.
     *
     * @param dm Das TableModel
     */
    public ScheduleTable(TableModel dm) {
        super(dm);
       
        /**
         * Grundlegende Einstellungen vornehmen
         */
        setRowHeight(90);
        setDefaultRenderer(ScheduleElement.class, new ScheduleTableCellRenderer());
        setDefaultRenderer(TimeSlot.class, new ScheduleTableCellRenderer());
        setGridColor(Color.LIGHT_GRAY);

        setColumnSelectionAllowed(false);
        setCellSelectionEnabled(false);
    }

}