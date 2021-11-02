package ie.ul.routeplanning.routes.data;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import ie.ul.routeplanning.routes.Waypoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a waypoint source that reads waypoints from a JSON file
 */
public class JSONWaypointSource implements WaypointSource {
	/**
	 * The GSON instance to read the json file
	 */
	private final Gson gson;
	/**
	 * The filename to the Waypoints JSON file
	 */
	private final String jsonFile;

	/**
	 * Construct the waypoint source with the provided json file
	 * @param jsonFile the file containing the JSON waypoint data
	 */
	public JSONWaypointSource(String jsonFile) {
		this.gson = new Gson();
		this.jsonFile = jsonFile;
	}

	/**
	 * Retrieve the waypoints from the JSON file and return the list of waypoints
	 *
	 * @return the list of retrieved way points. The return value shouldn't be null, but can be empty, if no waypoints
	 * are found
	 * @throws WaypointException if an error occurs reading the waypoints
	 */
	@Override
	public List<Waypoint> getWaypoints() throws WaypointException {
		try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
			Type waypointListType = new TypeToken<ArrayList<Waypoint>>(){}.getType();
			return gson.fromJson(reader, waypointListType);
		} catch (IOException ex) {
			throw new WaypointException("Failed to read waypoints from: " + jsonFile, ex);
		} catch (JsonParseException ex) {
			throw new WaypointException("Failed to read JSON", ex);
		}
	}
}
