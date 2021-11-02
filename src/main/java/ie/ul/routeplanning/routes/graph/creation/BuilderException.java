package ie.ul.routeplanning.routes.graph.creation;

import java.io.IOException;

/**
 * This class represents an exception that can occur when building the graph
 */
public class BuilderException extends IOException {
	/**
	 * Constructs the exception with the provided message
	 * @param message the message for the exception
	 */
	public BuilderException(String message) {
		super(message);
	}

	/**
	 * Constructs the exception with the provided message and causing throwable
	 * @param message the message for the exception
	 * @param throwable the cause of the exception
	 */
	public BuilderException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
