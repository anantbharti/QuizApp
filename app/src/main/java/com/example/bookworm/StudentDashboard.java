package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bookworm.adapter.AllTestsAdapter;
import com.example.bookworm.adapter.TeacherTestListAdapter;
import com.example.bookworm.model.Question;
import com.example.bookworm.model.TeachersTest;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentDashboard extends AppCompatActivity {

    RecyclerView recyclerView;
    AllTestsAdapter adapter;
    static int testCode=1;
    static boolean testDone=false;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef,tRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        recyclerView=findViewById(R.id.recycler_view_at);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<TeachersTest> options=
                new FirebaseRecyclerOptions.Builder<TeachersTest>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("AllTests"), TeachersTest.class)
                        .build();
        adapter=new AllTestsAdapter(options,this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }
    public void studentLogOut(View view){
        mAuth.signOut();
        startActivity(new Intent(StudentDashboard.this,LoginActivity.class));
        finish();
        Toast.makeText(StudentDashboard.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}