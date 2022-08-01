package com.example.mycashbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycashbook.db.DBHelper;
import com.example.mycashbook.utils.MoneyTextWatcher;

import java.util.Calendar;

public class TambahPengeluaranActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pengeluaran);
        init();
    }

    public void init() {
        dbHelper = new DBHelper(getApplicationContext());
        TextView edtTanggal = findViewById(R.id.edtTanggal);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                edtTanggal.setText(year + "-" + (month >= 10 ? String.valueOf(month) : "0" + month) + "-" + (day > 10 ? String.valueOf(day) : "0" + day));
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, dateSetListener, year, month, day);

        findViewById(R.id.btnDatepicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        EditText edtNominal = findViewById(R.id.edtNominal);
        edtNominal.addTextChangedListener(new MoneyTextWatcher(edtNominal));

        EditText edtKeterangan = findViewById(R.id.edtKeterangan);

        findViewById(R.id.btnSimpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TambahPengeluaranActivity.this, dbHelper.insertcatatankeuangan(edtTanggal.getText().toString(), edtNominal.getText().toString(), edtKeterangan.getText().toString(), getResources().getString(R.string.outcome)), Toast.LENGTH_SHORT).show();
                edtNominal.setText("");
                edtKeterangan.setText("");
                edtTanggal.setText("");
            }
        });

        findViewById(R.id.btnKembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TambahPengeluaranActivity.this, Beranda.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
        finish();
    }
}