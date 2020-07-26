package com.example.wgutracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgutracker.database.AssessmentEntity;
import com.example.wgutracker.database.CourseEntity;
import com.example.wgutracker.database.TermEntity;
import com.example.wgutracker.ui.ui.AssessmentsAdapter;
import com.example.wgutracker.ui.ui.CoursesAdapter;
import com.example.wgutracker.ui.ui.TermsAdapter;
import com.example.wgutracker.viewmodel.AssessmentViewModel;
import com.example.wgutracker.viewmodel.CourseViewModel;
import com.example.wgutracker.viewmodel.MainViewModel;
import com.example.wgutracker.viewmodel.TermViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

   private DrawerLayout drawer;
   private MainViewModel mViewModel;

    private List<AssessmentEntity> assessData = new ArrayList<>();
    private List<CourseEntity> coursesData = new ArrayList<>();
    private List<TermEntity> termsData = new ArrayList<>();

    @BindView(R.id.upcomingAssessmentRecycler)
    RecyclerView aRecyclerView;

    @BindView(R.id.upcomingCourseRecycler)
    RecyclerView cRecyclerView;

    @BindView(R.id.upcomingTermRecycler)
    RecyclerView tRecyclerView;

    private AssessmentsAdapter aAdapter;
    private AssessmentViewModel aViewModel;
    private CourseViewModel cViewModel;
    private CoursesAdapter cAdapter;
    private TermViewModel tViewModel;
    private TermsAdapter tAdapter;

    private Date today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.wgu_tracker);

        ButterKnife.bind(this);

        today = Calendar.getInstance().getTime();

        initAssessRecycler();
        initCourseRecycler();
        initTermRecycler();

        initAssessViewModel();
        initCourseViewModel();
        initTermViewModel();
        initViewModel();

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void initTermViewModel() {
        final Observer<List<TermEntity>> termsObserver = termEntities -> {
            termsData.clear();
            termsData.addAll(termEntities);

            if (tAdapter == null){
                tAdapter = new TermsAdapter(termsData, MainActivity.this);
                tRecyclerView.setAdapter(tAdapter);
            }
            else{
                tAdapter.notifyDataSetChanged();
            }
        };
        tViewModel = new ViewModelProvider(this)
                .get(TermViewModel.class);
        tViewModel.getCurrentTerm(today).observe(this, termsObserver);
    }

    private void initCourseViewModel() {
        final Observer<List<CourseEntity>> courseObserver = courseEntities -> {
            coursesData.clear();
            coursesData.addAll(courseEntities);

            if (cAdapter == null){
                cAdapter = new CoursesAdapter(coursesData, MainActivity.this);
                cRecyclerView.setAdapter(cAdapter);
            }
            else{
                cAdapter.notifyDataSetChanged();
            }
        };
        cViewModel = new ViewModelProvider(this)
                .get(CourseViewModel.class);
        cViewModel.getCourseSoon(today).observe(this, courseObserver);
    }

    private void initAssessViewModel() {
        final Observer<List<AssessmentEntity>> assessObserver = assessmentEntities -> {
            assessData.clear();
            assessData.addAll(assessmentEntities);

            if (aAdapter == null){
                aAdapter = new AssessmentsAdapter(assessData, MainActivity.this);
                aRecyclerView.setAdapter(aAdapter);
            }
            else{
                aAdapter.notifyDataSetChanged();
            }
        };
        aViewModel = new ViewModelProvider(this)
                .get(AssessmentViewModel.class);
        aViewModel.getAssessSoon(today).observe(this, assessObserver);
    }

    private void initTermRecycler() {
        tRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        tRecyclerView.setLayoutManager(layoutManager1);

        DividerItemDecoration divider = new DividerItemDecoration(
                tRecyclerView.getContext(), layoutManager1.getOrientation());
        tRecyclerView.addItemDecoration(divider);
    }

    private void initCourseRecycler() {
        cRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        cRecyclerView.setLayoutManager(layoutManager2);

        DividerItemDecoration divider = new DividerItemDecoration(
                cRecyclerView.getContext(), layoutManager2.getOrientation());
        cRecyclerView.addItemDecoration(divider);
    }

    private void initAssessRecycler() {
        aRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        aRecyclerView.setLayoutManager(layoutManager3);

        DividerItemDecoration divider = new DividerItemDecoration(
                aRecyclerView.getContext(), layoutManager3.getOrientation());
        aRecyclerView.addItemDecoration(divider);
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_home:
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_courses:
                Intent i1 = new Intent(MainActivity.this, CourseViewActivity.class);
                startActivity(i1);
                break;
            case R.id.nav_terms:
                Intent i2 = new Intent(MainActivity.this, TermViewActivity.class);
                startActivity(i2);
                break;
            case R.id.nav_assessments:
                Intent i3 = new Intent(com.example.wgutracker.MainActivity.this, com.example.wgutracker.AssessmentViewActivity.class);
                startActivity(i3);
                break;
            case R.id.nav_mentors:
                Intent i4 = new Intent(MainActivity.this, MentorViewActivity.class);
                startActivity(i4);
                break;
            case R.id.nav_notes:
                Intent i5 = new Intent(MainActivity.this, NoteViewActivity.class);
                startActivity(i5);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_sample_data) {
            addSampleData();
            return true;
        } else if (id == R.id.action_delete_all) {
            deleteAllData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllData() {
        mViewModel.deleteAllData();
    }

    private void addSampleData() {
        mViewModel.addSampleData();
    }

}

