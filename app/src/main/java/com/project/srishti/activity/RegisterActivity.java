package com.project.srishti.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.srishti.R;
import com.project.srishti.User;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout email, name, dob, password, phone;
    private Button register;
    private RadioGroup radioGroup;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Users");
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        setContentView(R.layout.activity_register);
        email = (TextInputLayout) findViewById(R.id.email_reg);
        name = (TextInputLayout) findViewById(R.id.name_reg);
        dob = (TextInputLayout) findViewById(R.id.dob_reg);
        password = (TextInputLayout) findViewById(R.id.password_reg);
        phone = (TextInputLayout) findViewById(R.id.phone_reg);
        register = (Button) findViewById(R.id.register);
        radioGroup = (RadioGroup) findViewById(R.id.choiceRadioGroup);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try {
                    dialog = new ProgressDialog(RegisterActivity.this);
                    dialog.setMessage("Please wait...");
                    dialog.show();
                    mAuth.createUserWithEmailAndPassword(email.getEditText().getText().toString(), password.getEditText().getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    final FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String key = myRef.push().getKey();
                                        User u = new User(user.getUid(), email.getEditText().getText().toString(), name.getEditText().getText().toString(), dob.getEditText().getText().toString(), phone.getEditText().getText().toString(), ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString());
                                        myRef.child(key).setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dialog.dismiss();
                                                prefs.edit().putBoolean("auth", true).apply();
                                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialog.dismiss();
                                                user.delete();
                                                Snackbar.make(view, "An error occured", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Snackbar.make(view, "An error occured", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
