package com.example.car_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText mNama, mEmail, mPassword, mConPassword;
    Button mSignUp;
    FirebaseFirestore mDatabase;
    FirebaseAuth mAuth;
    Spinner sDepartemen;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNama       = findViewById(R.id.edtRegisterNama);
        mEmail      = findViewById(R.id.edtRegisterEmail);
        mPassword   = findViewById(R.id.edtRegisterPassword);
        mConPassword= findViewById(R.id.edtRegisterConPassword);

        sDepartemen = findViewById(R.id.SpDepartemen);
        textView    = findViewById(R.id.text);

        mSignUp     = findViewById(R.id.btnSignUp);

        mAuth       = FirebaseAuth.getInstance();
        mDatabase   = FirebaseFirestore.getInstance();

        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this, R.array.Departemen, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sDepartemen.setAdapter(adapter);
        sDepartemen.setOnItemSelectedListener(this);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nama = mNama.getText().toString();
                final String email = mEmail.getText().toString();
                final String departemen  = textView.getText().toString();
                final String password = mPassword.getText().toString();
                String conpass = mConPassword.getText().toString();

                if (nama.isEmpty() | email.isEmpty() | password.isEmpty() | conpass.isEmpty()){
                    Toast.makeText(getBaseContext(), "Masukan data terlebih dahulu!..", Toast.LENGTH_LONG).show();
                } else if(!password.equals(conpass)){
                    Toast.makeText(getBaseContext(), "Password tidak sesuai!..", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String IDuser = mAuth.getCurrentUser().getUid();

                                Map<String, Object> Users = new HashMap<>();
                                Users.put("Nama", nama);
                                Users.put("Email", email);
                                Users.put("Password", password);
                                Users.put("Departemen", departemen);
                                Users.put("Role", "user");

                                mDatabase.collection("Users").document(IDuser).set(Users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getBaseContext(), "Pendaftaran berhasil", Toast.LENGTH_LONG).show();
                                            mAuth.signOut();
                                        } else {
                                            Toast.makeText(getBaseContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getBaseContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String Dep = parent.getItemAtPosition(position).toString();
        textView.setText(Dep);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
