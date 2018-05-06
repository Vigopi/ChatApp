package com.example.vinothgopigraj.chatapp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText name,email,password;
    private Button registerBtn,loginBtn;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.nameText);
        email = (EditText) findViewById(R.id.emailText);
        password = (EditText) findViewById(R.id.passwordText);
        registerBtn = (Button) findViewById(R.id.registerButton);
        loginBtn = (Button) findViewById(R.id.loginButton);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name_content,email_content,password_content;
                name_content = name.getText().toString().trim();
                email_content = email.getText().toString().trim();
                password_content = password.getText().toString().trim();
                if(!TextUtils.isEmpty(name_content) && !TextUtils.isEmpty(email_content) && !TextUtils.isEmpty(password_content))
                {
                    auth.createUserWithEmailAndPassword(email_content,password_content).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                String user_id = auth.getCurrentUser().getUid();
                                DatabaseReference current_user_id = mDatabase.child(user_id);
                                current_user_id.child("Name").setValue(name_content);
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            }
                        }
                    });
                }
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }

    public void registerButtonClicked(View view){
        final String name_content,email_content,password_content;
        name_content = name.getText().toString().trim();
        email_content = email.getText().toString().trim();
        password_content = password.getText().toString().trim();
        if(!TextUtils.isEmpty(name_content) && !TextUtils.isEmpty(email_content) && !TextUtils.isEmpty(password_content))
        {
            auth.createUserWithEmailAndPassword(email_content,password_content).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        String user_id = auth.getCurrentUser().getUid();
                        DatabaseReference current_user_id = mDatabase.child(user_id);
                        current_user_id.child("Name").setValue(name_content);
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    }
                }
            });
        }
    }

    public void loginButtonClicked(View view){
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
    }
}
