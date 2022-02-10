package com.example.boundservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private boolean binded=false;
    private WeatherService weatherService;
    private TextView textViewWeather;
    private EditText editTextLocation;
    private Button buttonWeather;

    ServiceConnection weatherServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            WeatherService.LocalWeatherBinder binder=(WeatherService.LocalWeatherBinder)iBinder;
            weatherService=binder.getService();
            binded=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            binded=false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewWeather=findViewById(R.id.textView2);
        editTextLocation=findViewById(R.id.editTextTextPersonName);
        buttonWeather=findViewById(R.id.button);
        buttonWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWeather();
            }
        });
    }
    public void showWeather()
    {
        String location=editTextLocation.getText().toString();
        String weather=weatherService.getWeatherToday(location);
        textViewWeather.setText(weather);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=new Intent(this,WeatherService.class);
        bindService(intent,weatherServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(binded)
        {
            unbindService(weatherServiceConnection);
            binded=false;
        }
    }
}