package com.example.bookworm.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookworm.AddingQuestion;
import com.example.bookworm.R;
import com.example.bookworm.TestActivity;
import com.example.bookworm.model.TeachersTest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AllTestsAdapter extends FirebaseRecyclerAdapter<TeachersTest,AllTestsAdapter.allTestViewHolder> {
    private Context context;
    public AllTestsAdapter(@NonNull FirebaseRecyclerOptions<TeachersTest> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final AllTestsAdapter.allTestViewHolder holder, int position, @NonNull final TeachersTest test) {
        holder.name.setText(test.getTestName());
        holder.roll.setText(""+test.getTestCode());
        holder.startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, TestActivity.class);
                intent.putExtra("TestName",test.getTestName());
                intent.putExtra("TestCode",test.getTestCode());
                intent.putExtra("TotalQues",test.getNoOfQues());
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @NonNull
    @Override
    public AllTestsAdapter.allTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_tests_row,parent,false);
        return new AllTestsAdapter.allTestViewHolder(view);
    }

    class allTestViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,roll;
        Button startTest;
        public allTestViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.user_name);
            roll=itemView.findViewById(R.id.roll_no);
            startTest=itemView.findViewById(R.id.start_test);
        }
    }
}
