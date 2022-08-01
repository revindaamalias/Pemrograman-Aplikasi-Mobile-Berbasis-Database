package com.example.mycashbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycashbook.db.DBHelper;

public class PengaturanActivity extends AppCompatActivity {
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);

        init();
    }

    public void init() {
        dbHelper = new DBHelper(getApplicationContext());
        //init komponen
        EditText edtCurrentpass = findViewById(R.id.edtCurrentpass);
        EditText edtNewpass = findViewById(R.id.edtNewpass);

        //fungsi button simpan
        findViewById(R.id.btnSimpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PengaturanActivity.this, dbHelper.updateuserdata(getIntent().getStringExtra("username"), edtCurrentpass.getText().toString(), edtNewpass.getText().toString()), Toast.LENGTH_SHORT).show();
            }
        });

        //fungsi button kembali
        findViewById(R.id.btnKembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    //method untuk back
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PengaturanActivity.this, Beranda.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
        finish();
    }
}