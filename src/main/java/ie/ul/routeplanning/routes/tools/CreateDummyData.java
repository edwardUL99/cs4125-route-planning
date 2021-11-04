package ie.ul.routeplanning.routes.tools;

import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.graph.GraphUtils;

import java.io.Reader;
import java.io.FileReader;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.concurrent.ThreadLocalRandom;
import java.io.File;

/**
 * Class used to create fake graph data
 * Data is stored in JSON files
 * Waypoint file format of waypoint id 0: 
 * ex. "0":{"name":"iedwcberg","lat":-25.214279029155648,"long":158.51958062559004}
 * 
 * WeightFunction file format of waypoint id 0:
 * ex. "0":{"distance":16740,"start":168,"end":21,"transportMethod":"Car"}
 */
public class CreateDummyData {
   private final static float LAT_MAX = 90;
   private final static float LAT_MIN = -90;
   private final static float LONG_MAX = 180;
   private final static float LONG_MIN = -180;

   private final static int NUM_OF_WAYPOINTS = 1000;
   private final static String FOLDER_DIR = "exampleGraphs";

   public static void main(String[] args) {
      createDummyWaypointData("waypoints", FOLDER_DIR, NUM_OF_WAYPOINTS);
      createDummyEdgeData( "edges", FOLDER_DIR + "/waypoints.json", NUM_OF_WAYPOINTS*10);
   }

   // creates a random coordinate for latitude or longitude
   private static double randomCoordinate(boolean latitude) {
      double rangeMin = (latitude) ? LAT_MIN:LONG_MIN;
      double rangeMax = (latitude) ? LAT_MAX:LONG_MAX;

      return rangeMin + (rangeMax - rangeMin) * new Random().nextDouble();
   }

   /**
    * Creates a JSON file of fake waypoints
    *@param fileName the name of the json file to be made. Will be appended with ".json"
    *@param folderDir Directory of where the JSON file will be placed
    *@param numOfWayPoints The number of waypoints to be created 
     */
   public static void createDummyWaypointData(String fileName, String folderDir, int numOfWayPoints) {
      //Creating a JSONObject object
      JsonArray jsonArray = new JsonArray();
      for(int i = 0; i<numOfWayPoints; i++){
         JsonObject list = new JsonObject();
         //Inserting key-value pairs into the json object
         list.addProperty("id", i);
         list.addProperty("name", createRandomCityName());
         list.addProperty("lat", randomCoordinate(true));
         list.addProperty("long", randomCoordinate(false));

         jsonArray.add(list);
      }
      try {
         File directory = new File(FOLDER_DIR);
         if (!directory.exists()){
            directory.mkdir();  
         }
         FileWriter file = new FileWriter(folderDir + "/" + fileName + ".json");
         file.write(jsonArray.toString());
         file.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
      //System.out.println("JSON file created: "+ fileName + ".json");
   }
   
   /**
    * Creates a JSON file of fake edges given a waypoint JSON file. A way point JSON file can be made using the createDummyWaypointData Method
    *@param fileName the name of the json file to be made. Will be appended with ".json"
    *@param waypointFileDir Directory of where the waypoint file is placed
    *@param numOfEdges The number of edges to be created 
     */
   public static void createDummyEdgeData(String fileName, String waypointFileDir, int numOfEdges){
      try{
         File directory = new File(FOLDER_DIR);
         if (!directory.exists()){
            directory.mkdir();  
         }
         Reader reader = new FileReader(waypointFileDir);
         JsonArray jsonWaypointArray = (JsonArray) JsonParser.parseReader(reader);
         //Create Root JSONObject 
         JsonArray jsonEdgesRoot = new JsonArray();          
         for(int i = 0; i < numOfEdges; i++){
            //Creates an array from 0-999 and shuffles it. First two ints of this array will be taken
            int[] a = IntStream.range(0,1000).toArray(); 
            shuffleArray(a);
            //Calculate distance between them
            JsonObject city1 = (JsonObject)jsonWaypointArray.get(a[0]);
            JsonObject city2 = (JsonObject)jsonWaypointArray.get(a[1]);
            double city1Lat = city1.get("lat").getAsDouble();
            double city1Lon = city1.get("long").getAsDouble();
            double city2Lat = city2.get("lat").getAsDouble();
            double city2Lon = city2.get("long").getAsDouble();

            int distance = (int)calculateDistance(city1Lat, city2Lat, city1Lon, city2Lon);
            
            
            //Create each edge 
            JsonObject edge = new JsonObject();
            edge.addProperty("id", i);
            edge.addProperty("start", a[0]);
            edge.addProperty("end", a[1]);
            edge.addProperty("transportMethod", "Car"); 
            edge.addProperty("distance", distance);

            //Add edge to Root JSONArray
            jsonEdgesRoot.add(edge);
         }

         //Create file
         FileWriter file = new FileWriter(FOLDER_DIR + "/" + fileName + ".json");
         file.write(jsonEdgesRoot.toString());
         file.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
      
   }

   /**
    * Generates a random five char string and adds a random city suffix to it.
    * @return Random string with city like suffixes
    */
   private static String createRandomCityName(){
      StringBuilder cityName = new StringBuilder();
      Random r = new Random(); 

      for(int i = 0; i < 5; i++){
         cityName.append((char)(r.nextInt(26) + 'a'));
      }

      int extraMod = r.nextInt(5);
      switch(extraMod){
         case 0: cityName.append("ville");
         break;
         case 1: cityName.append(" Town");
         break;
         case 2: cityName.append(" City");
         break;
         case 3: cityName.append("berg");
         break;
      }
      return cityName.toString();
   }
   /**
    * Calculate the distance between 2 points (ignoring altitude differences)
    * @return The distance between the two points in kilometers
    */
   private static double calculateDistance(double lat1, double lat2, double lon1, double lon2) {
      return GraphUtils.kilometreDistance(new Waypoint(0L, "temp", lat1, lon1),
              new Waypoint(0L, "temp", lat2, lon2));
   }

  // Implementing Fisher–Yates shuffle
   private static void shuffleArray(int[] ar)
      {
      // If running on Java 6 or older, use `new Random()` on RHS here
      Random rnd = ThreadLocalRandom.current();
      for (int i = ar.length - 1; i > 0; i--)
      {
         int index = rnd.nextInt(i + 1);
         // Simple swap
         int a = ar[index];
         ar[index] = ar[i];
         ar[i] = a;
      }
   }
}