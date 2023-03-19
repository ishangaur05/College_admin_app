package com.ishan.collegeadminapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registration extends AppCompatActivity {

    TextInputEditText editTextFacultyName, editTextFacultyId, editTextEmail, editTextPassword;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextFacultyId = findViewById(R.id.fac_id);
        editTextFacultyName = findViewById(R.id.fac_name);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login2.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String fac_name, fac_id, email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                fac_id = String.valueOf(editTextFacultyId.getText());
                fac_name = String.valueOf(editTextFacultyName.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Registration.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Registration.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(fac_id)) {
                    Toast.makeText(Registration.this, "Enter Faculty Id", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(fac_name)) {
                    Toast.makeText(Registration.this, "Enter Faculty Name", Toast.LENGTH_SHORT).show();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    String userId = firebaseUser.getUid();

                                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                                    Toast.makeText(Registration.this, referenceProfile.toString(), Toast.LENGTH_SHORT).show();

                                    HashMap<String,String> hashMap = new HashMap<>();

                                    hashMap.put("id",userId);
                                    hashMap.put("faculty id",fac_id);
                                    hashMap.put("faculty name",fac_name);
                                    hashMap.put("email",email);


                                    referenceProfile.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(Registration.this, "Account Created", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Registration.this,MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });

                                }
                                else {
                                    Toast.makeText(Registration.this, task.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}