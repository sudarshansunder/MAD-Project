package com.project.srishti.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.srishti.Post;
import com.project.srishti.R;
import com.project.srishti.User;
import com.project.srishti.adapter.RecyclerViewAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences prefs;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private DatabaseReference postRef;
    private TextView headerName, headerEmail;
    private FloatingActionButton fab;
    private ArrayList<Post> eatingList = new ArrayList<>();
    private ArrayList<Post> depressionList = new ArrayList<>();
    private ArrayList<Post> anxietyList = new ArrayList<>();
    private ArrayList<Post> adhdList = new ArrayList<>();
    private ArrayList<Post> substanceList = new ArrayList<>();
    private ArrayList<Post> otherList = new ArrayList<>();
    private int selected = R.id.nav_eating;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");
        postRef = database.getReference("Posts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        if (!prefs.getBoolean("auth", false)) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Eating Disorders");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) findViewById(R.id.button_new_post);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
                intent.putExtra("name", prefs.getString("username", "User"));
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_eating);
        View header = navigationView.getHeaderView(0);
        headerName = (TextView) header.findViewById(R.id.headerName);
        headerEmail = (TextView) header.findViewById(R.id.headerEmail);
        navigationView.setNavigationItemSelectedListener(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            prefs.edit().putBoolean("auth", false).apply();
        } else {
            ref.orderByChild("id").equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        User u = d.getValue(User.class);
                        headerName.setText(u.getName());
                        headerEmail.setText(u.getEmail());
                        prefs.edit().putString("username", u.getName()).putString("type", u.getType()).apply();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        postRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post p = dataSnapshot.getValue(Post.class);
                switch (p.getCat()) {
                    case "Eating disorder":
                        eatingList.add(p);
                        break;
                    case "Depression":
                        depressionList.add(p);
                        break;
                    case "Anxiety":
                        anxietyList.add(p);
                        break;
                    case "ADHD":
                        adhdList.add(p);
                        break;
                    case "Substance abuse":
                        substanceList.add(p);
                        break;
                    case "Other":
                        otherList.add(p);
                        break;

                }
                setListView(selected);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setListView(int id) {
        selected = id;
        switch (id) {
            case R.id.nav_adhd:
                getSupportActionBar().setTitle("ADHD");
                recyclerView.setAdapter(new RecyclerViewAdapter(adhdList, MainActivity.this));
                break;
            case R.id.nav_anxiety:
                getSupportActionBar().setTitle("Anxiety");
                recyclerView.setAdapter(new RecyclerViewAdapter(anxietyList, MainActivity.this));
                break;
            case R.id.nav_depression:
                getSupportActionBar().setTitle("Depression");
                recyclerView.setAdapter(new RecyclerViewAdapter(depressionList, MainActivity.this));
                break;
            case R.id.nav_eating:
                getSupportActionBar().setTitle("Eating Disorders");
                recyclerView.setAdapter(new RecyclerViewAdapter(eatingList, MainActivity.this));
                break;
            case R.id.nav_about:
                startActivityForResult(new Intent(MainActivity.this, ContactActivity.class), 69);
            case R.id.nav_other:
                getSupportActionBar().setTitle("Others");
                recyclerView.setAdapter(new RecyclerViewAdapter(otherList, MainActivity.this));
                break;
            case R.id.nav_substance:
                getSupportActionBar().setTitle("Substance Abuse");
                recyclerView.setAdapter(new RecyclerViewAdapter(substanceList, MainActivity.this));
                break;
            case R.id.nav_logout:
                prefs.edit().clear().apply();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 69) {
            navigationView.setCheckedItem(selected);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        setListView(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
