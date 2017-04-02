package com.project.srishti.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.srishti.Comment;
import com.project.srishti.MaterialColorPalette;
import com.project.srishti.Post;
import com.project.srishti.R;
import com.project.srishti.adapter.CommentAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class CommentsActivity extends AppCompatActivity {

    private TextView poster, content, date;
    private Button commentButton;
    private ImageView posterImage;
    private EditText newComment;
    private RecyclerView commentList;
    private Toolbar toolbar;
    private ArrayList<Comment> list = new ArrayList<>();
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.comment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        poster = (TextView) findViewById(R.id.commentPostName);
        content = (TextView) findViewById(R.id.commentPostDescription);
        date = (TextView) findViewById(R.id.dateOfPostComment);
        posterImage = (ImageView) findViewById(R.id.commentPostImage);
        newComment = (EditText) findViewById(R.id.commentText);
        commentButton = (Button) findViewById(R.id.commentPostButton);
        final DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("Comments");
        final Post p = getIntent().getParcelableExtra("post");
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog = new ProgressDialog(CommentsActivity.this);
                dialog.setMessage("Posting comment...");
                dialog.show();
                if (!newComment.getText().toString().isEmpty()) {
                    String key = commentsRef.push().getKey();
                    String commentId = UUID.randomUUID().toString();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String datestr = dateFormat.format(date); //2016/11/16 12:08:43
                    Comment c = new Comment(commentId, prefs.getString("username", "User"), newComment.getText().toString(), prefs.getString("type", "User"), datestr, p.getId());
                    commentsRef.child(key).setValue(c)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.dismiss();
                                    newComment.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        int color = MaterialColorPalette.getRandomColor("500");
        TextDrawable drawable = TextDrawable.builder().buildRound(p.getName().charAt(0) + "", color);
        posterImage.setImageDrawable(drawable);
        poster.setText(p.getName());
        content.setText(p.getContent());
        date.setText(parseDateTime(p.getDate()));
        commentList = (RecyclerView) findViewById(R.id.comment_list);
        commentList.setLayoutManager(new LinearLayoutManager(this));
        commentsRef.orderByChild("postid").equalTo(p.getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Comment child added", "added");
                list.add(dataSnapshot.getValue(Comment.class));
                commentList.setAdapter(new CommentAdapter(CommentsActivity.this, list));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static String parseDateTime(String dateTime) {
        //Input : 2016-12-10 15:35:46
        //Input : 2017/03/26 00:15:59
        String date = "";
        String temp = dateTime.substring(0, 10);
        String arrs[] = temp.split("/");
        String month = null;
        switch (arrs[1]) {
            case "01":
                month = "Jan";
                break;
            case "02":
                month = "Feb";
                break;
            case "03":
                month = "Mar";
                break;
            case "04":
                month = "Apr";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "Jun";
                break;
            case "07":
                month = "Jul";
                break;
            case "08":
                month = "Aug";
                break;
            case "09":
                month = "Sep";
                break;
            case "10":
                month = "Oct";
                break;
            case "11":
                month = "Nov";
                break;
            case "12":
                month = "Dec";
                break;
        }
        String time = "";
        int day = Integer.valueOf(arrs[2]);
        date = "" + month + " " + day;
        time = dateTime.substring(11, dateTime.length() - 3);
        return "" + date + " at " + time;
    }

}
