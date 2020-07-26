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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.example.wgutracker.R.id.courseText;
import static com.example.wgutracker.utilities.Constants.ASSESS_ID_KEY;
import static com.example.wgutracker.utilities.Constants.A_COURSE_ID_KEY;

public class AssessDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener{

    @BindView(R.id.assessName)
    TextView mTextView;

    @BindView(R.id.assessType)
    TextView tTextView;

    @BindView(R.id.assessDate)
    TextView dTextView;


    private DrawerLayout drawer;

    private AssessEditViewModel mViewModel;
    private CourseViewModel cViewModel;
    private CourseEditViewModel eViewModel;

    private ArrayAdapter<CourseEntity> cAdapter;

    private Integer courseID, assessId;

    private TextView notificationText;

    long timeInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        initViewModel();
        setCourseName();
        getSupportActionBar().setTitle(R.string.view_assessment);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        TextView notification = findViewById(R.id.notification_date);
        notification.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");;
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(AssessDetailActivity.this, AssessEditorActivity.class);
            intent.putExtra(ASSESS_ID_KEY, assessId);
            intent.putExtra(A_COURSE_ID_KEY, courseID);
            startActivity(intent);
        });
        Button setNotify = findViewById(R.id.set_notification);
        setNotify.setOnClickListener(v -> {
            if(timeInMillis == 0L){
                Toast.makeText(AssessDetailActivity.this, "You must first select a date on which to notify.", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(AssessDetailActivity.this, MyReceiver.class);
                Toast.makeText(AssessDetailActivity.this, "Notification set for assessment titled " + mTextView.getText()
                        + " on " + dTextView.getText(), Toast.LENGTH_LONG).show();
                intent.putExtra("key", "Assessment titled " + mTextView.getText() + " on " + dTextView.getText());
                PendingIntent sender = PendingIntent.getBroadcast(AssessDetailActivity.this, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
            }
        });
    }

    private void setCourseName() {
        eViewModel = new ViewModelProvider(this)
                .get(CourseEditViewModel.class);
        TextView cTextView = findViewById(courseText);
        eViewModel.mLiveCourse.observe(this, courseEntity -> {
            if(courseEntity != null) {
              cTextView.setText(courseEntity.getCourseName());
            }
        });
        eViewModel.loadData(courseID);
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this)
                .get(AssessEditViewModel.class);

        mViewModel.mLiveAssess.observe(this, aEntity -> {
            mTextView.setText(aEntity.getAssessName());
            tTextView.setText(aEntity.getAssessType());
            TextView dateText = findViewById(R.id.assessDate);
            dateText.setText(DatetoString(aEntity.getAssessDate()));
        });

        Bundle extras = getIntent().getExtras();
        assessId = extras.getInt(ASSESS_ID_KEY);
        courseID = extras.getInt(A_COURSE_ID_KEY);
        mViewModel.loadData(assessId);
    }

    private String DatetoString(Date date) {
        String pattern = "MMMM dd, yyyy";
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        String dateStr = sd.format(date);
        return dateStr;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_notify, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            Toast.makeText(AssessDetailActivity.this, "Assessment deleted.", Toast.LENGTH_SHORT);
            mViewModel.deleteAssess();
            finish();
        }
        else if (item.getItemId() == R.id.action_alert){
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
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent i = new Intent(AssessDetailActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_courses:
                Intent i1 = new Intent(AssessDetailActivity.this, CourseViewActivity.class);
                startActivity(i1);
                break;
            case R.id.nav_terms:
                ;
                Intent i2 = new Intent(AssessDetailActivity.this, TermViewActivity.class);
                startActivity(i2);
                break;
            case R.id.nav_assessments:
                Intent i3 = new Intent(AssessDetailActivity.this, AssessmentViewActivity.class);
                startActivity(i3);
                break;
            case R.id.nav_mentors:
                Intent i4 = new Intent(AssessDetailActivity.this, MentorViewActivity.class);
                startActivity(i4);
                break;
            case R.id.nav_notes:
                Intent i5 = new Intent(AssessDetailActivity.this, NoteViewActivity.class);
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        timeInMillis = c.getTimeInMillis();
        String selectedDate = DateFormat.getDateInstance().format(c.getTime());
        notificationText = findViewById(R.id.notification_date);
        notificationText.setText(selectedDate);
    }
}
