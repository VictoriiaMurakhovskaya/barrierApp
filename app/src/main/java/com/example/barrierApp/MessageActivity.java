package com.example.barrierApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.TextView;

import java.util.Locale;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final ContentResolver contxt = getBaseContext().getContentResolver();
        final String ANDROID_ID = Settings.Secure.getString(contxt, Settings.Secure.ANDROID_ID);
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

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);

        BarrierClient client = new BarrierClient();
        boolean openResult = client.openBarrier(ANDROID_ID, latlng.getLongitude(), latlng.getLatitude());

        Intent intent = new Intent(MessageActivity.this, ResultActivity.class);
        intent.putExtra("open_result", false);
        startActivity(intent);

    }
}