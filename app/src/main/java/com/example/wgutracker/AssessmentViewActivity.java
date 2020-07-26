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

import com.example.wgutracker.database.AssessmentEntity;
import com.example.wgutracker.ui.ui.AssessmentsAdapter;
import com.example.wgutracker.viewmodel.AssessmentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssessmentViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<AssessmentEntity> assessData = new ArrayList<>();

    private DrawerLayout drawer;

    @BindView(R.id.recycler_view)
    RecyclerView aRecyclerView;

    private AssessmentsAdapter aAdapter;

    private AssessmentViewModel aViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.assessments);

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
            Intent i = new Intent(AssessmentViewActivity.this, AssessEditorActivity.class);
            startActivity(i);
        });
    }

    private void initViewModel() {
        final Observer<List<AssessmentEntity>> assessObserver = assessmentEntities -> {
            assessData.clear();
            assessData.addAll(assessmentEntities);

            if (aAdapter == null){
                aAdapter = new AssessmentsAdapter(assessData, AssessmentViewActivity.this);
                aRecyclerView.setAdapter(aAdapter);
            }
            else{
                aAdapter.notifyDataSetChanged();
            }
        };
        aViewModel = new ViewModelProvider(this)
                .get(AssessmentViewModel.class);
        aViewModel.massess.observe(this, assessObserver);
    }

    private void initRecyclerView() {
        aRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        aRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                aRecyclerView.getContext(), layoutManager.getOrientation());
        aRecyclerView.addItemDecoration(divider);
    }

    @Override
    public boolean onNavigationItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent i = new Intent(com.example.wgutracker.AssessmentViewActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_courses:
                Intent i1 = new Intent(com.example.wgutracker.AssessmentViewActivity.this, CourseViewActivity.class);
                startActivity(i1);
                break;
            case R.id.nav_terms:
                Intent i2 = new Intent(com.example.wgutracker.AssessmentViewActivity.this, com.example.wgutracker.TermViewActivity.class);
                startActivity(i2);
                break;
            case R.id.nav_assessments:
                Intent i3 = new Intent(com.example.wgutracker.AssessmentViewActivity.this, com.example.wgutracker.AssessmentViewActivity.class);
                startActivity(i3);
                break;
            case R.id.nav_mentors:
                Intent i4 = new Intent(AssessmentViewActivity.this, MentorViewActivity.class);
                startActivity(i4);
                break;
            case R.id.nav_notes:
                Intent i5 = new Intent(AssessmentViewActivity.this, NoteViewActivity.class);
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
