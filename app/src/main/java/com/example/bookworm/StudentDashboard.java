package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bookworm.model.Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentDashboard extends AppCompatActivity {

    static int testCode=1;
    static boolean testDone=false;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef,tRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
    }
    public void startTest(View view){
       if(testDone){
           Toast.makeText(StudentDashboard.this, "Test finished already!", Toast.LENGTH_SHORT).show();
       }
       else {
           testDone=true;
           startActivity(new Intent(StudentDashboard.this,TestActivity.class));
           finish();
       }
    }
    public void studentLogOut(View view){
        mAuth.signOut();
        startActivity(new Intent(StudentDashboard.this,LoginActivity.class));
        finish();
        Toast.makeText(StudentDashboard.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}