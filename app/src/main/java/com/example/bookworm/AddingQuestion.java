package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookworm.model.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AddingQuestion extends AppCompatActivity {

    private EditText addQueStatement,addOptA,addOptB,addOptC,addOptD;
    private RadioGroup addOptRadGrp;
    private TextView addQuestionNo;
    private RadioButton addRadA,addRadB,addRadC,addRadD,addAnsRadBtn;
    private Button nextQues,prevQues;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    private static int quesNo=1;
    static DatabaseReference myRef,qRef;
    private static String testName;
    private static int totalQues,testCode;

    static HashMap<String,Question> oldQuestions=new HashMap<String, Question>();
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_question);
        setUpAddViews();
        mAuth=FirebaseAuth.getInstance();
        question=new Question();
        database=FirebaseDatabase.getInstance();
        testCode=getIntent().getExtras().getInt("TestCode");
        testName=getIntent().getExtras().getString("TestName");
        totalQues=getIntent().getExtras().getInt("TotalQues");
        setQues();

        nextQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addQue=addQueStatement.getText().toString().trim();
                String addA=addOptA.getText().toString().trim();
                String addB=addOptB.getText().toString().trim();
                String addC=addOptC.getText().toString().trim();
                String addD=addOptD.getText().toString().trim();
                if(addA.isEmpty()||addB.isEmpty()||addC.isEmpty()||addD.isEmpty()||addQue.isEmpty()){
                    Toast.makeText(AddingQuestion.this,"Empty field!",Toast.LENGTH_SHORT).show();
                }
                else if(!addRadA.isChecked()&& !addRadB.isChecked()&& !addRadC.isChecked()&& !addRadD.isChecked()){
                    Toast.makeText(AddingQuestion.this,"Select a correct option!",Toast.LENGTH_SHORT).show();
                }
                else{
                    int addAnsRadId=addOptRadGrp.getCheckedRadioButtonId();
                    addAnsRadBtn=findViewById(addAnsRadId);
                    String addAns= addAnsRadBtn.getText().toString().trim();
                    Question question = new Question();
                    question.setQue(addQue);
                    question.setA(addA);
                    question.setB(addB);
                    question.setC(addC);
                    question.setD(addD);
                    question.setAns(addAns);
                    question.setQueNo(quesNo);
                    oldQuestions.put("Q"+quesNo,question);

                    if(quesNo==totalQues){
                        quesNo=1;
                        upload();
                    }else{
                        quesNo++;
                        setQues();
                    }
                }
            }
        });

        prevQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addQue=addQueStatement.getText().toString().trim();
                String addA=addOptA.getText().toString().trim();
                String addB=addOptB.getText().toString().trim();
                String addC=addOptC.getText().toString().trim();
                String addD=addOptD.getText().toString().trim();
                if(addA.isEmpty()||addB.isEmpty()||addC.isEmpty()||addD.isEmpty()||addQue.isEmpty()){
                    Toast.makeText(AddingQuestion.this,"Empty field!",Toast.LENGTH_SHORT).show();
                }
                else if(!addRadA.isChecked()&& !addRadB.isChecked()&& !addRadC.isChecked()&& !addRadD.isChecked()){
                    Toast.makeText(AddingQuestion.this,"Select a correct option!",Toast.LENGTH_SHORT).show();
                }
                else{
                    int addAnsRadId=addOptRadGrp.getCheckedRadioButtonId();
                    addAnsRadBtn=findViewById(addAnsRadId);
                    String addAns= addAnsRadBtn.getText().toString().trim();
                    Question question = new Question();
                    question.setQue(addQue);
                    question.setA(addA);
                    question.setB(addB);
                    question.setC(addC);
                    question.setD(addD);
                    question.setAns(addAns);
                    question.setQueNo(quesNo);
                    oldQuestions.put("Q"+quesNo,question);

                    if(quesNo==1){
                        Toast.makeText(AddingQuestion.this,"Already first question!",Toast.LENGTH_SHORT).show();
                    }else{
                        quesNo--;
                        setQues();
                    }
                }
            }
        });
    }

    private void upload() {
        qRef=database.getReference("AllTests").child(testName+testCode).child("Questions");
        qRef.setValue(oldQuestions).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddingQuestion.this,"Questions saved",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(AddingQuestion.this,TeacherDashboard.class));
                }
                else {
                    Toast.makeText(AddingQuestion.this,"Error in uploading question!",Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    private void setQues() {
        if(quesNo==totalQues){
            nextQues.setText("Save");
        }
        myRef=database.getReference("AllTests").child(testName+testCode).child("Questions").child("Q"+quesNo);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                question=snapshot.getValue(Question.class);
                if(question!=null){
                    addQueStatement.setText(question.getQue());
                    addQuestionNo.setText("Q"+quesNo);
                    addOptA.setText(question.getA());
                    addOptB.setText(question.getB());
                    addOptC.setText(question.getC());
                    addOptD.setText(question.getD());
                    addOptRadGrp.clearCheck();
                    String ro=question.getAns();
                    if(ro.equals("a"))
                        addRadA.setChecked(true);
                    else if(ro.equals("b"))
                        addRadB.setChecked(true);
                    else if(ro.equals("c"))
                        addRadC.setChecked(true);
                    else
                        addRadD.setChecked(true);
                }
                else {
                    addQueStatement.setText("");
                    addQuestionNo.setText("Q"+quesNo);
                    addOptA.setText("");
                    addOptB.setText("");
                    addOptC.setText("");
                    addOptD.setText("");
                    addOptRadGrp.clearCheck();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddingQuestion.this,"Error"+error.getCode(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpAddViews(){
        addQueStatement=findViewById(R.id.add_que_statement);
        addOptA=findViewById(R.id.add_option_a);
        addOptB=findViewById(R.id.add_option_b);
        addOptC=findViewById(R.id.add_option_c);
        addOptD=findViewById(R.id.add_option_d);
        addOptRadGrp=findViewById(R.id.add_opt_rad_grp);
        addRadA=findViewById(R.id.add_rad_optA);
        addRadB=findViewById(R.id.add_rad_optB);
        addRadC=findViewById(R.id.add_rad_optC);
        addRadD=findViewById(R.id.add_rad_optD);
        addQuestionNo=findViewById(R.id.add_que_no);
        nextQues=findViewById(R.id.next_ques);
        prevQues=findViewById(R.id.prev_ques);
    }

    public void goToDashboard(View view)
    {
        startActivity(new Intent(AddingQuestion.this,TeacherDashboard.class));
        finish();
    }
}