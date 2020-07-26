package com.example.wgutracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.wgutracker.viewmodel.CourseEditViewModel;
import com.example.wgutracker.viewmodel.NoteEditViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wgutracker.utilities.Constants.A_COURSE_ID_KEY;
import static com.example.wgutracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.wgutracker.utilities.Constants.NOTE_ID_KEY;

public class NoteDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.note_name)
    TextView nTextView;

    @BindView(R.id.note_content)
    TextView cTextView;

    @BindView(R.id.course_title)
    TextView rTextView;

    private DrawerLayout drawer;

    private NoteEditViewModel nViewModel;
    private CourseEditViewModel eViewModel;

    private Integer courseID, noteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        initViewModel();
        setCourseName();

        getSupportActionBar().setTitle(R.string.view_note);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(NoteDetailActivity.this, NoteEditorActivity.class);
            intent.putExtra(NOTE_ID_KEY, noteID);
            intent.putExtra(A_COURSE_ID_KEY, courseID);
            startActivity(intent);
        });
    }
    private void setCourseName() {
        eViewModel = new ViewModelProvider(this)
                .get(CourseEditViewModel.class);
        eViewModel.mLiveCourse.observe(this, courseEntity -> {
            if(courseEntity != null) {
                rTextView.setText(courseEntity.getCourseName());
            }
        });
        eViewModel.loadData(courseID);
    }
    private void initViewModel() {
        nViewModel = new ViewModelProvider(this)
                .get(NoteEditViewModel.class);
        nViewModel.mLiveNote.observe(this, noteEntity -> {
           if(noteEntity != null) {
                nTextView.setText(noteEntity.getNoteName());
                cTextView.setText(noteEntity.getNoteText());
            }
        });
        Bundle extras = getIntent().getExtras();
        noteID = extras.getInt(NOTE_ID_KEY);
        courseID = extras.getInt(COURSE_ID_KEY);
        nViewModel.loadData(noteID);
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_notify, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.action_delete) {
            nViewModel.deleteNote();
            Toast.makeText(NoteDetailActivity.this, "Note deleted.", Toast.LENGTH_SHORT).show();
            finish();
        }
            else if (item.getItemId() == R.id.action_alert) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Note Text: " + cTextView.getText().toString());
                sendIntent.putExtra(Intent.EXTRA_TITLE, "Note Name: " + nTextView.getText().toString());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent i = new Intent(NoteDetailActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_courses:
                Intent i1 = new Intent(NoteDetailActivity.this, CourseViewActivity.class);
                startActivity(i1);
                break;
            case R.id.nav_terms:
                Intent i2 = new Intent(NoteDetailActivity.this, TermViewActivity.class);
                startActivity(i2);
                break;
            case R.id.nav_assessments:
                Intent i3 = new Intent(NoteDetailActivity.this, AssessmentViewActivity.class);
                startActivity(i3);
                break;
            case R.id.nav_mentors:
                Intent i4 = new Intent(NoteDetailActivity.this, MentorViewActivity.class);
                startActivity(i4);
                break;
            case R.id.nav_notes:
                Intent i5 = new Intent(NoteDetailActivity.this, NoteViewActivity.class);
                startActivity(i5);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed () {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}