package osjava.tl3.ui.components.logging;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import osjava.tl3.Protocol;

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
     * Die ScrollPane für die TextArea
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
         * Automatisches Scrolling aktivieren
         */
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        /**
         * Registrierung als Observer
         */
        Protocol.getInstance().addObserver(this);
    }

    /**
     * Implementierung des Interfaces Obeserver
     *
     * @param o Das Observable (Protocol)
     * @param arg Die letzte Logmeldung
     */
    @Override
    public void update(Observable o, Object arg) {

        /**
         * Die letzte Logmeldung an TextArea anhängen
         */
        textArea.append(arg.toString() + "\n");

    }

}