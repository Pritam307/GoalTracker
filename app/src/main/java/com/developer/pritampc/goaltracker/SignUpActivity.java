package com.developer.pritampc.goaltracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email_in,password_in;
    TextView signUp;
    Button btn_signup;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    private static final String TAG="FirebaseAuth";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.signup_btn).setOnClickListener(this);
        findViewById(R.id.signUp_tv).setOnClickListener(this);
        email_in=findViewById(R.id.email_in);
        password_in=findViewById(R.id.pass_in);
        progressBar=findViewById(R.id.progress);
        //progressBar.setVisibility(View.INVISIBLE);
    }


    public void registerUser()
    {
        String email=email_in.getText().toString().trim();
        String password=password_in.getText().toString().trim();
        if(email.isEmpty())
        {
            email_in.setError("Email is required!");
            email_in.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            email_in.setError("Please enter valid email");
            email_in.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            password_in.setError("password is required!");
            password_in.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            password_in.setError("Minimum password length is 6");
            password_in.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"User Registered Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                        }else
                        {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                Log.d(TAG, "onComplete: exception" + task.getException().getMessage());
                                Toast.makeText(getApplicationContext(), "User already Registered", Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.signup_btn:
                registerUser();


                break;
            case R.id.signUp_tv:
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }

}
