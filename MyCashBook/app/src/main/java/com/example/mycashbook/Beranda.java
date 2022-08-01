package com.example.mycashbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mycashbook.db.DBHelper;
import com.example.mycashbook.model.CatatanKeuanganModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Beranda extends AppCompatActivity {
    //initial class Dbhelper
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
        init();
    }

    public void init() {
        //initial komponen
        dbHelper = new DBHelper(getApplicationContext());
        TextView txtPemasukan = findViewById(R.id.txtPemasukan);
        TextView txtPengeluaran = findViewById(R.id.txtPengeluaran);
        LinearLayout tambahPemasukan = findViewById(R.id.btnPemasukanplus);
        LinearLayout tambahPengeluaran = findViewById(R.id.btnPengeluaranplus);
        LinearLayout pengaturan = findViewById(R.id.btnSetting);
        LinearLayout detailCash = findViewById(R.id.btnDetail);
        LineChart reportingChart = findViewById(R.id.reportingChart);

        //set value pengeluaran dan pemasukan
        txtPengeluaran.setText(getResources().getString(R.string.pengeluaran) + ": " + dbHelper.getcashthismonth("O", new SimpleDateFormat("MM").format(new Date())));
        txtPemasukan.setText(getResources().getString(R.string.pemasukan) + ": " + dbHelper.getcashthismonth("I", new SimpleDateFormat("MM").format(new Date())));

        //fungsi untuk pindah ke halaman tambah pemasukan
        tambahPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent(TambahPemasukanActivity.class);
            }
        });

        //fungsi untuk pindah ke halaman tambah pengeluaran
        tambahPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent(TambahPengeluaranActivity.class);
            }
        });

        //fungsi untuk pindah ke halaman pengaturan
        pengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent(PengaturanActivity.class);
            }
        });

        //fungsi untuk pindah ke halaman detail cash flow
        detailCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent(DetailCashFlowActivity.class);
            }
        });

        //init komponen line chart
        reportingChart = findViewById(R.id.reportingChart);
        reportingChart.setTouchEnabled(true);
        reportingChart.setPinchZoom(true);
        reportingChart.getDescription().setText("Cash flow perbulan");

        //konfigurasi sumbu x
        XAxis xAxis = reportingChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getMonth()));

        //konfigurasi line chart, untuk menampilkan total pemasukan dan pengeluaran perbulan pada sumbu y
        ArrayList<Entry> yValuesi = new ArrayList<>();
        ArrayList<Entry> yValueso = new ArrayList<>();
        for (int i = 0; i <= 12; i++) {
            yValuesi.add(new Entry(i, Float.valueOf(dbHelper.getcatatankeuanganmonthly(i, new SimpleDateFormat("yyyy").format(new Date()), getResources().getString(R.string.income)).getNominal())));
            yValueso.add(new Entry(i, Float.valueOf(dbHelper.getcatatankeuanganmonthly(i, new SimpleDateFormat("yyyy").format(new Date()), getResources().getString(R.string.outcome)).getNominal())));
        }

        LineDataSet lineDataSeti = new LineDataSet(yValuesi, getResources().getString(R.string.pemasukan));
        LineDataSet lineDataSeto = new LineDataSet(yValueso, getResources().getString(R.string.pengeluaran));
        lineDataSeti.setFillAlpha(110);
        lineDataSeto.setFillAlpha(110);

        lineDataSeti.setColor(getResources().getColor(R.color.green));
        lineDataSeto.setColor(getResources().getColor(R.color.red));

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSeti);
        dataSets.add(lineDataSeto);

        LineData data = new LineData(dataSets);

        reportingChart.setData(data);
    }

    //method untuk menambahkan bulan
    public ArrayList<String> getMonth() {
        ArrayList<String> label = new ArrayList<>();
        label.add("Jan");
        label.add("Feb");
        label.add("Mar");
        label.add("Apr");
        label.add("Mei");
        label.add("Jun");
        label.add("Jul");
        label.add("Aug");
        label.add("Sep");
        label.add("Oct");
        label.add("Nov");
        label.add("Des");
        label.add("");
        return label;
    }

    //centralisasi method intent
    public void intent(Class classes) {
        Intent intent = new Intent(Beranda.this, classes);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
        finish();
    }
}