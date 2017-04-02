package com.project.srishti.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.srishti.Post;
import com.project.srishti.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import mabbas007.tagsedittext.TagsEditText;

public class NewPostActivity extends AppCompatActivity {

    private TextView newPostName;
    private SharedPreferences prefs;
    private TextInputLayout input;
    private Button submit;
    private ProgressDialog dialog;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private RadioGroup group;
    private TagsEditText tagsEditText;
    private String tags = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        newPostName = (TextView) findViewById(R.id.new_post_name);
        newPostName.setText("Hey " + getIntent().getStringExtra("name") + ",");
        tagsEditText = (TagsEditText) findViewById(R.id.tagsEditText);
        tagsEditText.setTagsListener(new TagsEditText.TagsEditListener() {
            @Override
            public void onTagsChanged(Collection<String> collection) {
                tags = "";
                for (String s : collection) {
                    tags += s + ",";
                }
                if (collection.size() > 0)
                    tags = tags.substring(0, tags.length() - 1);
            }

            @Override
            public void onEditingFinished() {

            }
        });
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        input = (TextInputLayout) findViewById(R.id.new_post_content);
        submit = (Button) findViewById(R.id.submit_button);
        group = (RadioGroup) findViewById(R.id.new_post_type);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String text = input.getEditText().getText().toString();
                String type = getTextFromId(group.getCheckedRadioButtonId());
                String postId = UUID.randomUUID().toString();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String datestr = dateFormat.format(date); //2016/11/16 12:08:43
                Post p = new Post(postId, text, datestr, prefs.getString("username", "User"), type, tags);
                if (text.isEmpty()) {
                    input.setError("Enter some text");
                } else {
                    input.setError(null);
                    dialog = new ProgressDialog(NewPostActivity.this);
                    dialog.setMessage("Submitting post...");
                    dialog.show();
                    DatabaseReference ref = database.getReference("Posts");
                    String key = ref.push().getKey();
                    ref.child(key).setValue(p)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Snackbar.make(view, "An error occured", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private String getTextFromId(int checkedRadioButtonId) {
        switch (checkedRadioButtonId) {
            case R.id.post_type_adhd:
                return "ADHD";
            case R.id.post_type_anxiety:
                return "Anxiety";
            case R.id.post_type_depression:
                return "Depression";
            case R.id.post_type_eating:
                return "Eating disorder";
            case R.id.post_type_other:
                return "Other";
            case R.id.post_type_substance:
                return "Substance abuse";
        }
        return "";
    }
}
