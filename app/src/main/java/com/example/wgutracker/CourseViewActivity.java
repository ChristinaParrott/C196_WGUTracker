package com.example.wgutracker;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.wgutracker.database.CourseEntity;
import com.example.wgutracker.ui.ui.CoursesAdapter;
import com.example.wgutracker.viewmodel.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CourseViewActivity extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener {

    private List<CourseEntity> coursesData = new ArrayList<>();

    private DrawerLayout drawer;

    @BindView(R.id.recycler_view)
    RecyclerView cRecyclerView;

    private CourseViewModel cViewModel;

    private CoursesAdapter cAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_course_view);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setTitle("Courses");

            ButterKnife.bind(this);
            initRecyclerView();
            initViewModel();

            drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(view -> {
                    Intent i1 = new Intent(CourseViewActivity.this, CourseEditorActivity.class);
                    startActivity(i1);
                });
        }

    private void initViewModel() {
        final Observer<List<CourseEntity>> courseObserver = courseEntities -> {
            coursesData.clear();
            coursesData.addAll(courseEntities);

            if (cAdapter == null){
                cAdapter = new CoursesAdapter(coursesData, CourseViewActivity.this);
                cRecyclerView.setAdapter(cAdapter);
            }
            else{
                cAdapter.notifyDataSetChanged();
            }
        };
        cViewModel = new ViewModelProvider(this)
                .get(CourseViewModel.class);
        cViewModel.mcourses.observe(this, courseObserver);
    }

    private void initRecyclerView() {
        cRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        cRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                cRecyclerView.getContext(), layoutManager.getOrientation());
        cRecyclerView.addItemDecoration(divider);
    }

    @Override
        public boolean onNavigationItemSelected (MenuItem item){
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Intent i = new Intent(com.example.wgutracker.CourseViewActivity.this, MainActivity.class);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.nav_courses:
                    Intent i1 = new Intent(com.example.wgutracker.CourseViewActivity.this, CourseViewActivity.class);
                    startActivity(i1);
                    break;
                case R.id.nav_terms:
                    Intent i2 = new Intent(com.example.wgutracker.CourseViewActivity.this, com.example.wgutracker.TermViewActivity.class);
                    startActivity(i2);
                    break;
                case R.id.nav_assessments:
                    Intent i3 = new Intent(com.example.wgutracker.CourseViewActivity.this, com.example.wgutracker.AssessmentViewActivity.class);
                    startActivity(i3);
                    break;
                case R.id.nav_mentors:
                    Intent i4 = new Intent(CourseViewActivity.this, MentorViewActivity.class);
                    startActivity(i4);
                    break;
                case R.id.nav_notes:
                    Intent i5 = new Intent(CourseViewActivity.this, NoteViewActivity.class);
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