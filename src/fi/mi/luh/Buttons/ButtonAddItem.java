package fi.mi.luh.Buttons;

import fi.mi.luh.ListItem;
import fi.mi.luh.MyLinkedList;
import fi.mi.luh.MyWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Creates button which can add new items to MyLinkedList.
 *
 * @author Mikko Luhtasaari
 * @version 1.0, 8 Dec 2016
 * @since 2.0
 */
public class ButtonAddItem extends JButton {

    /**
     * Stores Main view.
     */
    private MyWindow window;

    /**
     * Stores buttons name.
     */
    private String name;

    /**
     * Constructs button and adds action listener.
     *
     * Adds items to shopping list.
     *
     * @param window main view.
     * @param name name of the button.
     */
    public ButtonAddItem(MyWindow window, String name) {
        super(name);
        this.window = window;
        this.name = name;

        addMyActionListener();
    }

    /**
     * Adds corresponding action listener.
     *
     * Adds items to shopping list.
     */
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
     * Updates buttons in listContainer.
     *
     */
    private void updateTextField() {
        window.getListContainer().removeAll();

        for (int i = 0; i < window.getList().size(); i++) {
            ListItem tempItem = (ListItem)window.getList().get(i);
            JButton temp = new JButton(tempItem.getName()+
                    " "+tempItem.getAmount());
            temp.setBackground(new Color(0, 0, 0));
            temp.setForeground(new Color(255, 255, 255));
            temp.addActionListener(e -> {
                Object[] options = {"Delete",
                        "Change amount"};
                int n = JOptionPane.showOptionDialog(this,
                        "Please select operation",
                        "Files from Dropbox",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (n == -1) {
                    JOptionPane.showMessageDialog(this,
                            "Clicked cancel.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }

                if (n == 0) {
                    this.window.getList().remove(tempItem);
                    this.window.getListContainer().remove(temp);
                    window.getListContainer().updateUI();
                }

                if (n == 1) {
                    int itemAmount = Integer.parseInt(JOptionPane.
                            showInputDialog(
                            "Please enter the amount of item(s)"));
                    tempItem.setAmount(itemAmount);
                    temp.setText(tempItem.getName()+" "+tempItem.getAmount());
                }
            });
            window.getListContainer().add(temp);
        }

        window.getListContainer().updateUI();
    }

    /**
     * Returns buttons name.
     *
     * @return return name.
     */
    public String getName() {
        return name;
    }
}
