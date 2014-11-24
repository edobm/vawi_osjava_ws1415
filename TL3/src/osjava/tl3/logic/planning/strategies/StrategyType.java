package osjava.tl3.logic.planning.strategies;

/**
 * Dieser Enum stellt für jede Implementierung der abstrakten Klasse Strategy
 * einen Eintrag bereit.
 * 
 * Zur Laufzeit kann die gewünschte Planungsstrategie an das Hauptprogramm 
 * übergeben werden.
 * 
 * @author Meikel Bode
 */
public enum StrategyType {
    /**
     * Repräsentiert die Kostenoptimierte Planung mittels der Klasse CostOptimizedStrategy
     */
    COST_OPTIMIZED   
}
