package com.brenguy.helpjet;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.klinker.android.send_message.Message;
import com.klinker.android.send_message.Settings;
import com.klinker.android.send_message.Transaction;

import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.SEND_SMS;
import static com.klinker.android.send_message.Transaction.NO_THREAD_ID;

class MyLocationListener implements LocationListener {

    static Location imHere;

    public static void SetUpLocationListener(Context context) // это нужно запустить в самом начале работы программы
    {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        imHere = (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
    }

    @Override
    public void onLocationChanged(Location loc) {
    }
    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
public class MainActivity extends Activity {
    final int REQUEST_CODE_PERMISSION_SEND_SMS = 1;
    // Обьявляем переменные
    Button buttonSend;
    Button locationSet;
    EditText editPhone;
    TextView tvLat;
    Button btnGPS;
    Button buttonSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MyLocationListener.SetUpLocationListener(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView buttonSend = findViewById(R.id.imageView);
        locationSet = findViewById(R.id.btnlocationsettings);
        tvLat = findViewById(R.id.tvLat);
        buttonSet = findViewById(R.id.buttonSet);
        Button ph = findViewById(R.id.ph);
        tvLat.setText(String.format(String.valueOf(MyLocationListener.imHere)).substring(13, 34));
        final int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        buttonSend.setEnabled(false);
        if(permissionStatus == PackageManager.PERMISSION_GRANTED) {
            buttonSend.setEnabled(true);
        } else {
            Toast.makeText(getApplicationContext(),
                    "No permission found, set all", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
        if (MyLocationListener.imHere == null){
            buttonSend.setEnabled(false);
        }
        /* Пускаем слушателя кнопки "Отправить" */
        buttonSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String gps = String.format(String.valueOf(MyLocationListener.imHere)).substring(13, 34);
                StringBuffer sb = new StringBuffer(" https://www.google.com/maps/place/");
                sb.insert(35, gps);
                String sms = getString(R.string.smsDefBody) + sb;
                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    String s1 = memory.SAVED_PHONE1;
                    String s2 = memory.SAVED_PHONE2;
                    String s3 = memory.SAVED_PHONE3;
                    String phones[] = {s1, s2, s3};
                    for(int i = 0; i != 3; i++){
                        Settings settings = new Settings();
                        settings.setUseSystemSending(true);
                        Transaction transaction = new Transaction(getApplicationContext(), settings);
                        Message message = new Message(sms, phones[i]);
                        transaction.sendNewMessage(message, NO_THREAD_ID);
                        Toast.makeText(getApplicationContext(), "SMS Sent to " + phones[i], Toast.LENGTH_LONG).show();
                    }
                }
            }});
// Пускаем слушателя кнопки "Настройка геолокации"
        locationSet.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
        buttonSet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        ph.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, contacts.class));
            }
        });


        }
}