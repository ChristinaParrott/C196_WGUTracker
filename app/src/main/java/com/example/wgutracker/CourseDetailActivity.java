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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgutracker.database.AssessmentEntity;
import com.example.wgutracker.database.NoteEntity;
import com.example.wgutracker.ui.ui.AssessmentsAdapter;
import com.example.wgutracker.ui.ui.NotesAdapter;
import com.example.wgutracker.viewmodel.AssessmentViewModel;
import com.example.wgutracker.viewmodel.CourseEditViewModel;
import com.example.wgutracker.viewmodel.MentorEditViewModel;
import com.example.wgutracker.viewmodel.NoteViewModel;
import com.example.wgutracker.viewmodel.TermEditViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wgutracker.utilities.Constants.COURSE_ID_KEY;
import static com.example.wgutracker.utilities.Constants.MENTOR_ID_KEY;
import static com.example.wgutracker.utilities.Constants.MENTOR_ID_KEY_C;
import static com.example.wgutracker.utilities.Constants.TERM_ID_KEY;
import static com.example.wgutracker.utilities.Constants.TERM_ID_KEY_C;

public class CourseDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener{

    @BindView(R.id.courseName)
    TextView mTextView;

    @BindView(R.id.startDate)
    TextView sTextView;

    @BindView(R.id.endDate)
    TextView eTextView;

    @BindView(R.id.status)
    TextView tTextView;

    @BindView(R.id.assessRecycler)
    RecyclerView aRecyclerView;

    @BindView(R.id.notesRecycler)
    RecyclerView nRecyclerView;

    private AssessmentsAdapter aAdapter;
    private AssessmentViewModel aViewModel;

    private NotesAdapter nAdapter;
    private NoteViewModel nViewModel;

    private List<AssessmentEntity> assessData = new ArrayList<>();
    private List<NoteEntity> noteData = new ArrayList<>();
    private DrawerLayout drawer;

    private CourseEditViewModel mViewModel;
    private TermEditViewModel tViewModel;
    private MentorEditViewModel rViewModel;

    private int termID, mentorID, courseID;

    private TextView notificationText;

    private long timeInMillis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        TextView termTextView = findViewById(R.id.term);
        TextView mentorTextView = findViewById(R.id.mentor);

        initRecyclerView();
        initViewModel();


