package osjava.tl3.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse repräsentiert die Entität Terminplan.
 *
 * Ein Terminplan besteht aus Terminelementen.
 *
 * Bei 5 Zeitfenstern à 2 Stunden pro Tag und 5 Tagen pro Woche ergegeben
 * grundsätzlich 25 mögliche Termine zur Platzierung von Kursen eines
 * Fachsemesters.
 *
 * Einschränkende Kriterien sind: - Anzahl der eigenen Räume - Verfügbare und
 * benötigte Ausstattung pro Raum und Lehrveranstaltung - Verfügbare und
 * benötigte Sitzplätze pro Raum und Lehrveranstaltug - Keine Überplanung der
 * Dozenten - Überschneidunngsfreie Planung der Kurse eines Fachsemesters -
 * Möglichst geringe Kosten bei externer Raumbelegung
 *
 * @author Meikel Bode
 * @version 1.0
 */
public class Schedule {

    /**
     * Der Typ des Plans
     */
    private final ScheduleType type;

    /**
     * Die Elemente des Plans
     */
    private final List<ScheduleElement> scheduleElements;

    /**
     * Falls dieser Plan ein Fachsemesterplan sein sollte, ist folgedes Attribut gefüllt
     */
    private Semester semester;
    
    /**
     * Falls dieser Plan ein Raumplan sein sollte, ist folgedes Attribut gefüllt
     */
    private Room room;
    
    /**
     * Falls dieser Plan ein Dozentenplan sein sollte, ist dieses Attribut gefüllt
     */
    private Academic academic;
    
    /**
     * Erzeugt einen neuen Plan vom gegebenen Typ
     *
     * @param type Der Typ des Plans
     */
    public Schedule(ScheduleType type) {
        this.type = type;
        this.scheduleElements = new ArrayList<>(25);
        initSchedule();
    }

    /**
     * Initialisiert den Plan mit 5 x 5 Koordinaten
     */
    private void initSchedule() {

        for (int day = 0; day < 5; day++) {
            for (int timeSlot = 0; timeSlot < 5; timeSlot++) {
                ScheduleElement scheduleElement = new ScheduleElement();
                scheduleElement.setCoordiate(new ScheduleCoordinate(Day.valueOf(day), TimeSlot.valueOf(timeSlot)));
                scheduleElements.add(scheduleElement);
            }
        }

    }

    /**
     * Der Typ des Plans
     * @return Der Typ
     */
    public ScheduleType getType() {
        return type;
    }

    /**
     * Liefert alle freien Koordinaten des Plans
     *
     * @return Die Liste der freien Koordinaten
     */
    public List<ScheduleCoordinate> getFreeCoordinates() {
        List<ScheduleCoordinate> coordinates = new ArrayList<>();

        for (ScheduleElement scheduleElement : scheduleElements) {
            if (!scheduleElement.isBlocked()) {
                coordinates.add(scheduleElement.getCoordiate());
            }
        }

        return coordinates;
    }

    /**
     * Liefert alle belegten Koordinaten des Plans
     *
     * @return Die Liste der belegten Koordinaten
     */
    public List<ScheduleCoordinate> getBlockedCoordinates() {
        List<ScheduleCoordinate> coordinates = new ArrayList<>();

        for (ScheduleElement scheduleElement : scheduleElements) {
            if (scheduleElement.isBlocked()) {
                coordinates.add(scheduleElement.getCoordiate());
            }
        }

        return coordinates;
    }

    /**
     * Liefert das Planelement der gegebenen Koordinate
     *
     * @param coordinate Die Koordinate des zu liefernden Planelements
     * @return Das zur Koordinate passende Planelement oder null, wenn zur
     * gegebenen Kordinate kein Element gefunden wurde
     */
    public ScheduleElement getScheduleElement(ScheduleCoordinate coordinate) {
        for (ScheduleElement scheduleElement : scheduleElements) {
            if (scheduleElement.getCoordiate().equals(coordinate)) {
                return scheduleElement;
            }
        }
        return null;
    }

    /**
     * Liefert die Planelemente
     * @return Die Planelemente
     */
    public List<ScheduleElement> getScheduleElements() {
        return scheduleElements;
    }

    /**
     * Liefert das Fachsemester falls dieser Plan ein Studiegangsplan ist
     * @return Das Fachsemester
     */
    public Semester getSemester() {
        return semester;
    }

    /**
     * Setzt das Semester des Studiengs, falls dies ein Studiengangsplan ist
     * @param semester Das Fachsemester
     */
    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    /**
     * Liefert den Raum falls dieser Plan ein Raumplan ist
     * @return Der Raum
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Setzt den Raum, falls dies ein Studiengangsplan ist
     * @param room Der Raum
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Liefer tden Dozenten, falls dieser Plan ein Dozentenplan ist
     * @return Der Dozent
     */
    public Academic getAcademic() {
        return academic;
    }

    /**
     * Setzt den Dozenten, falls dieser Plan ein Dozentenplan ist
     * @param academic Der Dozent
     */
    public void setAcademic(Academic academic) {
        this.academic = academic;
    }

}