package osjava.tl3.ui;

/**
 * Hilfsklasse zur Abbildung von ComboBoxModel Einträgen
 * 
 * @author Meikel Bode
 * 
 * @param <E> Der Typ des Elementes
 */
public class ComboxBoxElement<E> {
    
    /**
     * Der Text der in der ComboBox für dieses Element angezeigt werden soll
     */
    private final String text;
    
    /**
     * Der Wert den dieses ComboxBox Element halten soll vom generischen Typ E
     */
    private final E element;

    /**
     * Erzeugt eine neue Instanz mit dem angegebenen Text und Elementwert
     * @param text Der Anzeigetext
     * @param element Der Elementwert
     */
    public ComboxBoxElement(String text, E element) {
        this.text = text;
        this.element = element;
    }

    /**
     * Liefert den Elementwert als Typ E
     * @return Das Element
     */
    public E getElement() {
        return element;
    }

    /**
     * Liefert den Anzeigetext
     * @return Der Anzeigetext
     */
    public String getText() {
        return text;
    }

    /**
     * Die Stringdarstellung
     * @return Der Anzeigetext
     */
    @Override
    public String toString() {
        return text;
    }
}