        getSupportActionBar().setTitle(R.string.view_course);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(CourseDetailActivity.this, CourseEditorActivity.class);
            intent.putExtra(COURSE_ID_KEY, courseID);
            intent.putExtra(MENTOR_ID_KEY, mentorID);
            intent.putExtra(TERM_ID_KEY, termID);
            startActivity(intent);
        });

        mentorTextView.setOnClickListener(view -> {
            Intent intent = new Intent(CourseDetailActivity.this, MentorDetailActivity.class);
            intent.putExtra(MENTOR_ID_KEY, mentorID);
            startActivity(intent);
        });

        termTextView.setOnClickListener(view -> {
            Intent intent = new Intent(CourseDetailActivity.this, TermDetailActivity.class);
            intent.putExtra(TERM_ID_KEY, termID);
            startActivity(intent);
        });
        TextView notification = findViewById(R.id.notification_date);
        notification.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");;
        });

        Button setNotify = findViewById(R.id.set_notification);
        setNotify.setOnClickListener(v -> {
            if(timeInMillis == 0L){
                Toast.makeText(CourseDetailActivity.this, "You must first select a date on which to notify.", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(CourseDetailActivity.this, MyReceiver.class);
                Toast.makeText(CourseDetailActivity.this, "Notification set for course titled " + mTextView.getText()
                        , Toast.LENGTH_LONG).show();
                intent.putExtra("key", "Course titled " + mTextView.getText() + " starts on " + sTextView.getText() +
                        " and ends on " + eTextView.getText());
                PendingIntent sender = PendingIntent.getBroadcast(CourseDetailActivity.this, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
            }
        });

    }

    private void initRecyclerView() {
        aRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        aRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                aRecyclerView.getContext(), layoutManager.getOrientation());
        aRecyclerView.addItemDecoration(divider);

        nRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        nRecyclerView.setLayoutManager(layoutManager2);

        DividerItemDecoration divider2 = new DividerItemDecoration(
                nRecyclerView.getContext(), layoutManager.getOrientation());
        nRecyclerView.addItemDecoration(divider);
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this)
                .get(CourseEditViewModel.class);
        mViewModel.mLiveCourse.observe(this, cEntity -> {
            if (cEntity != null) {
                mTextView.setText(cEntity.getCourseName());
                sTextView.setText(DatetoString(cEntity.getCourseStart()));
                eTextView.setText(DatetoString(cEntity.getCourseEnd()));
                tTextView.setText(cEntity.getCourseStatus());
            }
        });
        Bundle extras = getIntent().getExtras();
        courseID = extras.getInt(COURSE_ID_KEY);
        mViewModel.loadData(courseID);
        termID = extras.getInt(TERM_ID_KEY_C);
        mentorID = extras.getInt(MENTOR_ID_KEY_C);

        TextView termTextView = findViewById(R.id.term);
        TextView mentorTextView = findViewById(R.id.mentor);


        tViewModel = new ViewModelProvider(this)
                .get(TermEditViewModel.class);
        tViewModel.mLiveTerm.observe(this, tEntity -> {
            if (tEntity != null) {
                termTextView.setText(tEntity.getTermName());
            }
        });
        tViewModel.loadData(termID);

        rViewModel = new ViewModelProvider(this)
                .get(MentorEditViewModel.class);
        rViewModel.mLiveMentor.observe(this, mEntity -> {
            if (mEntity != null) {
                mentorTextView.setText(mEntity.getMentorName());
            }
        });
        rViewModel.loadData(mentorID);

        final Observer<List<AssessmentEntity>> assessObserver = assessmentEntities -> {
            assessData.clear();
            assessData.addAll(assessmentEntities);

            if (aAdapter == null){
                aAdapter = new AssessmentsAdapter(assessData, CourseDetailActivity.this);
                aRecyclerView.setAdapter(aAdapter);
            }
            else{
                aAdapter.notifyDataSetChanged();
            }
        };
        aViewModel = new ViewModelProvider(this)
                .get(AssessmentViewModel.class);
        aViewModel.getAssessmentsByCourse(courseID).observe(this, assessObserver);

        final Observer<List<NoteEntity>> noteObserver = noteEntities -> {
            noteData.clear();
            noteData.addAll(noteEntities);

            if (nAdapter == null){
                nAdapter = new NotesAdapter(noteData, CourseDetailActivity.this);
                nRecyclerView.setAdapter(nAdapter);
            }
            else{
                nAdapter.notifyDataSetChanged();
            }
        };
        nViewModel = new ViewModelProvider(this)
                .get(NoteViewModel.class);
        nViewModel.getNotesByCourse(courseID).observe(this, noteObserver);
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
            if (courseID == 0) {
                Toast.makeText(CourseDetailActivity.this, "ERROR: Can't delete this course because it doesn't exist",
                        Toast.LENGTH_SHORT).show();
            } else {
                LiveData<Integer> count = mViewModel.getAssessCountForCourse(courseID);
                count.observe(this, integer -> {
                    if (count.getValue() == 0) {
                        mViewModel.deleteCourse();
                        finish();
                        Toast.makeText(CourseDetailActivity.this, "Course deleted",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CourseDetailActivity.this, "ERROR: Can't delete this course because it contains assessments",
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
                Intent i = new Intent(CourseDetailActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_courses:
                Intent i1 = new Intent(CourseDetailActivity.this, CourseViewActivity.class);
                startActivity(i1);
                break;
            case R.id.nav_terms:
                Intent i2 = new Intent(CourseDetailActivity.this, TermViewActivity.class);
                startActivity(i2);
                break;
            case R.id.nav_assessments:
                Intent i3 = new Intent(CourseDetailActivity.this, AssessmentViewActivity.class);
                startActivity(i3);
                break;
            case R.id.nav_mentors:
                Intent i4 = new Intent(CourseDetailActivity.this, MentorViewActivity.class);
                startActivity(i4);
                break;
            case R.id.nav_notes:
                Intent i5 = new Intent(CourseDetailActivity.this, NoteViewActivity.class);
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
