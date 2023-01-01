package com.alpha1.A911madadgaar.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.alpha1.A911madadgaar.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class reports_list extends AppCompatActivity {

    RecyclerView report_rv;
    ArrayList<report> reportArrayList;
    report_list_adapter report_list_adapter;
    FirebaseFirestore database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Getting all pending reports! ");
        progressDialog.show();

        setContentView(R.layout.activity_reports_list);
        report_rv = findViewById(R.id.reports_rv);
        report_rv.setHasFixedSize(true);
        report_rv.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseFirestore.getInstance();
        reportArrayList = new ArrayList<report>();
        report_list_adapter = new report_list_adapter(reports_list.this,reportArrayList);
        report_rv.setAdapter(report_list_adapter );
        EventChangeListen();
    }

    private void EventChangeListen() {
        database.collection("reports").whereEqualTo("status","Pending")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null)
                        {
                            if(progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(reports_list.this, "Some Error occured", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges())
                        {
                            if(dc.getType() == DocumentChange.Type.ADDED)
                            {
                                reportArrayList.add(dc.getDocument().toObject(report.class));
                            }
                            else if(dc.getType() == DocumentChange.Type.REMOVED)
                            {
                                //remove method.
                            }
                            report_list_adapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
    }
}