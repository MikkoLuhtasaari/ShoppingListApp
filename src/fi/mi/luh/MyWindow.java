package fi.mi.luh;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import fi.mi.luh.Buttons.*;

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

    private ButtonAddItem addItem;
    private ButtonNewList newList;
    private ButtonOpen open;
    private ButtonSave save;
    private ButtonCombine combine;
    private ButtonDropbox dropbox;

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

        addItem = new ButtonAddItem(shoppingList, this, "Add Items");
        newList = new ButtonNewList(shoppingList, this, "New List");
        open = new ButtonOpen(shoppingList, this, "Open");
        save = new ButtonSave(shoppingList, this, "Save");
        combine = new ButtonCombine(shoppingList, this, "Combine");
        dropbox = new ButtonDropbox(shoppingList, this, "Dropbox");

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

    public MyLinkedList getList(){
        return shoppingList;
    }

    public TextArea getItems(){
        return items;
    }

    public String getPath(){
        return path;
    }
}
