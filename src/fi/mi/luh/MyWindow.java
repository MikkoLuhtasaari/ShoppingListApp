package fi.mi.luh;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import fi.mi.luh.Buttons.AddItemButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import static fi.mi.luh.Main.ACCESS_TOKEN;

/**
 * Creates window to be shown.
 * Contains everything window related for now.
 *
 * @author Mikko Luhtasaari
 * @version 2.0, 06 Dec 2016
 * @since 1.0
 */
public class MyWindow extends JFrame {

    private AddItemButton addItem;


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
        /*newItem = new JButton("New Item");
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = JOptionPane.showInputDialog("Please " +
                        " enter name of the Item");
                int itemAmount = Integer.parseInt(JOptionPane.showInputDialog(
                        "Please enter the amount of item(s)"));
                insertItem(itemName, itemAmount, shoppingList);
                printShoppingListContent();
            }
        });*/
        addItem = new AddItemButton(shoppingList,this,"Add Items");


        newList = new JButton("New List");
        newList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shoppingList.clear();
                items.setText(Main.startForShoppingList);
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

        combine = new JButton("Combine");
        combine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                combineLists();
            }
        });
        dropbox = new JButton("Dropbox");
        dropbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDPOperation();
            }
        });

        buttonContainer = new JPanel();
        buttonContainer.add(addItem);
        buttonContainer.add(newList);
        buttonContainer.add(open);
        buttonContainer.add(save);
        buttonContainer.add(combine);
        buttonContainer.add(dropbox);

        listContainer = new JPanel();
        items = new TextArea(Main.startForShoppingList);
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
                String[] temp = line.split(" ");
                insertItem(temp[1], Integer.parseInt(temp[0]), shoppingList);
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
    public void updateTextField() {
        String temp = Main.startForShoppingList;

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

    /**
     * Tries to combine two shoppinglists.
     *
     */
    private void combineLists() {
        MyLinkedList<ListItem> tempList = new MyLinkedList<>();
        JFileChooser fc = new JFileChooser("Choose list to combine");
        fc.setCurrentDirectory(new File(path));
        fc.showOpenDialog(open);
        File file = fc.getSelectedFile();
        String path = file.getAbsolutePath();

        try(BufferedReader in = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = in.readLine();

            while (line != null) {
                String[] temp = line.split(" ");
                insertItem(temp[1], Integer.parseInt(temp[0]), tempList);
                sb.append(line);
                sb.append(System.lineSeparator());
                line = in.readLine();
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }

        for (int i = 0; i < tempList.size(); i++) {
            ListItem temp =(ListItem)tempList.get(i);
            insertItem(temp.getName(),temp.getAmount(),shoppingList);
        }
    }

    /**
     * Saves the file to Dropbox.
     *
     */
    private void dropboxSaving() {
        String fileName = JOptionPane.showInputDialog("Please enter" +
                " filename");
        String saveLocation = path + fileName+ ".txt";
        System.out.println(saveLocation);
        boolean exists = false;

        // Create temporary text file which is then uploaded to DP.
        try {
            PrintWriter out = new PrintWriter(saveLocation);

            for (int i = 0; i < shoppingList.size(); i++) {
                System.out.println("Tallennetaan");
                ListItem temp = (ListItem) shoppingList.get(i);
                out.println(temp.description());
            }

            out.close();
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
        }

        // Init connection to DP.
        DbxRequestConfig config = new DbxRequestConfig
                ("dropbox/ShoppingList-Mikko-Luhtasaari");
        DbxClientV1 client = new DbxClientV1(config, ACCESS_TOKEN);

        // Check DP, if file with a same name is found, delete it.
        try {
            DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");

            for (DbxEntry child : listing.children) {

                if (child.name.equalsIgnoreCase(fileName+".txt")) {
                    exists = true;
                    System.out.println("found");
                }
            }
        } catch (DbxException e) {
            e.printStackTrace();
        }

        // Delete the file.
        if (exists) {
            try {
                client.delete("/"+fileName+".txt");
                System.out.println("deleted from DP!");
            } catch(DbxException e) {
                e.printStackTrace();
            }
        }

        // Create another connection to save the file.
        DbxClientV2 client2 = new DbxClientV2(config, ACCESS_TOKEN);

        try (InputStream in = new FileInputStream(saveLocation)) {
            FileMetadata metadata = client2.files().
                    uploadBuilder("/"+fileName+".txt")
                    .uploadAndFinish(in);
        } catch (IOException | DbxException e) {
            e.printStackTrace();
        }

        // Delete temporary file.
        File file = new File(saveLocation);

        if (file.delete()) {
            System.out.println(file.getName() + " is deleted!");
        } else {
            System.out.println("Delete operation is failed.");
        }
    }

    /**
     * Loads the file from Dropbox.
     *
     */
    private void dropboxLoading() {
        DbxRequestConfig config = new DbxRequestConfig
                ("dropbox/ShoppingList-Mikko-Luhtasaari");
        DbxClientV1 client = new DbxClientV1(config, ACCESS_TOKEN);
        String fileNameTemp = JOptionPane.showInputDialog("Please enter" +
                " filename");
        String fileName = fileNameTemp+".txt";

        try {
            FileOutputStream outputStream = new FileOutputStream
                    (path + fileName);
            try {
                DbxEntry.File downloadedFile =
                        client.getFile("/" + fileName, null,
                        outputStream);
            } finally {
                outputStream.close();
            }
        } catch (IOException | DbxException e) {
            e.printStackTrace();
        }

        try (BufferedReader in =
                     new BufferedReader(new FileReader(path + fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = in.readLine();

            while (line != null) {
                String[] temp = line.split(" ");
                insertItem(temp[1], Integer.parseInt(temp[0]), shoppingList);
                sb.append(line);
                sb.append(System.lineSeparator());
                line = in.readLine();
            }

            String everything = sb.toString();
            System.out.println(everything);
            updateTextField();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Delete temporary file.
        File file = new File(path+fileName);

        if (file.delete()) {
            System.out.println(file.getName() + " is deleted!");
        } else {
            System.out.println("Delete operation is failed.");
        }
    }

    /**
     * Creates options for loading and saving via DP.
     *
     */
    private void setDPOperation() {
        Object[] options = {"Save",
                "Load"};
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
            dropboxSaving();
        }

        if (n == 1) {
            dropboxLoading();
        }
    }

    public TextArea getItems(){
        return items;
    }
}
