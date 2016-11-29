package fi.mi.luh;

/**
 * Created by M1k1tus on 2016-11-14.
 * @author Mikko Luhtasaari
 * @version 1.0, 14 Nov 2016
 * @since 1.0
 * MyList interface to be used in self-made linked list.
 */
public interface MyList<T>{
    /** Appends the specified element to the end of this list */
    void add(Object e);

    /** Removes all of the elements from this list **/
    void clear();

    /** Returns the element at the specified position in this list. */
    Object get(int index);

    /** Returns true if this list contains no elements. **/
    boolean isEmpty();

    /** Removes the element at the specified position in this list. Returns the removed element. */
    Object remove(int index);

    /** Removes the first occurrence of the specified element from this list, if it is present (return true) */
    boolean remove(Object o);

    /** Returns the number of elements in this list. */
    int size();
}
