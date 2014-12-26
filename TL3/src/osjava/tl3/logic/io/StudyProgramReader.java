package osjava.tl3.logic.io;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.model.Course;
import osjava.tl3.model.Semester;
import osjava.tl3.model.StudyProgram;
import osjava.tl3.model.controller.DataController;


/**
 * Diese Klasse behandelt das spezifische Zeilenformat für Studiengänge, Fachsemester und Kursnamen
 * und erzeugt Instanzen der Modellklassen Course und setzt das Fachsemester pro Instanz.
 * Die Details eines jeden Kurses werden durch die Klasse CourseReader ergänzt.
 * 
 * @author Fabian Simon
 * @version 1.0
 */
public class StudyProgramReader extends InputFileReader
{

    /**
     * Die Methode liest die Daten der Studiengänge aus den einzelnen Eingabedateien
     * der Studiuengänge ein, verarbeitet diese und erzeugt für jeden Studiengang ein Studiengang-Objekt. 
     * Diese Studiengang-Objekte werden in einer Liste gespeichert, welche
     * von der Methode zurückgegeben wird.
     *
     * @param fileNames Namen der einzulesenden Dateien
     * @param dataController DataController-Instanz zur Ablage der Daten
     */
    public void readStudyPrograms(String fileNames, DataController dataController){
        
        final String[] fileNamesTest = new String[]{
            "studiengang_bwlba.csv",
            "studiengang_drachenba.csv",
            "studiengang_mumama.csv",
            "studiengang_physikba.csv",
            "studiengang_seba.csv",
            "studiengang_wiba.csv",
            "studiengang_wiingba.csv",
            "studiengang_wiwila.csv"
        };
        
        // "BWL Bachelor";;;;
        // 1;"Mathematik 1";"Recht 1";"Einführung in BWL und VWL";
        
        List<StudyProgram> studyPrograms = new ArrayList<>();
        
        for(String fileName : fileNamesTest){
            boolean newStudyProgram = true;
            ArrayList<String> studyProgramDataRecord = super.readFile(fileName);
            studyPrograms.add(getStudyProgram(newStudyProgram, studyProgramDataRecord, dataController));
            
        }
        
        dataController.setStudyPrograms(studyPrograms);
        
    }
    
    /**
     * Hilfsmethode für das Erzeugen von Studienprogramm-Objekten
     * 
     * @param newStudyProgramm
     * @param studyProgramDataRecord
     * @param dataController
     * @return Studiengang-Objekt
     */
    private StudyProgram getStudyProgram(Boolean newStudyProgram, ArrayList<String> studyProgramDataRecord, DataController dataController){
        
        //Erzeugen des Studiengang-Objektes
        StudyProgram studyProgram = new StudyProgram();
        
        for(String data : studyProgramDataRecord){
            String [] studyProgramData = data.split(";");
            
            if (newStudyProgram){
                newStudyProgram = false;
                //Name für Studiengang setzen
                studyProgram.setName(super.removeQuotationMarks(studyProgramData[0]));
            }else{
                //Erzeugen Semester-Objekt
                Semester semester = new Semester();
                
                //Name und zugehörigen Studiengang für das Semester setzen
                semester.setStudyProgram(studyProgram);
                semester.setName(studyProgramData[0] + ". Semester");
                
                //Kurse für das Semester holen und setzen
                List<Course> courses = new ArrayList<>();
                
                for(int i = 1; i < studyProgramData.length; i++){
                    for(Course course : dataController.getCourses()){
                        if (course.getName().equals(super.removeQuotationMarks(studyProgramData[i]).trim())){
                            courses.add(course);
                        }else{
                            System.out.println("Der Kurs " + super.removeQuotationMarks(studyProgramData[i]).trim() + " konnte nicht gefunden werden!");
                        }
                    }
                }
                
                semester.setCourses(courses);
                
                //Semester zum Studiengang hinzufügen
                studyProgram.addSemester(semester);
            }
        }
        
        //Ausgabe des Studienganges
        return studyProgram;
    }
}
