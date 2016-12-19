package fi.mi.luh.Buttons;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import fi.mi.luh.ListItem;
import fi.mi.luh.MyLinkedList;
import fi.mi.luh.MyWindow;

import javax.swing.*;
import java.awt.*;
import java.io.*;

import static fi.mi.luh.Main.ACCESS_TOKEN;

/**
 * Creates button which connects to Dropbox.
 *
 * @author Mikko Luhtasaari
 * @version 1.0, 8 Dec 2016
 * @since 2.0
 */
public class ButtonDropbox extends JButton {

    /**
     * Stores main view.
     */
    private MyWindow window;

    /**
     * Stores buttons name.
     */
    private String name;

    /**
     * Stores the path to this app.
     */
    private String path;

    /**
     * Constructs the button.
     * Enables Dropbox saving and loading.
     *
     * @param window main view.
     * @param name buttons name.
     */
    public ButtonDropbox(MyWindow window, String name){
        super(name);
        this.window = window;
        this.name = name;
        this.path = window.getPath();

        addMyActionListener();
    }

    /**
     * Adds action listener.
     */
    private void addMyActionListener(){
        this.addActionListener(e ->{
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
        });
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
                insertItem(temp[1], Integer.parseInt(temp[0]), window.getList());
                sb.append(line);
                sb.append(System.lineSeparator());
                line = in.readLine();
            }

            String everything = sb.toString();
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
     * Saves the file to Dropbox.
     *
     */
    private void dropboxSaving() {
        String fileName = JOptionPane.showInputDialog("Please enter" +
                " filename");
        String saveLocation = path + fileName+ ".txt";
        boolean exists = false;

        // Create temporary text file which is then uploaded to DP.
        try {
            PrintWriter out = new PrintWriter(saveLocation);

            for (int i = 0; i < window.getList().size(); i++) {
                ListItem temp = (ListItem) window.getList().get(i);
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
     * Returns buttons name.
     *
     * @return buttons name.
     */
    public String getName(){
        return name;
    }
}
