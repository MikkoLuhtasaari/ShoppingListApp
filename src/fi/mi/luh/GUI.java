package fi.mi.luh;

/**
 * Starting class for creating window.
 *
 * @author Mikko Luhtasaari
 * @version 1.0, 18 Nov 2016
 * @since 1.0
 */
public class GUI {
    MyWindow window = new MyWindow();

    /**
     * Makes the window to be shown.
     *
     */
    public GUI() {
        window.setVisible(true);
    }
}
