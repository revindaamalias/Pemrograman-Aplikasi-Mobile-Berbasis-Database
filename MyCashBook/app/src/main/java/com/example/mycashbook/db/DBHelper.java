package com.example.mycashbook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mycashbook.model.CatatanKeuanganModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    //create db
    public DBHelper(Context context) {
        super(context, "mycashbook.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table
        db.execSQL("CREATE TABLE USER (ID_USER INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME CHAR(10) UNIQUE, PASSWORD CHAR(10))");
        db.execSQL("CREATE TABLE CATATANKEUANGAN (ID_CATATANKEUANGAN INTEGER PRIMARY KEY AUTOINCREMENT, TANGGAL DATE, NOMINAL DECIMAL(23,2), KETERANGAN CHAR(255), STATUS CHAR(1))");
        // create user
        insertuserdata(db, "user", "user");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //drop table user jika telah ada *saat upgrade versi db
        db.execSQL("DROP TABLE IF EXISTS USER");
    }

    //method insert data user
    public void insertuserdata(SQLiteDatabase db, String username, String passowrd) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("USERNAME", username);
        contentValues.put("PASSWORD", passowrd);
        long result = db.insert("USER", null, contentValues);
        if (result == -1)
            Log.e("CREATE USER: ", "failed");
        else
            Log.e("CREATE USER: ", "success");
    }

    //method update password user
    public String updateuserdata(String username, String password, String newpass) {
        String msg = null;
        String passwordres = null;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PASSWORD", newpass);
        Cursor cursor = db.rawQuery("SELECT * FROM USER WHERE USERNAME = ? ", new String[]{username});
        if (cursor.getCount() == 0) {
            msg = "Invalid username";
        } else {
            while (cursor.moveToNext()) {
                passwordres = cursor.getString(2);
            }

            if (passwordres != null) {
                if (passwordres.equalsIgnoreCase(password)) {
                    long result = db.update("USER", contentValues, "USERNAME=?", new String[]{username});
                    if (result == -1)
                        msg = "Change password failed";
                    else
                        msg = "Change password success";
                } else
                    msg = "Current password invalid";
            } else {
                msg = "Current password invalid";
            }
        }

        return msg;
    }

    // method mencari user *untuk login
    public String getUser(String username, String password) {
        String msg = null;
        String passwordres = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USER WHERE USERNAME = ? ", new String[]{username});

        if (cursor.getCount() == 0) {
            msg = "Invalid username";
        } else {
            while (cursor.moveToNext()) {
                passwordres = cursor.getString(2);
            }

            if (passwordres != null && passwordres.equalsIgnoreCase(password)) {
                msg = "Login";
            } else {
                msg = "Invalid password";
            }
        }

        return msg;
    }

    //method inser catatan keuangan *untuk pengeluaran dan pemasukan
    public String insertcatatankeuangan(String tanggal, String nominal, String
            keterangan, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TANGGAL", tanggal);
        contentValues.put("NOMINAL", nominal.replace("Rp", "").replace(".", ""));
        contentValues.put("KETERANGAN", keterangan);
        contentValues.put("STATUS", status);
        long result = db.insert("CATATANKEUANGAN", null, contentValues);
        if (result == -1)
            return "Failed";
        else
            return "Success";
    }

    // method select table catatan keuangan, untuk detail cash flow
    public List<CatatanKeuanganModel> getcatatankeuangan() {
        List<CatatanKeuanganModel> catatanKeuanganModelList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CATATANKEUANGAN", null);

        if (cursor.getCount() == 0) {
            catatanKeuanganModelList = new ArrayList<>();
        } else {
            DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
            decimalFormatSymbols.setCurrencySymbol("Rp");
            decimalFormatSymbols.setMonetaryDecimalSeparator(',');
            decimalFormatSymbols.setGroupingSeparator('.');
            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
            while (cursor.moveToNext()) {
                CatatanKeuanganModel catatanKeuanganModel = new CatatanKeuanganModel();
                catatanKeuanganModel.setNominal(decimalFormat.format(cursor.getDouble(2)));
                catatanKeuanganModel.setKeterangan(cursor.getString(3));
                catatanKeuanganModel.setStatus(cursor.getString(4));
                catatanKeuanganModel.setTanggal(cursor.getString(1));
                catatanKeuanganModelList.add(catatanKeuanganModel);
            }
        }

        return catatanKeuanganModelList;
    }

    //untuk mencari pengeluaran dan pemasukan perbulannya pada tahun saat ini
    public CatatanKeuanganModel getcatatankeuanganmonthly(int bulan, String tahun, String status) {
        String bulans = bulan < 10 ? "0" + (bulan + 1) : String.valueOf(bulan);
        List<CatatanKeuanganModel> catatanKeuanganModelList = new ArrayList<>();
        CatatanKeuanganModel catatanKeuanganModel = new CatatanKeuanganModel();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT CASE WHEN TANGGAL NOT NULL THEN SUM(NOMINAL) ELSE \"0\" END AS NOMINAL, CASE WHEN TANGGAL NOT NULL THEN SUBSTR(TANGGAL,6,2) ELSE \"" + bulans + "\" END AS TANGGAL FROM CATATANKEUANGAN WHERE TANGGAL LIKE '%-" + bulans + "-%' AND TANGGAL LIKE \'" + tahun + "-%\' AND STATUS = \'" + status + "\'GROUP BY TANGGAL", null);

        if (cursor.getCount() == 0) {
            catatanKeuanganModel.setNominal("0");
        } else {
            DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
            decimalFormatSymbols.setCurrencySymbol("Rp");
            decimalFormatSymbols.setMonetaryDecimalSeparator(',');
            decimalFormatSymbols.setGroupingSeparator('.');
            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
            while (cursor.moveToNext()) {
                catatanKeuanganModel.setNominal(cursor.getDouble(0) < 1 ? "0" : String.valueOf(cursor.getDouble(0)));
                catatanKeuanganModel.setTanggal(cursor.getString(1));
            }
        }

        return catatanKeuanganModel;
    }

    // method untuk menghitung pengeluaran atau pemasukan bulan ini
    public String getcashthismonth(String status, String bulan) {
        String nominal = null;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = null;
        if (status.equalsIgnoreCase("i")) {
            cursor = db.rawQuery("SELECT SUM(NOMINAL) AS NOMINAL FROM CATATANKEUANGAN WHERE STATUS = 'I' AND TANGGAL LIKE \'%-" + bulan + "-%\'  AND TANGGAL LIKE \'" + new SimpleDateFormat("yyyy").format(new Date()) + "%\'", null);
            nominal = getnonimanl(cursor);
        } else {
            cursor = db.rawQuery("SELECT SUM(NOMINAL) AS NOMINAL FROM CATATANKEUANGAN WHERE STATUS = 'O' AND TANGGAL LIKE \'%-" + bulan + "-%\'  AND TANGGAL LIKE \'" + new SimpleDateFormat("yyyy").format(new Date()) + "%\'", null);
            nominal = getnonimanl(cursor);
        }
        return nominal;
    }

    //method untuk convert nominla kedalam rupiah
    public String getnonimanl(Cursor cursor) {
        String nominal = null;
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("Rp");
        decimalFormatSymbols.setMonetaryDecimalSeparator(',');
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        if (cursor.getCount() == 0) {
            nominal = decimalFormat.format(0);
        } else {
            while (cursor.moveToNext()) {
                nominal = decimalFormat.format(cursor.getDouble(0));
            }
        }
        return nominal;
    }
}
