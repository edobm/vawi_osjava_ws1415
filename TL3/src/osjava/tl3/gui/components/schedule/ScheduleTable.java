package osjava.tl3.gui.components.schedule;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import osjava.tl3.model.TimeSlot;
import osjava.tl3.model.schedule.ScheduleElementViewWrapper;

/**
 * Implement eine JTable für die spezfischen Anforderungen zur Anzeige eines
 * Planes (Schedule)
 *
 * @author Meikel Bode
 */
public class ScheduleTable extends JTable {

    /**
     * Die Standardzeilenhöhe;
     */
    private final int defaultRowHeight = 90;

    /**
     * Erzeugt eine neue Instanz von ScheduleTable und setzt grundlegende
     * Einstellungen. Weiterhin wird zur Erzeugung einer stundenplanartigen
     * Ausgabe Elemente (ScheduleElementImpl) der jeweils zugeordneten Instanz
     * der Klasse Schedule (Plan), sowie der Instanzen von TimeSlot, der
     * spezialisierte Renderer ScheduleTableCellRenderer zugewiesen.
     *
     * @param dm Das TableModel
     */
    public ScheduleTable(TableModel dm) {
        super(dm);

        /**
         * Grundlegende Einstellungen vornehmen
         */
        setDefaultRenderer(ScheduleElementViewWrapper.class, new ScheduleTableCellRenderer());
        setDefaultRenderer(TimeSlot.class, new ScheduleTableCellRenderer());
        setGridColor(Color.LIGHT_GRAY);

        setColumnSelectionAllowed(false);
        setCellSelectionEnabled(false);

    }

    /**
     * Passt die Höhe jeder Zeile an ihren Inhalt an
     */
    @Override
    public void doLayout() {
        super.doLayout();
        int maxHeightInRow = 0;
        int currentRowHeight = 0;
        JLabel cellRenderer;

        /**
         * Alle Zeilen durchlaufen
         */
        for (int row = 0; row < getRowCount(); row++) {

            maxHeightInRow = 0;
            currentRowHeight = 0;

            /**
             * Alle Spalten durchlaufen und für jede Zelle die gewünschte Höhe
             * ermitteln
             */
            for (int column = 0; column < getColumnCount(); column++) {

                cellRenderer = (JLabel) prepareRenderer(getDefaultRenderer(ScheduleElementViewWrapper.class), row, column);
                currentRowHeight = cellRenderer.getPreferredSize().height + getIntercellSpacing().height + 20;
                if (currentRowHeight > maxHeightInRow) {
                    maxHeightInRow = currentRowHeight;
                }

            }

            /**
             * Zeilenhöhe setzen, aber nur wenn diese höher ist als die
             * Standardhöhe
             */
            setRowHeight(row, maxHeightInRow > defaultRowHeight ? maxHeightInRow : defaultRowHeight);
        }

    }

}