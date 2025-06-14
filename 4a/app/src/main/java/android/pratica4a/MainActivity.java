package android.pratica4a;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensorLight;
    private Sensor sensorProximity;

    private float sensorLightValue;
    private float sensorProximityValue;
    private MaterialSwitch switchL;
    private MaterialSwitch switchV;

    private LanternaHelper flashlight;
    private MotorHelper vibration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get light sensor
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Get proximity sensor
        sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // Initialize flashlight and vibration controllers
        flashlight = new LanternaHelper(this);
        vibration = new MotorHelper(this);

        // Get switches
        switchL = (MaterialSwitch) findViewById(R.id.material_switch_LIGHT);
        switchV = (MaterialSwitch) findViewById(R.id.material_switch_VIBRATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sensorProximity != null && sensorLight != null){
            // Start to listen sensor
            sensorManager.registerListener(this, sensorProximity,SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(this, sensorLight,SensorManager.SENSOR_DELAY_GAME);

            Log.i("SENSOR_STARTED", sensorProximity.getName() + " e " + sensorLight.getName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop listening sensors
        sensorManager.unregisterListener(this);
        Log.i("SENSOR_STOPPED", "Sensors stopped");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        // Stop vibration and flashlight
        flashlight.desligar();
        vibration.pararVibracao();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // Update sensors values

        Sensor s = event.sensor; // Get changed sensor
        Log.i("SENSOR_CHANGED", "Type: " + s.getStringType());

        if (s.getType() == Sensor.TYPE_PROXIMITY) {
            Log.i("SENSOR_CHANGED", "Proximity: " + event.values[0] + " cm.");

            // Update value of proximity sensor
            sensorProximityValue = event.values[0];
        }
        else if(s.getType() == Sensor.TYPE_LIGHT){
            Log.i("SENSOR_CHAGED", "Light: " + event.values[0] + "lx.");

            // Update value of light sensor
            sensorLightValue = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Called when sensor accuracy changes

        String prec = "";
        switch (accuracy) {
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                prec = "Low";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                prec = "Medium";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                prec = "High";
                break;
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                prec = "Signal unavailable – unreliable";
                break;
            default:
        }
        Log.i("SENSOR_ACCURACY", "Name: " + sensor.getName() + " Accuracy: " + prec);
    }

    public void onClickClassifyReadings (View v){
        // Intent with customized action
        Intent it = new Intent("CLASSIFY_SENSORS_READING");

        // Send sensors last reading
        Bundle params = new Bundle();
        params.putFloat("light", sensorLightValue);
        params.putFloat("proximity", sensorProximityValue);

        it.putExtras(params);

        startActivityForResult(it, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent it) {
        super.onActivityResult(requestCode, resultCode, it);

        // Get result and update switches
        Bundle params = it.getExtras();
        assert params != null;

        Log.i("RESULT", "chegou o resultado");

        String lightResult = params.getString("lightResult");
        String proximityResult = params.getString("proximityResult");

        updateLight(lightResult);
        updateVibration(proximityResult);
    }


    private void updateLight(String lightResult){
        // Update switch and flashlight
        if(Objects.equals(lightResult, "baixa")){
            switchL.setChecked(true);
            flashlight.ligar();
        } else if (Objects.equals(lightResult, "alta")){
            switchL.setChecked(false);
            flashlight.desligar();
        } else {
            Toast.makeText(this, "Resultado inesperado!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateVibration(String proximityResult){
        // Update switch and vibration
        if(Objects.equals(proximityResult, "distante")){
            switchV.setChecked(true);
            vibration.iniciarVibracao();
        } else if (Objects.equals(proximityResult, "perto")){
            switchV.setChecked(false);
            vibration.pararVibracao();
        } else {
            Toast.makeText(this, "Resultado inesperado!", Toast.LENGTH_SHORT).show();
        }
    }

}