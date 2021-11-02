package ie.ul.routeplanning.constants;

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
}
