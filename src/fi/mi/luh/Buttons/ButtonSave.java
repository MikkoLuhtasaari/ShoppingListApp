package fi.mi.luh.Buttons;

import fi.mi.luh.ListItem;
import fi.mi.luh.MyLinkedList;
import fi.mi.luh.MyWindow;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by M1k1tus on 08-Dec-16.
 */
public class ButtonSave extends JButton {
    private MyLinkedList list;
    private MyWindow window;
    private String name;
    private String path;

    public ButtonSave(MyLinkedList list, MyWindow window, String name){
        super(name);
        this.list = list;
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

                for (int i = 0; i < list.size(); i++) {
                    System.out.println("Tallennetaan");
                    ListItem temp = (ListItem)list.get(i);
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
