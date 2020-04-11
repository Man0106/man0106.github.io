package com.example.car_e;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    ImageButton Profile, History, Order, Logout;
    TextView Nama, Bidang;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            GoToLogin();
        }
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String nama     = documentSnapshot.get("Nama").toString();
                        String bidang   = documentSnapshot.get("Departemen").toString();
                        Nama.setText(nama);
                        Bidang.setText("Bidang "+bidang);
                    }

                });
    }

    private void GoToLogin() {
        Intent L = new Intent(DashboardActivity.this, MainActivity.class);
        startActivity(L);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        Profile     = findViewById(R.id.imgbtnProfile);
        History     = findViewById(R.id.imgbtnHistory);
        Order       = findViewById(R.id.imgbtnPesan);
        Logout      = findViewById(R.id.imgbtnLogout);

        Nama        = findViewById(R.id.textNama);
        Bidang      = findViewById(R.id.textBidang);

        db          = FirebaseFirestore.getInstance();
        mAuth       = FirebaseAuth.getInstance();

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(DashboardActivity.this, HomeActivity.class);
                startActivity(o);
                finish();
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DashboardActivity.this);
                alertDialogBuilder.setTitle("Logout");
                alertDialogBuilder.setMessage("Are you sure to logout?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Map<String,Object> tokenMapRemove = new HashMap<>();
                        tokenMapRemove.put( "token_id", FieldValue.delete() );

                        String UserId = mAuth.getCurrentUser().getUid();

                        db.collection( "Users" ).document(UserId).update( tokenMapRemove ).addOnSuccessListener( new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mAuth.signOut();
                                GoToLogin();
                            }
                        } );

                    }
                });
                alertDialogBuilder.show();
            }
        });
    }
}
