package com.alpha1.A911madadgaar.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.alpha1.A911madadgaar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
//TODO: (Bug) Progress dialog remains there if there are no reports pending. - FIXED
//TODO: (Bug) When some report's status is changed, it does not gets removed on its own. -NOTFIXED
public class reports_list extends AppCompatActivity {

    RecyclerView report_rv;
    ArrayList<report> reportArrayList;
    report_list_adapter report_list_adapter;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
                            Toast.makeText(reports_list.this, "Some Error occured", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(value.isEmpty())
                        {
                            Toast.makeText(reports_list.this, "No Reports Found", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            for(DocumentChange dc : value.getDocumentChanges())
                            {
                                if(dc.getType() == DocumentChange.Type.ADDED)
                                {
                                    reportArrayList.add(dc.getDocument().toObject(report.class));
                                }
                                if(dc.getType() == DocumentChange.Type.REMOVED)
                                {
                                    reportArrayList.remove(dc.getOldIndex());
                                }
                                report_list_adapter.notifyDataSetChanged();
                            }
                        }

                    }
                });

    }
}