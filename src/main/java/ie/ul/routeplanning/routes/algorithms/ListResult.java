package ie.ul.routeplanning.routes.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A result that contains a list of items
 * @param <T> the type the result holds
 */
public class ListResult<T> implements Result<T> {
	/**
	 * The list of items behind the result
	 */
	private final List<T> items;
	/**
	 * The position in the list
	 */
	private int position;

	/**
	 * Construct the ListResult from the provided routes
	 * @param items the list of routes to initialise the result with
	 */
	public ListResult(Collection<T> items) {
		this.items = new ArrayList<>(items);
	}

	/**
	 * Constructs a default ListResult
	 */
	public ListResult() {
		this(new ArrayList<>());
	}

	/**
	 * Collect all the remaining elements in this result into a single list.
	 * I.e. if 3 items have already been taken using next(), the elements returned should only be the remaining elements,
	 * excluding those 3. If no elements are available, this returns an empty list
	 * This operation should end in the result having no more elements.
	 * @return the list of all elements in the result.
	 */
	@Override
	public List<T> collect() {
		List<T> items = new ArrayList<>(this.items);
		this.position = items.size();

		return items;
	}

	/**
	 * Get the next element in the result
	 *
	 * @return the next element of the result, null if there are no more elements
	 */
	@Override
	public T next() {
		return (hasNext()) ? items.get(position++):null;
	}

	/**
	 * Determines if there are items left in this result
	 *
	 * @return true if there are items left, false if not
	 */
	@Override
	public boolean hasNext() {
		return position < items.size();
	}

	/**
	 * Add an item to this result
	 *
	 * @param item the item to add to the result
	 */
	@Override
	public void addItem(T item) {
		items.add(item);
	}
}
