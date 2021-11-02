package ie.ul.routeplanning.routes.data;

import java.io.IOException;

/**
 * This class represents an exception thrown if an error occurs reading the waypoints. Since it is related to the operation
 * of reading waypoints, it extends an IOException
 */
public class WaypointException extends IOException {
	/**
	 * Constructs the exception with the provided message
	 * @param message the message for the exception
	 */
	public WaypointException(String message) {
		super(message);
	}

	/**
	 * Constructs the exception with the provided message and causing throwable
	 * @param message the message for the exception
	 * @param throwable the cause of the exception
	 */
	public WaypointException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
