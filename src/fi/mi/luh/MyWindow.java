package fi.mi.luh;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Creates window to be shown.
 * Contains everything window related for now.
 *
 * @author Mikko Luhtasaari
 * @version 1.0, 18 Nov 2016
 * @since 1.0
 */
public class MyWindow extends JFrame {

    /**
     * Adds new items.
     */
    private JButton newItem;

    /**
     * Clears the list.
     */
    private JButton newList;

    /**
     * Opens another list.
     */
    private JButton open;

    /**
     * Saves the list.
     */
    private JButton save;

    /**
     * Combines two lists. Currently not in use. Does nothing in version 1.0.
     */
    private JButton combine;

    /**
     * Saves and loads from DP. Does nothing in version 1.0.
     */
    private JButton dropbox;

    /**
     * Shows this shoppinglist.
     */
    private TextArea items;

    /**
     * Contains button in top of the window.
     */
    private JPanel buttonContainer;

    /**
     * Contains shoppinglists.
     */
    private JPanel listContainer;

    /**
     * Contains items.
     */
    private MyLinkedList <ListItem> shoppingList;

    /**
     * Sets starting text to be shown.
     */
    private final String startForShoppingList = "Your shopping list " +
            "now contains: \n";

    /**
     * Contains path where files are saved.
     */
    private String path;

    /**
     * Creates the window and everything relate to it.
     */
    public MyWindow() {
        makePath();
        shoppingList = new MyLinkedList<>();

        setSize(600, 800);
        setLayout(new BorderLayout());

        makeButtonsAndContainers("");
        add(buttonContainer, BorderLayout.PAGE_START);
        add(listContainer, BorderLayout.CENTER);

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Makes buttons and containers.
     *
     * @param nonSense for java lint
     */
    private void makeButtonsAndContainers(String nonSense) {
        newItem = new JButton("New Item");
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = JOptionPane.showInputDialog("Please " +
                        " enter name of the Item");
                int itemAmount = Integer.parseInt(JOptionPane.showInputDialog(
                        "Please enter the amount of item(s)"));
                insertItem(itemName, itemAmount);
                printShoppingListContent();
            }
        });

        newList = new JButton("New List");
        newList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shoppingList.clear();
                items.setText(startForShoppingList);
            }
        });

        open = new JButton("Open");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tryToLoad();
            }
        });

        save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tryToSave();
            }
        });

        // Currently not functioning
        combine = new JButton("Combine");
        dropbox = new JButton("Dropbox");

        buttonContainer = new JPanel();
        buttonContainer.add(newItem);
        buttonContainer.add(newList);
        buttonContainer.add(open);
        buttonContainer.add(save);
        buttonContainer.add(combine);
        buttonContainer.add(dropbox);

        listContainer = new JPanel();
        items = new TextArea(startForShoppingList);
        items.setEditable(false);
        listContainer.add(items);
    }

    /**
     * Tries to load existing list.
     */
    private void tryToLoad() {
        JFileChooser fc = new JFileChooser("Open file");
        System.out.println(path);
        fc.setCurrentDirectory(new File(path));
        fc.showOpenDialog(open);
        File file = fc.getSelectedFile();
        String path = file.getAbsolutePath();
        shoppingList.clear();

        try(BufferedReader in = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = in.readLine();

            while (line != null) {
                //insertItemsFromLine(line);
                String[] temp = line.split(" ");
                insertItem(temp[1],Integer.parseInt(temp[0]));
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
    }

    /**
     * Adds item from given parameters.
     *
     * @param name Name of the item
     * @param amount Amount of item(s).
     */
    private void insertItem(String name, int amount) {
        if (amount > 0) {
            ListItem temp;
            boolean found = false;

            // If list is empty create new ListItem
            if (shoppingList.size() == 0) {
                shoppingList.add(new ListItem(name, amount));
            } else {

                for (int i = 0; i < shoppingList.size(); i++) {
                    temp = (ListItem) shoppingList.get(i);
                    // If item with the same name is found change its amount

                    if (temp.getName().equalsIgnoreCase(name)) {
                        temp.setAmount(temp.getAmount() + amount);
                        found = true;
                    }
                }

                // If not matching items were found, create new ListItem
                if (!found) {
                    shoppingList.add(new ListItem(name, amount));
                }
            }

            updateTextField();
        }
    }

    /**
     * Prints shoppinglist into console.
     */
    private void printShoppingListContent() {

        if (shoppingList.size() > 0) {
            System.out.println("Your Shopping List now:");

            for (int i = 0; i > shoppingList.size(); i++) {
                System.out.println(shoppingList.get(i).toString());
            }
        }
    }

    /**
     * Updates text to be shown in text area.
     *
     */
    private void updateTextField() {
        String temp = startForShoppingList;

        if (!shoppingList.isEmpty()) {

            for (int i = 0; i < shoppingList.size(); i++) {
                temp += shoppingList.get(i).toString()+"\n";
            }
        }

        items.setText(temp);
    }

    /**
     * Tries to save the file. Asks the filename from user.
     *
     */
    private void tryToSave() {
        String saveLocation = JOptionPane.showInputDialog("Please enter" +
                " filename");
        try{
            PrintWriter out = new PrintWriter(path+saveLocation+".txt");

            for (int i = 0; i < shoppingList.size(); i++) {
                System.out.println("Tallennetaan");
                ListItem temp = (ListItem)shoppingList.get(i);
                out.println(temp.description());
            }

            out.close();
        } catch(IOException exp) {
            exp.printStackTrace();
        }
    }

    /**
     * Modifies path variable if program is being run from .jar.
     */
    private void makePath() {
        String pathTemp = Main.class.getProtectionDomain().
                getCodeSource().getLocation().getPath();

        if (pathTemp.contains("luhtasaari-mikko.jar")) {
            int index = pathTemp.indexOf("luhtasaari-mikko.jar");
            path = pathTemp.substring(0, index);
            System.out.println(path);
        } else {
            path = pathTemp;
        }
    }
}
