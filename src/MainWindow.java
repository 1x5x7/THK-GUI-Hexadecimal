import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ActionListener {
    static private int width = 400, height = 300;       // Breite + Höhe des Hauptfensters
    static private int[] value_hPositions = new int[4]; // horizontale Koordinaten der Hexadezimal-Stellen
    static private int value_vPosition;                 // vertikale Koordinate der Hexadezimal-Stellen

    JMenuItem openWindow; // Optionseintrag für die Menüauswahl; global für ActionListener
    static private int box_width = 40, box_height = 40; // Breite + Höhe einer Hexadezimal-Stellen-Box (hier gilt: Höhe = Breite)

    JLabel[] hex_stelle = new JLabel[4]; // Repräsentation der einzelnen Hexadezimal-Stellen

    MainWindow() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // terminiert Programm vollständig
        this.setTitle("Hexadezimalzahl-Anzeige");
        this.setSize(width, height);
        this.setResizable(false); // verhindert Größenveränderung des Fensters
        this.setLayout(null);
        this.setLocationRelativeTo(this); // zentriert das Fenster

        JMenuBar menuBar = new JMenuBar(); // fügt dem Hauptfenster eine Menübar hinzu
        JMenu windowMenu = new JMenu("Regler"); // Eintrag in die Menübar

        // Option (zur Öffnung des Dialogfensters) wird erstellt
        openWindow = new JMenuItem("Öffnen");
        openWindow.addActionListener(this);

        // Menübar wird vollständig hinzugefügt und gesetzt
        windowMenu.add(openWindow);
        menuBar.add(windowMenu);
        this.setJMenuBar(menuBar);

        initPositions(); // Positionen der Rechtecke und Hexadezimal-Stellen werden ermittelt

        /* Erstelle Labels als korrespondierende Hexadezimal-Stellen */
        hex_stelle[0] = createLabel("0", value_hPositions[0] + box_width/5, height/3, false);
        hex_stelle[1] = createLabel("0", value_hPositions[1] + box_width/5, height/3, false);
        hex_stelle[2] = createLabel("0", value_hPositions[2] + box_width/5, height/3, false);
        hex_stelle[3] = createLabel("0", value_hPositions[3] + box_width/5, height/3, true);

        for (int i = 0; i < 4; i++) {
            this.add(hex_stelle[i]);
        }

        this.setVisible(true); // Hauptfenster ist sichtbar
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        /* Bei Auswahl des Menüeintrags öffnet sich das Dialogfenster mit dem Regler,
        *  wenn nicht bereits eine Instanz existiert.*/
        if (event.getSource() == openWindow) {
            if (WinAPIJava.regler_isOpen == false) {
                WinAPIJava.regler_isOpen = true; // Dialogfenster wird als "geöffnet" markiert
                new DialogWindow(); // öffne Dialogfenster
            } else {
                JOptionPane.showMessageDialog(null, "Das Dialogfenster zum Regler ist bereits geöffnet.", "Achtung!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    @Override
    public void paint(Graphics g) { // Zeichnen der einzelnen Boxen um die Stellen herum, ggf. sie sind sichtbar (KEINE führende Null)
        super.paint(g);
        g.drawRect(value_hPositions[3], value_vPosition, box_width, box_height); // Box 4

        if (hex_stelle[2].isVisible()) {
            g.drawRect(value_hPositions[2], value_vPosition, box_width, box_height); // Box 3
        }

        if (hex_stelle[1].isVisible()) {
            g.drawRect(value_hPositions[1], value_vPosition, box_width, box_height); // Box 2
        }

        if (hex_stelle[0].isVisible()) {
            g.drawRect(value_hPositions[0], value_vPosition, box_width, box_height); // Box 1
        }
    }

    /* Diese Methode erstellt die x- und y-Koordinaten zur relativen Orientierung der Boxen und Zahlenwerte */
    public void initPositions() {
        value_hPositions[0] = width/4 + box_width/2;
        value_hPositions[1] = width/4 + box_width/2 + box_width;
        value_hPositions[2] = width/4 + box_width/2 + box_width * 2;
        value_hPositions[3] = width/4 + box_width/2 + box_width * 3;

        value_vPosition = height/2;
    }

    /* Diese Methode erzeugt Labels basierend auf vorgegebenem Textwert, Positionen und Sichtbarkeit */
    public JLabel createLabel(String text, int x_pos, int y_pos, boolean visible) {
        JLabel return_Label = new JLabel();

        return_Label.setText(text);
        return_Label.setFont(new Font("New Times Roman", Font.PLAIN, 20));
        return_Label.setBounds(x_pos, y_pos, box_width, box_height);
        return_Label.setVisible(visible);

        return return_Label;
    }

    public void updateLabels() {
        String hex_String = String.format("%04X", WinAPIJava.value); // wandelt Dezimalwert um zu hexadezimalen Wert mit führenden Nullen (4-stellig)

        // Hexadezimal-Stellen werden die entsprechenden Ziffern zugeordnet
       for (int i = 0; i < 4; i++) {
            hex_stelle[i].setText("" + hex_String.charAt(i));
            hex_stelle[i].setVisible(true);
        }

       /* Fallbetrachtungen, um führende Nullen unsichtbar zu machen */
       // Fall 1: 3 führende Nullen
       if (hex_stelle[2].getText().equals("0") && (hex_stelle[0].getText().equals("0") && (hex_stelle[1].getText().equals("0"))) ) {
           hex_stelle[0].setVisible(false);
           hex_stelle[1].setVisible(false);
           hex_stelle[2].setVisible(false);
       // Fall 2: 2 führende Nullen
       } else if (hex_stelle[1].getText().equals("0") && (hex_stelle[0].getText().equals("0"))) {
           hex_stelle[0].setVisible(false);
           hex_stelle[1].setVisible(false);
       // Fall 3: nur eine führende Null
       } else if (hex_stelle[0].getText().equals("0")) {
           hex_stelle[0].setVisible(false);
       }
    }
}