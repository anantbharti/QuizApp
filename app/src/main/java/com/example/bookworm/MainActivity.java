package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookworm.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private User user;
    private String loginDesignation;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar;
        actionBar=getSupportActionBar();
        actionBar.hide();
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        user= new User();
        firebaseUser=mAuth.getCurrentUser();
        getStarted=findViewById(R.id.get_started);

        if(firebaseUser!=null){
            getStarted.setVisibility(View.GONE);
            myRef=database.getReference(mAuth.getUid());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user=snapshot.getValue(User.class);
                    loginDesignation= user.getDesignation();
                    if(loginDesignation.equals("Student")) {
                        startActivity(new Intent(MainActivity.this, StudentDashboard.class));
                    }
                    else {
                        startActivity(new Intent(MainActivity.this, TeacherDashboard.class));
                    }
                    finish();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this,"Error"+error.getCode(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void startLoginActivity(View view){
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }
}