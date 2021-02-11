package com.example.virtue_task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText  password, email;
    private TextView textlink,forgotlink;
    private Button btn_login;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initializeVariable();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailId = email.getText().toString().trim();
                String passwordKey = password.getText().toString().trim();
                if (TextUtils.isEmpty(emailId)) {
                    email.setError("Email ID Required ");
                    return;
                }
                if (TextUtils.isEmpty(passwordKey)) {
                    password.setError("Password is Required");
                    return;
                }
                if (passwordKey.length() <= 6) {
                    password.setError("Password Length minimun 6 character");
                    return;
                }


              //  progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(emailId,passwordKey).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.i("task","LOGIN Succeed");
                            Toast.makeText(Login.this, " LogIn Successfully.... !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, DashBorad.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Error.... !", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        textlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });
        forgotlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initializeVariable() {
        email = findViewById(R.id.Lemail);
        password = findViewById(R.id.Lpassword);
        btn_login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBarlogin);
        textlink=findViewById(R.id.TExtlibk);
        forgotlink=findViewById(R.id.FPLogin);
    }
}
