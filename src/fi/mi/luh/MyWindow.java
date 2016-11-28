package fi.mi.luh;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Created by Mikko Luhtsaari on 18-Nov-16.
 *  @author Mikko Luhtasaari
 *  @version 1.0, 18 Nov 2016
 *  @since 1.0
 *  Creates window to be shown.
 *  Contains everything window related for now.
 */
public class MyWindow extends JFrame {
    private JButton newItem;
    private JButton newList;
    private JButton open;
    private JButton save;
    private JButton combine;
    private JButton dropbox;

    private TextArea items;

    private JPanel buttonContainer;
    private JPanel listContainer;

    private MyLinkedList <ListItem> shoppingList;

    private final String startForShoppingList = "Your shopping list " +
            "now contains: \n";
    private String path;


    /**
     * Creates the window and everything relate to it.
     */
    public MyWindow() {
        makePath();
        shoppingList = new MyLinkedList<>();

        setSize(600,800);
        setLayout(new BorderLayout());

        makeButtonsAndContainers();
        add(buttonContainer, BorderLayout.PAGE_START);
        add(listContainer, BorderLayout.CENTER);

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Makes buttons and containers.
     */
    private void makeButtonsAndContainers() {
        newItem = new JButton("New Item");
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = JOptionPane.showInputDialog("Please enter name of the Item");
                int itemAmount = Integer.parseInt(JOptionPane.showInputDialog("Please enter the amount of item(s)"));
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

        //Currently not functioning
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
    private void tryToLoad(){
        JFileChooser fc = new JFileChooser("Open file");
        System.out.println(path);
        fc.setCurrentDirectory(new File(path));
        fc.showOpenDialog(open);
        File file = fc.getSelectedFile();
        String path = file.getAbsolutePath();
        shoppingList.clear();

        try(BufferedReader in = new BufferedReader(new FileReader(path))){
            StringBuilder sb = new StringBuilder();
            String line = in.readLine();

            while (line != null) {
                insertItemsFromLine(line);
                sb.append(line);
                sb.append(System.lineSeparator());
                line = in.readLine();
            }
            String everything = sb.toString();
            System.out.println(everything);
            updateTextField();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Adds item from given parameters
     *
     * @param name Name of the item
     * @param amount Amount of item(s).
     */
    private void insertItem(String name, int amount){
        if (amount > 0) {
            ListItem temp;
            boolean found = false;

            //If list is empty create new ListItem
            if (shoppingList.size() == 0) {
                shoppingList.add(new ListItem(name, amount));
            }
            //Iterate through list
            else {
                for (int i = 0; i < shoppingList.size(); i++) {
                    temp = (ListItem) shoppingList.get(i);
                    //If item with the same name is found change its amount
                    if (temp.getName().equalsIgnoreCase(name)) {
                        temp.setAmount(temp.getAmount() + amount);
                        found = true;
                    }
                }
                //If not matching items were found, create new ListItem
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
    private void printShoppingListContent(){

        if(shoppingList.size()>0) {
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

        if(!shoppingList.isEmpty()){
            for (int i = 0; i < shoppingList.size(); i++) {
                temp += shoppingList.get(i).toString()+"\n";
            }
        }
        items.setText(temp);
    }

    /**
     * Tries to save the file.
     * Asks the name of the file from user.
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
        }catch(IOException exp){
            exp.printStackTrace();
        }
    }

    /**
     * Creates ListItems from input and adds them to shoppingList.
     *
     * @param line modified user input.
     */
    private void insertItemsFromLine(String line) {
        line = line.trim();
        int amount = 0;

        // Creating integer from String requires extra attention.
        try {
            String temp = "" + line.charAt(0);
            amount = Integer.parseInt(temp);
        } catch (NumberFormatException exp) {
            System.out.println("!!!ERROR!!!");
            System.out.println("Enter item as <amount> <item>");
            System.out.println("Example input shown below");
            System.out.println("1 milk;");
            System.out.println("or multiple items in one line");
            System.out.println("1 milk; 2 apple; 3 beers");
        }

        // If input was proper create item.
        if (amount > 0) {
            ListItem temp;
            boolean found = false;
            String name = line.substring(2);

            // If list is empty create new ListItem.
            if (shoppingList.size() == 0) {
                shoppingList.add(new ListItem(name, amount));
            } else {
                for (int i = 0; i < shoppingList.size(); i++) {
                    temp = (ListItem) shoppingList.get(i);

                    // If item with the same name is found change its amount.
                    if (temp.getName().equalsIgnoreCase(name)) {
                        temp.setAmount(temp.getAmount() + amount);
                        found = true;
                    }
                }

                // If not matching items were found, create new ListItem.
                if (!found) {
                    shoppingList.add(new ListItem(name, amount));
                }
            }
        }
    }

    /**
     * Modifies path variable if program is being run from .jar.
     */
    private void makePath(){
        String pathTemp = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        if(pathTemp.contains("luhtasaari-mikko.jar")) {
            System.out.println("Löytyy");
            int index = pathTemp.indexOf("luhtasaari-mikko.jar");
            path = pathTemp.substring(0,index);
            System.out.println(path);
        } else{
            System.out.println("Ei löydy");
            path = pathTemp;
        }
    }
}
