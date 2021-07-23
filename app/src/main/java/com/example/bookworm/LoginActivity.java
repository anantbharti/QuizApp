package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookworm.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmailId,loginPassword;
    private Button login;
    private FirebaseAuth mAuth;
    private User user;
    private String loginDesignation;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpLogViews();
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        user= new User();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginEmail=loginEmailId.getText().toString().trim();
                String loginPwd=loginPassword.getText().toString().trim();
                if(loginEmail.isEmpty()||loginPwd.isEmpty())
                    Toast.makeText(LoginActivity.this,"Fill all the details!",Toast.LENGTH_SHORT).show();
                else{
                    mAuth.signInWithEmailAndPassword(loginEmail,loginPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                firebaseUser=mAuth.getCurrentUser();
                                myRef=database.getReference(mAuth.getUid());
                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        user=snapshot.getValue(User.class);
                                        loginDesignation= user.getDesignation();
                                        Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                                        finish();
                                        if(loginDesignation.equals("Student")) {
                                            startActivity(new Intent(LoginActivity.this, StudentDashboard.class));
                                        }
                                        else {
                                            startActivity(new Intent(LoginActivity.this, TeacherDashboard.class));
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(LoginActivity.this,"Error"+error.getCode(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                                Toast.makeText(LoginActivity.this,"Log in failed!.",Toast.LENGTH_SHORT).show();
                        }
                    });}
            }
        });
    }

    public void startRegistrationActivity(View view){
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
    }
    private void setUpLogViews(){
        login= findViewById(R.id.login_button);
        loginEmailId= findViewById(R.id.login_email_id);
        loginPassword=findViewById(R.id.login_password);
    }
}