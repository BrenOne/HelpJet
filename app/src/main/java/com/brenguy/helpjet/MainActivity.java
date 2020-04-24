package com.brenguy.helpjet;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.SEND_SMS;

class MyLocationListener implements LocationListener {

    static Location imHere; // здесь будет всегда доступна самая последняя информация о местоположении пользователя.

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
        imHere = loc;
    }
    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}

public class MainActivity extends Activity {

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    // Обьявляем переменные
    Button buttonSend;
    Button locationSet;
    EditText editPhone;
    TextView tvLat;
    Button btnGPS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MyLocationListener.SetUpLocationListener(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSend = findViewById(R.id.btnSend);
        locationSet = findViewById(R.id.btnlocationsettings);
        editPhone = findViewById(R.id.editPhone);
        tvLat = findViewById(R.id.tvLat);

        editPhone.setEnabled(false);
        if(checkPermission(SEND_SMS)) {
            editPhone.setEnabled(true);
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        /* Пускаем слушателя кнопки "Отправить" */

        buttonSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editPhone.getText().toString();
                String sms = getString(R.string.smsDefBody) + (String.format(String.valueOf(MyLocationListener.imHere)).substring(12,34));
                if (number == null || number.length() == 0) {
                    return;
                }
                if (checkPermission(SEND_SMS)){
                    SmsManager sm = SmsManager.getDefault();
                    sm.sendTextMessage(number, null, sms, null, null);
                    Toast.makeText(getApplicationContext(),
                            "SMS Sent!", Toast.LENGTH_LONG).show();
                }

                    /* Если произошла ошибка выводим тост с ошибкой отправки */
                    Toast.makeText(getApplicationContext(), getString(R.string.smsError), Toast.LENGTH_LONG).show();
                }
            });



// Пускаем слушателя кнопки "Настройка геолокации"
        btnGPS.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLat.setText((String.format(String.valueOf(MyLocationListener.imHere)).substring(12,34)));
            }
        });
        locationSet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

    }

        public boolean checkPermission(String sendSms) {
            int check = ContextCompat.checkSelfPermission(this, sendSms);
            return (check == PackageManager.PERMISSION_GRANTED);
    }
}