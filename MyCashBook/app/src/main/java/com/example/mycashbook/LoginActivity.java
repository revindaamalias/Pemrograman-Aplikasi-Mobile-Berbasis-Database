package com.example.mycashbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycashbook.db.DBHelper;

public class LoginActivity extends AppCompatActivity {
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init() {
        dbHelper = new DBHelper(getApplicationContext());

        TextView textView = findViewById(R.id.txtLabelApp);
        textView.setText(getResources().getString(R.string.app_name) + " " + getResources().getString(R.string.version));

        EditText edtUsername = findViewById(R.id.edtUsername);
        EditText edtPassword = findViewById(R.id.edtPassword);

        //fungsi button login
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordres = null;

                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                if (username.length() == 0 || password.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Username and Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                } else {
                    //cek user dan password untuk login
                    String res = dbHelper.getUser(username, password);
                    if (res.equalsIgnoreCase("login")) {
                        Intent intent = new Intent(LoginActivity.this, Beranda.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}