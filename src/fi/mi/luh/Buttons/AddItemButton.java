package fi.mi.luh.Buttons;

import fi.mi.luh.ListItem;
import fi.mi.luh.Main;
import fi.mi.luh.MyLinkedList;
import fi.mi.luh.MyWindow;

import javax.swing.*;

/**
 * Created by M1k1tus on 08-Dec-16.
 */
public class AddItemButton extends JButton {
    MyLinkedList list;
    MyWindow window;
    String name;

    public AddItemButton(MyLinkedList list, MyWindow window, String name){
        super(name);
        this.list = list;
        this.window = window;
        this.name = name;

        addMyActionListener();
        //window.add(this);
    }

    private void addMyActionListener() {
        this.addActionListener(e -> {
            String itemName = JOptionPane.showInputDialog("Please " +
                    " enter name of the Item");
            int itemAmount = Integer.parseInt(JOptionPane.showInputDialog(
                    "Please enter the amount of item(s)"));
            insertItem(itemName, itemAmount, list);
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

        if (!list.isEmpty()) {

            for (int i = 0; i < list.size(); i++) {
                temp += list.get(i).toString()+"\n";
            }
        }

        window.getItems().setText(temp);
    }

    public String getName(){
        return name;
    }
}
