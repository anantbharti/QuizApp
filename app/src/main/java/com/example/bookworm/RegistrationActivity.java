   package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bookworm.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

   public class RegistrationActivity extends AppCompatActivity {

       private EditText registerUserName,registerPassword,registerEmailId;
       private Button register;
       private RadioGroup regRadioGroup;
       private RadioButton regRadioBtn;
       private FirebaseAuth mAuth;
       FirebaseDatabase database;
       DatabaseReference myRef;
       User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setUpRegViews();
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        user = new User();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String regUsername=registerUserName.getText().toString().trim();
                String regPassword=registerPassword.getText().toString().trim();
                String regEmailId=registerEmailId.getText().toString().trim();
                int regRadId=regRadioGroup.getCheckedRadioButtonId();
                regRadioBtn=findViewById(regRadId);
                final String regDesignation=regRadioBtn.getText().toString().trim();
                if(regUsername.isEmpty()||regPassword.isEmpty()||regEmailId.isEmpty())
                    Toast.makeText(RegistrationActivity.this,"Fill all the details!",Toast.LENGTH_SHORT).show();
                else if(regPassword.length()<6)
                    Toast.makeText(RegistrationActivity.this,"Password too short!",Toast.LENGTH_SHORT).show();
                else{
                    user.setEmail(regEmailId);
                    user.setName(regUsername);
                    user.setPassword(regPassword);
                    user.setDesignation(regDesignation);
                    mAuth.createUserWithEmailAndPassword(regEmailId,regPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                myRef=database.getReference(mAuth.getUid());
                                 myRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                            Toast.makeText(RegistrationActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(RegistrationActivity.this,"Error in uploading data!",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                                Toast.makeText(RegistrationActivity.this,"Registration failed!",Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                        }
                    });}
            }
        });

    }

    private void setUpRegViews(){
        registerUserName=findViewById(R.id.register_user_name);
        registerEmailId=findViewById(R.id.register_email_id);
        registerPassword=findViewById(R.id.register_password);
        register=findViewById(R.id.register_button);
        regRadioGroup=findViewById(R.id.register_radio_group);
    }
}