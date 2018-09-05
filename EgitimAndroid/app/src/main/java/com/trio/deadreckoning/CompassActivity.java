package com.trio.deadreckoning;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CompassActivity extends Activity implements SensorEventListener {

    private ImageView mPointer;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    SensorManager sm_orient_compass;
    TextView textView_compass;
    TextView textView_direction;

    List list_orient_compass;

    //private float [ ] kalman_array= new float [5] ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        assert mSensorManager != null;
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPointer = (ImageView) findViewById(R.id.pointer);

        sm_orient_compass = (SensorManager) getSystemService(SENSOR_SERVICE);
        textView_direction = findViewById(R.id.textView_direction);
        textView_compass   = findViewById(R.id.textView_compass);
        list_orient_compass = sm_orient_compass.getSensorList(Sensor.TYPE_ORIENTATION);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        sm_orient_compass.registerListener(orientevent_compass,  (Sensor) list_orient_compass.get(0), SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onStop() {

        if (list_orient_compass.size() > 0) {
            sm_orient_compass.unregisterListener(orientevent_compass);
        }
        super.onStop();
    }
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            //float azimuthInRadians = Math.round(mOrientation[0]*10)/10;

            //double newnum = Math.round(mOrientation[0]*100)/100;
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
            azimuthInDegress=Math.round(azimuthInDegress);
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -azimuthInDegress,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(250);

            ra.setFillAfter(true);

            mPointer.startAnimation(ra);
            mCurrentDegree = -azimuthInDegress;
            textView_compass.setText("ROTATION" + "\n   " + azimuthInDegress);
 }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }
    SensorEventListener orientevent_compass = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @SuppressLint("SetTextI18n")
        public void onSensorChanged(SensorEvent event) {
            float[] orient = event.values;
            @SuppressLint("DefaultLocale") String orient_x = String.format("%.2f", orient[0]);
            @SuppressLint("DefaultLocale") String orient_y = String.format("%.2f", orient[1]);
            @SuppressLint("DefaultLocale") String orient_z = String.format("%.2f", orient[2]);
            //textView_compass.setText("ORIENTATION" + "\nx: " + orient_x + "\ny: " + orient_y + "\nz: " + orient_z);
            //textView_compass.setText("ROTATION" + "\n" + orient_x);

            //direction
            if(22<orient[0] && orient[0]<=67){
                textView_direction.setText("NORTH EAST");
            }
            else if(67<orient[0] && orient[0]<=112) {
                textView_direction.setText("EAST");
            }
            else if(112<orient[0] && orient[0]<=157){
                textView_direction.setText("SOUTH EAST");
            }
            else if(157<orient[0] && orient[0]<=202){
                textView_direction.setText("SOUTH");
            }
            else if(202<orient[0] && orient[0]<=248){
                textView_direction.setText("SOUTH WEST");
            }
            else if(248<orient[0] && orient[0]<=292){
                textView_direction.setText("WEST");
            }
            else if(292<orient[0] && orient[0]<=338){
                textView_direction.setText("NORTH WEST");
            }
            else if(338<orient[0] && orient[0]<=360){
                textView_direction.setText("NORTH");
            }
            else if(orient[0]<=22){
                textView_direction.setText("NORTH");
            }
            else {
                textView_direction.setText("-");
            }

        }
    };

}
