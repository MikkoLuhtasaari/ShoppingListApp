package fi.mi.luh.Buttons;

import fi.mi.luh.ListItem;
import fi.mi.luh.MyWindow;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Creates button which saves shopping lists.
 *
 * @author Mikko Luhtasaari
 * @version 1.0, 8 Dec 2016
 * @since 2.0
 */
public class ButtonSave extends JButton {

    /**
     * Stores main view.
     */
    private MyWindow window;

    /**
     * Stores buttons name.
     */
    private String name;

    /**
     * Stores the path to this app.
     */
    private String path;

    /**
     * Constructs button.
     * Saves shoppinglist to a text file.
     *
     * @param window main view.
     * @param name buttons name.
     */
    public ButtonSave(MyWindow window, String name){
        super(name);
        this.window = window;
        this.name = name;
        this.path = window.getPath();

        addMyActionListener();
    }

    /**
     * Adds action listener.
     */
    private void addMyActionListener(){
        this.addActionListener(e -> {
            String saveLocation = JOptionPane.showInputDialog("Please enter" +
                    " filename");
            if(!saveLocation.equalsIgnoreCase("")) {
                try {
                    PrintWriter out = new PrintWriter(path + saveLocation + ".txt");

                    for (int i = 0; i < window.getList().size(); i++) {
                        System.out.println("Tallennetaan");
                        ListItem temp = (ListItem) window.getList().get(i);
                        out.println(temp.description());
                    }

                    out.close();
                } catch (IOException exp) {
                    exp.printStackTrace();
                }
            } else {
                System.out.println("User cancelled operation");
            }
        });

    }

    /**
     * Returns buttons name.
     *
     * @return buttons name.
     */
    public String getName(){
        return name;
    }
}
