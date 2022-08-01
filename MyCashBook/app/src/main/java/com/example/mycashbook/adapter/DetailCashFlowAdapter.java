package com.example.mycashbook.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycashbook.R;
import com.example.mycashbook.model.CatatanKeuanganModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class DetailCashFlowAdapter extends RecyclerView.Adapter<DetailCashFlowAdapter.MyViewHolder> {
    private Context context;
    private List<CatatanKeuanganModel> catatanKeuanganModels = new ArrayList<>();

    // constructor untuk passing parameter
    public DetailCashFlowAdapter(Context context, List<CatatanKeuanganModel> catatanKeuanganModelst) {
        this.context = context;
        this.catatanKeuanganModels = catatanKeuanganModelst;
    }


    @NonNull
    @Override
    public DetailCashFlowAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.contentdetailcashflow, parent, false);
        return new MyViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull DetailCashFlowAdapter.MyViewHolder holder, int position) {
        // mengisi kontent detail recyclerview detail cash flow
        if (catatanKeuanganModels.size() > 0) {

            if (catatanKeuanganModels.get(position).getStatus().equalsIgnoreCase(context.getResources().getString(R.string.income))) {
                holder.txtNonimal.setText("[ + ] " + catatanKeuanganModels.get(position).getNominal());
                holder.imgArraow.setImageDrawable(context.getDrawable(R.drawable.ic_action_arrow_left));
            } else if (catatanKeuanganModels.get(position).getStatus().equalsIgnoreCase(context.getResources().getString(R.string.outcome))) {
                holder.txtNonimal.setText("[ - ] " + catatanKeuanganModels.get(position).getNominal());
                holder.imgArraow.setImageDrawable(context.getDrawable(R.drawable.ic_action_arrow_right));
            }
            holder.txtKeterangan.setText(catatanKeuanganModels.get(position).getKeterangan());
            holder.txtTanggal.setText(catatanKeuanganModels.get(position).getTanggal());
        } else {
            Toast.makeText(context, "Detail Cash Flow is Empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return catatanKeuanganModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //initial komponen
        TextView txtNonimal, txtKeterangan, txtTanggal;
        ImageView imgArraow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNonimal = itemView.findViewById(R.id.txtNominal);
            txtKeterangan = itemView.findViewById(R.id.txtKeterangan);
            txtTanggal = itemView.findViewById(R.id.txtTanggal);
            imgArraow = itemView.findViewById(R.id.imgArrow);
        }
    }

}
