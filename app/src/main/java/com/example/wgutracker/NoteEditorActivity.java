package com.example.wgutracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.wgutracker.database.CourseEntity;
import com.example.wgutracker.viewmodel.CourseEditViewModel;
import com.example.wgutracker.viewmodel.CourseViewModel;
import com.example.wgutracker.viewmodel.NoteEditViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wgutracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.wgutracker.utilities.Constants.EDITING_KEY;
import static com.example.wgutracker.utilities.Constants.NOTE_ID_KEY;

public class NoteEditorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemSelectedListener {

    @BindView(R.id.note_name)
    EditText nTextView;

    @BindView(R.id.note_content)
    EditText cTextView;

    @BindView(R.id.course_spinner)
    Spinner mCourseSpinner;

    private DrawerLayout drawer;

    private NoteEditViewModel nViewModel;
    private CourseViewModel cViewModel;
    private CourseEditViewModel eViewModel;

    private ArrayAdapter<CourseEntity> cAdapter;

    private boolean mNewNote, mEditing;

    private Spinner courseSpinner;
    private Integer courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        if(savedInstanceState != null){
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
            if (savedInstanceState.getInt(COURSE_ID_KEY) != 0){
                courseID = savedInstanceState.getInt(COURSE_ID_KEY);
            }
        }

        initCourseSpinner();
        initViewModel();

        if(!mNewNote && !mEditing) {
            initSpinnerSelect();
        }

        getSupportActionBar().setTitle(R.string.edit_note);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        Button newCourse = findViewById(R.id.new_course);
        newCourse.setOnClickListener(v -> {
            cAdapter.clear();
            Intent i1 = new Intent(NoteEditorActivity.this, CourseEditorActivity.class);
            startActivity(i1);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            saveAndReturn();
        });
    }

    private void initSpinnerSelect() {
        eViewModel = new ViewModelProvider(this)
                .get(CourseEditViewModel.class);
        eViewModel.mLiveCourse.observe(this, courseEntity -> {
            int p =  (searchList());
            if (p > -1) {
                courseSpinner.setSelection(p);
            }
        });
        eViewModel.loadData(courseID);
    }
    private int searchList(){
        for(int i =0; i<cAdapter.getCount(); i++){
            if(cAdapter.getItem(i).getCourseID() == courseID)
                return i;
        }
        return -1;
    }

    private void initCourseSpinner() {
        courseSpinner = findViewById(R.id.course_spinner);
        cAdapter = new ArrayAdapter<CourseEntity>(
                this, android.R.layout.simple_spinner_item);
        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(cAdapter);
        courseSpinner.setOnItemSelectedListener(this);

        cViewModel = new ViewModelProvider(this)
                .get(CourseViewModel.class);
        cViewModel.mcourses.observe(this, courseEntities -> {
            cAdapter.addAll(courseEntities);
        });
    }

    private void saveAndReturn() {
        String noteName = nTextView.getText().toString();
        String noteText = cTextView.getText().toString();
        CourseEntity selectedCourse = (CourseEntity) mCourseSpinner.getSelectedItem();
        int courseID = selectedCourse.getCourseID();
        nViewModel.saveNote(noteName, noteText, courseID);
        finish();
    }

    private void initViewModel() {
        nViewModel = new ViewModelProvider(this)
                .get(NoteEditViewModel.class);
        nViewModel.mLiveNote.observe(this, noteEntity -> {
           if(noteEntity != null && !mEditing) {
                nTextView.setText(noteEntity.getNoteName());
                cTextView.setText(noteEntity.getNoteText());
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras == null){
            setTitle("New Note");
            mNewNote = true;
        }
        else{
            setTitle("Edit Note");
            int noteId = extras.getInt(NOTE_ID_KEY);
            courseID = extras.getInt(COURSE_ID_KEY);
            nViewModel.loadData(noteId);
            mNewNote = false;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (!mNewNote){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.nav_notify, menu);
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
            nViewModel.deleteNote();
            Toast.makeText(NoteEditorActivity.this, "Note deleted.", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(NoteEditorActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_courses:
                Intent i1 = new Intent(NoteEditorActivity.this, CourseViewActivity.class);
                startActivity(i1);
                break;
            case R.id.nav_terms:
                Intent i2 = new Intent(NoteEditorActivity.this, TermViewActivity.class);
                startActivity(i2);
                break;
            case R.id.nav_assessments:
                Intent i3 = new Intent(NoteEditorActivity.this, AssessmentViewActivity.class);
                startActivity(i3);
                break;
            case R.id.nav_mentors:
                Intent i4 = new Intent(NoteEditorActivity.this, MentorViewActivity.class);
                startActivity(i4);
                break;
            case R.id.nav_notes:
                Intent i5 = new Intent(NoteEditorActivity.this, NoteViewActivity.class);
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
        outState.putInt(COURSE_ID_KEY, courseSpinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int selIndex = courseSpinner.getSelectedItemPosition();
        if(selIndex != 0){
            courseSpinner.setSelection(selIndex);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}