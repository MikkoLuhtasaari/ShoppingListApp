package fi.mi.luh;

/**
 * Created by M1k1tus on 18-Nov-16.
 *  @author Mikko Luhtasaari
 *  @version 1.0, 18 Nov 2016
 *  @since 1.0
 *  Starting class for creating window.
 */
public class GUI {
    MyWindow window = new MyWindow();

    /**
     * Makes the window to be shown.
     *
     */
    public GUI(){
        window.setVisible(true);
    }

}
