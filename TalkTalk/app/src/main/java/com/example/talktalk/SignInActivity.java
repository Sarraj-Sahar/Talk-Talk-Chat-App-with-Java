package com.example.talktalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.talktalk.databinding.ActivitySignInBinding;
import com.example.talktalk.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView (binding.getRoot());
        getSupportActionBar().hide();


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Logging In...");
        progressDialog.setMessage("Please Wait \nValidation in Progress");

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.txtEmail.getText().toString().isEmpty() && !binding.txtPassword.getText().toString().isEmpty())
                {
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful())
                                {
                                    //Once Sign in is successful , move to MainActivity
                                    //See Intents
                                    //An Intent is basically a message to say you did or want something to happen.
                                    Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(SignInActivity.this, task.getException().toString(),Toast.LENGTH_SHORT).show();
                                }
                        }
                    });
                }
                else
                {
                    Toast.makeText(SignInActivity.this,"Email or Password Missing",Toast.LENGTH_SHORT).show();
                }
            }
        });

       //  Skipping the SignIn page if user is already signed in
        if (mAuth.getCurrentUser() != null)
        {
            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
        }

        // Move to Sign Up page
        binding.txtClickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
}