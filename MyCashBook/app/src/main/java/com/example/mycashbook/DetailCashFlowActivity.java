package com.example.mycashbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mycashbook.adapter.DetailCashFlowAdapter;
import com.example.mycashbook.db.DBHelper;

public class DetailCashFlowActivity extends AppCompatActivity {
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cash_flow);

        init();
    }

    public void init() {
        dbHelper = new DBHelper(getApplicationContext());

        //konfigurasi recyclerview untuk menampilkan catatan keuangan
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        DetailCashFlowAdapter detailCashFlowAdapter = new DetailCashFlowAdapter(getApplicationContext(), dbHelper.getcatatankeuangan());
        recyclerView.setAdapter(detailCashFlowAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        // fungsi button kembali
        findViewById(R.id.btnKembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    //method untuk menekan tombol back
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DetailCashFlowActivity.this, Beranda.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
        finish();
    }
}