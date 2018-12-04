package com.example.user.lab5;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.os.Vibrator;
import android.app.Service;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {
    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    public void setVibrate(int time){
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==8){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                loc(null);
            }
        }
    }

    public void loc(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},8);
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);
    }
    public void locstop(View view){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lm.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {

        TextView textView1= (TextView)findViewById(R.id.textView);
        Double longitude = location.getLongitude();   //取得經度
        Double latitude = location.getLatitude();     //取得緯度
        boolean flag;
        flag = location.hasSpeed();//T if this location has a speed
        if (flag == false)
        {
            textView1.setText("目前為0.0 Km/h\n"+"經度: "+String.valueOf(longitude) +"\n"+"緯度: "+String.valueOf(latitude));
        }
        else
        {
            textView1.setText("現在時速為 "+String.valueOf(location.getSpeed()*(3600)/1000) + " Km/h \n"
                    +"經度: "+String.valueOf(longitude) +"\n"+"緯度: "+String.valueOf(latitude));
            //location.getSpeed() turn speed to km per hr and 經緯
            if((location.getSpeed()*(3600)/1000) >= 60){
                setVibrate(5000); //5 sec
                Toast.makeText(MainActivity.this,"速限60, 已超速", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
