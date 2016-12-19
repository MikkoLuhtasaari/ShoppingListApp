package fi.mi.luh;

import java.util.Scanner;

/**
 * Uses command line interface to access shopping list. Doesn't contain
 * possibility to save or load lists. List items can be given one by one
 * or as one line. User can exit the app by typing "exit".
 *
 * @author Mikko Luhtasaari
 * @version 1.0, 14 Nov 2016
 * @since 1.0
 */
public class CLI {

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
            String[] temp2 = temporary.split(" ");
            insertItem(temp2[1], Integer.parseInt(temp2[0]));
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
