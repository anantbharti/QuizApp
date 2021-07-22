package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookworm.model.Question;
import com.example.bookworm.model.Test;
import com.example.bookworm.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Vector;

public class TestActivity extends AppCompatActivity {

    private TextView testStudentName,testSubject,testRoll,testTime,testQuestion,testOptA,testOptB,testOptC,testOptD,userAnswer;
    private RadioGroup optionsRadioGroup;
    private RadioButton radOptionA,radOptionB,radOptionC,radOptionD,radSelectedBtn;
    private Button saveAndPrev, saveAndNext,endTest;
    private ProgressDialog testProgressDialog;
    static int[] score = new int[10];
    static String[] userAns = new String[10];
    FirebaseAuth mAuth;
    static int cqn=1,testCode,mo=0;
    static String ans;
    FirebaseDatabase database;
    DatabaseReference qRef,myRef;
    User user;
    Test test;
    Question question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setUpTestViews();
        testProgressDialog=new ProgressDialog(this);
        testProgressDialog.setMessage("Loading question...");
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        user= new User();
        test = new Test();
        question=new Question();

        qRef=database.getReference("Questions").child("Q"+cqn);
        qRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                question=snapshot.getValue(Question.class);
                testQuestion.setText("Q"+cqn+". "+question.getQue());
                testOptA.setText("(a) "+question.getA());
                testOptB.setText("(b) "+question.getB());
                testOptC.setText("(c) "+question.getC());
                testOptD.setText("(d) "+question.getD());
                ans=question.getAns();
                userAnswer.setText("Your answer: "+ userAns[cqn-1]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TestActivity.this,"Error"+error.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        saveAndNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testProgressDialog.show();
                if(radOptionA.isChecked()||radOptionB.isChecked()||radOptionC.isChecked()||radOptionD.isChecked()){
                    int selectedRadId= optionsRadioGroup.getCheckedRadioButtonId();
                    radSelectedBtn=findViewById(selectedRadId);
                    String selectedAns=radSelectedBtn.getText().toString().trim();
                    userAns[cqn-1]=selectedAns;
                    if(selectedAns.equals(ans)){
                        score[cqn-1]=1;
                    }
                    else{
                        score[cqn-1]=0;
                    }
                    optionsRadioGroup.clearCheck();
                }
                setNextQuestion();
                testProgressDialog.dismiss();
            }
        });

        saveAndPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testProgressDialog.show();
                if(radOptionA.isChecked()||radOptionB.isChecked()||radOptionC.isChecked()||radOptionD.isChecked()){
                    int selectedRadId= optionsRadioGroup.getCheckedRadioButtonId();
                    radSelectedBtn=findViewById(selectedRadId);
                    String selectedAns=radSelectedBtn.getText().toString().trim();
                    userAns[cqn-1]=selectedAns;
                    if(selectedAns.equals(ans)){
                        score[cqn-1]=1;
                    }
                    else {
                        score[cqn-1]=0;
                    }
                    optionsRadioGroup.clearCheck();
                }
                    setPreviousQuestion();
                    testProgressDialog.dismiss();
            }
        });

        endTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testProgressDialog.show();
                HashMap<String, Integer> us=new HashMap<String, Integer>();
                HashMap<String,String> ua=new HashMap<String, String>();
                for(int i=1;i<=10;i++){
                    us.put("Q"+i,score[i-1]);
                    ua.put("Q"+i,userAns[i-1]);
                    mo=mo+score[i-1];
                }
                test.setFullMarks(10);
                test.setMarksObtained(mo);
                test.setTestCode(1);
                test.setUserAnswer(ua);
                test.setUserScore(us);
                myRef=database.getReference(mAuth.getUid()).child("T1");
                myRef.setValue(test).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Toast.makeText(TestActivity.this,"Your score:"+mo,Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(TestActivity.this,"Error in uploading answers!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

    }

    private void setNextQuestion(){
        if(cqn<10){
            cqn++;
            qRef=database.getReference("Questions").child("Q"+cqn);
            qRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    question=snapshot.getValue(Question.class);
                    testQuestion.setText("Q"+cqn+". "+question.getQue());
                    testOptA.setText("(a) "+question.getA());
                    testOptB.setText("(b) "+question.getB());
                    testOptC.setText("(c) "+question.getC());
                    testOptD.setText("(d) "+question.getD());
                    ans=question.getAns();
                    userAnswer.setText("Your answer: "+userAns[cqn-1]);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(TestActivity.this,"Error"+error.getCode(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(TestActivity.this, "No more questions!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setPreviousQuestion(){
        if(cqn>1){
            cqn--;
            qRef=database.getReference("Questions").child("Q"+cqn);
            qRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    question=snapshot.getValue(Question.class);
                    testQuestion.setText("Q"+cqn+". "+question.getQue());
                    testOptA.setText("(a) "+question.getA());
                    testOptB.setText("(b) "+question.getB());
                    testOptC.setText("(c) "+question.getC());
                    testOptD.setText("(d) "+question.getD());
                    ans=question.getAns();
                    userAnswer.setText("Your answer: "+userAns[cqn-1]);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(TestActivity.this,"Error"+error.getCode(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(TestActivity.this, "Already at first question!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpTestViews(){
        testStudentName=findViewById(R.id.test_student_name);
        testSubject=findViewById(R.id.test_subject);
        testRoll=findViewById(R.id.test_roll);
        testTime=findViewById(R.id.test_time);
        testQuestion=findViewById(R.id.test_question);
        testOptA=findViewById(R.id.option_a);
        testOptB=findViewById(R.id.option_b);
        testOptC=findViewById(R.id.option_c);
        testOptD=findViewById(R.id.option_d);
        optionsRadioGroup=findViewById(R.id.options_radio_group);
        radOptionA=findViewById(R.id.option_a_radio);
        radOptionB=findViewById(R.id.option_b_radio);
        radOptionC=findViewById(R.id.option_c_radio);
        radOptionD=findViewById(R.id.option_d_radio);
        saveAndNext=findViewById(R.id.save_and_next);
        saveAndPrev=findViewById(R.id.save_and_prev);
        userAnswer=findViewById(R.id.user_answer);
        endTest=findViewById(R.id.end_test);
    }
}