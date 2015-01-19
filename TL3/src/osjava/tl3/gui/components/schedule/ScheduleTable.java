package osjava.tl3.gui.components.schedule;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import osjava.tl3.model.schedule.TimeSlot;
import osjava.tl3.model.schedule.ScheduleElementViewWrapper;

/**
 * Implementiert eine JTable für die spezfischen Anforderungen zur Anzeige eines
 * Planes (Schedule).
 * Die Tabelle nutzt zur Darstellung von ScheduleElementViewWrapper Elementen
 * einen entsprechenden Renderer ScheduleTableCellRenderer.
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
     * @param tableModel Das TableModel
     */
    public ScheduleTable(TableModel tableModel) {
        super(tableModel);

        /**
         * Grundlegende Einstellungen vornehmen
         */
        setDefaultRenderer(ScheduleElementViewWrapper.class, new ScheduleTableCellRenderer());
        setDefaultRenderer(TimeSlot.class, new ScheduleTableCellRenderer());
        setGridColor(Color.LIGHT_GRAY);
        setDoubleBuffered(true);
        setColumnSelectionAllowed(false);
        setCellSelectionEnabled(false);

    }

    /**
     * Passt die Höhe jeder Zeile an ihren Inhalt an.
     * Da der Inhalt einer Tabellenzelle variablen hoch und breit ist durch
     * eine abweichende Anzahl von Terminen pro Plankoordinate, muss dynamisch
     * die notwendige Höhe pro Zeile ermittelt werden.
     * 
     * @see JTable#doLayout() 
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