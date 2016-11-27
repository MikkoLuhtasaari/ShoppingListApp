package fi.mi.luh;

/**
 * Created by M1k1tus on 14-Nov-16.
 * @author Mikko Luhtasaari
 * @version 1.0, 14 Nov 2016
 * @since 1.0
 * Shopping list must contain items. Class ListItem simulates these items
 * containing amount and name of the item.
 */
public class ListItem {

    /**
     * Contains name of the item.
     */
    private String name;

    /**
     * Contains the amount of these items.
     */
    private int amount;

    /**
     * Constructs ListItem.
     *
     * @param name sets the name of item
     * @param amount sets the amount of items
     */
    public ListItem(String name, int amount) {
        setName(name);
        setAmount(amount);
    }

    public String description(){
        return amount + " " + name;
    }

    // Getters and setters.

    /**
     * Sets the name.
     *
     * @param name sets the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the amount if amount if over zero.
     *
     * @param amount sets the amount
     */
    public void setAmount(int amount) {
        if (amount > 0) {
            this.amount = amount;
        }
    }

    /**
     * Returns amount.
     *
     * @return amount
     */
    public int getAmount() {
        return amount;
    }

    // Overridden methods.

    /**
     * Returns textual representation of ListItem object.
     *
     * @return Returns textual representation of ListItem object
     */
    @Override
    public String toString() {
        return "  "+getAmount()+" "+getName();
    }
}
