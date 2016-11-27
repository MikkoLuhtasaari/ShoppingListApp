package fi.mi.luh;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/**
 * Created by Mikko Luhtsaari on 18-Nov-16.
 *  @author Mikko Luhtasaari
 *  @version 1.0, 18 Nov 2016
 *  @since 1.0
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


    public MyWindow(){
        shoppingList = new MyLinkedList<>();

        setSize(600,800);
        setLayout(new BorderLayout());

        makeButtonsAndContainers();

        add(buttonContainer, BorderLayout.PAGE_START);
        add(listContainer, BorderLayout.CENTER);



        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }





    private void makeButtonsAndContainers(){
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
                System.out.println("Open");
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

        //listContainer = new JPanel();
        //listContainer.setLayout(new GridLayout(0,1));
        //listContainer.add(new JButton("1 milk"));
        //listContainer.add(new JButton("2 carrot"));
    }


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

    private void printShoppingListContent(){
        if(shoppingList.size()>0) {
            System.out.println("Your Shopping List now:");
            for (int i = shoppingList.size()-1; i >= 0; i--) {
                System.out.println(shoppingList.get(i).toString());
            }
        }
    }

    private void updateTextField(){
        String temp = startForShoppingList;

        if(!shoppingList.isEmpty()){
            for (int i = 0; i < shoppingList.size(); i++) {
                temp += shoppingList.get(i).toString()+"\n";
            }
        }
        items.setText(temp);
    }

    private void tryToSave(){
        /*String[] temp = new String[shoppingList.size()];
        for (int i = 0; i < shoppingList.size(); i++) {
            temp[i] = shoppingList.get(i).toString();
        }*/
        String saveLocation = JOptionPane.showInputDialog("Please enter" +
                " filename");
        try{
            PrintWriter out = new PrintWriter(saveLocation+".txt");
            for (int i = 0; i < shoppingList.size(); i++) {
                System.out.println("Tallennetaan");
                out.println(shoppingList.get(i).toString());
            }
            out.close();
        }catch(IOException exp){
            exp.printStackTrace();
        }
    }
}
