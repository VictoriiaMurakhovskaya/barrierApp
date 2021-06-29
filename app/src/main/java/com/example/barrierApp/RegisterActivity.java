package com.example.barrierApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.provider.Settings.Secure;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private String server_ip;
    private int server_port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ContentResolver contxt = getBaseContext().getContentResolver();
        final String ANDROID_ID = Secure.getString(contxt, Secure.ANDROID_ID);

        Context context = getApplicationContext();
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(context);
        server_ip = myPreferences.getString("SERVER_IP", "0.0.0.0");
        server_port = myPreferences.getInt("PORT", 22);

        // привязка формы к активности
        setContentView(R.layout.register);

        // установка настроек локализации
        Locale rus = new Locale("ru", "RU");

        TextView text_id = findViewById(R.id.text_id);
        text_id.setText(String.format("ID: %s", ANDROID_ID));

        Button pushButton = findViewById(R.id.button);
        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView login_view = findViewById(R.id.editTextTextPersonName2);
                try {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    RunRegistration opener = new RunRegistration(ANDROID_ID,
                            login_view.getText().toString(),
                            server_ip, server_port);
                    executor.execute(opener);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    finish();
                }
            }
        });
    }
}

class RunRegistration implements Runnable{
    String id;
    String login;
    String serverIP;
    int serverPort;

    public RunRegistration(String login, String id, String serverIP, int serverPort){
        this.id = id;
        this.login = login;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        BarrierClient client = new BarrierClient(this.serverIP, this.serverPort);
        client.addUser(this.login, this.id);
    }
}
