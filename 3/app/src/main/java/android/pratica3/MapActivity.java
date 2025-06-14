package android.pratica3;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;
import java.util.List;

// Using onMapReadyCallBack to async map
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    // private LatLng DPI = new LatLng(-20.76498763191783, -42.86820330793967);
    // private LatLng VICOSA = new LatLng(-20.75341788007787, -42.87741937820796);
    // private LatLng PONTENOVA = new LatLng(-20.407935319042718, -42.89542055880212);

    private LatLng LOCAL;

    private LatLng CURRENT_POSITION;
    private Marker currentMarker = null;

    private SupportMapFragment supMap;
    private GoogleMap map;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Load map
        supMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (supMap != null) {
            supMap.getMapAsync(this);
        }

        // Location Permission
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
    }

    // Treat permission result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //getLastLocation();
            } else {
                Toast.makeText(this,
                        "Permissão de localização negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // When map is ready, load local defined before
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // Map is ready
        Intent it = getIntent();
        Bundle params = it.getExtras();

        assert params != null;
        int local_option = params.getInt("local");

        switch (local_option) {
            case 0:
                // Ponte Nova
                centerLocal("PONTENOVA");
                break;

            case 1:
                // Vicosa
                centerLocal("VICOSA");
                break;

            case 2:
                // DPI
                centerLocal("DPI");
                break;
        }
    }

    public void onClickPonteNova(View v) {
        centerLocal("PONTENOVA");
    }

    public void onClickVicosa(View v) {
        centerLocal("VICOSA");
    }

    public void onClickDPI(View v) {
        centerLocal("DPI");
    }

    private LatLng searchLocalInDatabase(String localDescription) {
        Cursor c = BancoDadosSingleton.getInstance().buscar("Location", new String[]{"latitude", "longitude"}, "descricao = '" + localDescription + "'", "");

        Log.i("BANCO", "tentou buscar o trem");
        LatLng local = new LatLng(0,0);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                int latitudeIndex = c.getColumnIndex("latitude");
                int longitudeIndex = c.getColumnIndex("longitude");

                float latitude = c.getFloat(latitudeIndex);
                float longitude = c.getFloat(longitudeIndex);
                local = new LatLng(latitude, longitude);
            }
            c.close();
        }

        return local;
    }

    // Function to center a LOCAL and add a marker searching in database to coordinates
    private void centerLocal(String localDescription) {
        LOCAL = searchLocalInDatabase(localDescription);

        CameraUpdate camera = CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(LOCAL)
                        .zoom(17)
                        .build());
        map.animateCamera(camera);

        map.addMarker(new MarkerOptions()
                .position(LOCAL)
                .title(localDescription));
    }

    // Function to read current position each time button is clicked
    public void onClickCurrentPosition(View v) {
        // Return if permission was denied
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão de localização não concedida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current position
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    CURRENT_POSITION = new LatLng(location.getLatitude(), location.getLongitude());

                    LatLng VICOSA = searchLocalInDatabase("VICOSA");

                    // Calculate and show distance in meters of house and current position
                    DecimalFormat df = new DecimalFormat("#.00");
                    String distance = df.format(calculateDistanceInMeters(CURRENT_POSITION, VICOSA));

                    Toast.makeText(getBaseContext(), "Você está a " + distance + " m de casa", Toast.LENGTH_SHORT).show();

                    // Remove old marker
                    if (currentMarker != null) {
                        currentMarker.remove();
                    }

                    // Update zoom and center position
                    CameraUpdate c = CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(CURRENT_POSITION)
                                    .zoom(17)
                                    .build());
                    map.animateCamera(c);

                    // Add blue marker
                    currentMarker = map.addMarker(new MarkerOptions()
                            .position(CURRENT_POSITION)
                            .title("Minha localização atual")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                } else {
                    Toast.makeText(getBaseContext(), "Localização não disponível", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Function to calculate distance in meters of two points
    public float calculateDistanceInMeters(LatLng ponto1, LatLng ponto2) {
        Location loc1 = new Location("");
        loc1.setLatitude(ponto1.latitude);
        loc1.setLongitude(ponto1.longitude);

        Location loc2 = new Location("");
        loc2.setLatitude(ponto2.latitude);
        loc2.setLongitude(ponto2.longitude);

        return loc1.distanceTo(loc2);
    }

}