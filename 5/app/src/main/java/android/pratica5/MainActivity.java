package android.pratica5;

import android.Manifest;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback  {

    FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    LatLng CURRENT_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Action bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Setup autocomplete and spinner
        setupEditTextLocalName();
        setupSpinnerCategory();

        // Asks for permission to access location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Get current location
        setupCurrentLocation();
    }

    // Treat permission result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //getLastLocation();
            } else {
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setupCurrentLocation() {
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

                    // Update current position on screen
                    TextView lat = (TextView) findViewById(R.id.txtLatitudeResult);
                    lat.setText(String.valueOf(CURRENT_POSITION.latitude));

                    TextView log = (TextView) findViewById(R.id.txtLongitudeResult);
                    log.setText(String.valueOf(CURRENT_POSITION.longitude));
                }
            }
        });
    }

    public void setupEditTextLocalName() {
        // Get all locals name from database
        Cursor c = DatabaseSingleton.getInstance().buscar("Checkin", new String[]{"Local"}, "", "");
        List<String> localList = new ArrayList<>();

        if (c != null) {
            try {
                int columnIndex = c.getColumnIndex("Local");
                if (columnIndex != -1 && c.moveToFirst()) {
                    do {
                        String local = c.getString(columnIndex);
                        if (local != null && !local.isEmpty()) {
                            localList.add(local);
                        }
                    } while (c.moveToNext());
                }
            } finally {
                c.close();
            }
        }

        // Veirify if list is not empty before set adapter
        if (!localList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.select_dialog_item,
                    localList
            );

            AutoCompleteTextView locals = findViewById(R.id.edtLocalName);
            if (locals != null) {
                locals.setAdapter(adapter);
            }
        }
    }

    public void setupSpinnerCategory() {
        // Get all categories from database
        Cursor c = DatabaseSingleton.getInstance().buscar("Categoria", new String[]{"nome"}, "", "idCategoria");
        List<String> categoryList = new ArrayList<>();

        if (c != null) {
            try {
                int columnIndex = c.getColumnIndex("nome");
                if (columnIndex != -1 && c.moveToFirst()) {
                    do {
                        String category = c.getString(columnIndex);
                        if (category != null && !category.isEmpty()) {
                            categoryList.add(category);
                        }
                    } while (c.moveToNext());
                }
            } finally {
                c.close();
            }
        }

        Spinner catogories = (Spinner) findViewById(R.id.spnLocalCategory);
        catogories.setOnItemSelectedListener(this);

        // Set adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                R.id.spinnerText,
                categoryList
        );
        catogories.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Load action bar options
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    // Treat selection of toolbar options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_option1) {
            if (CURRENT_POSITION != null) {
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("latitude", CURRENT_POSITION.latitude);
                intent.putExtra("longitude", CURRENT_POSITION.longitude);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Localização ainda não foi obtida. Aguarde o GPS.", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.action_option2) {
            startActivity(new Intent(this, ManagementActivity.class));
            return true;
        } else if (id == R.id.action_option3) {
            startActivity(new Intent(this, ReportActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onItemSelected(AdapterView parent, View v, int posicao, long id) {

    }
    public void onNothingSelected(AdapterView arg0) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    @SuppressLint("Range")
    public void onClickCheckIn(View v) {
        EditText edtLocal = findViewById(R.id.edtLocalName);
        Spinner spnCategoria = findViewById(R.id.spnLocalCategory);

        String nomeLocal = edtLocal.getText().toString().trim();
        int posCategoria = spnCategoria.getSelectedItemPosition();

        String nomeCategoria = spnCategoria.getSelectedItem().toString();

        // Get idCategoria by category's name
        Cursor categoriaCursor = DatabaseSingleton.getInstance().buscar(
                "Categoria",
                new String[]{"idCategoria"},
                "nome = '" + nomeCategoria + "'",
                ""
        );

        int idCategoria = -1;
        if (categoriaCursor.moveToFirst()) {
            idCategoria = categoriaCursor.getInt(categoriaCursor.getColumnIndex("idCategoria"));
        }
        categoriaCursor.close();

        // If doesnt find category, alert user
        if (idCategoria == -1) {
            Toast.makeText(this, "Categoria inválida!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if all info was given
        if (nomeLocal.isEmpty() || posCategoria == AdapterView.INVALID_POSITION || CURRENT_POSITION == null) {
            Toast.makeText(this, "Preencha o nome do local, selecione a categoria e permita a localização!", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if local is already on database
        Cursor cursor = DatabaseSingleton.getInstance().buscar("Checkin",
                new String[]{"qtdVisitas"},
                "Local = '" + nomeLocal + "'",
                "");

        if (cursor.getCount() == 0) {
            // New place, so insert on database
            ContentValues valores = new ContentValues();
            valores.put("Local", nomeLocal);
            valores.put("qtdVisitas", 1);
            valores.put("cat", idCategoria);
            valores.put("latitude", String.valueOf(CURRENT_POSITION.latitude));
            valores.put("longitude", String.valueOf(CURRENT_POSITION.longitude));

            DatabaseSingleton.getInstance().inserir("Checkin", valores);
            Toast.makeText(this, "Novo check-in cadastrado!", Toast.LENGTH_SHORT).show();

        } else {
            // Existence place, so update number of visits on database
            cursor.moveToFirst();
            int qtdVisitas = cursor.getInt(cursor.getColumnIndex("qtdVisitas"));
            ContentValues valores = new ContentValues();
            valores.put("qtdVisitas", qtdVisitas + 1);

            DatabaseSingleton.getInstance().atualizar("Checkin", valores, "Local = '" + nomeLocal + "'");
            Toast.makeText(this, "Check-in atualizado!", Toast.LENGTH_SHORT).show();
        }

        cursor.close();

        // Reopen activity
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }
}