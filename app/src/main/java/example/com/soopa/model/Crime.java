package example.com.soopa.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Crime {
    private final LatLng location;
    private final String crimeType;
    private final int crimeDuration;
    private final ArrayList<VantagePoint> vantagePoints;
    private final int severity;

    // Field names used in server requests
    public static final String SERVER_LAT = "lat";
    public static final String SERVER_LNG = "lng";
    public static final String SERVER_CRIME_TYPE = "type";
    public static final String SERVER_CRIME_DURATION = "crime_duration";
    public static final String SERVER_VANTAGE_POINTS = "vantage_points";
    public static final String SERVER_SEVERITY = "severity";

    public Crime(LatLng location, String crimeType, int crimeDuration, ArrayList<VantagePoint> vantagePoints, int severity) {
        this.location = location;
        this.crimeType = crimeType;
        this.crimeDuration = crimeDuration;
        this.vantagePoints = vantagePoints;
        this.severity = severity;
    }

    /**
     * Constructor to create object directly from JSONObject
     * @param jsonCrime JSONObject containing crime
     * @throws JSONException When can't parse JSONObject
     */
    public Crime(JSONObject jsonCrime) throws JSONException {
        JSONArray jsonVantagePoints = jsonCrime.getJSONArray(SERVER_VANTAGE_POINTS);
        ArrayList<VantagePoint> vantagePoints = new ArrayList<>();
        for (int j = 0; j < jsonVantagePoints.length(); j++) {
            JSONObject jsonVP = jsonVantagePoints.getJSONObject(j);
            vantagePoints.add(new VantagePoint(new LatLng(jsonVP.getDouble(VantagePoint.SERVER_LAT), jsonVP.getDouble(VantagePoint.SERVER_LNG)), jsonVP.getDouble(VantagePoint.SERVER_HEIGHT)));
        }
        this.location = new LatLng(jsonCrime.getDouble(SERVER_LAT), jsonCrime.getDouble(Crime.SERVER_LNG));
        this.crimeType = jsonCrime.getString(Crime.SERVER_CRIME_TYPE);
        this.crimeDuration = jsonCrime.getInt(Crime.SERVER_CRIME_DURATION);
        this.vantagePoints = vantagePoints;
        this.severity = jsonCrime.getInt(Crime.SERVER_SEVERITY);
    }

    public LatLng getLocation() {
        return location;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public int getCrimeDuration() {
        return crimeDuration;
    }

    public ArrayList<VantagePoint> getVantagePoints() {
        return vantagePoints;
    }

    public int getSeverity() {
        return severity;
    }

    /**
     * Creates a list of Crime objects directly from JSONArray
     * @param jsonArray JSONArray of Crime objects
     * @return a list of crimes
     * @throws JSONException when can't parse JSONArray
     */
    public static ArrayList<Crime> fromJSONArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Crime> crimes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonCrime = jsonArray.getJSONObject(i);
            crimes.add(new Crime(jsonCrime));
        }
        return crimes;
    }
}
