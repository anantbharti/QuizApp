package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookworm.model.Question;
import com.example.bookworm.model.Result;
import com.example.bookworm.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TestActivity extends AppCompatActivity {

    private TextView testStudentName,testSubject,testRoll,testTime,testQuestion,testOptA,testOptB,testOptC,testOptD,userAnswer;
    private RadioGroup optionsRadioGroup;
    private RadioButton radOptionA,radOptionB,radOptionC,radOptionD,radSelectedBtn;
    private Button saveAndPrev, saveAndNext,endTest;
//    private ProgressDialog testProgressDialog;
    static int[] score = new int[100];
    static String[] userAns = new String[100];
    FirebaseAuth mAuth;
    static int cqn=1,mo=0;
    static String ans,testName;
    static int totalQues,testCode;
    FirebaseDatabase database;
    DatabaseReference qRef,myRef;
    User user;
    Result result;
    Question question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setUpTestViews();
        testCode=getIntent().getExtras().getInt("TestCode");
        testName=getIntent().getExtras().getString("TestName");
        totalQues=getIntent().getExtras().getInt("TotalQues");
        testSubject.setText(testName);

        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        user= new User();
        myRef=database.getReference(mAuth.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user=snapshot.getValue(User.class);
                testStudentName.setText(user.getName().toString().trim());
                testRoll.setText(user.getEmail().toString().trim());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TestActivity.this,"Error"+error.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        question=new Question();
        result=new Result();
        for(int i=0;i<100;i++)
            score[i]=0;
        setQuestion();

//        int TIME=2*2*60;
        new CountDownTimer(totalQues*2*60*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                testTime.setText((millisUntilFinished / 1000)/60+"m "+(millisUntilFinished/1000)%60+"s");
            }
            public void onFinish() {
                for(int i=1;i<=100;i++){
                    mo=mo+score[i-1];
                }
                Toast.makeText(TestActivity.this, "Test finished! Your Score : "+mo, Toast.LENGTH_SHORT).show();
                finish();
            }
        }.start();

        saveAndNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                if(cqn==totalQues){
                    Toast.makeText(TestActivity.this, "No more questions!", Toast.LENGTH_SHORT).show();
                }
                else{
                    cqn++;
                    setQuestion();
                }
            }
        });

        saveAndPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                if(cqn==1){
                    Toast.makeText(TestActivity.this, "Already first question!", Toast.LENGTH_SHORT).show();
                }
                else{
                    cqn--;
                    setQuestion();
                }
            }
        });

        endTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=1;i<=10;i++){
                    mo=mo+score[i-1];
                }
                Toast.makeText(TestActivity.this, "Test finished! Your Score : "+mo, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(TestActivity.this,StudentDashboard.class));
//                result.setFullMarks(test.getFullMarks());
//                result.setMarksObtained(mo);
//                result.setTestName(test.getTestName());
//                int min = 100;
//                int max = 900;
//                int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
//
//                myRef=database.getReference(mAuth.getUid()).child("Results").child(test.getTestName()+random_int);
//                myRef.setValue(result).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful())
//                            Toast.makeText(TestActivity.this,"Your score:"+mo,Toast.LENGTH_SHORT).show();
//                        else
//                            Toast.makeText(TestActivity.this,"Error in uploading answers!",Toast.LENGTH_SHORT).show();
//                        finish();
//                        startActivity(new Intent(TestActivity.this,StudentDashboard.class));
//                    }
//                });
            }
        });
    }

    private void setQuestion(){
        qRef=database.getReference("AllTests").child(testName+testCode).child("Questions").child("Q"+cqn);
        qRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                question=snapshot.getValue(Question.class);
                if(question==null){
                    Toast.makeText(TestActivity.this, "Test not complete!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(TestActivity.this,StudentDashboard.class));
                }
                else{
                    testQuestion.setText("Q"+cqn+". "+question.getQue());
                    testOptA.setText("(a) "+question.getA());
                    testOptB.setText("(b) "+question.getB());
                    testOptC.setText("(c) "+question.getC());
                    testOptD.setText("(d) "+question.getD());
                    ans=question.getAns();
                    userAnswer.setText("Your answer: "+userAns[cqn-1]);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TestActivity.this,"Error"+error.getCode(),Toast.LENGTH_SHORT).show();
            }
        });
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