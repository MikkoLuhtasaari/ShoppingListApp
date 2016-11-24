package fi.mi.luh;

import java.util.Scanner;

/**
 * @author Mikko Luhtasaari
 * @version 1.0, 14 Nov 2016
 * @since 1.0
 * Created by M1k1tus on 14-Nov-16.
 * Uses command line interface to access shopping list. Doesn't contain
 * possibility to save or load lists. List items can be given one by one
 * or as one line. User can exit the app by typing "exit".
 */
public class CLI implements Runnable {

    /**
     * Scanner for reading user input from command prompt.
     */
    private Scanner reader;

    /**
     * Linked list containing given items.
     */
    private MyLinkedList <ListItem> shoppingList;

    /**
     * Initialize String which stores user input.
     *
     * before parsing it.
     */
    private String input = "";

    /**
     * Constructs the CLI.
     */
    public CLI() {
        reader = new Scanner(System.in);
        shoppingList = new MyLinkedList<>();
        System.out.println("SHOPPING LIST");
        System.out.println("Tampere University of Applied Sciences");
        System.out.println("Give shopping list (example: 1" +
                " milk;2 tomato;3 carrot;)");
    }

    /**
     * Runs the app.
     */
    @Override
    public void run() {
        while (!input.equalsIgnoreCase("exit")) {
            input = reader.nextLine();

            if (!input.equalsIgnoreCase("exit")) {
                parseInput(input);
                printShoppingListContent();
                System.out.println("");
                System.out.println("Give shopping list (example: 1 " +
                        "milk;2 tomato;3 carrot;)");
            }
        }
    }

    /**
     * Modifies the user input to right format.
     *
     * @param input user input.
     */
    private void parseInput(String input) {
        String[] temp = input.split(";");

        for (String temporary : temp) {
            insertItems(temporary);
        }
    }

    /**
     * Creates ListItems from input and adds them to shoppingList.
     *
     * @param line modified user input.
     */
    private void insertItems(String line) {
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
     * Prints the list in correct format.
     */
    private void printShoppingListContent() {
        if (shoppingList.size() > 0) {
            System.out.println("Your Shopping List now:");

            for (int i = shoppingList.size()-1; i >= 0; i--) {
                System.out.println(shoppingList.get(i).toString());
            }
        }
    }
}
