package com.alpha1.A911madadgaar.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alpha1.A911madadgaar.R;

import java.util.ArrayList;

public class report_list_adapter extends RecyclerView.Adapter<report_list_adapter.MyViewHolder> {

    Context context;

    public report_list_adapter(Context context, ArrayList<report> reportArrayList) {
        this.context = context;
        this.reportArrayList = reportArrayList;
    }

    ArrayList<report> reportArrayList;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View v = LayoutInflater.from(context).inflate(R.layout.report_item_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        report report = reportArrayList.get(position);
        holder.reportno.setText(report.reportno);
        holder.incident.setText(report.incident);
        holder.city.setText(report.city);

        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Button Clicked! ", Toast.LENGTH_SHORT).show();
                Intent gotoViewReport = new Intent(context, admin_view_report.class);
                gotoViewReport.putExtra("reportno",report.reportno);
                context.startActivity(gotoViewReport);
            }
        });

    }



    @Override
    public int getItemCount() {
        return reportArrayList.size();
    }
    public static class MyViewHolder extends  RecyclerView.ViewHolder
    {
        TextView reportno,city,incident;
        ImageView viewBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            reportno = itemView.findViewById(R.id.reportno);
            city = itemView.findViewById(R.id.city);
            incident = itemView.findViewById(R.id.incident);
            viewBtn = itemView.findViewById(R.id.viewBtn);
        }
    }
}
