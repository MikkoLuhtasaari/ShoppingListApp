package fi.mi.luh;

import fi.mi.luh.Buttons.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * Creates window to be shown.
 * Contains everything window related for now.
 *
 * @author Mikko Luhtasaari
 * @version 2.0, 06 Dec 2016
 * @since 1.0
 */
public class MyWindow extends JFrame {

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

        ButtonAddItem addItem = new ButtonAddItem(this, "Add Items");
        ButtonNewList newList = new ButtonNewList(this, "New List");
        ButtonOpen open = new ButtonOpen(this, "Open");
        ButtonSave save = new ButtonSave(this, "Save");
        ButtonCombine combine = new ButtonCombine(this, "Combine");
        ButtonDropbox dropbox = new ButtonDropbox(this, "Dropbox");

        buttonContainer = new JPanel();
        buttonContainer.add(addItem);
        //buttonContainer.add(newList);
        //buttonContainer.add(open);
        //buttonContainer.add(save);
        //buttonContainer.add(combine);
        //buttonContainer.add(dropbox);

        listContainer = new JPanel();
        listContainer.setBackground(new Color(145,145,145));
        listContainer.setLayout(new GridLayout(10,1));
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
     * Returns list containing list items.
     *
     * @return return MyLinkedList.
     */
    public MyLinkedList getList(){
        return shoppingList;
    }

    /**
     * Returns textarea containing shopping list.
     *
     * @return textarea containing shopping list.
     */
    public TextArea getItems(){
        return items;
    }

    /**
     * Returns path. Path knows from where
     * it's being run.
     *
     * @return path.
     */
    public String getPath(){
        return path;
    }

    /**
     * Returns listContainer
     *
     * @return listContainer
     */
    public JPanel getListContainer() {
        return listContainer;
    }
}
