package fi.mi.luh.Buttons;

import fi.mi.luh.Main;
import fi.mi.luh.MyLinkedList;
import fi.mi.luh.MyWindow;

import javax.swing.*;

/**
 * Created by M1k1tus on 08-Dec-16.
 */
public class ButtonNewList extends JButton {

    private MyLinkedList list;
    private MyWindow window;
    private String name;

    public ButtonNewList(MyLinkedList list, MyWindow window, String name){
        super(name);
        this.list = list;
        this.window = window;
        this.name = name;

        addMyActionListener();
    }

    private void addMyActionListener(){
        this.addActionListener(e -> {
            list.clear();
            window.getItems().setText(Main.startForShoppingList);
        });
    }

    public String getName(){
        return name;
    }
}
