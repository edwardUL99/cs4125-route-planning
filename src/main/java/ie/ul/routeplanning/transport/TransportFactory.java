package ie.ul.routeplanning.transport;

import ie.ul.routeplanning.constants.Constant;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class provides a Factory for generating TransportMethods
 */
public final class TransportFactory {
	/**
	 * An enumeration for transportation methods
	 */
	public enum TransportMethods {
		/**
		 * A transport method for a car
		 */
		CAR("Car"),
		/**
		 * A transport method for a bus
		 */
		BUS("Bus"),
		/**
		 * A transport method for a train
		 */
		TRAIN("Train"),
		/**
		 * A transport method for a plane
		 */
		PLANE("Plane"),
		/**
		 * A transport method for a ferry
		 */
		FERRY("Ferry");

		/**
		 * The label provided in the JSON files
		 */
		public final String label;

		/**
		 * Construct a enum value from the provided label
		 * @param label the label for the transport method located in the JSON files
		 */
		TransportMethods(String label) {
			this.label = label;
		}

		/**
		 * Retrieve the transport method for the provided string
		 * @param transportMethod the transport method label
		 * @return the appropriate enum value
		 * @throws IllegalArgumentException if transport method is not found in the enum
		 */
		public static TransportMethods valueOfLabel(String transportMethod) {
			final String transportMethodCapitalised = Constant.capitalise(transportMethod.toLowerCase());
			Optional<TransportMethods> transportMethods = Arrays.stream(values())
					.filter(v -> v.label.equals(transportMethodCapitalised))
					.findFirst();

			return transportMethods.orElseThrow(() -> new IllegalArgumentException("The transport method " + transportMethod +
					" is not a valid enum value"));
		}
	}

	/**
	 * The map of implementations
	 */
	private static final Map<TransportMethods, Class<? extends TransportMethod>> TRANSPORT_METHODS = new HashMap<>();

	static {
		TRANSPORT_METHODS.put(TransportMethods.BUS, BusTransportMethod.class);
		TRANSPORT_METHODS.put(TransportMethods.CAR, CarTransportMethod.class);
		TRANSPORT_METHODS.put(TransportMethods.TRAIN, TrainTransportMethod.class);
		TRANSPORT_METHODS.put(TransportMethods.PLANE, PlaneTransportMethod.class);
		TRANSPORT_METHODS.put(TransportMethods.FERRY, BusTransportMethod.class);
	}

	/**
	 * Retrieve the corresponding transport method implementation for the provided transport methods enum value
	 * @param transportMethods the transport methods enum value
	 * @return the implementation for the provided enum value
	 * @throws IllegalArgumentException if the implementation for the provided transport method does not have a constructor
	 * that takes a long for id and string for the name of the method or fails to construct the transport method
	 */
	public static TransportMethod getTransportMethod(TransportMethods transportMethods) {
		Class<? extends TransportMethod> clz = TRANSPORT_METHODS.get(transportMethods);
		try {
			return clz.getDeclaredConstructor(Long.class, String.class).newInstance(null, transportMethods.label);
		} catch (NoSuchMethodException ex) {
			throw new IllegalStateException("The implementation for the Transport method " + transportMethods + " does not have" +
					" a constructor that takes Long ID and String name", ex);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
			throw new IllegalStateException("Failed to get the Transport method " + transportMethods, ex);
		}
	}
}
