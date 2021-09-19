import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DialogWindow extends JFrame implements ActionListener {
    static private int width = 320, height = 200;

    JButton confirm_Button, close_Button;
    JScrollBar scrollBar;
    JTextField editBox;

    DialogWindow() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // terminiert nur das Dialogfenster beim Schließen
        this.setTitle("Regler");
        this.setSize(width, height);
        this.setResizable(false); // verhindert Größenveränderung des Fensters
        this.setLayout(null);
        this.setLocationRelativeTo(this); // zentriert das Fenster

        /* Erstellt eine ScrollBar mit geeignetem Maximum und deren Position, die beim bekannten hexadezimalen-Wert eingestellt wird */
        scrollBar = createScrollBar(2, height/5, 65535, 0, 1, WinAPIJava.value);
        scrollBar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent event) {
                // Passe Dezimalwert für hexadezimalen-Wert an
                WinAPIJava.value = scrollBar.getValue();
                WinAPIJava.mWindow.updateLabels(); // Update die Labels

                // Zeichne Hauptfenster neu
                WinAPIJava.mWindow.repaint();
                WinAPIJava.mWindow.revalidate();
            }
        });
        this.add(scrollBar);

        /* Erstellt eine Editbox (JTextField) zur manuellen Eingabe eines hexadezimalen Werts */
        editBox = createTextField(width/3 + 10, height/3 + 20);
        editBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent event) {
                if (editBox.getText().length() > 3) { // limitiert die Größe der Eingabe auf 4 Zeichen
                    event.consume();
                }
            }
            @Override
            public void keyPressed(KeyEvent event) { // filtert die Eingabe, sodass nur Zahlenwerte und Buchstaben von A-F eingegeben werden. Zusätzlich darf der Benutzer auch Zeichen löschen
                if ((event.getKeyChar() >= '0' && event.getKeyChar() <= '9') || (event.getKeyChar() >= 'A' && event.getKeyChar() <= 'F') || event.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    editBox.setEditable(true); // öffnet Editbox zur Eingabe
                } else {
                    editBox.setEditable(false); // schließt Editbox zur Eingabe, um falsche Zeichen zu verhindern
                }
            }
        });
        this.add(editBox);

        /* Buttons zum Bestätigen des Editbox-Inhalts und
        * zum Schließen des Fensters werden erstellt und anschließend
        * hinzugefügt */
        confirm_Button = createButton("OK", width/4 - 40, height/2 + 20);
        confirm_Button.addActionListener(this);
        close_Button = createButton("Schließen", width/2 + 20, height/2 + 20);
        close_Button.addActionListener(this);
        this.add(confirm_Button);
        this.add(close_Button);

        /* Mittels WindowListener bzw. Verwendung eines WindowAdapter() ist es möglich,
        * bestimmte Events der Fensternutzung (in diesem Falle: das Schließen) anzupassen */
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                WinAPIJava.regler_isOpen = false; // das Schließen des Fensters gibt den bool wieder frei; Dialogfenster kann im Hauptfenster wieder geöffnet werden
            }
        });

        this.setVisible(true); // Dialogfenster ist sichtbar
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        /* Bei Betätigen des OK-Buttons wird der Inhalt der Editbox auf Korrektheit geprüft
        * und ggf. übernommen. */
        if (event.getSource() == confirm_Button) {
            try {
                WinAPIJava.value = Integer.parseInt(editBox.getText(),16); // wandelt den hexadezimalen-String in einen dezimalen Übergangswert um
                WinAPIJava.mWindow.updateLabels(); // Update die Labels

                scrollBar.setValue(WinAPIJava.value); // aktualisiere ScrollBar nach manueller Werteingabe
                editBox.setText(""); // leere Textfeld für erneute Eingabe

                // Zeichne Hauptfenster neu
                WinAPIJava.mWindow.repaint();
                WinAPIJava.mWindow.revalidate();
            } catch (Exception e) { } // fängt Fehler ab, wenn Editbox leer gelassen wurde
        } else if (event.getSource() == close_Button) { /* Bei Betätigen des Schließen-Buttons wird das Dialogfenster geschlossen. */
            WinAPIJava.regler_isOpen = false; // gibt den bool wieder frei; Dialogfenster kann im Hauptfenster wieder geöffnet werden
            this.dispose(); // schließt Dialogfenster
        }
    }

    /* Methode zur Erstellung eines einheitlichen Buttons */
    public JButton createButton(String text, int x_pos, int y_pos) {
        JButton return_Button = new JButton();

        return_Button.setText(text);
        return_Button.setBounds(x_pos, y_pos, 100, 25);
        return_Button.setFocusable(false);

        return return_Button;
    }

    /* Methode zur Erstellung einer einheitlichen ScrollBar, bei der Inkremente und Grenzen festgelegt werden */
    public JScrollBar createScrollBar(int x_pos, int y_pos, int max, int min, int increment, int position) {
        JScrollBar return_ScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);

        return_ScrollBar.setMinimum(min);
        return_ScrollBar.setMaximum(max + 4);
        return_ScrollBar.setBlockIncrement(max/4);
        return_ScrollBar.setUnitIncrement(increment);
        return_ScrollBar.setValue(position);
        return_ScrollBar.setVisibleAmount(increment * 4);
        return_ScrollBar.setBounds(x_pos, y_pos, 300, 25);

        return return_ScrollBar;
    }

    /* Methode zur Erstellung eines einheitlichen TextFields (Editbox) */
    public JTextField createTextField(int x_pos, int y_pos) {
        JTextField return_TextField = new JTextField();

        return_TextField.setBounds(x_pos, y_pos, 80, 25);

        return return_TextField;
    }
}