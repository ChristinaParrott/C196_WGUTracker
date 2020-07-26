package com.example.wgutracker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.wgutracker.database.MentorEntity;
import com.example.wgutracker.database.TermEntity;
import com.example.wgutracker.viewmodel.CourseEditViewModel;
import com.example.wgutracker.viewmodel.MentorEditViewModel;
import com.example.wgutracker.viewmodel.MentorViewModel;
import com.example.wgutracker.viewmodel.TermEditViewModel;
import com.example.wgutracker.viewmodel.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wgutracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.wgutracker.utilities.Constants.EDITING_KEY;
import static com.example.wgutracker.utilities.Constants.END_DATE_KEY;
import static com.example.wgutracker.utilities.Constants.MENTOR_ID_KEY;
import static com.example.wgutracker.utilities.Constants.MENTOR_ID_KEY_C;
import static com.example.wgutracker.utilities.Constants.NEW_MENTOR_KEY;
import static com.example.wgutracker.utilities.Constants.NEW_TERM_KEY;
import static com.example.wgutracker.utilities.Constants.START_DATE_KEY;
import static com.example.wgutracker.utilities.Constants.TERM_ID_KEY;
import static com.example.wgutracker.utilities.Constants.TERM_ID_KEY_C;

public class CourseEditorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.courseName)
    TextView mTextView;

    @BindView(R.id.startDate)
    TextView sTextView;

    @BindView(R.id.endDate)
    TextView eTextView;

    @BindView(R.id.status)
    TextView tTextView;

    @BindView(R.id.termPicker)
    Spinner mTermSpinner;

    @BindView(R.id.mentorPicker)
    Spinner mMentorSpinner;


    private DrawerLayout drawer;

    private CourseEditViewModel mViewModel;
    private TermViewModel tViewModel;
    private TermEditViewModel eViewModel;
    private MentorViewModel rViewModel;
    private MentorEditViewModel dViewModel;

    private ArrayAdapter<TermEntity> tAdapter;
    private ArrayAdapter<MentorEntity> mentorAdapter;

    private boolean mNewCourse, mEditing, mNewMentor, mNewTerm;

    private Spinner termSpinner, mentorSpinner;

    private TextView clickedText;
    private int courseId, termID, mentorID;

    private TextView notificationText;

    private long timeInMillis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
            sTextView.setText(savedInstanceState.getString(START_DATE_KEY));
            eTextView.setText(savedInstanceState.getString(END_DATE_KEY));
            mNewTerm = savedInstanceState.getBoolean(NEW_TERM_KEY);
            mNewMentor = savedInstanceState.getBoolean(NEW_MENTOR_KEY);
            if (savedInstanceState.getInt(MENTOR_ID_KEY) != 0){
                mentorID = savedInstanceState.getInt(MENTOR_ID_KEY);
            }
            if (savedInstanceState.getInt(TERM_ID_KEY) != 0){
                termID = savedInstanceState.getInt(MENTOR_ID_KEY);
            }
        }

        initTermSpinner();
        initMentorSpinner();
        initViewModel();

        if (!mNewCourse && !mEditing) {
            initSpinnerTermSelect();
            initSpinnerMentorSelect();
        }


        getSupportActionBar().setTitle(R.string.edit_course);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        TextView startDate = findViewById(R.id.startDate);
        startDate.setOnClickListener(v -> {
            clickedText = (TextView) v;
            DialogFragment sdatePicker = new DatePickerFragment();
            sdatePicker.show(getSupportFragmentManager(), "start date picker");
        });

        TextView endDate = findViewById(R.id.endDate);
        endDate.setOnClickListener(v -> {
            clickedText = (TextView) v;
            DialogFragment edatePicker = new DatePickerFragment();
            edatePicker.show(getSupportFragmentManager(), "end date picker");
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            saveAndReturn();
        });

        Button newMentor = findViewById(R.id.new_mentor);
        newMentor.setOnClickListener(v -> {
            mNewMentor = true;
            mentorAdapter.clear();
            Intent i1 = new Intent(CourseEditorActivity.this, MentorEditorActivity.class);
            startActivity(i1);
        });

        Button newTerm = findViewById(R.id.new_term);
        newTerm.setOnClickListener(v -> {
            mNewTerm = true;
            tAdapter.clear();
            Intent i1 = new Intent(CourseEditorActivity.this, TermEditorActivity.class);
            startActivity(i1);
        });

        Button newNote = findViewById(R.id.note_btn);
        newNote.setOnClickListener(v -> {
            Intent i1 = new Intent(CourseEditorActivity.this, NoteEditorActivity.class);
            startActivity(i1);
        });

        TextView notification = findViewById(R.id.notification_date);
        notification.setOnClickListener(v -> {
            clickedText = (TextView) v;
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");;
        });

        Button setNotify = findViewById(R.id.set_notification);
        setNotify.setOnClickListener(v -> {
            if(timeInMillis == 0L){
                Toast.makeText(CourseEditorActivity.this, "You must first select a date on which to notify.", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(CourseEditorActivity.this, MyReceiver.class);
                Toast.makeText(CourseEditorActivity.this, "Notification set for course titled " + mTextView.getText()
                        , Toast.LENGTH_LONG).show();
                intent.putExtra("key", "Course titled " + mTextView.getText() + " starts on " + sTextView.getText() +
                        " and ends on " + eTextView.getText());
                PendingIntent sender = PendingIntent.getBroadcast(CourseEditorActivity.this, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
            }
        });
    }

    private void initSpinnerTermSelect() {
        eViewModel = new ViewModelProvider(this)
                .get(TermEditViewModel.class);
        eViewModel.mLiveTerm.observe(this, termEntity -> {
            int p = (searchTermList());
            if (p > -1) {
                termSpinner.setSelection(p, false);
            }
        });
        eViewModel.loadData(termID);
    }

    private int searchTermList() {
        for (int i = 0; i < tAdapter.getCount(); i++) {
            if (tAdapter.getItem(i).getTermID() == termID)
                return i;
        }
        return -1;
    }

    private void initSpinnerMentorSelect() {
        dViewModel = new ViewModelProvider(this)
                .get(MentorEditViewModel.class);
        dViewModel.mLiveMentor.observe(this, mentorEntity -> {
            int p = (searchMentorList());
            if (p > -1) {
                mentorSpinner.setSelection(p, false);
            }
        });
        dViewModel.loadData(mentorID);
    }

    private int searchMentorList() {
        for (int i = 0; i < mentorAdapter.getCount(); i++) {
            if (mentorAdapter.getItem(i).getMentorID() == mentorID)
                return i;
        }
        return -1;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        timeInMillis = c.getTimeInMillis();
        String selectedDate = DateFormat.getDateInstance().format(c.getTime());
        clickedText.setText(selectedDate);
    }

    private void initMentorSpinner() {
        mentorSpinner = findViewById(R.id.mentorPicker);
        mentorAdapter = new ArrayAdapter<MentorEntity>(
                this, android.R.layout.simple_spinner_item);
        mentorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mentorSpinner.setAdapter(mentorAdapter);
        mentorSpinner.setOnItemSelectedListener(this);

        rViewModel = new ViewModelProvider(this)
                .get(MentorViewModel.class);
        rViewModel.mmentor.observe(this, mentorEntities -> {
            mentorAdapter.addAll(mentorEntities);
        });
    }

    private void initTermSpinner() {
        termSpinner = findViewById(R.id.termPicker);
        tAdapter = new ArrayAdapter<TermEntity>(
                this, android.R.layout.simple_spinner_item);
        tAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termSpinner.setAdapter(tAdapter);
        termSpinner.setOnItemSelectedListener(this);

        tViewModel = new ViewModelProvider(this)
                .get(TermViewModel.class);
        tViewModel.mterms.observe(this, termEntities -> {
            tAdapter.addAll(termEntities);
        });
    }

    private void saveAndReturn() {
        String courseName = mTextView.getText().toString();
        String courseStart = sTextView.getText().toString();
        String courseEnd = eTextView.getText().toString();
        String courseStatus = tTextView.getText().toString();
        TermEntity termSelected = (TermEntity) termSpinner.getSelectedItem();
        MentorEntity mentorSelected = (MentorEntity) mentorSpinner.getSelectedItem();
        int termID = termSelected.getTermID();
        int mentorID = mentorSelected.getMentorID();

        mViewModel.saveCourse(courseName, courseStart, courseEnd, courseStatus, termID, mentorID);
        finish();
    }


    private void initViewModel() {
        mViewModel = new ViewModelProvider(this)
                .get(CourseEditViewModel.class);
        mViewModel.mLiveCourse.observe(this, cEntity -> {
            if (cEntity != null && !mEditing) {
                mTextView.setText(cEntity.getCourseName());
                sTextView.setText(DatetoString(cEntity.getCourseStart()));
                eTextView.setText(DatetoString(cEntity.getCourseEnd()));
                tTextView.setText(cEntity.getCourseStatus());
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle(getString(R.string.new_course));
            mNewCourse = true;
        } else {
            setTitle(getString(R.string.edit_course));
            courseId = extras.getInt(COURSE_ID_KEY);
            mViewModel.loadData(courseId);
            termID = extras.getInt(TERM_ID_KEY_C);
            mentorID = extras.getInt(MENTOR_ID_KEY_C);
        }
    }

    private String DatetoString(Date date) {
        String pattern = "MMMM dd, yyyy";
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        String dateStr = sd.format(date);
        return dateStr;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNewCourse) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.nav_notify, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            if (courseId == 0) {
                Toast.makeText(CourseEditorActivity.this, "ERROR: Can't delete this course because it doesn't exist",
                        Toast.LENGTH_SHORT).show();
            } else {
                LiveData<Integer> count = mViewModel.getAssessCountForCourse(courseId);
                count.observe(this, integer -> {
                    if (count.getValue() == 0) {
                        mViewModel.deleteCourse();
                        finish();
                        Toast.makeText(CourseEditorActivity.this, "Course deleted",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CourseEditorActivity.this, "ERROR: Can't delete this course because it contains assessments",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        if (item.getItemId() == R.id.action_alert) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Course ends on " + eTextView.getText() + " for " +
                    mTextView.getText());
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Course Reminder: " + mTextView.getText());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent i = new Intent(CourseEditorActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_courses:
                Intent i1 = new Intent(CourseEditorActivity.this, CourseViewActivity.class);
                startActivity(i1);
                break;
            case R.id.nav_terms:
                Intent i2 = new Intent(CourseEditorActivity.this, TermViewActivity.class);
                startActivity(i2);
                break;
            case R.id.nav_assessments:
                Intent i3 = new Intent(CourseEditorActivity.this, AssessmentViewActivity.class);
                startActivity(i3);
                break;
            case R.id.nav_mentors:
                Intent i4 = new Intent(CourseEditorActivity.this, MentorViewActivity.class);
                startActivity(i4);
                break;
            case R.id.nav_notes:
                Intent i5 = new Intent(CourseEditorActivity.this, NoteViewActivity.class);
                startActivity(i5);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        outState.putBoolean(NEW_TERM_KEY, mNewTerm);
        outState.putBoolean(NEW_MENTOR_KEY, mNewMentor);
        String startDate = sTextView.getText().toString();
        String endDate = eTextView.getText().toString();
        outState.putString(START_DATE_KEY, startDate);
        outState.putString(END_DATE_KEY, endDate);
        outState.putInt(MENTOR_ID_KEY, mentorSpinner.getSelectedItemPosition());
        outState.putInt(TERM_ID_KEY, termSpinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int selIndexTerm = termSpinner.getSelectedItemPosition();
        if (selIndexTerm != 0) {
            termSpinner.setSelection(selIndexTerm);
        }
        int selIndexMentor = mentorSpinner.getSelectedItemPosition();
        if (selIndexMentor != 0) {
            mentorSpinner.setSelection(selIndexMentor);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}
