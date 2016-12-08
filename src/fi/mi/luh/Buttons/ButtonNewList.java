package fi.mi.luh.Buttons;

import fi.mi.luh.Main;
import fi.mi.luh.MyLinkedList;
import fi.mi.luh.MyWindow;

import javax.swing.*;

/**
 * Creates button which can clear given MyLinkedList.
 *
 * @author Mikko Luhtasaari
 * @version 1.0, 8 Dec 2016
 * @since 2.0
 */
public class ButtonNewList extends JButton {

    private MyWindow window;
    private String name;

    public ButtonNewList(MyWindow window, String name){
        super(name);
        this.window = window;
        this.name = name;

        addMyActionListener();
    }

    private void addMyActionListener(){
        this.addActionListener(e -> {
            window.getList().clear();
            window.getItems().setText(Main.startForShoppingList);
        });
    }

    public String getName(){
        return name;
    }
}
