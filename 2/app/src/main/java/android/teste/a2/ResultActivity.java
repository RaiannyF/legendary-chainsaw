package android.teste.a2;

import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_screen);

        Log.i("Ciclo De Vida", getClassName() + ".onCreate() chamado.");

        Intent it = getIntent();

        // Get serializable object intent
        Person p2 = (Person) it.getSerializableExtra("person");

        TextView nameTxt = (TextView) findViewById(R.id.txtNameResult);
        TextView ageTxt = (TextView) findViewById(R.id.txtAgeResult);
        TextView weightTxt = (TextView) findViewById(R.id.txtWeightResult);
        TextView heightTxt = (TextView) findViewById(R.id.txtHeightResult);
        TextView imcTxt = (TextView) findViewById(R.id.txtIMCResult);
        TextView ratingTxt = (TextView) findViewById(R.id.txtRatingResult);

        DecimalFormat df = new DecimalFormat("0.00");
        assert p2 != null;
        float imc = Float.parseFloat(df.format(calculateIMC(p2)));
        String rating = classifyIMC(imc);

        nameTxt.setText(p2.getName());
        ageTxt.setText(p2.getAge() + " anos");
        weightTxt.setText(String.valueOf(p2.getWeight()) + " Kg");
        heightTxt.setText(String.valueOf(p2.getHeight()) + " m");
        imcTxt.setText(String.valueOf(imc) + " Kg/m²");
        ratingTxt.setText(rating);
    }

    public float calculateIMC(Person p){
        return p.getWeight() / (p.getHeight() * p.getHeight());
    }

    public String classifyIMC(float imc){
        if(imc < 18.5){
            return "Abaixo do Peso";
        } else if (imc < 24.9){
            return "Saudável";
        } else if(imc < 29.9){
            return "Sobrepeso";
        } else if(imc < 34.9){
            return "Obesidade Grau I";
        } else if(imc < 39.9){
            return "Obesidade Grau II (severa)";
        } else {
            return "Obesidade Grau III (mórbida)";
        }
    }

    public void goBack(View v){
        // Setting intent
        Intent it = new Intent(getBaseContext(), MainActivity.class);

        // Adding flag
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(it);

        //finish();
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