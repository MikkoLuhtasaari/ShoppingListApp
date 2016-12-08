package fi.mi.luh.Buttons;

import fi.mi.luh.ListItem;
import fi.mi.luh.Main;
import fi.mi.luh.MyLinkedList;
import fi.mi.luh.MyWindow;

import javax.swing.*;

/**
 * Creates button which can add new items to MyLinkedList.
 *
 * @author Mikko Luhtasaari
 * @version 1.0, 8 Dec 2016
 * @since 2.0
 */
public class ButtonAddItem extends JButton {
    private MyWindow window;
    private String name;

    public ButtonAddItem(MyWindow window, String name){
        super(name);
        this.window = window;
        this.name = name;

        addMyActionListener();
    }

    private void addMyActionListener() {
        this.addActionListener(e -> {
            String itemName = JOptionPane.showInputDialog("Please " +
                    " enter name of the Item");
            int itemAmount = Integer.parseInt(JOptionPane.showInputDialog(
                    "Please enter the amount of item(s)"));
            insertItem(itemName, itemAmount, window.getList());
        });
    }

    /**
     * Adds item from given parameters.
     *
     * @param name Name of the item.
     * @param amount Amount of item(s).
     * @param list MyLinkedList where items are added.
     */
    private void insertItem(String name, int amount, MyLinkedList list) {
        if (amount > 0) {
            ListItem temp;
            boolean found = false;

            // If list is empty create new ListItem
            if (list.size() == 0) {
                list.add(new ListItem(name, amount));
            } else {

                for (int i = 0; i < list.size(); i++) {
                    temp = (ListItem) list.get(i);

                    // If item with the same name is found change its amount
                    if (temp.getName().equalsIgnoreCase(name)) {
                        temp.setAmount(temp.getAmount() + amount);
                        found = true;
                    }
                }

                // If not matching items were found, create new ListItem
                if (!found) {
                    list.add(new ListItem(name, amount));
                }
            }

            updateTextField();
        }
    }

    /**
     * Updates text to be shown in text area.
     *
     */
    private void updateTextField() {
        String temp = Main.startForShoppingList;

        if (!window.getList().isEmpty()) {

            for (int i = 0; i < window.getList().size(); i++) {
                temp += window.getList().get(i).toString()+"\n";
            }
        }

        window.getItems().setText(temp);
    }

    public String getName(){
        return name;
    }
}
