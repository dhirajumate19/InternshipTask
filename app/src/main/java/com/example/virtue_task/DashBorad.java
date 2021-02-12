package com.example.virtue_task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashBorad extends AppCompatActivity {
    private Button logoutbtn, fetchdataapi, resetpassword;
    private EditText userEmail, userName, userMobile;
    boolean yes = true;
    boolean no = false;

    FirebaseAuth mAuth;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_borad);

        initializeAVariable();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DashBorad.this);
                builder.setMessage("Are You Sure  That You Want Exit ").setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.getInstance().signOut();
                        Intent intent = new Intent(DashBorad.this, Login.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });
        //Fetch Data From API
        fetchdataapi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBorad.this, FetchDataAPI.class);
                startActivity(intent);
            }
        });

        // fetch data from DB
        db.collection("UserData").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d("Obj DB","Working "+ task.isSuccessful() + " data " + task.getResult());
                if (task.isSuccessful() && task.getResult() != null) {
                    String Uemail = task.getResult().getString("femail");
                    String Umobile = task.getResult().getString("fmobile");
                    String Uname = task.getResult().getString("fusername");
                    userEmail.setText(Uemail);
                    userMobile.setText(Umobile);
                    userName.setText(Uname);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DashBorad.this, "Some Fetching Error!", Toast.LENGTH_SHORT).show();
            }
        });
        //Password Reset
        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        FirebaseUser User=mAuth.getCurrentUser();
                        User.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DashBorad.this, "Password Reset Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DashBorad.this, "Password Reset Failed.", Toast.LENGTH_SHORT).show();
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
        });
    }

    private void initializeAVariable() {
        logoutbtn = findViewById(R.id.logout);
        resetpassword = findViewById(R.id.resetpassword);
        fetchdataapi = findViewById(R.id.fetchbtn);
        userEmail = findViewById(R.id.emailuser);
        userMobile = findViewById(R.id.mobileuser);
        userName = findViewById(R.id.nameuser);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finishAffinity();
    }
}
