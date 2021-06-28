package com.example.barrierApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.provider.Settings.Secure;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private final String ANDROID_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ContentResolver contxt = getBaseContext().getContentResolver();
        final String ANDROID_ID = Secure.getString(contxt, Secure.ANDROID_ID);

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
                BarrierClient client = new BarrierClient();
                client.addUser(login_view.toString(), ANDROID_ID);
                finish();
            }
        });
    }
}
