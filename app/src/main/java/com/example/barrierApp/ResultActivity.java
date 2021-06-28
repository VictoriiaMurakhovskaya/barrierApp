package com.example.barrierApp;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // привязка формы к активности
        setContentView(R.layout.open_result);

        // установка настроек локализации
        Locale rus = new Locale("ru", "RU");

        Bundle arguments = getIntent().getExtras();
        boolean open_result = (boolean) arguments.get("open_result");

        if (!open_result){
            ImageView img = (ImageView) findViewById(R.id.imageResult);
            img.setImageResource(R.drawable.ic_traffic_semaphore_red_light);
        }

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);
//


    }
}
