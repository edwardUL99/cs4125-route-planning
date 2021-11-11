package ie.ul.routeplanning.routes.graph.creation;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import ie.ul.routeplanning.routes.RouteLeg;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.data.WaypointException;
import ie.ul.routeplanning.routes.data.WaypointSource;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.transport.TransportFactory;
import ie.ul.routeplanning.transport.TransportMethod;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class reads edges from a JSON file and constructs a graph from it
 */
public class JSONGraphBuilder implements GraphBuilder {
	/**
	 * The JSONFile containing the edges
	 */
	private final String jsonFile;
	/**
	 * The GSON object to read out edges file
	 */
	private final Gson gson;
	/**
	 * The JSONGraphBuilder requires waypoints to parse the vertices, so it uses a WaypointSource
	 */
	private final WaypointSource waypointSource;
	/**
	 * A map of transport method names to the corresponding instances
	 */
	private final Map<String, TransportMethod> transportMethods;

	/**
	 * Constructs a builder from the provided waypoint source
	 * @param jsonFile the json file containing the edges definitions
	 * @param waypointSource the waypoint source to read waypoints from
	 * @param transportMethods a map mapping name to transport methods. Expected to have all the supported transport methods in the JSON file
	 */
	public JSONGraphBuilder(String jsonFile, WaypointSource waypointSource, Map<String, TransportMethod> transportMethods) {
		this.jsonFile = jsonFile;
		this.gson = new Gson();
		this.waypointSource = waypointSource;
		this.transportMethods = transportMethods;
	}

	/**
	 * Construct an edge from start to end. It checks if a vertex has already been created for start and end using the
	 * createdVertices map. If not, it creates a vertex using the corresponding waypoint, else, it uses the existing vertec
	 * @param createdVertices the map of vertices creating for that waypoint ID
	 * @param waypoints the map of waypoints and their IDs
	 * @param jsonEdge the json parsed edge
	 * @return the constructed edge
	 */
	private RouteLeg constructEdge(Map<Long, Waypoint> createdVertices, Map<Long, Waypoint> waypoints, JSONEdge jsonEdge) {
		long start = jsonEdge.start, end = jsonEdge.end;

		Waypoint startPoint = createdVertices.get(start);

		if (startPoint == null) {
			startPoint = waypoints.get(start);
			createdVertices.put(start, startPoint);
		}

		Waypoint endPoint = createdVertices.get(end);

		if (endPoint == null) {
			endPoint = waypoints.get(end);
			createdVertices.put(end, endPoint);
		}

		TransportMethod transportMethod = transportMethods.get(jsonEdge.transportMethod);

		return new RouteLeg(startPoint, endPoint, transportMethod, jsonEdge.distance);
	}

	/**
	 * Reads the edges ensuring each edge has corresponding waypoints and then constructs an edge
	 * @param graph the graph to add new edges to
	 * @param edges the list of read edges
	 * @param waypoints the map of waypoints read from the waypoint source
	 * @throws BuilderException if the graph has inconsistencies (undefined waypoints etc.)
	 */
	private void buildGraph(Graph graph, List<JSONEdge> edges, Map<Long, Waypoint> waypoints) throws BuilderException {
		Map<Long, Waypoint> createdVertices = new HashMap<>();

		for (JSONEdge edge : edges) {
			long start = edge.start, end = edge.end;

			if (!waypoints.containsKey(start))
				throw new BuilderException("No Waypoint with ID: " + start + " exists");

			if (!waypoints.containsKey(end))
				throw new BuilderException("No Waypoint with ID: " + start + " exists");

			graph.addEdge(constructEdge(createdVertices, waypoints, edge), edge.isBidirectional());
		}
	}

	/**
	 * Builds and returns the constructed graph by using the waypoint source and edges JSON file
	 * @return the constructed graph
	 * @throws BuilderException if an error occurs building the graph
	 */
	@Override
	public Graph buildGraph() throws BuilderException {
		try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
			Map<Long, Waypoint> waypoints = waypointSource.getWaypoints().stream()
					.collect(Collectors.toMap(Waypoint::getId, Function.identity()));
			Type edgeListType = new TypeToken<ArrayList<JSONEdge>>(){}.getType();
			ArrayList<JSONEdge> edges = gson.fromJson(reader, edgeListType);

			Graph graph = new Graph();
			buildGraph(graph, edges, waypoints);

			return graph;
		} catch (WaypointException ex) {
			throw new BuilderException("Failed to build graph", ex);
		} catch (IOException ex) {
			throw new BuilderException("Failed to read edges JSON file: " + jsonFile, ex);
		} catch (JsonParseException ex) {
			throw new BuilderException("Failed to read JSON", ex);
		}
	}

	/**
	 * This class represents a JSONEdge in the JSONFile
	 */
	private static class JSONEdge {
		/**
		 * The ID of the start waypoint
		 */
		private final long start;
		/**
		 * The ID of the end waypoint
		 */
		private final long end;
		/**
		 * The transport method for this edge
		 */
		private final String transportMethod;
		/**
		 * The distance between start and end
		 */
		private final double distance;
		/**
		 * Indicates that the edge is bidirectional. The default is true
		 */
		private final Boolean bidirectional;

		/**
		 * Constructs a JSONEdge with the provided parameters
		 * @param start the start vertex waypoint ID
		 * @param end the end vertex waypoint ID
		 * @param transportMethod the transport method travelling this edge
		 * @param distance the distance between the start and end in km
		 * @param bidirectional true if the edge is bidirectional, false if it is only one way
		 */
		private JSONEdge(long start, long end, String transportMethod, double distance, Boolean bidirectional) {
			this.start = start;
			this.end = end;
			this.transportMethod = transportMethod;
			this.distance = distance;
			this.bidirectional = bidirectional == null || bidirectional;
		}

		/**
		 * Constructs a bidirectional JSONEdge with the provided parameters
		 * @param start the start vertex waypoint ID
		 * @param end the end vertex waypoint ID
		 * @param transportMethod the transport method travelling this edge
		 * @param distance the distance between the start and end in km
		 */
		private JSONEdge(long start, long end, String transportMethod, float distance) {
			this(start, end, transportMethod, distance, true);
		}

		/**
		 * This should be called rather than directly accessing the property
		 * @return true if bidirectional, false if not. If null, the default true is returned
		 */
		private boolean isBidirectional() {
			return bidirectional == null || bidirectional;
		}
	}
}
