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

import com.example.wgutracker.database.NoteEntity;
import com.example.wgutracker.ui.ui.NotesAdapter;
import com.example.wgutracker.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NoteViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<NoteEntity> notesData = new ArrayList<>();

    private DrawerLayout drawer;

    @BindView(R.id.recycler_view)
    RecyclerView tRecyclerView;

    private NoteViewModel nViewModel;

    private NotesAdapter nAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();

        getSupportActionBar().setTitle("Notes");


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent i1 = new Intent(NoteViewActivity.this, NoteEditorActivity.class);
            startActivity(i1);
        });

    }

    private void initViewModel() {
        final Observer<List<NoteEntity>> notesObserver = noteEntities -> {
            notesData.clear();
            notesData.addAll(noteEntities);

            if (nAdapter == null){
                nAdapter = new NotesAdapter(notesData, NoteViewActivity.this);
                tRecyclerView.setAdapter(nAdapter);
            }
            else{
                nAdapter.notifyDataSetChanged();
            }
        };
        nViewModel = new ViewModelProvider(this)
                .get(NoteViewModel.class);
        nViewModel.mnotes.observe(this, notesObserver);

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
                    Intent i = new Intent(NoteViewActivity.this, MainActivity.class);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.nav_courses:
                    Intent i1 = new Intent(NoteViewActivity.this, CourseViewActivity.class);
                    startActivity(i1);
                    break;
                case R.id.nav_terms:
                    Intent i2 = new Intent(NoteViewActivity.this, NoteViewActivity.class);
                    startActivity(i2);
                    break;
                case R.id.nav_assessments:
                    Intent i3 = new Intent(NoteViewActivity.this, AssessmentViewActivity.class);
                    startActivity(i3);
                    break;
                case R.id.nav_mentors:
                    Intent i4 = new Intent(NoteViewActivity.this, MentorViewActivity.class);
                    startActivity(i4);
                    break;
                case R.id.nav_notes:
                    Intent i5 = new Intent(NoteViewActivity.this, NoteViewActivity.class);
                    startActivity(i5);
                    break;
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
