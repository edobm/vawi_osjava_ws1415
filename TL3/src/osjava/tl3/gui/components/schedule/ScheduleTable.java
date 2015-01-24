package osjava.tl3.gui.components.schedule;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import osjava.tl3.model.schedule.ScheduleElementViewWrapper;
import osjava.tl3.model.schedule.TimeSlot;

/**
 * Implementiert eine JTable für die spezfischen Anforderungen zur Anzeige einer
 * Plansicht (ScheduleView). Die Tabelle nutzt zur Darstellung von
 * ScheduleElementViewWrapper Einträgen einen entsprechenden Renderer
 * (ScheduleTableCellRenderer).
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
     * Ausgabe für die Planelemente (ScheduleElementImpl) der spezialisierte
     * Zellenformatierer ScheduleTableCellRenderer zugewiesen.
     *
     * @param tableModel Das TableModel auf dem diese Tabelle operiert
     */
    public ScheduleTable(TableModel tableModel) {
        super(tableModel);

        /**
         * Grundlegende Einstellungen vornehmen
         */
        setGridColor(Color.LIGHT_GRAY);
        setDoubleBuffered(true);
        setColumnSelectionAllowed(false);
        setCellSelectionEnabled(false);

        /**
         * Diese Tabelle zeigt entwerder Instanzen von ScheduleElementViewWrapper
         * oder Instanzen der Klasse TimeSlot an.
         * In beiden Fällen wird die spezifische Darstellung über die spezialisierte
         * Klasse ScheduleTableCellRenderer realisiert.
         */
        setDefaultRenderer(ScheduleElementViewWrapper.class, new ScheduleTableCellRenderer());
        setDefaultRenderer(TimeSlot.class, new ScheduleTableCellRenderer());

    }

    /**
     * Passt die Höhe jeder Zeile an ihren Inhalt an. Da der Inhalt einer
     * Tabellenzelle variablen hoch und breit ist durch eine abweichende Anzahl
     * von Terminen pro Plankoordinate, muss dynamisch die notwendige Höhe pro
     * Zeile ermittelt werden.
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
         * Alle Zeilen durchlaufen.
         */
        for (int row = 0; row < getRowCount(); row++) {

            maxHeightInRow = 0;
            currentRowHeight = 0;

            /**
             * Alle Spalten durchlaufen und für jede Zelle die gewünschte Höhe
             * ermitteln und die maximale Höhe jeweils zwischenspeichern
             */
            for (int column = 0; column < getColumnCount(); column++) {

                cellRenderer = (JLabel) prepareRenderer(getDefaultRenderer(ScheduleElementViewWrapper.class), row, column);
                currentRowHeight = cellRenderer.getPreferredSize().height + getIntercellSpacing().height + 20;
                
                /**
                 * Ggf. maximale Höhe anpassen
                 */
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
