package com.example.pc.accelerometer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements SensorEventListener{

    private TextView xText, yText, zText;
    private Sensor mySensor;
    private SensorManager SM;
    public static MqttClient client;
    Timer timer = new Timer();
    public static double x ;
    public static double y;
    public static double z;
    public static boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(flag == false){
            try {
                client = new MqttClient(
                        "tcp://54.186.96.23:2882", //URI
                        MqttClient.generateClientId(), //ClientId
                        new MemoryPersistence());
                client.connect();
                TimerTask updateBall = new UpdateBallTask();
                timer.scheduleAtFixedRate(updateBall, 0, 2000);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        // Create our Sensor Manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Register sensor Listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Assign TextView
        xText = (TextView)findViewById(R.id.xText);
        yText = (TextView)findViewById(R.id.yText);
        zText = (TextView)findViewById(R.id.zText);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
        xText.setText("X: " + event.values[0]);
        yText.setText("Y: " + event.values[1]);
        zText.setText("Z: " + event.values[2]);
        /*String clientId = "androidangle";
        try {
            client.publish(
                    "topic", // topic
                    "payload".getBytes(), // payload
                    2, // QoS
                    false); // retained?
        } catch (MqttException e) {
            e.printStackTrace();
        }*/


       /* String topic = "/smartchair/data/angle";
        String payload = "the payload";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
          //  MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, encodedPayload,0,false);
        } catch (UnsupportedEncodingException  e) {
            e.printStackTrace();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
class UpdateBallTask extends TimerTask {
    //Ball myBall;
    private double alpha = 0.5;
    static double fXg = 0;
    static double fYg = 0;
    static double fZg = 0;
    public void run() {
        try {
            double  Xg, Yg, Zg;
            int pitch, roll;
            Xg = MainActivity.x;
            Yg = MainActivity.y;
            Zg = MainActivity.z;

            //Low Pass Filter
            fXg = Xg * alpha + (fXg * (1.0 - alpha));
            fYg = Yg * alpha + (fYg * (1.0 - alpha));
            fZg = Zg * alpha + (fZg * (1.0 - alpha));

            roll  = (int) ((int)(Math.atan2(-fYg, fZg)*180.0)/3.14);
            pitch = (int) ((Math.atan2(fXg, Math.sqrt(fYg*fYg + fZg*fZg))*180.0)/3.14);

            String payload = roll + " " + pitch;
            MainActivity.client.publish(
                    "/smartchair/data/android", // topic
                    payload.getBytes(), // payload
                    0, // QoS
                    false); // retained?

        } catch (MqttException e) {
            e.printStackTrace();
        }
        //calculate the new position of myBall
    }
}