package fi.mi.luh.Buttons;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import fi.mi.luh.ListItem;
import fi.mi.luh.Main;
import fi.mi.luh.MyLinkedList;
import fi.mi.luh.MyWindow;

import javax.swing.*;
import java.io.*;

import static fi.mi.luh.Main.ACCESS_TOKEN;

/**
 * Created by M1k1tus on 08-Dec-16.
 */
public class ButtonDropbox extends JButton {

    private MyWindow window;
    private String name;
    private String path;

    public ButtonDropbox(MyWindow window, String name){
        super(name);
        this.window = window;
        this.name = name;
        this.path = window.getPath();

        addMyActionListener();
    }

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

            for (int i = 0; i < window.getList().size(); i++) {
                System.out.println("Tallennetaan");
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
     * Updates text to be shown in text area.
     *
     */
    public void updateTextField() {
        String temp = Main.startForShoppingList;

        if (!window.getList().isEmpty()) {

            for (int i = 0; i < window.getList().size(); i++) {
                temp += window.getList().get(i).toString()+"\n";
            }
        }

        window.getItems().setText(temp);
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

    public String getName(){
        return name;
    }
}
