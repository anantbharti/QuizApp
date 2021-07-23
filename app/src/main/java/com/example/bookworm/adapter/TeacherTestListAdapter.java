package com.example.bookworm.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookworm.AddingQuestion;
import com.example.bookworm.R;
import com.example.bookworm.model.TeachersTest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TeacherTestListAdapter extends FirebaseRecyclerAdapter<TeachersTest,TeacherTestListAdapter.TestViewHolder> {

    private Context context;
    public TeacherTestListAdapter(@NonNull FirebaseRecyclerOptions<TeachersTest> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final TestViewHolder holder, int position, @NonNull final TeachersTest test) {
        final String tstNam=test.getTestName();
        final int tstCode=test.getTestCode();
        holder.name.setText(test.getTestName());
        holder.roll.setText(""+test.getTestCode());
        holder.editTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, AddingQuestion.class);
                intent.putExtra("TestName",tstNam);
                intent.putExtra("TestCode",tstCode);
                intent.putExtra("TotalQues",test.getNoOfQues());
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth;
                auth=FirebaseAuth.getInstance();
                FirebaseDatabase database;
                database=FirebaseDatabase.getInstance();
                DatabaseReference tRef,reference;
                tRef=database.getReference(auth.getUid()).child("Tests").child(tstNam+tstCode);
                reference=database.getReference("AllTests").child(tstNam+tstCode);
                reference.removeValue();
                ((Activity)context).finish();
                tRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context,"Test deleted",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context,"Error in deletion!",Toast.LENGTH_SHORT).show();
                        }
                        ((Activity)context).finish();
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.teachers_test_row,parent,false);
        return new TestViewHolder(view);
    }

    class TestViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,roll;
        Button editTest;
        ImageView deleteBtn;
        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.user_name);
            roll=itemView.findViewById(R.id.roll_no);
            editTest=itemView.findViewById(R.id.edit_test);
            deleteBtn=itemView.findViewById(R.id.delete_btn);
        }
    }
}
