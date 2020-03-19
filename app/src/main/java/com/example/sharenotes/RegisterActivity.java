package com.example.sharenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private ImageView cloud1,star;
    Animation animCloud,animStar ;

    EditText email,password;
    Button register;
    ProgressDialog progressDialog;
    TextView alreadyacc;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        cloud1      = findViewById(R.id.cloud1);
        star        = findViewById(R.id.star);
        animCloud   = AnimationUtils.loadAnimation(this,R.anim.animcloud);
        animStar    = AnimationUtils.loadAnimation(this,R.anim.animstar);
        cloud1.startAnimation(animCloud);
        star.startAnimation(animStar);

        email = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        alreadyacc= findViewById(R.id.tvLogin);

        register=findViewById(R.id.btReg);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Plz Wait");

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailid= email.getText().toString().trim();
                String pass= password.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(emailid).matches())
                {
                    //set error to email edittext
                    email.setError("Invalid Email");
                    email.setFocusable(true);



                }
                else if(password.length()<6) {

                    //set error to email edittext
                    password.setError("Password length at least 6 char");
                    password.setFocusable(true);

                }
                else
                {
                    registerUser(emailid,pass);
                }

//                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
//                startActivity(intent);

            }
        });

        alreadyacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });


    }



    private void registerUser(String emailid, String pass)
    {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(emailid,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    progressDialog.dismiss();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
//                            updateUI(currentUser);
                    Toast.makeText(RegisterActivity.this, "Registered...\n"+currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                    finish();


                } else {
                    // If sign in fails, display a message to the user.
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }





}

