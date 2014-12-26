package osjava.tl3.logic.io;

import java.util.ArrayList;
import java.util.List;
import static osjava.tl3.logic.io.OutputFormat.PLAINTEXT;
import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleCoordinate;
import osjava.tl3.model.ScheduleElement;



/**
 * Diese Klasse erzeugt die Ausgabe für die Raumpläne
 * 
 * @author Fabian Simon
 * @version 1.0
 */
public class RoomScheduleWriter extends OutputFileWriter
{
    
    /**
     * Umwandeln des gegebenen Schedules in eine Liste aus Strings und Ausgabe 
     * eines Raumplanes im angegebenen Format in den angegebenen Pfad.
     *
     * @param schedule
     * @param outputFormat
     * @param outputPath
     */
    public void writeRoomSchedule (Schedule schedule, OutputFormat outputFormat, String outputPath){
        
        List<ScheduleElement> scheduleElements = schedule.getScheduleElements();
        
        if (outputFormat == PLAINTEXT){
            ArrayList<String> outputCSV = new ArrayList<>(27);
            String roomName;
            String nameLine;
            String descriptionLine;
            Integer i = 2;
            
            roomName = getFirstScheduleElement(schedule).getRoom().getName();
            nameLine = "Raumname:;" + roomName + ";" + ";";
            outputCSV.set(0, nameLine);
            
            descriptionLine = "Tag;" + "Zeit;" + "Kurs;" + "Dozent";
            outputCSV.set(1, descriptionLine);
            
            for(ScheduleElement scheduleElement : scheduleElements){
                String line, column1, column2, column3, column4;
                
                column1 = scheduleElement.getCoordiate().getDay().toString();
                column2 = scheduleElement.getCoordiate().getTimeSlot().toString();
                
                if (scheduleElement.getCourse().getName() != null){
                    column3 = scheduleElement.getCourse().getName();
                }else{
                    column3 = "Kein Kurs";
                }
                
                if (scheduleElement.getCourse().getAcademic().getName() != null){
                    column4 = scheduleElement.getCourse().getAcademic().getName();
                } else{
                    column4 = "Kein Dozent";
                }
                
                line = column1 + ";" + column2 + ";" + column3 + ";" + column4;
                outputCSV.set(i, line);
                i++;
                
            }
            
            super.writeCSVFile(outputCSV, outputPath);
        }
        
    }
    
}
