package com.example.uiapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

/** Login & account‑creation screen. */
public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextInputEditText userEt, passEt;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_main);

        db     = new DatabaseHelper(this);
        userEt = findViewById(R.id.usernameEditText);
        passEt = findViewById(R.id.passwordEditText);
        Button loginBtn  = findViewById(R.id.loginButton);
        Button createBtn = findViewById(R.id.createAccountButton);

        loginBtn.setOnClickListener(v -> login());
        createBtn.setOnClickListener(v -> createAccount());
    }

    private void login() {
        String u = userEt.getText().toString().trim();
        String p = passEt.getText().toString().trim();
        if (TextUtils.isEmpty(u) || TextUtils.isEmpty(p)) {
            Toast.makeText(this, R.string.enter_both, Toast.LENGTH_SHORT).show();
            return;
        }
        if (db.login(u, p)) {
            startActivity(new Intent(this, GridActivity.class));
            finish();
        } else {
            Toast.makeText(this, R.string.invalid_login, Toast.LENGTH_SHORT).show();
        }
    }

    private void createAccount() {
        String u = userEt.getText().toString().trim();
        String p = passEt.getText().toString().trim();
        if (TextUtils.isEmpty(u) || TextUtils.isEmpty(p)) {
            Toast.makeText(this, R.string.enter_both, Toast.LENGTH_SHORT).show();
            return;
        }
        if (db.register(u, p)) {
            Toast.makeText(this, R.string.account_created, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, GridActivity.class));
            finish();
        } else {
            Toast.makeText(this, R.string.username_exists, Toast.LENGTH_SHORT).show();
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
