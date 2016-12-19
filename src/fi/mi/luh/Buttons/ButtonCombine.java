package fi.mi.luh.Buttons;

import fi.mi.luh.ListItem;
import fi.mi.luh.MyLinkedList;
import fi.mi.luh.MyWindow;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Creates button which combines shopping lists.
 *
 * @author Mikko Luhtasaari
 * @version 1.0, 8 Dec 2016
 * @since 2.0
 */
public class ButtonCombine extends JButton {

    /**
     * Stores main view.
     */
    private MyWindow window;

    /**
     * Stores buttons name.
     */
    private String name;

    /**
     * Stores the path.
     */
    private String path;

    /**
     * Constructs button.
     * Combines two shopping lists.
     *
     * @param window main view.
     * @param name buttons name.
     */
    public ButtonCombine(MyWindow window, String name){
        super(name);
        this.window = window;
        this.name = name;
        this.path = window.getPath();

        addMyActionListener();
    }

    /**
     * Adds action listener.
     */
    private void addMyActionListener() {
        this.addActionListener(e -> {
            MyLinkedList<ListItem> tempList = new MyLinkedList<>();
            JFileChooser fc = new JFileChooser("Choose list to combine");
            fc.setCurrentDirectory(new File(path));
            fc.showOpenDialog(this);
            File file = fc.getSelectedFile();
            String path = file.getAbsolutePath();

            try (BufferedReader in = new BufferedReader(new FileReader(path))) {
                StringBuilder sb = new StringBuilder();
                String line = in.readLine();

                while (line != null) {
                    String[] temp = line.split(" ");
                    insertItem(temp[1], Integer.parseInt(temp[0]), tempList);
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = in.readLine();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            for (int i = 0; i < tempList.size(); i++) {
                ListItem temp = (ListItem) tempList.get(i);
                insertItem(temp.getName(), temp.getAmount(), window.getList());
            }
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
     * @return buttons name.
     */
    public String getName(){
        return name;
    }
}
