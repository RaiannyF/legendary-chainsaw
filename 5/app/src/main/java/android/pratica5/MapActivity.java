package android.pratica5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private SupportMapFragment supMap;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Action bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Load map
        supMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (supMap != null) {
            supMap.getMapAsync(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Load action bar options
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.map_type_normal) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            return true;
        } else if (id == R.id.map_type_hybrid) {
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            return true;
        } else if (id == R.id.action_option1) {
            finish();
            return true;
        } else if (id == R.id.action_option2) {
            startActivity(new Intent(this, ManagementActivity.class));
            return true;
        } else if (id == R.id.action_option3) {
            startActivity(new Intent(this, ReportActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("Range")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Load current position to center it
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("latitude", 0);
        double lon = intent.getDoubleExtra("longitude", 0);

        if (lat != 0 && lon != 0) {
            LatLng currentPos = new LatLng(lat, lon);

            map.moveCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition.Builder()
                            .target(currentPos)
                            .zoom(17)
                            .build()));

            map.addMarker(new MarkerOptions()
                    .position(currentPos)
                    .title("Minha posição atual"));
        }

        // Get all checkins on database
        Cursor cursor = DatabaseSingleton.getInstance().buscar(
                "Checkin",
                new String[]{"Local", "cat", "latitude", "longitude", "qtdVisitas"},
                "",
                ""
        );

        // Add a marker to each checkin
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String nomeLocal = cursor.getString(cursor.getColumnIndex("Local"));
                @SuppressLint("Range") int idCategoria = cursor.getInt(cursor.getColumnIndex("cat"));
                @SuppressLint("Range") String latitude = cursor.getString(cursor.getColumnIndex("latitude"));
                @SuppressLint("Range") String longitude = cursor.getString(cursor.getColumnIndex("longitude"));
                @SuppressLint("Range") int qtdVisitas = cursor.getInt(cursor.getColumnIndex("qtdVisitas"));

                // Get category name
                Cursor catCursor = DatabaseSingleton.getInstance().buscar(
                        "Categoria",
                        new String[]{"nome"},
                        "idCategoria = " + idCategoria,
                        ""
                );

                String nomeCategoria = "Desconhecida";
                if (catCursor.moveToFirst()) {
                    nomeCategoria = catCursor.getString(catCursor.getColumnIndex("nome"));
                }
                catCursor.close();

                // Add marker
                LatLng pos = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                map.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(nomeLocal)
                        .snippet("Categoria: " + nomeCategoria + " — Visitas: " + qtdVisitas)
                );

            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
