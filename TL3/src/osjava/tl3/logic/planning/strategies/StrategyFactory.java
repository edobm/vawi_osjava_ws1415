package osjava.tl3.logic.planning.strategies;

import java.util.logging.Level;
import java.util.logging.Logger;
import osjava.tl3.Protocol;

/**
 * Erzeugt Instanzen von Planungsstrategien
 * 
 * @author Meikel Bode
 */
public class StrategyFactory {
    
    /**
     * Erzeugt eine Instanz der angegebenen Planungsstrategie Klasse
     * @param className Der Name der Klasse 
     * @return Die Instanz der Klasse oder null, wenn diese nicht gefunden werden konnte
     */
    public static final Strategy getInstanceByClassName(String className) {

        /**
         * Legt das zugelassene Package für Strategieimplementierungen fest
         */
        final String packagePath = Strategy.class.getName().substring(0, Strategy.class.getName().lastIndexOf("."));

        try {
            /**
             * Die Klasse laden
             */
            Class<?> cls = Class.forName(packagePath + "." + className);

            /**
             * Prüfen ob die Klasse wirklich von Strategy.class abstammt
             */
            if (Strategy.class.isAssignableFrom(cls)) {
                Protocol.log("Strategie geladen: " + cls.getName());
                return (Strategy) cls.newInstance();
            } else {
                Protocol.log("Strategie konnte nicht geladen werden: " + packagePath + "." + className);
                return null;
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Protocol.log("Strategie konnte nicht geladen werden: " + packagePath + "." + className);
            Logger.getAnonymousLogger().log(Level.SEVERE, "Strategie konnte nicht geladen werden: " + packagePath + "." + className, ex);
            return null;
        }
    }

}