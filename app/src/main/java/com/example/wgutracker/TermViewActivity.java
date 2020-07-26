package com.example.wgutracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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

import com.example.wgutracker.database.TermEntity;
import com.example.wgutracker.ui.ui.TermsAdapter;
import com.example.wgutracker.viewmodel.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TermViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<TermEntity> termsData = new ArrayList<>();

    private DrawerLayout drawer;

    @BindView(R.id.recycler_view)
    RecyclerView tRecyclerView;

    private TermViewModel tViewModel;

    private TermsAdapter tAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();

        getSupportActionBar().setTitle("Terms");


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(TermViewActivity.this, TermEditorActivity.class);
                startActivity(i1);
            }
        });

    }

    private void initViewModel() {
        final Observer<List<TermEntity>> termsObserver = termEntities -> {
            termsData.clear();
            termsData.addAll(termEntities);

            if (tAdapter == null){
                tAdapter = new TermsAdapter(termsData, TermViewActivity.this);
                tRecyclerView.setAdapter(tAdapter);
            }
            else{
                tAdapter.notifyDataSetChanged();
            }
        };
        tViewModel = new ViewModelProvider(this)
                .get(TermViewModel.class);
        tViewModel.mterms.observe(this, termsObserver);

    }

    private void initRecyclerView() {
        tRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        tRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                tRecyclerView.getContext(), layoutManager.getOrientation());
        tRecyclerView.addItemDecoration(divider);
    }


    @Override
        public boolean onNavigationItemSelected (MenuItem item){
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Intent i = new Intent(TermViewActivity.this, MainActivity.class);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.nav_courses:
                    Intent i1 = new Intent(TermViewActivity.this, CourseViewActivity.class);
                    startActivity(i1);
                    break;
                case R.id.nav_terms:
                    Intent i2 = new Intent(TermViewActivity.this, TermViewActivity.class);
                    startActivity(i2);
                    break;
                case R.id.nav_assessments:
                    Intent i3 = new Intent(TermViewActivity.this, AssessmentViewActivity.class);
                    startActivity(i3);
                    break;
                case R.id.nav_mentors:
                    Intent i4 = new Intent(TermViewActivity.this, MentorViewActivity.class);
                    startActivity(i4);
                    break;
                case R.id.nav_notes:
                    Intent i5 = new Intent(TermViewActivity.this, NoteViewActivity.class);
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
