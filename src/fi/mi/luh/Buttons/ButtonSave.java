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
    private MyWindow window;
    private String name;
    private String path;

    public ButtonSave(MyWindow window, String name){
        super(name);
        this.window = window;
        this.name = name;
        this.path = window.getPath();

        addMyActionListener();
    }

    private void addMyActionListener(){
        this.addActionListener(e -> {
            String saveLocation = JOptionPane.showInputDialog("Please enter" +
                    " filename");
            try{
                PrintWriter out = new PrintWriter(path+saveLocation+".txt");

                for (int i = 0; i < window.getList().size(); i++) {
                    System.out.println("Tallennetaan");
                    ListItem temp = (ListItem)window.getList().get(i);
                    out.println(temp.description());
                }

                out.close();
            } catch(IOException exp) {
                exp.printStackTrace();
            }
        });
    }

    public String getName(){
        return name;
    }
}
