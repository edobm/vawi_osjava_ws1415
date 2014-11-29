package osjava.tl3.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse repräsentiert die Entität Terminplan.
 * 
 * Ein Terminplan besteht aus Terminelementen.
 * 
 * Bei 5 Zeitfenstern à 2 Stunden pro Tag und 5 Tagen pro Woche ergegeben grundsätzlich 25 mögliche
 * Termine zur Platzierung von Kursen eines Fachsemesters.
 * 
 * Einschränkende Kriterien sind:
 *  - Anzahl der eigenen Räume
 *  - Verfügbare und benötigte Ausstattung pro Raum und Lehrveranstaltung
 *  - Verfügbare und benötigte Sitzplätze pro Raum und Lehrveranstaltug
 *  - Keine Überplanung der Dozenten
 *  - Überschneidunngsfreie Planung der Kurse eines Fachsemesters
 *  - Möglichst geringe Kosten bei externer Raumbelegung
 * 
 * @author Christoph Lurz
 * @version 1.0
 */
public class Schedule
{

    /**
     * @return the type
     */
    public ScheduleType getType() {
        return type;
    }
    
    
    private ScheduleType type;
    
    private List<ScheduleElement> scheduleElements =  new ArrayList<>();

    /**
     * Erzeugt einen neuen Plan
     * @param type Der Typ des Plans
     */
    public Schedule(ScheduleType type) {
        this.type = type;
        initSchedule();
    }
    
    /**
     * Initialisiert den Plan
     */
    private void initSchedule() {
        
        for (int i = 0; i < 5; i++) {
            for(int y = 0; y < 5; y++) {
                ScheduleElement scheduleElement = new ScheduleElement();
                scheduleElement.setCoordiate(new ScheduleCoordinate(Day.valueOf(i), TimeSlot.valueOf(y)));
                scheduleElements.add(scheduleElement);
            }
        }
        
    }
    
    /**
     * Liefert alle freien Koordinaten des Plans
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
     * @param coordinate Die Koordinate des zu liefernden Planelements
     * @return Das zur Koordinate passende Planelement oder null, wenn zur gegebenen Kordinate kein Element gefunden wurde
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
     * @return the scheduleElements
     */
    public List<ScheduleElement> getScheduleElements() {
        return scheduleElements;
    }

    /**
     * @param scheduleElements the scheduleElements to set
     */
    public void setScheduleElements(List<ScheduleElement> scheduleElements) {
        this.scheduleElements = scheduleElements;
    }
    
}
