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

public class ManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        // Action bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setupLocals();
    }

    public void setupLocals() {
        LinearLayout layoutContent = findViewById(R.id.layoutContent);
        LinearLayout layoutDeletar = findViewById(R.id.layoutBtnDelete);

        // Clean old info
        layoutContent.removeAllViews();
        layoutDeletar.removeAllViews();

        // Get all checkins from database
        Cursor c = DatabaseSingleton.getInstance().buscar("Checkin", new String[]{"Local"}, "", "");

        if (c != null && c.moveToFirst()) {
            do {
                @SuppressLint("Range") String local = c.getString(c.getColumnIndex("Local"));

                // Create TextView to local name
                TextView txt = new TextView(this);
                txt.setText(local);
                txt.setTextSize(18f);
                txt.setPadding(10, 20, 10, 20);
                layoutContent.addView(txt);

                // Create delete button
                ImageButton btnDelete = new ImageButton(this);
                btnDelete.setImageResource(android.R.drawable.ic_delete);
                btnDelete.setBackgroundColor(Color.TRANSPARENT);
                btnDelete.setTag(local);
                btnDelete.setOnClickListener(deleteListener);
                layoutDeletar.addView(btnDelete);

            } while (c.moveToNext());
            c.close();
        }
    }
    View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String local = (String) v.getTag();

            new AlertDialog.Builder(v.getContext())
                    .setTitle("Confirmar exclusão")
                    .setMessage("Deseja realmente deletar o local: " + local + "?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Delete register from database
                            DatabaseSingleton.getInstance().deletar("Checkin", "Local = '" + local + "'");

                            // Reopen activity
                            Intent intent = new Intent(getApplicationContext(), ManagementActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        }
    };

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