package osjava.tl3;

import osjava.tl3.model.controller.DataController;

/**
 * Diese Klasse stellt mit der Main-Methode den Einstiegspunkt in das Klassensystem dar.
 * Als Parameter erwartet die Main-Methode die Parameter:
 * 
 *  -in="<directory>" - Verzeichnis das die Eingabedatein enthält
 *  -out="<directory>" - Verzeichnis in das die Ausgabedatein geschrieben werden sollen
 *  -format="<plaintext, html>" - Das gewünschte Ausgabeformat
 *  -in="c:\tmp\int" -out="blablub" -format="plaintext" 
 * @author Christian Müller
 * @version 1.0
 */
public class SchedulePlanner
{

   private String inputDirectory = null;
   private String outputDirectory = null;
   private String outputFormat = null;
   
   private DataController dataController;
       
   public static void main(String[] argv) {
       
       /* 
        * Argumenten Vektor prozessieren, validieren und
        * Instanz des SchedulePlanners erzeugen
        */
       
       
       SchedulePlanner schedulePlanner = new SchedulePlanner(); 
       schedulePlanner.parseArgumentVector(argv);
       schedulePlanner.loadInputData();
       schedulePlanner.createSchedule();
       schedulePlanner.writeOutput();
   }
   
   /**
    * Initialisierung
    */
   public SchedulePlanner() {
       dataController = new DataController();
   }
   
   public void parseArgumentVector(String[] argv) {
       for(String argument : argv) {
           System.out.println(argument);
       }
   }
   
   /**
    * Laden der Eingabedaten
    */
   public void loadInputData() {
       dataController.load();
   }
   
   
   /**
    * Erzeugen der Zeitplanung
    */
   public void createSchedule() {
   }
   
   /**
    * Erzeugen des Outputs im gewünschten Format
    */
   public void writeOutput() {
   }
}
