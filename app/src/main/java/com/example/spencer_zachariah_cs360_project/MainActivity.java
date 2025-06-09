package com.example.spencer_zachariah_cs360_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button login = findViewById(R.id.open_login);
        Button data = findViewById(R.id.open_data);
        Button sms = findViewById(R.id.open_sms);

        login.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        data.setOnClickListener(v -> startActivity(new Intent(this, DataDisplayActivity.class)));
        sms.setOnClickListener(v -> startActivity(new Intent(this, SmsPermissionActivity.class)));
    }
}