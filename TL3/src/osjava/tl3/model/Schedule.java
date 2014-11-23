package osjava.tl3.model;

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
    private List<ScheduleElement> scheduleElements;
}
