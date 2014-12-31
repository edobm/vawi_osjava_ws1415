package osjava.tl3.ui.components.schedule;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import osjava.tl3.model.TimeSlot;
import osjava.tl3.model.schedule.ScheduleAppointment;
import osjava.tl3.model.schedule.ScheduleElement;

/**
 * Dieser Renderer kann Instanzen der Klassen ScheduleElement und TimeSlot
 * ausgeben. Die Klasse erweitert die Klasse DefaultTableCellRenderer
 *
 * @author Meikel Bode
 */
public class ScheduleTableCellRenderer extends DefaultTableCellRenderer {

    private final Color colorFree = Color.WHITE;
    private final Color colorTimeSlot = new Color(212, 212, 212);

    /**
     * Erzeugt eine neue Instanz des Renderers
     */
    public ScheduleTableCellRenderer() {
        super();
        setBorder(new LineBorder(Color.DARK_GRAY, 1));
        setFont(new Font("Helvetica", Font.PLAIN, 12));
    }

    /**
     * Erzeugt eine Instanz von JLabel, die als darstellende Komponente der
     * zugewiesenen Instanz von JTable an der Koordinate (row, column) verwendet
     * werden soll. Dieser Renderer kann Instanzen von ScheduleElement und
     * TimeSlot darstellen.
     *
     * @param table Die JTable auf der der Renderer operiert
     * @param value Der Wert des TableModels, der durch diesen Renderer
     * dargestellt werden soll
     * @param isSelected Ob die aktuelle Zelle der Tabelle aktuell selektiert
     * ist
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

            if (scheduleElement.isEmpty()) {
                setText("");
                setToolTipText("Nicht belegt.");
                setBackground(colorFree);
            } else {
                setBackground(colorFree);
                StringBuilder sb = new StringBuilder();
                sb.append("<html><body>");
                for (ScheduleAppointment appointment : scheduleElement.getAppointments()) {
                    sb.append("<div style=\" width: 100%; margin: 2px; float: left;");
                    sb.append("border: 1px solid #dddddd; ");

                    if (appointment.getCourse().getType().getName().equals("Uebung")) {
                        sb.append("background-color: #99eeaa; ");
                    } else {
                        sb.append("background-color: #99ddff; ");
                    }
                    sb.append("\">");
                    sb.append("Kurs ");
                    sb.append(appointment.getCourse().getNumber()).append(" (")
                            .append(appointment.getCourse().getType().getName().equals("Uebung") ? "Übung" : "Vorlesung").append("):<br/>");
                    sb.append("<b>").append(appointment.getCourse().getName()).append("</b><br/>");
                    sb.append("Raum: ").append(appointment.getRoom().getName()).append("<br/>");
                    sb.append("Dozent: ").append(appointment.getCourse().getAcademic().getName()).append("<br/>");
                    sb.append("Teilnehmer: ").append(appointment.getCourse().getStudents());
                    sb.append("</div>");

                }
//                
//                setBackground(scheduleElement.getCourse().getType().getName().equals("Uebung") ? colorTutorial : colorHearing);
//                setToolTipText("<html><body>Raum ID: " + scheduleElement.getRoom().getRoomId() + "<br>Plätze vorhanden: " 
//                        + scheduleElement.getRoom().getSeats() + "<br>Plätze benötigt: " + scheduleElement.getCourse().getStudents() 
//                        + "<br>Vorhandene Austattung: " +scheduleElement.getRoom().getAvailableEquipments()+ "<br>Benötigte Austattung: " 
//                        + scheduleElement.getCourse().getRequiredEquipments()+"</body></html>");

                setText(sb.toString());            

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
