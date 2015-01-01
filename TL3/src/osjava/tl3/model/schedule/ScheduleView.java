package osjava.tl3.model.schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse repräsentiert eine Sicht auf den Gesamtplan.
 * Die Klasse stellt allgemeine Methoden für spezifische Implementierungen bereit.
 *
 * @author Meikel Bode
 */
public abstract class ScheduleView extends ScheduleBasis {

    /**
     * Der Plan auf dem diese Sicht operiert
     */
    protected final Schedule schedule;

    /**
     * Erzeugt eine neue Sicht auf den Plan
     *
     * @param schedule Der Plan auf den die Sicht operieren soll
     */
    public ScheduleView(Schedule schedule) {
        this.schedule = schedule;
    }

    /**
     * Liefert das Planelement der gegebenen Koordinate
     *
     * @param coordinate Die Koordinate des zu liefernden Planelements
     * @return Das zur Koordinate passende Planelement oder null, wenn zur
     * gegebenen Kordinate kein Element gefunden wurde
     */
    public abstract ScheduleElement getScheduleElement(ScheduleCoordinate coordinate);

    /**
     * Liefert die Planelemente
     *
     * @return Die Planelemente
     */
    public List<ScheduleElement> getScheduleElements() {
        List<ScheduleElement> elements = new ArrayList<>();

        for (ScheduleCoordinate coordinate : getPossibleScheduleCoordinates()) {
            elements.add(new ScheduleElementViewWrapper(this, getScheduleElement(coordinate)));
        }

        return elements;
    }

    /**
     * Liefert alle belegten oder unbelegten Koordinaten der Plansicht
     *
     * @param onlyBlocked Nur belegte oder unbelegt
     * @return Die Liste der belegten Koordinaten
     */
    private List<ScheduleCoordinate> getCoordinates(boolean onlyBlocked) {
        List<ScheduleCoordinate> coordinates = new ArrayList<>();

        for (ScheduleElement scheduleElement : getScheduleElements()) {
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