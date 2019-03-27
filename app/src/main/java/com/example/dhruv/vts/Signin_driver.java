package com.example.dhruv.vts;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signin_driver extends AppCompatActivity implements View.OnClickListener  {

    FirebaseAuth mAuth;
    EditText editTextEmail,editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_driver);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail=(EditText)findViewById(R.id.editextEmail);
        editTextPassword=(EditText)findViewById(R.id.edittextPassword);

        findViewById(R.id.button_login).setOnClickListener(this);


        //TextView tv=findViewById(R.id.textviewLogin);
        //tv.setOnClickListener(this);
    }
    private void userLogin(){

        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("email required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("plz enter valid email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.length()<6){
            editTextPassword.setError("min 6 char");
            editTextPassword.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("password req");
            editTextPassword.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    Intent intent= new Intent(Signin_driver.this,TestActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(this,TestActivity.class));
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                userLogin();
                break;
        }


    }
}


