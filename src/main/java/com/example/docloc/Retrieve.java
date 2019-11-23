package com.example.docloc;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.common.util.Strings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Retrieve extends AppCompatActivity {
   ListView myPdf;
   List<UploadPDF> uploadPDFS;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();
    String arg="";
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference mref =database.getReference("uploads");

    @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);
        myPdf=(ListView)findViewById(R.id.listView);
        for(int j=0;j<100;j++)
        {
            if(email.charAt(j)=='@')
                break;
            arg+=email.charAt(j);
        }
        uploadPDFS=new ArrayList<>();
        viewAllFiles();
        myPdf.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UploadPDF uploadPDF = uploadPDFS.get(position);
                Intent intent = new Intent();
                intent.setData(Uri.parse(uploadPDF.getUrl()));
                startActivity(intent);
            }
        });

        }


    private void viewAllFiles() {
        mref.child(arg);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadPDF uploadPDF=postSnapshot.getValue(UploadPDF.class) ;
                    uploadPDFS.add(uploadPDF);
                }
                String[] uploads=new String[uploadPDFS.size()];
                for(int i=0;i<uploads.length;i++){
                    uploads[i]  = uploadPDFS.get(i).getName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads);
                myPdf.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Retrieve.this, "Failed", Toast.LENGTH_SHORT).show();
            }
    });
    }
}




