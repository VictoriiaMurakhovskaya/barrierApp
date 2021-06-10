package com.example.barrierApp;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // получение координат из ключей переданного интента
        Bundle arguments = getIntent().getExtras();
        Location latlng = (Location) arguments.get("latlng");

        // привязка формы к активности
        setContentView(R.layout.activity_message);

        // установка настроек локализации
        Locale rus = new Locale("ru", "RU");

        // обновление TextView на форме
        TextView longView  = findViewById(R.id.textView2);
        longView.setText(String.format(rus, "Долгота: %f\nШирота: %f",
                latlng.getLongitude(), latlng.getLatitude()));
    }
}