package osjava.tl3.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author meikelbode
 */
public class ComboBoxElementModel extends AbstractListModel implements ComboBoxModel {

    int selectedIndex = 0;

    List<ComboxBoxElement> elements = new ArrayList<>();

    public ComboBoxElementModel() {
        super();
    }

    public void addElement(ComboxBoxElement anObject) {
        elements.add(anObject);
        fireContentsChanged(anObject, 0, elements.size());
    }

    @Override
    public String getElementAt(int index) {
        if (index < 0) {
            return "";
        } else {
            return elements.get(index).getText();
        }
    }

    public int getIndexOf(Object anObject) {
        return elements.indexOf(anObject);
    }

    public List<ComboxBoxElement> getElements() {
        return elements;
    }

    @Override
    public void setSelectedItem(Object anObject) {
        
        for (ComboxBoxElement element : elements) {
            if (element.getText().equals(anObject.toString())) {
                this.selectedIndex = elements.indexOf(element);
            }
        }
        
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public Object getSelectedItem() {
        
        return selectedIndex < 0 ? null : elements.get(selectedIndex);
    }

    @Override
    public int getSize() {
        return elements.size();
    }

}
