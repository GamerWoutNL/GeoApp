package com.example.geoapp.view;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geoapp.R;
import com.example.geoapp.control.DataParser;
import com.example.geoapp.control.SharedPrefs;
import com.example.geoapp.model.TrainingSession;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.yashovardhan99.timeit.Stopwatch;
import com.yashovardhan99.timeit.Timer;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String MY_API_KEY = "725b85df-2ff8-4de2-bebc-2ab56b12b701";

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private GeofencingClient geofencingClient;
    private ImageView ivWorkoutButton;
    private boolean workoutState;
    private Stopwatch stopwatch;
    private Location beginLocation;
    private float distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //SharedPrefs.deleteObject("MY_PREFS", "sessions");
        // RUN THIS TO DELETE THE SESSIONS

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        MapView mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onStart();
        mapView.getMapAsync(this);

        this.workoutState = false;
        this.ivWorkoutButton = findViewById(R.id.buttonWorkoutDone);

        changeWorkoutButton();

        ivWorkoutButton.setOnClickListener((v) -> {
            if (workoutState) {
                Toast.makeText(v.getContext(), "TRAINING KLAAR", Toast.LENGTH_SHORT).show();
                stopwatch.pause();

                long elapsedTimeMillis = stopwatch.getElapsedTime();
                long timeStarted = stopwatch.getStart();

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(c);

                fusedLocationClient.getLastLocation().addOnSuccessListener(this, (location) -> {
                    distance = location.distanceTo(beginLocation);
                });

                TrainingSession trainingSession = new TrainingSession(getTimeFromMillies(timeStarted), formattedDate, distance, elapsedTimeMillis);

                List<TrainingSession> sessions = SharedPrefs.getObject("MY_PREFS", "sessions");
                if (sessions == null) {
                    sessions = new ArrayList<>();
                }
                sessions.add(trainingSession);
                SharedPrefs.addObject("MY_PREFS", "sessions", sessions);
            } else {
                // stopwatch test
                setStopwatch(stopwatchInit());
                stopwatch.start();

                fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> beginLocation = location);
            }
            workoutState = !workoutState;
            changeWorkoutButton();
        });
    }

    public Stopwatch stopwatchInit(){
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.setTextView(findViewById(R.id.timerTextView));
        return stopwatch;
    }

    public void setStopwatch(Stopwatch stopwatch){
        this.stopwatch = stopwatch;
    }

    public void changeWorkoutButton() {
        if (Resources.getSystem().getConfiguration().locale.getLanguage().equals("nl")) {
            // Dutch language
            if (workoutState) {
                ivWorkoutButton.setImageResource(R.drawable.button_training_klaar);
            } else {
                ivWorkoutButton.setImageResource(R.drawable.button_begin_training);
            }
        } else {
            // English language
            if (workoutState) {
                ivWorkoutButton.setImageResource(R.drawable.button_workout_done);
            } else {
                ivWorkoutButton.setImageResource(R.drawable.button_begin_workout);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGPSClients();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGPSClients();
            mMap.setMyLocationEnabled(true);
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12));
            }
        });
    }


    // Here is were all the magic happens, balleke
    @Override
    public void onMapClick(final LatLng latLng) {
        // Ekkes de map schoonmaken
        this.mMap.clear();

        // Add a marker of the destination
        mMap.addMarker(new MarkerOptions().position(latLng).title("Destination"));

        // Get the current location and request the route, dikzak
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Jouw dikke moeder, Lars
                String url = getUrl(new LatLng(location.getLatitude(), location.getLongitude()), latLng, "walking");

                // Call this to request the route, josefien
                new FetchUrl().execute(url);
            }
        });

        // Move that nigga camera, skltj
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
    }

    protected synchronized void buildGPSClients() {
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        this.geofencingClient = LocationServices.getGeofencingClient(this);
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String trafficMode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + trafficMode + "&key=" + MY_API_KEY;
        return "http://145.48.6.80:3000/directions" + "?" + parameters;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
            e.printStackTrace();
            System.out.println();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (fusedLocationClient == null) {
                            buildGPSClients();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                break;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }


    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    private String getTimeFromMillies(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);

        return hours + ":" + minutes + ":" + seconds;
    }
}

