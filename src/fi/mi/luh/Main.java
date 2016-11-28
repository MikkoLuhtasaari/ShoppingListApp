package fi.mi.luh;

import java.net.URLDecoder;

public class Main {
    static final String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    public static void main(String[] args) {
        //String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        //String decodedPath = URLDecoder.decode(path, "UTF-8");
        //CLI juttu = new CLI();
        //juttu.run();



        GUI testi = new GUI();
    }
}
