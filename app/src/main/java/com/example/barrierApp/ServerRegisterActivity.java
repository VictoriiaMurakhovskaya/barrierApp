package com.example.barrierApp;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class ServerRegisterActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getApplicationContext();

        // привязка формы к активности
        setContentView(R.layout.server_data);

        // установка настроек локализации
        Locale rus = new Locale("ru", "RU");

        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(this.context);

        String server_ip = myPreferences.getString("SERVER_IP", "0.0.0.0");
        Integer server_port = myPreferences.getInt("PORT", 22);

        TextView serverText = findViewById(R.id.textServer);
        TextView portNumber = findViewById(R.id.numberPort);

        serverText.setText(server_ip);
        portNumber.setText(server_port.toString());

        Button pushButton = findViewById(R.id.pushServer);
        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor myEditor = myPreferences.edit();
                myEditor.putString("SERVER_IP", serverText.getText().toString());
                myEditor.putInt("PORT", Integer.parseInt(portNumber.getText().toString()));
                myEditor.apply();
                finish();
            }
        });
    }
}
