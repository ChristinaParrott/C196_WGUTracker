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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.wgutracker.viewmodel.TermEditViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wgutracker.utilities.Constants.EDITING_KEY;
import static com.example.wgutracker.utilities.Constants.TERM_ID_KEY;

public class TermEditorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DatePickerDialog.OnDateSetListener{

    @BindView(R.id.termName)
    TextView mTextView;

    @BindView(R.id.startDate)
    TextView sTextView;

    @BindView(R.id.endDate)
    TextView eTextView;

    private DrawerLayout drawer;

    private TermEditViewModel mViewModel;

    private boolean mNewTerm, mEditing;

    private TextView clickedText;
    private TextView notificationText;

    int termId;

    long timeInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        if(savedInstanceState != null){
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
            sTextView.setText(savedInstanceState.getString("start_date"));
            eTextView.setText(savedInstanceState.getString("end_date"));
        }

        initViewModel();

        getSupportActionBar().setTitle(R.string.edit_term);


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

        TextView notification = findViewById(R.id.notification_date);
        notification.setOnClickListener(v -> {
            clickedText = (TextView) v;
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");;
        });

        Button setNotify = findViewById(R.id.set_notification);
        setNotify.setOnClickListener(v -> {
            if(timeInMillis == 0L){
                Toast.makeText(TermEditorActivity.this, "You must first select a date on which to notify.", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(TermEditorActivity.this, MyReceiver.class);
                Toast.makeText(TermEditorActivity.this, "Notification set for term titled " + mTextView.getText()
                        , Toast.LENGTH_LONG).show();
                intent.putExtra("key", "Term titled " + mTextView.getText() + " starts on " + sTextView.getText() +
                        " and ends on " + eTextView.getText());
                PendingIntent sender = PendingIntent.getBroadcast(TermEditorActivity.this, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->  {
            saveAndReturn();
                });
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


    private void saveAndReturn() {
        String termName = mTextView.getText().toString();
        String termStart = sTextView.getText().toString();
        String termEnd = eTextView.getText().toString();
        mViewModel.saveTerm(termName, termStart, termEnd);
        finish();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this)
                .get(TermEditViewModel.class);
        mViewModel.mLiveTerm.observe(this, termEntity -> {
            if(termEntity != null && !mEditing) {
                mTextView.setText(termEntity.getTermName());
                sTextView.setText(DatetoString(termEntity.getTermStart()));
                eTextView.setText(DatetoString(termEntity.getTermEnd()));
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras == null){
            setTitle(getString(R.string.new_term));
            mNewTerm = true;
        }
        else{
            setTitle(getString(R.string.edit_term));
            termId = extras.getInt(TERM_ID_KEY);
            mViewModel.loadData(termId);
        }
    }
    private String DatetoString(Date date){
        String pattern = "MMMM, dd yyyy";
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        String dateStr = sd.format(date);
        return dateStr;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (!mNewTerm){
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
            if (termId == 0) {
                Toast.makeText(TermEditorActivity.this, "ERROR: Can't delete this term because it doesn't exist",
                        Toast.LENGTH_SHORT).show();
            } else {
                LiveData<Integer> count = mViewModel.getCourseCountForTerm(termId);
                count.observe(this, integer -> {
                    if (count.getValue() == 0) {
                        mViewModel.deleteTerm();
                        finish();
                        Toast.makeText(TermEditorActivity.this, "Term deleted",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TermEditorActivity.this, "ERROR: Can't delete this term because it contains courses",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        if (item.getItemId() == R.id.action_alert) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Term ends on " + eTextView.getText() + " for " +
                    mTextView.getText());
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Term Reminder: " + mTextView.getText());
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
                Intent i = new Intent(TermEditorActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_courses:
                Intent i1 = new Intent(TermEditorActivity.this, CourseViewActivity.class);
                startActivity(i1);
                break;
            case R.id.nav_terms:
                Intent i2 = new Intent(TermEditorActivity.this, TermViewActivity.class);
                startActivity(i2);
                break;
            case R.id.nav_assessments:
                Intent i3 = new Intent(TermEditorActivity.this, AssessmentViewActivity.class);
                startActivity(i3);
                break;
            case R.id.nav_mentors:
                Intent i4 = new Intent(TermEditorActivity.this, MentorViewActivity.class);
                startActivity(i4);
                break;
            case R.id.nav_notes:
                Intent i5 = new Intent(TermEditorActivity.this, NoteViewActivity.class);
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
        String startDate = sTextView.getText().toString();
        String endDate = eTextView.getText().toString();
        outState.putString("start_date", startDate);
        outState.putString("end_date", endDate);
        super.onSaveInstanceState(outState);
    }
}
