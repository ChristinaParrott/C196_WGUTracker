package com.example.wgutracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.wgutracker.viewmodel.MentorEditViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wgutracker.utilities.Constants.EDITING_KEY;
import static com.example.wgutracker.utilities.Constants.MENTOR_ID_KEY;

public class MentorEditorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.mentorName)
    TextView mTextView;

    @BindView(R.id.mentorPhone)
    TextView pTextView;

    @BindView(R.id.mentorEmail)
    TextView eTextView;

    private DrawerLayout drawer;

    private MentorEditViewModel mViewModel;

    private boolean mNewMentor, mEditing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        if(savedInstanceState != null){
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();

        getSupportActionBar().setTitle(R.string.edit_mentor);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->  {
            saveAndReturn();
        });
    }

    private void saveAndReturn() {
        String mentorName = mTextView.getText().toString();
        String mentorPhone = pTextView.getText().toString();
        String mentorEmail = eTextView.getText().toString();
        mViewModel.saveMentor(mentorName, mentorPhone, mentorEmail);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("mentor", mentorName);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this)
                .get(MentorEditViewModel.class);
        mViewModel.mLiveMentor.observe(this, mentorEntity -> {
            if(mentorEntity != null && !mEditing) {
                mTextView.setText(mentorEntity.getMentorName());
                pTextView.setText(mentorEntity.getMentorPhone());
                eTextView.setText(mentorEntity.getMentorEmail());
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras == null){
            setTitle("New Mentor");
            mNewMentor = true;
        }
        else{
            setTitle("Edit Mentor");
            int mentorId = extras.getInt(MENTOR_ID_KEY);
            mViewModel.loadData(mentorId);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (!mNewMentor){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.nav, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        }
        else if (item.getItemId() == R.id.action_delete){
            mViewModel.deleteMentor();
            Toast.makeText(MentorEditorActivity.this, "Mentor deleted.", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent i = new Intent(MentorEditorActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_courses:
                Intent i1 = new Intent(MentorEditorActivity.this, CourseViewActivity.class);
                startActivity(i1);
                break;
            case R.id.nav_terms:
                Intent i2 = new Intent(MentorEditorActivity.this, TermViewActivity.class);
                startActivity(i2);
                break;
            case R.id.nav_assessments:
                Intent i3 = new Intent(MentorEditorActivity.this, AssessmentViewActivity.class);
                startActivity(i3);
                break;
            case R.id.nav_mentors:
                Intent i4 = new Intent(MentorEditorActivity.this, MentorViewActivity.class);
                startActivity(i4);
                break;
            case R.id.nav_notes:
                Intent i5 = new Intent(MentorEditorActivity.this, NoteViewActivity.class);
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
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }
}