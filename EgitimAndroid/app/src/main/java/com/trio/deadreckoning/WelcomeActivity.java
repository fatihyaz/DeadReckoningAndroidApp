package com.trio.deadreckoning;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.hardware.Sensor.TYPE_STEP_COUNTER;

public class WelcomeActivity extends AppCompatActivity {

    Button PDRBtn;
    Button CompassBtn;

    SensorManager sm_accelo;
    SensorManager sm_magnet;
    SensorManager sm_gyro;
    SensorManager sm_orient;


    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;

    List list_accelo;
    List list_magnet;
    List list_gyro;
    List list_orient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sensor);
        sm_accelo = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm_magnet = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm_gyro   = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm_orient = (SensorManager) getSystemService(SENSOR_SERVICE);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);

        list_accelo = sm_accelo.getSensorList(Sensor.TYPE_ACCELEROMETER);
        list_magnet = sm_magnet.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        list_gyro   = sm_gyro.getSensorList(Sensor.TYPE_GYROSCOPE);
        list_orient = sm_orient.getSensorList(Sensor.TYPE_ORIENTATION);


        initializeViews();
    }
    private void initializeViews() {

        CompassBtn = findViewById(R.id.CompassBtn);

        CompassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, CompassActivity.class));

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (list_accelo.size() > 0 & list_magnet.size() > 0 & list_gyro.size() > 0) {
            sm_accelo.registerListener(acceloevent, (Sensor) list_accelo.get(0), SensorManager.SENSOR_DELAY_NORMAL);
            sm_magnet.registerListener(magnetevent,(Sensor) list_magnet.get(0), SensorManager.SENSOR_DELAY_NORMAL);
            sm_gyro.registerListener(gyroevent,  (Sensor) list_gyro.get(0), SensorManager.SENSOR_DELAY_NORMAL);
            sm_orient.registerListener(orientevent,  (Sensor) list_orient.get(0), SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(getBaseContext(), "All Sensors Enabled", Toast.LENGTH_LONG).show();


        } else {
            Toast.makeText(getBaseContext(), "Error: No Accelerometer.", Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(), "Error: No Magnetometer.", Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(), "Error: No Gyroscope.", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onStop() {
        if (list_accelo.size() > 0) {
            sm_accelo.unregisterListener(acceloevent);
        }
        if (list_magnet.size() > 0) {
            sm_magnet.unregisterListener(magnetevent);
        }
        if (list_gyro.size() > 0) {
            sm_gyro.unregisterListener(gyroevent);
        }
        if (list_orient.size() > 0) {
            sm_orient.unregisterListener(orientevent);
        }


        super.onStop();
    }

    SensorEventListener acceloevent = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @SuppressLint("SetTextI18n")
        public void onSensorChanged(SensorEvent event) {
            float[] accelo = event.values;
            @SuppressLint("DefaultLocale") String accelo_x = String.format("%.2f", accelo[0]);
            @SuppressLint("DefaultLocale") String accelo_y = String.format("%.2f", accelo[1]);
            @SuppressLint("DefaultLocale") String accelo_z = String.format("%.2f", accelo[2]);

            float accelo_aver =  (float) Math.sqrt((accelo[0]*accelo[0]) + (accelo[1]*accelo[1]) + (accelo[2]*accelo[2]));
            textView1.setText("Accelerometer" + "\nx(m/s2): " + accelo_x + "\ny(m/s2): " + accelo_y + "\nz(m/s2): " + accelo_z);
            @SuppressLint("DefaultLocale") String accelo_average = String.format("%.2f", accelo_aver);
            textView4.setText("Accelero Average" +  "\n" +accelo_average+"m/s2");
        }
    };
    SensorEventListener magnetevent = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @SuppressLint("SetTextI18n")
        public void onSensorChanged(SensorEvent event) {
            float[] magnet = event.values;
            @SuppressLint("DefaultLocale") String magnet_x = String.format("%.2f", magnet[0]);
            @SuppressLint("DefaultLocale") String magnet_y = String.format("%.2f", magnet[1]);
            @SuppressLint("DefaultLocale") String magnet_z = String.format("%.2f", magnet[2]);
            textView2.setText("Magnetometer" + "\nx(uT): " + magnet_x + "\ny(uT): " + magnet_y + "\nz(uT): " + magnet_z);

        }
    };

    SensorEventListener gyroevent = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @SuppressLint("SetTextI18n")
        public void onSensorChanged(SensorEvent event) {
            float[] gyro = event.values;
            @SuppressLint("DefaultLocale") String gyro_x = String.format("%.2f", gyro[0]);
            @SuppressLint("DefaultLocale") String gyro_y = String.format("%.2f", gyro[1]);
            @SuppressLint("DefaultLocale") String gyro_z = String.format("%.2f", gyro[2]);
            textView3.setText("Gyroscope" + "\nx(rad/s): " + gyro_x + "\ny(rad/s): " + gyro_y + "\nz(rad/s): " + gyro_z);

        }
    };
    SensorEventListener orientevent = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @SuppressLint("SetTextI18n")
        public void onSensorChanged(SensorEvent event) {
            float[] orient = event.values;
            @SuppressLint("DefaultLocale") String orient_x = String.format("%.2f", orient[0]);
            @SuppressLint("DefaultLocale") String orient_y = String.format("%.2f", orient[1]);
            @SuppressLint("DefaultLocale") String orient_z = String.format("%.2f", orient[2]);
            textView5.setText("Orientation" + "\nx: " + orient_x + "\ny: " + orient_y + "\nz: " + orient_z);

        }
    };

}

