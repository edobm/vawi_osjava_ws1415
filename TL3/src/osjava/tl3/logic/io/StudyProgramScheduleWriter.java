package osjava.tl3.logic.io;

import java.util.ArrayList;
import java.util.List;
import static osjava.tl3.logic.io.OutputFormat.HTML;
import static osjava.tl3.logic.io.OutputFormat.PLAINTEXT;
import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleElement;



/**
 * Diese Klasse erzeugt die Pläne für Studiengang und Fachsemester
 * 
 * @author Fabian Simon
 * @version 1.0
 */
public class StudyProgramScheduleWriter extends OutputFileWriter
{
   
    /**
     * Umwandeln des gegebenen Schedules in eine Liste aus Srings und Ausgabe eines 
     * Studiengang-/Semesterplanes im angegebenen Format in den angegebenen Pfad.
     *
     * @param schedule
     * @param outputFormat
     * @param outputPath
     */
    public void writeStudyProgramSchedule (Schedule schedule, OutputFormat outputFormat, String outputPath){
        
        if (outputFormat == PLAINTEXT){
            writePlaintext(schedule, outputPath);
        } else if (outputFormat == HTML){
            writeHTML(schedule, outputPath);
        }
        
    }
    
    private void writePlaintext (Schedule schedule, String outputPath){
        
        List<ScheduleElement> scheduleElements = schedule.getScheduleElements();
        ArrayList<String> outputCSV = new ArrayList<>(27);
        String studyProgramName;
        String semester;
        String nameLine;
        String descriptionLine;
        Integer i = 2;
            
        studyProgramName = "noch keine Ahnung";
        semester = "auch noch keine Ahnung";
        nameLine = "Studiengang:;" + studyProgramName + ";Semester:;" + semester + ";";
        outputCSV.set(0, nameLine);
            
        descriptionLine = "Tag;Zeit;Kurs;Raum;Dozent";
        outputCSV.set(1, descriptionLine);
            
        for(ScheduleElement scheduleElement : scheduleElements){
            String line, column1, column2, column3, column4, column5;
               
            column1 = scheduleElement.getCoordiate().getDay().toString();
            column2 = scheduleElement.getCoordiate().getTimeSlot().toString();
                
            if (scheduleElement.getCourse().getName() != null){
                column3 = scheduleElement.getCourse().getName();
            } else {
                column3 = "kein Kurs";
            }
                
            if (scheduleElement.getRoom().getName() != null){
                column4 = scheduleElement.getRoom().getName();
            } else{
                column4 = "kein Raum";
            }
              
            if (super.getFirstScheduleElement(schedule).getCourse().getAcademic().getName() != null){
                column5 = super.getFirstScheduleElement(schedule).getCourse().getAcademic().getName();
            } else{
                column5 = "kein Dozent";
            }
                
            line = column1 + ";" + column2 + ";" + column3 + ";" + column4 + ";" + column5;
            outputCSV.set(i, line);
            i++; 
        }    
        super.writeCSVFile(outputCSV, outputPath);
        
    }
    
    private void writeHTML (Schedule schedule, String outputPath){
        
    }
    
}
