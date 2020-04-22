package com.brenguy.helpjet;

import java.util.Date;

import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.gsm.SmsManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

public class MainActivity extends Activity {
    // Обьявляем переменные
    Button buttonSend;
    Button locationSet;
    EditText editPhone;
    TextView tvLong;
    TextView tvLat;
    TextView tvGpsstatus;

    private LocationManager locationManager;
    StringBuilder sbGPS = new StringBuilder();
    StringBuilder sbNet = new StringBuilder();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSend = findViewById(R.id.btnSend);
        locationSet = findViewById(R.id.btnlocationsettings);
        editPhone = findViewById(R.id.editPhone);
        tvLat = findViewById(R.id.tvLat);
        tvGpsstatus = findViewById(R.id.tvGpsstatus);
        /* Пускаем слушателя кнопки "Отправить" */
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        buttonSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editPhone.getText().toString();
                String sms = getString(R.string.smsDefBody);
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, sms, null, null);
                    Toast.makeText(getApplicationContext(),
                            "SMS Sent!", Toast.LENGTH_LONG).show();
                    /*Пытаемся отправить сообщение и и вложить в это любви(координат)*/
                } catch (Exception e) {
                    /* Если произошла ошибка выводим тост с ошибкой отправки */
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.smsError),
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
// Пускаем слушателя кнопки "Настройка геолокации"
        locationSet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
    }
    public void onClickLocationSettings(View view) {
        startActivity(new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
}
