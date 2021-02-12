package com.example.virtue_task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity implements View.OnClickListener {

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
        //Firebase Instance
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeVariable();

        btn_login.setOnClickListener(this);
        textlink.setOnClickListener(this);
        forgotlink.setOnClickListener(this);

    }

    private void initializeVariable() {
        email = findViewById(R.id.Lemail);
        password = findViewById(R.id.Lpassword);
        btn_login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBarlogin);
        textlink=findViewById(R.id.TExtlibk);
        forgotlink=findViewById(R.id.FPLogin);
    }

    @Override
    public void onClick(View view) {
        if (view==btn_login){
            //login method
            login();
        }
        if (view==textlink){
            //this method go to registration activity
            goTo();
        }
        if (view==forgotlink){
            forgotPassword(view);
        }
    }

    private void forgotPassword(View view) {
        final EditText resetPassword = new EditText(view.getContext());

        final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
        passwordResetDialog.setTitle("Reset Password ?");
        passwordResetDialog.setMessage("Enter New Password >= 6 Characters long.");
        passwordResetDialog.setView(resetPassword);
        passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // extract the email and send reset link

                String newPassword = resetPassword.getText().toString();
                if (!(newPassword.length() <=6)){
                    return;
                }
                FirebaseUser User=mAuth.getCurrentUser();
                User.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Login.this, "Password Reset Successfully.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Password Reset Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // close
            }
        });
        passwordResetDialog.create().show();
    }

    private void goTo() {
        Intent intent = new Intent(Login.this, Registration.class);
        startActivity(intent);
    }

    private void login() {
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
        if (!(passwordKey.length() <= 6)) {
            password.setError("Password Length minimum 6 character");
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
}
