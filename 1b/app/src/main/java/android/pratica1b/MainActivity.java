package android.pratica1b;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    boolean resultOnScreen = false, errorState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickNumber(View v) {
        String tag = v.getTag().toString();

        EditText screen = (EditText) findViewById(R.id.screen);

        String text = screen.getText().toString();

        if (resultOnScreen || errorState) {
            screen.setText(tag);
            resultOnScreen = false;
            errorState = false;

        } else if (text.isEmpty() || Character.isDigit(text.charAt(text.length() - 1)) || text.charAt(text.length() - 1) == '.') {
            screen.append(tag);
        } else {
            screen.append(" " + tag);
        }
    }

    public void onClickOperator(View v) {
        String tag = v.getTag().toString();

        EditText screen = (EditText) findViewById(R.id.screen);

        String text = screen.getText().toString();

        if (errorState) {
            screen.setText("");
            errorState = false;
        } else if (!text.isEmpty() && !(text.contains(" - ") || text.contains("+") || text.contains("x") || text.contains("/"))) {
            screen.append(" " + tag);
            if (resultOnScreen) resultOnScreen = false;
        }
    }

    public void deleteLastDigit(View v) {
        EditText screen = (EditText) findViewById(R.id.screen);
        String text = screen.getText().toString();

        if (errorState) {
            screen.setText("");
            errorState = false;
        } else if (!text.isEmpty()) {
            String newText = (text.substring(0, text.length() - 1)).trim();
            screen.setText(newText);
        }
    }

    public void clearScreen(View v) {
        EditText screen = (EditText) findViewById(R.id.screen);
        screen.setText("");

        if (errorState) errorState = false;
    }

    public void calculate(View v) {
        EditText screen = (EditText) findViewById(R.id.screen);
        String text = screen.getText().toString();

        if (errorState) {
            screen.setText("");
            errorState = false;
        }

        if (!text.isEmpty()) {
            String[] digits = text.split(" ");

            if (digits.length == 3) {
                double value1 = Double.parseDouble(digits[0]);
                double value2 = Double.parseDouble(digits[2]);
                double result = 0;

                switch (digits[1]) {
                    case "+":
                        result = value1 + value2;
                        screen.setText(String.valueOf(result));
                        break;
                    case "-":
                        result = value1 - value2;
                        screen.setText(String.valueOf(result));
                        break;
                    case "x":
                        result = value1 * value2;
                        screen.setText(String.valueOf(result));
                        break;
                    case "/":
                        if (value2 == 0) {
                            screen.setText("ERROR");
                            errorState = true;
                        } else {
                            result = value1 / value2;
                            screen.setText(String.valueOf(result));
                        }
                    default:
                        break;
                }
                resultOnScreen = true;
            }
        }
    }

    public void onClickDot(View v) {
        EditText screen = (EditText) findViewById(R.id.screen);

        String text = screen.getText().toString();

        if (errorState) {
            screen.setText("");
            errorState = false;
        } else if (!text.isEmpty()) {
            String[] digits = text.split(" ");
            String digit = digits[digits.length - 1];

            if (!digit.contains(".") && Character.isDigit(digit.charAt(digit.length() - 1))) {
                screen.append(".");
            }
        }
    }
}