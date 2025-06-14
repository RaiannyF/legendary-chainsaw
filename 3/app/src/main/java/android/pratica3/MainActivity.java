package android.pratica3;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

// setListAdapter is deprecated, so i change it
// Using a list view in xml and set array adapter with menu
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get List View from XML
        ListView listView = findViewById(R.id.my_list_view);

        // Menu items
        String[] menu = new String [] {"Minha casa na cidade natal",
                                        "Minha casa em Viçosa",
                                        "Meu departamento",
                                        "Fechar Aplicação"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);

        // Replace with setAdapter directly on List View
        listView.setAdapter(arrayAdapter);

        // Sending list item clicked to map page, adapted to get click event on List View
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 3){
                    finish();
                } else {
                    Intent it = new Intent(getBaseContext(), MapActivity.class);

                    it.putExtra("local", position);

                    // Get item text
                    String text = parent.getItemAtPosition(position).toString();

                    // Show toast of item clicked
                    Toast.makeText(getBaseContext(), "Posição: " + position + "\nTexto: " + text, Toast.LENGTH_SHORT).show();

                    // Insert in Logs table

                    startActivity(it);
                }
            }
        });


    }
}