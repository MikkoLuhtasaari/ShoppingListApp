package fi.mi.luh.Buttons;

import fi.mi.luh.ListItem;
import fi.mi.luh.Main;
import fi.mi.luh.MyLinkedList;
import fi.mi.luh.MyWindow;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Creates button which loads shopping lists.
 *
 * @author Mikko Luhtasaari
 * @version 1.0, 8 Dec 2016
 * @since 2.0
 */
public class ButtonOpen extends JButton {
    private MyLinkedList list;
    private MyWindow window;
    private String name;
    private String path;

    public ButtonOpen(MyLinkedList list, MyWindow window, String name){
        super(name);
        this.list = list;
        this.window = window;
        this.name = name;
        this.path = window.getPath();

        addMyActionListener();
    }

    private void addMyActionListener(){
        this.addActionListener(e -> {
            JFileChooser fc = new JFileChooser("Open file");
            System.out.println(path);
            fc.setCurrentDirectory(new File(path));
            fc.showOpenDialog(this);
            File file = fc.getSelectedFile();
            String path = file.getAbsolutePath();
            list.clear();

            try(BufferedReader in = new BufferedReader(new FileReader(path))) {
                StringBuilder sb = new StringBuilder();
                String line = in.readLine();

                while (line != null) {
                    String[] temp = line.split(" ");
                    insertItem(temp[1], Integer.parseInt(temp[0]), list);
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = in.readLine();
                }

                String everything = sb.toString();
                System.out.println(everything);
                updateTextField();
            } catch(IOException ex) {
                ex.printStackTrace();
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
        }
    }

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
