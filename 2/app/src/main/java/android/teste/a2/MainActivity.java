package android.teste.a2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_screen);

        Log.i("Ciclo De Vida", getClassName() + ".onCreate() chamado.");
    }

    public void showReport (View v) {
        EditText edtName = (EditText) findViewById(R.id.editName);
        EditText edtAge = (EditText) findViewById(R.id.editAge);
        EditText edtWeight = (EditText) findViewById(R.id.editWeight);
        EditText edtHeight = (EditText) findViewById(R.id.editHeight);

        // Create Person object Serializable
        Person p = new Person();
        p.setName(edtName.getText().toString());
        p.setAge(edtAge.getText().toString());
        p.setWeight(Float.parseFloat(edtWeight.getText().toString()));
        p.setHeight(Float.parseFloat(edtHeight.getText().toString()));

        // Setting intent
        Intent it = new Intent(getBaseContext(), ResultActivity.class);

        it.putExtra("person", p);
        startActivity(it);
    }

    protected String getClassName(){
        String s = getClass().getName();
        return s;
    }

    protected void onStart(){
        super.onStart();
        Log.i("Ciclo De Vida", getClassName() + ".onStart() chamado.");
    }

    protected void onRestart(){
        super.onRestart();
        Log.i("Ciclo De Vida", getClassName() + ".onRestart() chamado.");
    }

    protected void onResume(){
        super.onResume();
        Log.i("Ciclo De Vida", getClassName() + ".onResume() chamado.");
    }

    protected void onPause(){
        super.onPause();
        Log.i("Ciclo De Vida", getClassName() + ".onPause() chamado.");
    }

    protected void onStop(){
        super.onStop();
        Log.i("Ciclo De Vida", getClassName() + ".onStop() chamado.");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.i("Ciclo De Vida", getClassName() + ".onDestroy() chamado.");
    }
}