package com.example.bookworm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class TeacherDashboard extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        mAuth=FirebaseAuth.getInstance();
    }
    public void startAddingQuestion(View view){
        startActivity(new Intent(TeacherDashboard.this,AddingQuestion.class));
    }
    public void teacherLogOut(View view ){
        mAuth.signOut();
        startActivity(new Intent(TeacherDashboard.this,LoginActivity.class));
        finish();
        Toast.makeText(TeacherDashboard.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}