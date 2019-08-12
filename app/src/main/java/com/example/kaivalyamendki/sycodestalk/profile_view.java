package com.example.kaivalyamendki.sycodestalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profile_view extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView headerImage;
    TextView headerUsername, headerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);

        headerImage = (ImageView)header.findViewById(R.id.nav_profilePic);
        headerUsername = (TextView)header.findViewById(R.id.nav_username);
        headerEmail = (TextView)header.findViewById(R.id.nav_email);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        headerUsername.setText(user.getDisplayName());
        headerEmail.setText(user.getEmail());

        //Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(headerImage);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.menuLogout){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(profile_view.this, login_page.class));
        }
        else if(id == R.id.action_add){
            AddBlog addBlog = new AddBlog();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, addBlog, addBlog.getTag()).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            setTitle("CodeStalk Home");
            dashboard dash = new dashboard();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, dash, dash.getTag()).commit();
        }
        else if (id == R.id.nav_performance)
        {
            setTitle("Performance Analyzer");
            PerformanceAnalyzer analyzer = new PerformanceAnalyzer();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, analyzer, analyzer.getTag()).commit();
        }
        else if (id == R.id.nav_myProfile)
        {
            setTitle("My Profile");
            profile_show profile = new profile_show();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, profile, profile.getTag()).commit();
            /*setTitle("Performance Analyzer");
            PerformanceAnalyzer analyzer = new PerformanceAnalyzer();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, analyzer, analyzer.getTag()).commit();*/
        }
        else if (id == R.id.nav_follow)
        {
            setTitle("Followers/Following");
            follow_management followManagement = new follow_management();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, followManagement, followManagement.getTag()).commit();
        }
        else if (id == R.id.nav_forum)
        {
            setTitle("TechForum");
            TechForum techForum = new TechForum();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, techForum, techForum.getTag()).commit();
        }
        else if (id == R.id.nav_search)
        {
            setTitle("User Search");
            search_user search = new search_user();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, search, search.getTag()).commit();
        }
        else if(id == R.id.nav_contest)
        {
            setTitle("Upcoming Contests");
            UpcomingContests contests = new UpcomingContests();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, contests, contests.getTag()).commit();
        }
        else if(id == R.id.nav_codeAssistant)
        {
            setTitle("Code-Assistant");
            CodeAssistant codeAssistant = new CodeAssistant();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, codeAssistant, codeAssistant.getTag()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
