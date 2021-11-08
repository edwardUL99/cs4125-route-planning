package ie.ul.routeplanning.routes.algorithms;

import java.util.List;

/**
 * The result interface represents the result of an algorithm operation
 * @param <T> the type of the items contained within this result
 */
public interface Result<T> {
    /**
     * Collect all the remaining elements in this result into a single list.
     * I.e. if 3 items have already been taken using next(), the elements returned should only be the remaining elements,
     * excluding those 3. If no elements are available, this returns an empty list
     * This operation should end in the result having no more elements.
     * @return the list of all elements in the result.
     */
    List<T> collect();

    /**
     * Get the next element in the result
     * @return the next element of the result, null if there are no more elements
     */
    T next();

    /**
     * Determines if there are items left in this result
     * @return true if there are items left, false if not
     */
    boolean hasNext();

    /**
     * Add an item to this result
     * @param item the item to add to the result
     */
    void addItem(T item);
}
