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

import com.example.wgutracker.database.MentorEntity;
import com.example.wgutracker.ui.ui.MentorsAdapter;
import com.example.wgutracker.viewmodel.MentorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MentorViewActivity extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener {

    private List<MentorEntity> mentorsData = new ArrayList<>();

    private DrawerLayout drawer;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private MentorViewModel mViewModel;

    private MentorsAdapter mAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mentor_view);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setTitle("Mentors");

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
                    Intent i1 = new Intent(MentorViewActivity.this, MentorEditorActivity.class);
                   startActivity(i1);
                });
        }

    private void initViewModel() {
        final Observer<List<MentorEntity>> mentorObserver = mentorEntities -> {
            mentorsData.clear();
            mentorsData.addAll(mentorEntities);

            if (mAdapter == null){
                mAdapter = new MentorsAdapter(mentorsData, MentorViewActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }
            else{
                mAdapter.notifyDataSetChanged();
            }
        };
        mViewModel = new ViewModelProvider(this)
                .get(MentorViewModel.class);
        mViewModel.mmentor.observe(this, mentorObserver);
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);
    }

    @Override
        public boolean onNavigationItemSelected (MenuItem item){
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Intent i = new Intent(MentorViewActivity.this, MainActivity.class);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.nav_courses:
                    Intent i1 = new Intent(MentorViewActivity.this, CourseViewActivity.class);
                    startActivity(i1);
                    break;
                case R.id.nav_terms:
                    Intent i2 = new Intent(MentorViewActivity.this, TermViewActivity.class);
                    startActivity(i2);
                    break;
                case R.id.nav_assessments:
                    Intent i3 = new Intent(MentorViewActivity.this, AssessmentViewActivity.class);
                    startActivity(i3);
                    break;
                case R.id.nav_mentors:
                    Intent i4 = new Intent(MentorViewActivity.this, MentorViewActivity.class);
                    startActivity(i4);
                    break;
                case R.id.nav_notes:
                    Intent i5 = new Intent(MentorViewActivity.this, NoteViewActivity.class);
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