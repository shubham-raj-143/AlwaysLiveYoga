package com.shubham.alwaysliveyoga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegistrationActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView, name, phone;
    private Button Btn, login_link_btn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        Btn = findViewById(R.id.btnregister);
        progressbar = findViewById(R.id.progressbar);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        login_link_btn = findViewById(R.id.login_link_btn);

        login_link_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();

        String e = emailTextView.getText().toString();

        databaseReference = firebaseDatabase.getReference("UserInfo");



        userInfo = new UserInfo();

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });
    }

    private void registerNewUser()
    {

        progressbar.setVisibility(View.VISIBLE);

        String email, password, userName="", userPhone="";
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        userName = name.getText().toString();
        userPhone = name.getText().toString();

        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPhone)) {
            addDatatoFirebase(userName, userPhone, email);
//            Toast.makeText(RegistrationActivity.this, "Please add some data about yourself", Toast.LENGTH_LONG).show();
        }


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_LONG)
                                    .show();


                            progressbar.setVisibility(View.GONE);

                            Intent intent
                                    = new Intent(RegistrationActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                        }
                        else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    "Registration failed!!\nEmail already exists!"
                                            + " Please try again later",
                                    Toast.LENGTH_LONG)
                                    .show();

                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void addDatatoFirebase(String name, String phone, String email) {

        userInfo.setUserName(name);
        userInfo.setUserPhone(phone);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                databaseReference.setValue(userInfo);

                Toast.makeText(RegistrationActivity.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegistrationActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}