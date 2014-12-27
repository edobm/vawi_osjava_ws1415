package osjava.tl3.ui;

/**
 *
 * @author meikelbode
 * @param <T>
 */
public class ComboxBoxElement<E> {
    
    private final String text;
    
    private final E element;

    public ComboxBoxElement(String text, E element) {
        this.text = text;
        this.element = element;
    }

    public E getElement() {
        return element;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
