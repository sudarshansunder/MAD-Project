package com.project.srishti.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.srishti.R;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout email, password;
    private Button login, register;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog dialog = null;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        email = (TextInputLayout) findViewById(R.id.email_login);
        password = (TextInputLayout) findViewById(R.id.password_login);
        login = (Button) findViewById(R.id.login_button);
        register = (Button) findViewById(R.id.register_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String emailStr = email.getEditText().getText().toString();
                String passwordStr = password.getEditText().getText().toString();
                boolean a = Patterns.EMAIL_ADDRESS.matcher(emailStr).matches();
                boolean b = !passwordStr.isEmpty();
                if (!a)
                    email.setError("Invalid email address");
                else
                    email.setError(null);
                if (!b)
                    password.setError("Enter a valid password");
                else
                    password.setError(null);
                if (a && b) {
                    dialog = new ProgressDialog(LoginActivity.this);
                    dialog.setMessage("Please wait...");
                    dialog.show();
                    mAuth.signInWithEmailAndPassword(emailStr, passwordStr)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    dialog.dismiss();
                                    prefs.edit().putBoolean("auth", true).apply();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Snackbar.make(view, "An error occured", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Snackbar.make(view, "Enter valid credentials", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
