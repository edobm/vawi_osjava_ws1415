package osjava.tl3.model.helpers;

import java.util.Iterator;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Day;
import osjava.tl3.model.MasterSchedule;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleCoordinate;
import osjava.tl3.model.ScheduleElement;
import osjava.tl3.model.TimeSlot;

/**
 * Helferklasse zur Ausgabe eines Gesamtplans auf der Konsole
 *
 * @author Meikel Bode
 */
public class MasterScheduleConsoleWriter {

    /**
     * Die Instanz des Gesamtplans auf deren Basis Daten ausgegeben werden
     * sollen
     */
    private final MasterSchedule masterSchedule;

    /**
     * Erzeugt eine neue Instanz der Konsoleneschreibers für die gegebene
     * Gesamtplaninstanz
     *
     * @param masterSchedule Der Gesamtplan
     */
    public MasterScheduleConsoleWriter(MasterSchedule masterSchedule) {
        this.masterSchedule = masterSchedule;
    }

    /**
     * Gibt eine Übersicht der Raumpläne für den gegebene Raumtyp aus
     *
     * @param roomType Der Raumtyp
     */
    public void printRoomScheduleOverview(RoomType roomType) {
        final String formatPattern = "|%-22s|%-10s|%6s|%6s|%7s|%s%n";
        System.out.printf(formatPattern, "Raum", "Typ", "Plätze", "Termine", "Ausstattung");
        printSeparator(true);
        Iterator<Room> rooms = masterSchedule.getRoomSchedules().keySet().iterator();
        while (rooms.hasNext()) {
            Room r = rooms.next();
            if (r.getType() == roomType) {

                Schedule schedule = masterSchedule.getRoomSchedules().get(r);
                System.out.printf(formatPattern, r.getName(), r.getType(), r.getSeats(), 
                        schedule.getBlockedCoordinates().size() + "/" 
                                + masterSchedule.generateMaximumCoordinates().size(), r.getAvailableEquipments());

            }
        }
    }

    /**
     * Gibt die Gesamtstatistik aus
     */
    public void printStatistics() {
        printHeader();
        printCoreStats();
        printSeparator(true);
        printRoomScheduleOverview(RoomType.INTERNAL);
        printSeparator(false);
        printRoomScheduleOverview(RoomType.EXTERNAL);
        printSeparator(false);
        printRoomSchedules(RoomType.INTERNAL);
        printSeparator(false);
        printRoomSchedules(RoomType.EXTERNAL);
        printSeparator(false);
        printAcademicSchedules();

    }

    /**
     * Gibt Kopfinfos aus
     */
    private void printHeader() {
        printSeparator(false);
        System.out.println(" GESAMTPLAN ");
        printSeparator(false);
    }

    /**
     * Gibt eine Trennlinie des gewünschten Typs aus
     *
     * @param single true = "----", false = "===="
     */
    private void printSeparator(boolean single) {

        if (single) {
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println("===============================================================================================================================================================================================================================================================================================================================================================================");
        }

    }

    /**
     * Gibt zentrale Statistiken aus
     */
    public void printCoreStats() {
        System.out.println("Räume (intern)     : " + masterSchedule.getRoomCount(RoomType.INTERNAL, false));
        System.out.println("Räume (extern)     : " + masterSchedule.getRoomCount(RoomType.EXTERNAL, false));
        System.out.println("Interne Sitzplätze : " + masterSchedule.getInternallyScheduledSeats());
        System.out.println("Externe Sitzplätze : " + masterSchedule.getExternallyScheduledSeats());
        System.out.println("Anzahl Termine     : " + (masterSchedule.getTotalRoomBlocks(RoomType.INTERNAL) + masterSchedule.getTotalRoomBlocks(RoomType.EXTERNAL)));
    }

    /**
     * Gibt Raumpläne des gewünschten Raumtyps aus
     *
     * @param roomType Der Raumtyp
     */
    public void printRoomSchedules(RoomType roomType) {

        Iterator<Room> rooms = masterSchedule.getRoomSchedules().keySet().iterator();
        while (rooms.hasNext()) {
            Room r = rooms.next();
            if (r.getType() == roomType) {
                printSeparator(false);
                System.out.println("Raumplan: " + r + "; Termine: " + masterSchedule.getRoomSchedules().get(r).getBlockedCoordinates().size());

                printSchedule(masterSchedule.getRoomSchedules().get(r), false);

            }
        }
    }

    /**
     * Gibt die Dozentenpläne aus
     */
    public void printAcademicSchedules() {

        Iterator<Academic> academics = masterSchedule.getAcadademicSchedules().keySet().iterator();
        while (academics.hasNext()) {
            Academic r = academics.next();
            printSeparator(false);
            System.out.println("Dozentenplan: " + r.getName() + "; Termine: " + masterSchedule.getAcadademicSchedules().get(r).getBlockedCoordinates().size());

            printSchedule(masterSchedule.getAcadademicSchedules().get(r), true);

        }
    }

    /**
     * Gibt einen Plan aus, wenn gewünscht mit Rauminfos
     *
     * @param schedule Der auszugebende Plan
     * @param withRoom Mit Rauminfos ausgeben
     */
    public void printSchedule(Schedule schedule, boolean withRoom) {
        final String formatPattern = "%-11s|%-70s|%-70s|%-70s|%-70s|%-70s|%n";

        System.out.printf(formatPattern, "Zeitrahmen", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag");
        printSeparator(true);

        String[] line = new String[6];

        for (int i = 0; i < 5; i++) {
            for (int y = 0; y < 5; y++) {
                ScheduleElement scheduleElement = schedule.getScheduleElement(new ScheduleCoordinate(Day.valueOf(y), TimeSlot.valueOf(i)));
                if (scheduleElement.isBlocked()) {
                    line[y + 1] = scheduleElement.getCourse().getName() + " (" + scheduleElement.getCourse().getNumber() + "; " + scheduleElement.getCourse().getAcademic().getName() + "; " + scheduleElement.getCourse().getStudents();
                    if (withRoom) {
                        line[y + 1] += "; " + scheduleElement.getRoom().getName();
                    }
                    line[y + 1] += ")";
                } else {
                    line[y + 1] = "";
                }

                line[0] = TimeSlot.valueOf(i).toString();

                if (y == 4) {

                    System.out.printf(formatPattern, line[0], line[1], line[2], line[3], line[4], line[5]);
                }
            }
        }
    }
}
