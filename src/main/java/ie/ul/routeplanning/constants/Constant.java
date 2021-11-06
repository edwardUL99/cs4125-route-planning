package ie.ul.routeplanning.constants;

import java.util.HashSet;
import java.util.Set;

/**
 * This class will contain all constant values.
 */
public class Constant {
	/**
	 * Capitalises the first letter of the provided name
	 * @param name the name to capitalise
	 * @return the capitalised string
	 */
	public static String capitalise(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	/**
	 * Convert the provided iterable to a set
	 * @param iterable the iterable to convert
	 * @return the converted set
	 */
	public static <T> Set<T> iterableToSet(Iterable<T> iterable) {
		Set<T> result = new HashSet<>();
		iterable.forEach(result::add);

		return result;
	}
}
