package osjava.tl3.logic.io;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.model.StudyProgram;


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
     * @return Liste mit Objekten der angebotenen Studiengänge.
     */
    public List<StudyProgram> readStudyProgram(){
        
        final String[] fileNames = new String[]{
            "studiengang_bwlba.csv",
            "studiengang_drachenba.csv",
            "studiengang_mumama.csv",
            "studiengang_physikba.csv",
            "studiengang_seba.csv",
            "studiengang_wiba.csv",
            "studiengang_wiingba.csv",
            "studiengang_wiwila.csv"
        };
       
        List<StudyProgram> studyPrograms = new ArrayList<>();
        
        for(String fileName : fileNames){
            ArrayList<String> studyProgramData = super.readFile(fileName);
            
            
        }
        
        
        return studyPrograms;
    }
    
    /**
     * Hilfsmethode für das Erzeugen von Studienprogramm-Objekten
     * 
     * @param studyProgramDataRecord
     * @return Studiengang-Objekt
     */
    private StudyProgram getStudyProgram(String studyProgramDataRecord){
        
        StudyProgram sp = new StudyProgram();
        
        return sp;
    }
}
