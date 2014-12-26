package osjava.tl3.logic.io;

import java.util.ArrayList;
import java.util.List;
import static osjava.tl3.logic.io.OutputFormat.HTML;
import static osjava.tl3.logic.io.OutputFormat.PLAINTEXT;
import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleElement;


/**
 * Diese Klasse erzeugt den Stundenplan für Dozenten
 * 
 * @author Fabian Simon
 * @version 1.0
 */
public class AcademicScheduleWriter extends OutputFileWriter
{

    /**
     * Umwandeln des gegebenen Schedules in eine Liste aus Strings und Ausgabe 
     * eines Dozentenplanes im angegebenen Format in den angegebenen Pfad.
     *
     * @param schedule
     * @param outputFormat
     * @param outputPath
     */
    public void writeAcademicSchedule(Schedule schedule, OutputFormat outputFormat, String outputPath){
        
        if (outputFormat == PLAINTEXT){
            writePlaintext(schedule, outputPath);
        } else if (outputFormat == HTML){
            writeHTML(schedule, outputPath);
        }
        
    }
    
    private void writePlaintext (Schedule schedule, String outputPath){
        
        List<ScheduleElement> scheduleElements = schedule.getScheduleElements();
        ArrayList<String> outputCSV = new ArrayList<>(27);
        String academicName;
        String nameLine;
        String descriptionLine;
        Integer i = 2;
            
        academicName = super.getFirstScheduleElement(schedule).getCourse().getAcademic().getName();
        nameLine = "Name des Dozenten:;" + academicName + ";" + ";";
        outputCSV.set(0, nameLine);
         
        descriptionLine = "Tag;Zeit;Kurs;Raum";
        outputCSV.set(1, descriptionLine);
            
        for(ScheduleElement scheduleElement : scheduleElements){
            String line, column1, column2, column3, column4;
             
            column1 = scheduleElement.getCoordiate().getDay().toString();
            column2 = scheduleElement.getCoordiate().getTimeSlot().toString();
                
            if (scheduleElement.getCourse().getName() != null){
                column3 = scheduleElement.getCourse().getName();
            } else{
                column3 = "Keine Veranstaltung";
            }
                
            if (scheduleElement.getRoom().getName() != null){
                column4 = scheduleElement.getRoom().getName();
            } else{
                column4 = "Kein Dozent";
            }
                
            line = column1 + ";" + column2 + ";" + column3 + ";" + column4;
            outputCSV.set(i, line);
            i++;       
        }
        super.writeFile(outputCSV, outputPath);
    }
    
    private void writeHTML (Schedule schedule, String outputPath){
        
        ArrayList<String> outputHTML = new ArrayList<>();
        
        
        super.writeFile(outputHTML, outputPath);
    }
}
