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
import androidx.lifecycle.ViewModelProvider;

import com.example.wgutracker.database.CourseEntity;
import com.example.wgutracker.viewmodel.AssessEditViewModel;
import com.example.wgutracker.viewmodel.CourseEditViewModel;
import com.example.wgutracker.viewmodel.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wgutracker.utilities.Constants.ASSESS_ID_KEY;
import static com.example.wgutracker.utilities.Constants.A_COURSE_ID_KEY;
import static com.example.wgutracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.wgutracker.utilities.Constants.DATE_KEY;
import static com.example.wgutracker.utilities.Constants.EDITING_KEY;

public class AssessEditorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener{

    @BindView(R.id.assessName)
    TextView mTextView;

    @BindView(R.id.assessType)
    TextView tTextView;

    @BindView(R.id.assessDate)
    TextView dTextView;

    @BindView(R.id.coursePicker)
    Spinner mCourseSpinner;

    private DrawerLayout drawer;

    private AssessEditViewModel mViewModel;
    private CourseViewModel cViewModel;
    private CourseEditViewModel eViewModel;

    private ArrayAdapter<CourseEntity> cAdapter;

    private boolean mNewAssess, mEditing;

    private Spinner courseSpinner;
    private Integer courseID;

    private TextView clickedText, notificationText;

    private String datePickType;

    long timeInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        if(savedInstanceState != null){
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
            dTextView.setText(savedInstanceState.getString(DATE_KEY));
            if (savedInstanceState.getInt(COURSE_ID_KEY) != 0){
                courseID = savedInstanceState.getInt(COURSE_ID_KEY);
            }
        }

        initCourseSpinner();
        initViewModel();


        if(!mNewAssess && !mEditing) {
            initSpinnerSelect();
        }

        getSupportActionBar().setTitle(R.string.edit_assess);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        TextView clicked = findViewById(R.id.assessDate);
        clicked.setOnClickListener(v -> {
            clickedText = (TextView) v;
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
            datePickType = "assessDate";
        });

        TextView notification = findViewById(R.id.notification_date);
        notification.setOnClickListener(v -> {
                    DialogFragment datePicker = new DatePickerFragment();
                    datePicker.show(getSupportFragmentManager(), "date picker");
                    datePickType = "notify";
                });

        Button setNotify = findViewById(R.id.set_notification);
        setNotify.setOnClickListener(v -> {
            if(timeInMillis == 0L){
                Toast.makeText(AssessEditorActivity.this, "You must first select a date on which to notify.", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(AssessEditorActivity.this, MyReceiver.class);
                Toast.makeText(AssessEditorActivity.this, "Notification set for assessment titled " + mTextView.getText()
                        + " on " + dTextView.getText(), Toast.LENGTH_LONG).show();
                intent.putExtra("key", "Assessment titled " + mTextView.getText() + " on " + dTextView.getText());
                PendingIntent sender = PendingIntent.getBroadcast(AssessEditorActivity.this, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
            }
        });

        Button newCourse = findViewById(R.id.new_course);
        newCourse.setOnClickListener(v -> {
            cAdapter.clear();
            Intent i1 = new Intent(AssessEditorActivity.this, CourseEditorActivity.class);
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
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        if (datePickType == "assessDate"){
            String selectedDate = DateFormat.getDateInstance().format(c.getTime());
            clickedText.setText(selectedDate); }
        if (datePickType == "notify") {
            timeInMillis = c.getTimeInMillis();
            String selectedDate = DateFormat.getDateInstance().format(c.getTime());
            notificationText = findViewById(R.id.notification_date);
            notificationText.setText(selectedDate);
        }
    }

    private void initCourseSpinner() {
        courseSpinner = findViewById(R.id.coursePicker);
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
        String assessName = mTextView.getText().toString();
        String assessType = tTextView.getText().toString();
        String assessDate = dTextView.getText().toString();
        CourseEntity selectedCourse = (CourseEntity) courseSpinner.getSelectedItem();
        int courseID = selectedCourse.getCourseID();
        mViewModel.saveAssess(assessName, assessType, assessDate, courseID);
        finish();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this)
                .get(AssessEditViewModel.class);

        mViewModel.mLiveAssess.observe(this, aEntity -> {
            if(aEntity != null && !mEditing) {
                mTextView.setText(aEntity.getAssessName());
                tTextView.setText(aEntity.getAssessType());
                TextView dateText = findViewById(R.id.assessDate);
                dateText.setText(DatetoString(aEntity.getAssessDate()));
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null){
            setTitle(getString(R.string.new_assess));
            mNewAssess = true;
        }
        else{
            setTitle(getString(R.string.edit_assess));
            int assessId = extras.getInt(ASSESS_ID_KEY);
            courseID = extras.getInt(A_COURSE_ID_KEY);
            mViewModel.loadData(assessId);
            mNewAssess = false;
            }
        }
    private String DatetoString(Date date){
        String pattern = "MMMM dd, yyyy";
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        String dateStr = sd.format(date);
        return dateStr;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (!mNewAssess){
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
            mViewModel.deleteAssess();
            Toast.makeText(AssessEditorActivity.this, "Assessment deleted.", Toast.LENGTH_SHORT);
            finish();
        }
        else if (item.getItemId() == R.id.action_alert) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Assessment on " + dTextView.getText() + " for " +
                    mTextView.getText());
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Assessment Reminder: " + mTextView.getText());
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
                Intent i = new Intent(AssessEditorActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_courses:
                Intent i1 = new Intent(AssessEditorActivity.this, CourseViewActivity.class);
                startActivity(i1);
                break;
            case R.id.nav_terms:
;                Intent i2 = new Intent(AssessEditorActivity.this, TermViewActivity.class);
                startActivity(i2);
                break;
            case R.id.nav_assessments:
                Intent i3 = new Intent(AssessEditorActivity.this, AssessmentViewActivity.class);
                startActivity(i3);
                break;
            case R.id.nav_mentors:
                Intent i4 = new Intent(AssessEditorActivity.this, MentorViewActivity.class);
                startActivity(i4);
                break;
            case R.id.nav_notes:
                Intent i5 = new Intent(AssessEditorActivity.this, NoteViewActivity.class);
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
        String date = dTextView.getText().toString();
        outState.putString(DATE_KEY, date);
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
