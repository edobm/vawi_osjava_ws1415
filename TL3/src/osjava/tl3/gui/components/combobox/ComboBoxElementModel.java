package osjava.tl3.gui.components.combobox;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * Modell für ComboBoxElements
 * 
 * @author Meikel Bode
 */
public class ComboBoxElementModel extends AbstractListModel implements ComboBoxModel {

    /**
     * Der aktuell selektierte Index
     */
    private int selectedIndex = 0;

    /**
     * Die Elemente der ComboBox
     */
    private final List<ComboxBoxElement> elements = new ArrayList<>();

    /**
     * Default Kontruktor
     */
    public ComboBoxElementModel() {
        super();
    }

    /**
     * Fügt der ComboBox ein Element hinzu
     * @param anObject Das neue Element
     */
    public void addElement(ComboxBoxElement anObject) {
        elements.add(anObject);
        fireContentsChanged(anObject, 0, elements.size());
    }

    /**
     * Liefert das Element am angegebenen Index
     * @param index Der Index
     * @return Das Element am Index
     */
    @Override
    public String getElementAt(int index) {
        if (index < 0) {
            return "";
        } else {
            return elements.get(index).getText();
        }
    }

    /**
     * Liefert den Index des angegebenen Objekts
     * @param anObject Das Objekt dessen Index abgfragt werden soll
     * @return Der Index des Objekts
     */
    public int getIndexOf(Object anObject) {
        return elements.indexOf(anObject);
    }

    /**
     * Liefert die Elemente der CoboBox
     * @return Die Elemente
     */
    public List<ComboxBoxElement> getElements() {
        return elements;
    }

    /**
     * Setzt das aktuell selektierte Element
     * @param anObject Das selektierte Elemente
     */
    @Override
    public void setSelectedItem(Object anObject) {
        
        for (ComboxBoxElement element : elements) {
            if (element.getText().equals(anObject.toString())) {
                this.selectedIndex = elements.indexOf(element);
            }
        }
        
    }

    /**
     * Setzt den aktuell selektierten Index
     * @param selectedIndex Der selektierte Index
     */
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    /**
     * Liefert den aktuell selektierten Index
     * @return 
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Liefert das aktuell selektierte Element
     * @return 
     */
    @Override
    public Object getSelectedItem() {
        
        return selectedIndex < 0 ? null : elements.get(selectedIndex);
    }

    /**
     * Liefert die Anzahl der Elemente
     * @return Die Anzahl
     */
    @Override
    public int getSize() {
        return elements.size();
    }

}