package fi.mi.luh;

/**
 * Starting point for program.
 *
 * @author Mikko Luhtasaari
 * @version 1.0
 * @since 1.0, 14 Nov 2016
 */
public class Main {
    public static final String ACCESS_TOKEN = "saewg_CgqBIAAAAAAAAJT_lsI3wwCZO1Ogz-w87rUzeyh_5lyHanN5x0nYEA5dBK";

    /**
     * Sets starting text to be shown.
     */
    public static final String startForShoppingList = "Your shopping list " +
            "now contains: \n";

    /**
     * Starts the app.
     *
     * @param args command line parameter
     */
    public static void main(String[] args) {
        //System.out.println(ACCESS_TOKEN);
        // CLI juttu = new CLI();
        // juttu.run();
        GUI testi = new GUI();
    }
}
