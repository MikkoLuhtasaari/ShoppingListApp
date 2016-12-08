package fi.mi.luh.Buttons;

import fi.mi.luh.Main;
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

    /**
     * Stores main view.
     */
    private MyWindow window;

    /**
     * Stores buttons name.
     */
    private String name;

    /**
     * Constructs button.
     * Clears existing shoppinglist.
     *
     * @param window main view.
     * @param name buttons name.
     */
    public ButtonNewList(MyWindow window, String name){
        super(name);
        this.window = window;
        this.name = name;

        addMyActionListener();
    }

    /**
     * Add action listener.
     */
    private void addMyActionListener(){
        this.addActionListener(e -> {
            window.getList().clear();
            window.getItems().setText(Main.startForShoppingList);
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
