public class WinAPIJava {
    static MainWindow mWindow; // Hauptfenster
    static boolean regler_isOpen = false; // bestimmt, ob Dialogfenster bereits geöffnet ist
    static int value; // dezimaler Wert zur Repräsentation des hexadezimalen Werts

    public static void main(String[] args) {
        value = 0;
        mWindow = new MainWindow();
    }
}