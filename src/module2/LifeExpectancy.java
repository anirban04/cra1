package module2;
import java.util.*;
import java.util.Map;

import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

//@SuppressWarnings("serial")
public class LifeExpectancy extends PApplet {

	UnfoldingMap myMap;
	Map<String, Float> lifeExpByCountry;
	List<Feature> countries;
	List<Marker> countryMarker;
	
	public void setup(){
		
		size(800,600, OPENGL);
		myMap = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, myMap);
		
		countries = new ArrayList<Feature>();
		countryMarker = new ArrayList<Marker>();
		lifeExpByCountry = loadLifeExpFromCSV("../data/LifeExpectancyWorldBank.csv");
		countries = GeoJSONReader.loadData(this, "../data/countries.geo.json");
		countryMarker = MapUtils.createSimpleMarkers(countries);
		myMap.addMarkers(countryMarker);
		shadeCountries();
	}

	private void shadeCountries() {
		for(Marker mrk : countryMarker) {
			String countryId = mrk.getId();
			
			if(lifeExpByCountry.containsKey(countryId)) {
				Float lifExp = lifeExpByCountry.get(countryId);
				if(lifExp < 100) {
					int colorLevel = (int) map(lifExp, 45, 90, 10, 255);
					mrk.setColor(color(255 - colorLevel, 0, colorLevel));
				}
				else
					mrk.setColor(color(150, 150, 150));
			}
			else {
				mrk.setColor(color(150, 150, 150));
			}
		}
	}

	private HashMap<String, Float> loadLifeExpFromCSV(String filename) {
		HashMap<String, Float> lifeExpMap = new HashMap<String, Float>();
		String[] rows = loadStrings(filename);
		
		for (String row : rows) {
			String[] col = row.split(",");
			if (col[5].contains(".."))
				lifeExpMap.put(col[4], 1000.0f);
			else if(col[4].charAt(0) == ' ') {
				float value = Float.parseFloat(col[6]);
				lifeExpMap.put(col[5], value);
			}
			else {
				float value = Float.parseFloat(col[5]);
				lifeExpMap.put(col[4], value);
			}
		}
		return lifeExpMap;
	}
	/** Draw the Applet window.  */
	public void draw() {
		myMap.draw();
	}
}