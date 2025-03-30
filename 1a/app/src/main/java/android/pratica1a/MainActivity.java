package android.pratica1a;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void calculate(View v) {
        double val1 = Double.parseDouble(((EditText) findViewById(R.id.editTxt1)).getText().toString());
        double val2 = Double.parseDouble(((EditText) findViewById(R.id.editTxt2)).getText().toString());

        String tag = v.getTag().toString();
        msg = (TextView) findViewById(R.id.txtView4);
        double result = 0;

        switch (tag) {
            case "1":
                result = val1 + val2;
                msg.setText(String.valueOf(result));
                break;
            case "2":
                result = val1 - val2;
                msg.setText(String.valueOf(result));
                break;
            case "3":
                result = val1 * val2;
                msg.setText(String.valueOf(result));
                break;
            case "4":
                if(val2 == 0) {
                    msg.setText("Inválido!");
                } else {
                    result = val1 / val2;
                    msg.setText(String.valueOf(result));
                }
            default:
                break;
        }
    }
}