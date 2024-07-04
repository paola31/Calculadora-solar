package com.example.repasosemana3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import java.time.LocalDate;


public class MainActivity extends AppCompatActivity {

    //Tipos de datos

    private EditText latitudEditText;
    private EditText longitudEditText;
    private EditText areaEditText;
    private SeekBar inclinationSeekBar;
    private TextView inclinationTextView;
    private Button calculateButton;
    private TextView resultTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudEditText = findViewById(R.id.LalitudeText);
        longitudEditText = findViewById(R.id.LongitudeText);
        areaEditText = findViewById(R.id.AreaText);
        inclinationSeekBar = findViewById(R.id.seekBar);
        inclinationTextView = findViewById(R.id.textView3);
        calculateButton = findViewById(R.id.button);
        resultTextView = findViewById(R.id.resultView);

        inclinationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progreso, boolean formulario) {
                inclinationTextView.setText("Inclinación de paneles: " + progreso + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                int validacionResultado = validateInputFields(latitudEditText, longitudEditText, areaEditText);

                switch (validacionResultado) {
                    case 0:

                        double latitud = Double.parseDouble(latitudEditText.getText().toString());
                        double longitud = Double.parseDouble(longitudEditText.getText().toString());
                        double area = Double.parseDouble(areaEditText.getText().toString());

                        int inclinacion = inclinationSeekBar.getProgress();
                        double produccionEnergia = calcularProduccionEnergia(latitud, longitud, area, inclinacion);

                        resultTextView.setText("Producción de energía: " + produccionEnergia + "kwh");

                        break;

                    case 1:
                        resultTextView.setText("Por favor complete todos los datos");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private int validateInputFields(EditText latitudEditText, EditText longitudEditText, EditText areaEditText) {
        if (!latitudEditText.getText().toString().isEmpty() &&
                !longitudEditText.getText().toString().isEmpty() &&
                !areaEditText.getText().toString().isEmpty()) {
            return 0;
        } else {
            return 1;
        }
    }

    private double calcularProduccionEnergia(double latitud, double longitud, double area, int inclinacion) {

        double latitudRad = Math.toRadians(latitud);
        double longitudRad = Math.toRadians(longitud);
        double inclinacionRad = Math.toRadians(inclinacion);
        double produccionEnergia = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int diaDeAnio = LocalDate.now().getDayOfYear();

            double anguloIncidencia = Math.acos(Math.sin(latitudRad) * Math.sin(inclinacionRad) * Math.cos(inclinacionRad));

            double constanteSolar = 0.1367;
            double radiacion = constanteSolar * Math.cos(anguloIncidencia) * (1 + 0.033 * Math.cos(Math.toRadians(360 * diaDeAnio / 360)));

            double areaPanel = area / 1000.0;
            double eficienciaPanel = 0.16;
            double factorPerdidas = 0.9;
            produccionEnergia = areaPanel * radiacion * eficienciaPanel * factorPerdidas;

        }
        
        return produccionEnergia;
    }
}






