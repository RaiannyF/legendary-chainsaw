package android.pratica5;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Action bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setupReport();
    }

    public void setupReport() {
        LinearLayout layoutNomes = findViewById(R.id.layoutPlaces);
        LinearLayout layoutVisitas = findViewById(R.id.layoutVisits);

        // Clean old info
        layoutNomes.removeAllViews();
        layoutVisitas.removeAllViews();

        // Get all checking order by number of visits
        Cursor c = DatabaseSingleton.getInstance().buscar(
                "Checkin",
                new String[]{"Local", "qtdVisitas"},
                "",
                "qtdVisitas DESC"
        );

        if (c != null && c.moveToFirst()) {
            do {
                @SuppressLint("Range") String nome = c.getString(c.getColumnIndex("Local"));
                @SuppressLint("Range") int visitas = c.getInt(c.getColumnIndex("qtdVisitas"));

                // Create TextView to local name
                TextView txtNome = new TextView(this);
                txtNome.setText(nome);
                txtNome.setTextSize(16f);
                txtNome.setPadding(5, 20, 5, 20);
                layoutNomes.addView(txtNome);

                // Create TextView to number of visits
                TextView txtQtd = new TextView(this);
                txtQtd.setText(String.valueOf(visitas));
                txtQtd.setTextSize(16f);
                txtQtd.setPadding(5, 20, 5, 20);
                layoutVisitas.addView(txtQtd);

            } while (c.moveToNext());
            c.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Load action bar options
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_option1) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
