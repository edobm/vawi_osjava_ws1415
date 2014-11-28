package osjava.tl3.logic.planning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import osjava.tl3.model.CourseType;
import osjava.tl3.model.Equipment;

/**
 *
 * @author meikelbode
 */
public class ParsingHelpers {

    public static Equipment mapEquipment(String externalName) {

        switch (removeQuotationMarks(externalName).trim()) {
            case "Tafel":
                return Equipment.BLACKBOARD;
            case "Mikrofonanlage":
                return Equipment.MICROPHONE_SYSTEM;
            case "Beamer":
                return Equipment.BEAMER;
            case "DozentenPC":
                return Equipment.COMPUTER_ACADEMIC;
            case "StudentenPCs":
                return Equipment.COMPUTER_STUDENT;
            case "Projektor":
                return Equipment.PROJECTOR;
            case "Fotokamera":
                return Equipment.PHOTO_CAMERA;
            case "Kartenstaender":
                return Equipment.CARDS_STAND;
            case "Staffelei":
                return Equipment.EASEL_ACADEMIC;
            case "Studentenstaffeleien":
                return Equipment.EASEL_STUDENT;
            case "Betaeubungsgewehr":
                return Equipment.STUN_GUN;
            case "Mechanikbaukasten":
                return Equipment.MECHANICS_KIT;
            case "Elektrotechnikbaukasten":
                return Equipment.ELECTRICAL_CONSTRUCTION_KIT;
            case "Videotelefoniesystem":
                return Equipment.VIDEO_TELEPHONY_SYSTEM;
            case "Zuckerbrot":
                return Equipment.SUGAR_BREAD;
            case "Peitsche":
                return Equipment.LASH;
            case "Lautsprecher":
                return Equipment.SPEAKER;
            case "Abspielgeraet":
                return Equipment.PLAYER;
            case "Ohrenstoepsel":
                return Equipment.EAR_PLUGS;
            case "Laermschutzwand":
                return Equipment.NOISE_BARRIER;
            case "Bongotrommelsatz":
                return Equipment.BONGO_DRUM_SET;
            case "Taktstock":
                return Equipment.BATON;
            case "Uranbrennstaebe":
                return Equipment.URANIUM_FUEL_RODS;
            case "Bleiwesten":
                return Equipment.LEAD_VEST;
            case "Experimentierkoffer":
                return Equipment.EXPERIMENTAL_KIT;
            case "Dunstabzugshaube":
                return Equipment.RANGE_HOOD;
            case "Chemikaliensortiment":
                return Equipment.CHEMICALS;
            case "Fluegel":
                return Equipment.GRAND_PIANO;
            case "Periodensystem":
                return Equipment.PERIODIC_SYSTEM;

            default:
                System.out.println("Unknown Equipment: " + externalName);
                return Equipment.UNKNOWN_EQUIPMENT;
        }
    }

    public static CourseType mapCourseType(String externalName) {

        switch (removeQuotationMarks(externalName).trim()) {
            case "Uebung":
                return CourseType.TUTORIAL;
            case "Vorlesung":
                return CourseType.READING;

            default:
                System.out.println("Unknown CourseType: " + externalName);
                return CourseType.UNKNOWN_TYPE;
        }
    }

    public static String removeQuotationMarks(String inString) {
        if (inString != null) {

            return inString.trim().replaceAll("\"", "");
        } else {
            return "";
        }
    }

    public static List<Equipment> parseEquipments(String record) {
        final String delimiter = ",";

        List<Equipment> equipments = new ArrayList<>();

        if (record != null) {

            String[] columns = record.split(delimiter);
            for (String equipmentExternal : columns) {
                equipments.add(ParsingHelpers.mapEquipment(equipmentExternal.trim()));
            }

        }

        return equipments;
    }

   
     
    public static List<String> getInputFile(String fileName) {

        final String path = "/osjava/tl3/inputfiles/";

        List<String> records = new ArrayList<>();

        BufferedReader br = null;
        InputStreamReader isr = null;

        try {
            isr = new InputStreamReader(Class.class.getResourceAsStream(path + fileName));
            br = new BufferedReader(isr);

            while (br.ready()) {
                records.add(br.readLine());
            }
        } catch (IOException ex) {
            Logger.getLogger(SchedulerTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (Exception e) {

            }
            try {
                isr.close();
            } catch (Exception e) {

            }
        }

        return records;

    }
}
