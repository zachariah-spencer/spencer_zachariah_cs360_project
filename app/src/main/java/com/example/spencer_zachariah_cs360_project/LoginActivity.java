package com.example.spencer_zachariah_cs360_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spencer_zachariah_cs360_project.db.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    private EditText usernameInput;
    private EditText passwordInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        Button loginButton = findViewById(R.id.login_button);
        Button createButton = findViewById(R.id.create_account_button);

        loginButton.setOnClickListener(v -> attemptLogin());
        createButton.setOnClickListener(v -> createAccount());
    }

    /**
     * Attempt to log the user in using the provided credentials.
     */
    private void attemptLogin() {
        String user = usernameInput.getText().toString().trim();
        String pass = passwordInput.getText().toString();
        if (dbHelper.checkUser(user, pass)) {
            startActivity(new Intent(this, DataDisplayActivity.class));
        } else {
            Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Create a new account using entered username/password.
     */
    private void createAccount() {
        String user = usernameInput.getText().toString().trim();
        String pass = passwordInput.getText().toString();
        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, R.string.fill_credentials, Toast.LENGTH_SHORT).show();
            return;
        }
        if (dbHelper.addUser(user, pass)) {
            Toast.makeText(this, R.string.account_created, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.account_exists, Toast.LENGTH_SHORT).show();
        }
    }
}