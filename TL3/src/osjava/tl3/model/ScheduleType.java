package osjava.tl3.model;

/**
 * Mögliche Plantypen
 * 
 * @author Meikel Bode
 */
public enum ScheduleType {
    
    /**
     * Repräsentiert einen Dozentenplan
     */
    ACADAMIC, 
    
    /**
     * Repräsentiert einen Plan für einen internen Raum
     */
    ROOM_INTERNAL, 
    
    /**
     * Repräsentiert einen Plan für einen externen Raum
     */
    ROOM_EXTERNAL, 
    
    /**
     * Repräsentiert einen Studiengangsplan
     */
    STUDY_PROGRAM
    
}