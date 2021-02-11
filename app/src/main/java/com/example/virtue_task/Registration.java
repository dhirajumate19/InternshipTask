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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    //Declare Private Variable
    private FirebaseAuth mAuth;
    private EditText username, password, mobile, email;
    private Button btn_register;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private TextView text_link_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // Firebase Instance Initialize
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initializeVariable();

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(Registration.this, DashBorad.class);
            startActivity(intent);
        }

        btn_register.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                String mobiles = mobile.getText().toString().trim();
                String userName = username.getText().toString().trim();
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
                if (TextUtils.isEmpty(mobiles) && mobile.length() != 10) {
                    mobile.setError(" Valid Mobile Number is Required");
                }

                progressBar.setVisibility(View.VISIBLE);

                //Sign up using Email Pass Via Firebase mAuth createUserWithEmailAndPassword
                mAuth.createUserWithEmailAndPassword(emailId, passwordKey).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("task ", "is successful or not");
                            Toast.makeText(Registration.this, "Sign Up Successfully.... !", Toast.LENGTH_SHORT).show();


                            String mailIds = email.getText().toString().trim();
                            String passwords = password.getText().toString().trim();
                            String mobiles = mobile.getText().toString().trim();
                            String userName = username.getText().toString().trim();
                            DocumentReference DF = db.collection("UserData").document(mAuth.getCurrentUser().getUid());

                            Map<String, Object> user = new HashMap<>();
                            user.put("fusername", userName);
                            user.put("femail", mailIds);
                            user.put("fmobile", mobiles);
                            user.put("fpassword", passwords);
                            Log.i("task", "data" + user);

                            DF.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.i("task intent", "Sent Data to DB");
                                    Log.i("task", "USerID" + mAuth.getCurrentUser().getUid());
                                    Intent intent = new Intent(Registration.this, DashBorad.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("task", "Fail while adding data");
                                }
                            });

                        } else {
                            Toast.makeText(Registration.this, "Please Try Again !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        text_link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void initializeVariable() {
        username = findViewById(R.id.Rusername);
        email = findViewById(R.id.Remail);
        mobile = findViewById(R.id.Rmobile);
        password = findViewById(R.id.Lpassword);
        btn_register = findViewById(R.id.Register);
        progressBar = findViewById(R.id.progressBar);
        text_link_login=findViewById(R.id.logintext);
    }
}