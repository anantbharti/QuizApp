package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookworm.model.Question;
import com.example.bookworm.model.User;
import com.google.android.gms.common.util.Clock;
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
    private Button saveQues,nextQues,prevQues;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    static int quesNo=1;
    DatabaseReference myRef,qRef;
    User user;
    HashMap<String,Question> oldQuestions=new HashMap<String, Question>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_question);
        setUpAddViews();
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
//        question=new Question();
//        myRef=database.getReference(mAuth.getUid());
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                user=snapshot.getValue(User.class);
//                addQNo=user.getCqn();
//                addQNo++;
//                addQuestionNo.setText("Q"+addQNo+":");
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(AddingQuestion.this,"Error"+error.getCode(),Toast.LENGTH_SHORT).show();
//            }
//        });

        saveQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Question> newQuestions=new HashMap<String, Question>();
                int qn=1;
                Iterator it = oldQuestions.entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry mapElement = (Map.Entry)it.next();
                    Question ques=(Question) mapElement.getValue();
                    ques.setQueNo(qn);
                    qn++;
                    newQuestions.put(""+qn,ques);
                }
                qRef=database.getReference("Tests").child("Questions");
                qRef.setValue(newQuestions).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddingQuestion.this,"Question uploaded successfully",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(AddingQuestion.this,TeacherDashboard.class));
                        }
                        else {
                            Toast.makeText(AddingQuestion.this,"Error in uploading question!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

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
//                    question.setQueNo(addQNo);

                    oldQuestions.put(""+quesNo,question);
                    quesNo++;
                    int qno=quesNo;
                    while (qno<101&&oldQuestions.containsKey(""+qno)==false){
                        qno++;
                    }
                    if(oldQuestions.containsKey(qno))
                    {
                        quesNo=qno;
                        Question nQ=oldQuestions.get(""+quesNo);
                        addQueStatement.setText(nQ.getQue());
                        addOptA.setText(nQ.getA());
                        addOptB.setText(nQ.getB());
                        addOptC.setText(nQ.getC());
                        addOptD.setText(nQ.getD());
                        addOptRadGrp.clearCheck();
                        String ro=nQ.getAns();
                        if(ro.equals("a"))
                            addRadA.setChecked(true);
                        else if(ro.equals("b"))
                            addRadB.setChecked(true);
                        else if(ro.equals("c"))
                            addRadC.setChecked(true);
                        else
                            addRadD.setChecked(true);
                    }
                    else if(oldQuestions.size()==50)
                    {
                        Toast.makeText(AddingQuestion.this,"Only 50 questions allowed!",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        addQueStatement.getText().clear();
                        addOptA.getText().clear();
                        addOptB.getText().clear();
                        addOptC.getText().clear();
                        addOptD.getText().clear();
                        addOptRadGrp.clearCheck();
                    }
                }
            }
        });
//        prevQues.setOnClickListener(new O);
    }

    private void setUpAddViews(){
        addQueStatement=findViewById(R.id.add_que_statement);
        addOptA=findViewById(R.id.add_option_a);
        addOptB=findViewById(R.id.add_option_b);
        addOptC=findViewById(R.id.add_option_c);
        addOptD=findViewById(R.id.add_option_d);
        addOptRadGrp=findViewById(R.id.add_opt_rad_grp);
        saveQues=findViewById(R.id.save_ques);
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