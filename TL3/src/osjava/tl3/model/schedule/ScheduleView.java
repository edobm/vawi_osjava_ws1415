package osjava.tl3.model.schedule;

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
 */
public abstract class ScheduleView extends ScheduleBasis {

    /**
     * Der Plan auf dem diese Sicht operiert
     */
    protected final ScheduleNew schedule;

    /**
     * Erzeugt eine neue Sicht auf den Plan
     *
     * @param schedule Der Plan auf den die Sicht operieren soll
     */
    public ScheduleView(ScheduleNew schedule) {
        this.schedule = schedule;
    }

    /**
     * Liefert das Planelement der gegebenen Koordinate
     *
     * @param coordinate Die Koordinate des zu liefernden Planelements
     * @return Das zur Koordinate passende Planelement oder null, wenn zur
     * gegebenen Kordinate kein Element gefunden wurde
     */
    public abstract ScheduleElementNew getScheduleElement(ScheduleCoordinate coordinate);

    /**
     * Liefert die Planelemente
     *
     * @return Die Planelemente
     */
    public List<ScheduleElementNew> getScheduleElements() {
        List<ScheduleElementNew> elements = new ArrayList<>();

        for (ScheduleCoordinate coordinate : possibleScheduleCoordinates) {
            elements.add(getScheduleElement(coordinate));
        }

        return elements;
    }

    /**
     * Liefert alle belegten Koordinaten des Plans
     *
     * @return Die Liste der belegten Koordinaten
     */
    private List<ScheduleCoordinate> getCoordinates(boolean onlyBlocked) {
        List<ScheduleCoordinate> coordinates = new ArrayList<>();

        for (ScheduleElementNew scheduleElement : getScheduleElements()) {
            if (onlyBlocked) {
                if (!scheduleElement.isEmpty()) {
                    coordinates.add(scheduleElement.getCoordiate());
                }
            } else {
                if (scheduleElement.isEmpty()) {
                    coordinates.add(scheduleElement.getCoordiate());
                }
            }
        }

        return coordinates;
    }
    
     /**
     * Liefert alle freien Koordinaten des Plans
     *
     * @return Die Liste der freien Koordinaten
     */
    public List<ScheduleCoordinate> getFreeCoordinates() {
       return getCoordinates(false);
    }

    /**
     * Liefert alle belegten Koordinaten des Plans
     *
     * @return Die Liste der belegten Koordinaten
     */
    public List<ScheduleCoordinate> getBlockedCoordinates() {
       return getCoordinates(true);
    }
    
}