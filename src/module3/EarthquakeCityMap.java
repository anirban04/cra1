package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;
import java.util.Map;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
    // The List you will populate with new SimplePointMarkers
    List<Marker> markers = new ArrayList<Marker>();

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    //List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	for (PointFeature f: earthquakes) {
	    		//PointFeature f = earthquakes.get(0);
	    		//System.out.println(f.getProperties());
	    		//Object magObj = f.getProperty("magnitude");
	    		//float mag = Float.parseFloat(magObj.toString());
	    		//Map<String, Object> properties = f.getProperties();
	    		markers.add(createMarker(f));
	    		// PointFeatures also have a getLocation method
	    	}
	    }
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
	    
	    //TODO: Add code here as appropriate
	    addMarkers();
	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// finish implementing and use this method, if it helps.
		return new SimplePointMarker(feature.getLocation(), feature.getProperties());
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}
	
	private void addMarkers() {
		map.addMarkers(markers);
		for (Marker mrk: markers) {
			SimplePointMarker sMrk = (SimplePointMarker)mrk;
    		Object magObj = sMrk.getProperty("magnitude");
    		float mag = Float.parseFloat(magObj.toString());
    		if(mag<4.0) {
    			sMrk.setColor(color(0, 0, 255));
    			sMrk.setStrokeColor(color(0, 0, 255));
    			sMrk.setRadius(5);	
    		}
    		else if(mag<4.9) {
    			sMrk.setColor(color(255, 255, 0));
    			sMrk.setStrokeColor(color(255, 255, 0));
    			sMrk.setRadius(10);
    		}
    		else {
    			sMrk.setColor(color(255, 0, 0));
    			sMrk.setStrokeColor(color(255, 0, 0));
    			sMrk.setRadius(15);
    		}
		}	
	}

	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{
		//map.draw();
		fill(250, 247, 240);
		rect(30, 40, 150, 160);
		fill(0, 0, 0);
		textSize(15);
		text("Earthquake Key", 42, 60);
		textSize(12);
		text("5.0+ Magnitude", 65, 100);
		text("4.0+ Magnitude", 65, 125);
		text("Below 4.0", 65, 150);
		
		fill(250, 0, 0);
		ellipse(45, 100, 15, 15);
		fill(255, 255, 0);
		ellipse(45, 125, 10, 10);
		fill(0, 0, 255);
		ellipse(45, 150, 5, 5);
	}
}
