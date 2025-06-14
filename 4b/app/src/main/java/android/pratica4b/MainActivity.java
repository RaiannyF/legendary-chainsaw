package android.pratica4b;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private float sensorLightValue;
    private float sensorProximityValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get sensors values of intent
        Intent it = getIntent();
        Bundle params = it.getExtras();
        assert params != null;

        sensorLightValue = params.getFloat("light");
        sensorProximityValue = params.getFloat("proximity");
    }

    public void onClickReturnClassification(View v){
        // Classify sensors values
        String lightResult = sensorLightValue < 20 ? "baixa" : "alta";
        String proximityResult = sensorProximityValue > 3 ? "distante" : "perto";

        // Put classification on intent and return
        Intent it = new Intent();
        Bundle params = new Bundle();

        params.putString("lightResult", lightResult);
        params.putString("proximityResult", proximityResult);
        it.putExtras(params);

        // Set return in intent
        setResult(1, it);

        // Finish activity (and app)
        finish();
    }
}