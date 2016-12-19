package fi.mi.luh;

/**
 * Starting point for program.
 *
 * @author Mikko Luhtasaari
 * @version 1.0
 * @since 1.0, 14 Nov 2016
 */
public class Main {

    /**
     * Access token to get into DP.
     */
    public static final String ACCESS_TOKEN = "saewg_CgqBIAAAAAAAAJT_lsI3wwCZO1Ogz-w87rUzeyh_5lyHanN5x0nYEA5dBK";

    /**
     * Starts the app.
     *
     * @param args command line parameter
     */
    public static void main(String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("--cli")) {
            CLI juttu = new CLI();
        } else {
            GUI testi = new GUI();
        }
    }
}
