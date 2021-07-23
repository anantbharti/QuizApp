package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookworm.adapter.TeacherTestListAdapter;
import com.example.bookworm.model.TeachersTest;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherDashboard extends AppCompatActivity {

    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    TeacherTestListAdapter adapter;
    Button addTest,nextBtn,cancelBtn;
    EditText testName,totalQues;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        mAuth=FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.recycler_view_tt);
        addTest=findViewById(R.id.add_test);
        testName=findViewById(R.id.test_name);
        nextBtn=findViewById(R.id.next_btn);
        cancelBtn=findViewById(R.id.cancel_btn);
        totalQues=findViewById(R.id.total_ques);
        database=FirebaseDatabase.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        testName.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.GONE);
        totalQues.setVisibility(View.GONE);
        FirebaseRecyclerOptions<TeachersTest> options=
                new FirebaseRecyclerOptions.Builder<TeachersTest>()
                        .setQuery(FirebaseDatabase.getInstance().getReference(mAuth.getUid()).child("Tests"), TeachersTest.class)
                        .build();
        adapter=new TeacherTestListAdapter(options,this);
        recyclerView.setAdapter(adapter);

        addTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.GONE);
                testName.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
                totalQues.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.VISIBLE);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                testName.setVisibility(View.GONE);
                nextBtn.setVisibility(View.GONE);
                totalQues.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String testNameStr=testName.getText().toString().trim();
                String totalQ=totalQues.getText().toString();
                if(testNameStr.isEmpty()){
                    Toast.makeText(TeacherDashboard.this, "Enter test name!", Toast.LENGTH_SHORT).show();
                }
                else if(totalQ.isEmpty()){
                    Toast.makeText(TeacherDashboard.this, "Enter number of questions!", Toast.LENGTH_SHORT).show();
                }
                else{
                    int noOfQues=Integer.parseInt(totalQ);
                    createTest(testNameStr,noOfQues);
                }
            }
        });
    }

    private void createTest(String testNameStr,int noOfQues) {
        int min = 1;
        int max = 99999;
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);

        TeachersTest teachersTest=new TeachersTest();
        teachersTest.setTestCode(random_int);
        teachersTest.setTestName(testNameStr);
        teachersTest.setNoOfQues(noOfQues);
        DatabaseReference testListRef=database.getReference(mAuth.getUid()).child("Tests").child(testNameStr+random_int);
        testListRef.setValue(teachersTest);
        finish();
        DatabaseReference reference=database.getReference("AllTests").child(testNameStr+random_int);
        reference.setValue(teachersTest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(TeacherDashboard.this, "Test created successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(TeacherDashboard.this, "Error in creating test!", Toast.LENGTH_SHORT).show();
                }
                recyclerView.setVisibility(View.VISIBLE);
                testName.setVisibility(View.GONE);
                nextBtn.setVisibility(View.GONE);
                totalQues.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
                finish();
            }
        });

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

    public void teacherLogOut(View view ){
        mAuth.signOut();
        startActivity(new Intent(TeacherDashboard.this,LoginActivity.class));
        finish();
        Toast.makeText(TeacherDashboard.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}