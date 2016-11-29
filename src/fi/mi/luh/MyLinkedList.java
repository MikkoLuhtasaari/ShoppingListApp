package fi.mi.luh;

/**
 * Self-made linked list. Each element contains a reference
 * to next element if such element exists.
 *
 * @author Mikko Luhtasaari
 * @version 1.0, 14 Nov 2016
 * @since 1.0
 */
public class MyLinkedList <T> implements MyList {

    /**
     * The starting point of linked list.
     */
    private Element<T> first;

    /**
     * Size of the linked list.
     */
    private int size;

    /**
     * Creates object MyLinkedList.
     */
    public MyLinkedList() {
        size = 0;
        first = null;
    }

    /**
     * Adds object.
     *
     * @see MyList#add(Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void add(Object e) {
        if (first == null) {
            first = new Element<>();
            first.setNext(null);
            first.setContent(e);
        } else {
            Element newElement = new Element<>();
            newElement.setContent(e);
            newElement.setNext(first);
            first = newElement;
        }

        size++;
    }

    /**
     * Clears List.
     *
     * @see MyList#clear()
     */
    @Override
    public void clear() {

        size = 0;

        if (first != null) {
            first = null;
        }
    }

    /**
     * Returns object from selected index.
     *
     * @see MyList#get(int)
     */
    @Override
    public Object get(int index) {

        Element temp = first;

        if (index < 0) {
            return null;
        } else{
            for (int i = 0; i < index ; i++) {
                temp = temp.getNext();
            }

            return temp.getContent();
        }
    }

    /**
     * Checks if the list is empty.
     *
     * @see MyList#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Removes object from given index and returns it.
     *
     * @see MyList#remove(int)
     */
    @Override
    public Object remove(int index) {

        Element temp = first;
        Object toBeReturned;

        if (index < 0 || index >= size) {
            return null;
        } else{

            size--;

            if (index != 0) {

                for (int i = 0; i < index - 1; i++) {
                    temp = temp.getNext();
                }

                toBeReturned = temp;
                temp.setNext(temp.getNext().getNext());
            } else{
                first = temp.getNext();
                toBeReturned = temp;
            }
        }

        return toBeReturned;
    }

    /**
     * Removes matching object if such is found.
     *
     * @see MyList#remove(Object)
     */
    @Override
    public boolean remove(Object o) {
        Element temp = first;

        /**
         * Sets values wasFound and index to represent false values.
         */
        boolean wasFound = false;
        int index = -1;

        for (int i = 0; i < size; i++) {

            if (temp.getContent().equals(o)) {
                index = i;
                wasFound = true;
            }

            temp = temp.getNext();
        }

        if (wasFound) {
            remove(index);
        }

        return wasFound;
    }

    /**
     * Returns size of the list.
     *
     * @see MyList#size()
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Creates new anonymous element to be stored in a list.
     *
     * @param <T> Takes in any object type value
     */
    private class Element<T> {

        /**
         * Contains the pointer to following Element.
         */
        private Element<T> next;

        /**
         * Is what element holds within.
         */
        private T content;

        /**
         * Sets elements next value.
         *
         * @param a element to be set as next
         */
        public void setNext(Element a) {
            next = a;
        }

        /**
         * Gets whatever element follows this one.
         *
         * @return returns the element to follow this one
         */
        public Element getNext() {
            return next;
        }

        /**
         * Sets the elements content.
         *
         * @param t object to be set into Element
         */
        @SuppressWarnings("unchecked")
        public void setContent(Object t) {
            this.content = (T)t;
        }

        /**
         * Returns contents of the Element.
         *
         * @return return contents of the element
         */
        public T getContent() {
            return content;
        }
    }
}
