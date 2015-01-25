package osjava.tl3.gui.components.logging;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import osjava.tl3.logging.Protocol;

/**
 * Stellt Logausgaben bereit und implementiert das Interface Observer, um bei
 * neuen Logeinträgen automatisch informiert zu werden
 *
 * @author Meikel Bode
 */
public class LoggingPanel extends JPanel implements Observer {

    /**
     * Die TextArea für Logausgaben
     */
    private final JTextArea textArea = new JTextArea();

    /**
     * Die ScrollPane, die die TextArea aufnimmt
     */
    private final JScrollPane scrollPane = new JScrollPane();

    /**
     * Erzeugt eine neue Instaz von LoggingPanel
     */
    public LoggingPanel() {

        /**
         * Grundlegende Einstellungen
         */
        setLayout(new BorderLayout());
        scrollPane.setViewportView(textArea);
        add(scrollPane, BorderLayout.CENTER);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospace", Font.PLAIN, 12));

        /**
         * Automatisches Scrolling aktivieren, um immer die neuesten
         * Logeinträge direkt anzuzeigen
         */
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        /**
         * Registrierung als Observer
         */
        Protocol.getInstance().addObserver(LoggingPanel.this);
    }

    /**
     * Implementierung des Interfaces Obeserver
     *
     * @param observable Das Observable (Protocol)
     * @param lastChange Die letzte Logmeldung
     * 
     * @see Observer#update(java.util.Observable, java.lang.Object) 
     */
    @Override
    public void update(Observable observable, Object lastChange) {

        /**
         * Die letzte Logmeldung an TextArea anhängen
         */
        textArea.append(lastChange.toString() + "\n");

    }

}