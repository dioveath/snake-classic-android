package com.charicha.gameframework.impl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Charicha on 12/22/2017.
 */

public class AccelerometerHandler implements SensorEventListener {

    float accelX;
    float accelY;
    float accelZ;

    public AccelerometerHandler(Context context){
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() > 0){
            Sensor accelerometer = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        accelX = sensorEvent.values[0];
        accelY = sensorEvent.values[1];
        accelZ = sensorEvent.values[2];
    }

    public float getAccelX(){
        return accelX;
    }

    public float getAccelY(){
        return accelY;
    }

    public float getAccelZ(){
        return accelZ;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
