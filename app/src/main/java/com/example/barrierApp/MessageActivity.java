package com.example.barrierApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MessageActivity extends AppCompatActivity {

    private String server_ip;
    private int server_port;
    Boolean openerResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // получение id устройства
        final ContentResolver contxt = getBaseContext().getContentResolver();
        final String ANDROID_ID = Settings.Secure.getString(contxt, Settings.Secure.ANDROID_ID);

        super.onCreate(savedInstanceState);

        // получение основного контекста приложения и чтение данных из хранилища устройства
        Context context = getApplicationContext();
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(context);
        server_ip = myPreferences.getString("SERVER_IP", "0.0.0.0");
        server_port = myPreferences.getInt("PORT", 22);

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

        // вынесение запроса на сервер в отдельный поток и обработка Future из этого потока
        Handler openerHandler = new Handler(getMainLooper());
        openerHandler.post(new Runnable() {
            @Override
            public void run() {
                // запрос на сервер
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<Boolean> openResult  = executor.submit(new RunOpener(ANDROID_ID,
                                                      latlng.getLongitude(), latlng.getLatitude(),
                                                      server_ip, server_port));
                try {
                    // ожидание ответа Future с таймером в 5 секунд
                    openerResult = openResult.get(100, TimeUnit.SECONDS);
                } catch (ExecutionException | InterruptedException | TimeoutException e) {
                    // по истечении таймера без ответа - неудача запроса
                    e.printStackTrace();
                    openerResult = false;
                }

                // создание нового интента с демонстрацией формы результата и закрытие текущей
                Intent intent = new Intent(MessageActivity.this, ResultActivity.class);
                intent.putExtra("open_result", openerResult);
                startActivity(intent);
                finish();
            }
        });

    }
}

// класс реализующий Callable
// запуск запроса на сервер в отдельном потоке и возврат результата через call
class RunOpener implements Callable<Boolean>{
    String id;
    double longitude;
    double latitude;
    String serverIP;
    int serverPort;

    public RunOpener(String id, double longtd, double lat, String serverIP, int serverPort){
        this.id = id;
        this.longitude = longtd;
        this.latitude = lat;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    @Override
    public Boolean call() {
        BarrierClient client = new BarrierClient(this.serverIP, this.serverPort);
        return client.openBarrier(this.id, this.longitude, this.latitude);
    }
}