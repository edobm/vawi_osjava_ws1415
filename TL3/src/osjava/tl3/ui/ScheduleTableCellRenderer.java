package osjava.tl3.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import osjava.tl3.model.ScheduleElement;
import osjava.tl3.model.TimeSlot;

/**
 * Dieser Renderer kann Instanzen der Klassen ScheduleElement und TimeSlot ausgeben.
 * Die Klasse erweitert die Klasse DefaultTableCellRenderer
 * 
 * @author Christian Müller
 */
public class ScheduleTableCellRenderer extends DefaultTableCellRenderer {

    Color colorTutorial = new Color(143, 188, 143);
    Color colorHearing = new Color(240, 255, 240);
    Color colorFree = Color.WHITE;
    Color colorTimeSlot = new Color(212, 212, 212);
    
    /**
     * Erzeugt eine neue Instanz des Renderers
     */
    public ScheduleTableCellRenderer() {
        super();
        setBorder(new LineBorder(Color.DARK_GRAY, 1));
        setFont(new Font("Helvetica", Font.PLAIN, 12));
    }

    /**
     * Erzeugt eine Instanz von JLabel, die als darstellende Komponente der zugewiesenen
     * Instanz von JTable an der Koordinate (row, column) verwendet werden soll.
     * Dieser Renderer kann Instanzen von ScheduleElement und TimeSlot darstellen.
     * @param table Die JTable auf der der Renderer operiert
     * @param value Der Wert des TableModels, der durch diesen Renderer dargestellt werden soll
     * @param isSelected Ob die aktuelle Zelle der Tabelle aktuell selektiert ist
     * @param hasFocus Ob die aktuelle Zelle der Tabelle aktuell den Fokus hält
     * @param row Der Index der Tabellenzeile
     * @param column Der Index der Tabellenspalte
     * @return Die Renderer Komponente (JLabel)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column) {
        
        if (value instanceof ScheduleElement) {
            ScheduleElement scheduleElement = (ScheduleElement) value;
          
            if (!scheduleElement.isBlocked()) {
                setText("");
                setToolTipText("Nicht belegt.");
                setBackground(colorFree);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("<html><body>");
                sb.append("Kurs ").append(scheduleElement.getCourse().getNumber()).append(" (").append(scheduleElement.getCourse().getType().getName().equals("Uebung") ? "Übung" : "Vorlesung").append("):<br>");
                sb.append("<b>").append(scheduleElement.getCourse().getName()).append("</b><br>");
                sb.append("Raum: ").append(scheduleElement.getRoom().getName()).append("<br>");
                sb.append("Dozent: ").append(scheduleElement.getCourse().getAcademic().getName()).append("<br>");
                sb.append("Teilnehmer: ").append(scheduleElement.getCourse().getStudents());
                sb.append("</body></html>");
                setText(sb.toString());
                setBackground(colorHearing);
                setToolTipText("<html><body>Raum ID: " + scheduleElement.getRoom().getRoomId() + "<br>Plätze vorhanden: " + scheduleElement.getRoom().getSeats() + "<br>Plätze benötigt: " + scheduleElement.getCourse().getStudents() + "<br>Vorhandene Austattung: " +scheduleElement.getRoom().getAvailableEquipments()+ "<br>Benötigte Austattung: " + scheduleElement.getCourse().getRequiredEquipments()+"</body></html>");
            }
        } else if (value instanceof TimeSlot) {
            setText(value.toString());
            setToolTipText("");
            setBackground(colorTimeSlot);
        } else {
            setToolTipText("Nicht belegt.");
            setText("");
            setBackground(colorFree);
        }
        return this;
    }
    
}
