package com.dmytro.manager_zadan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.DatePicker;

import com.dmytro.manager_zadan.adapter.Zadania;
import com.dmytro.manager_zadan.adapter.ZadaniaAdapter;
import com.dmytro.manager_zadan.db.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recView;
    private View rview;
    private ZadaniaAdapter adapter;
    private List<Zadania> zadaniaItems;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHandler db;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rview = findViewById(R.id.rootView);
        calendar = Calendar.getInstance();

        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(MainActivity.this, TaskEditActivity.class));
                intent.putExtra("origin", "forNew");
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Implementacja RecyclerView
        zadaniaItems = new ArrayList<>();
        recView = (RecyclerView) findViewById(R.id.rec_list_f_task);
        adapter = new ZadaniaAdapter(zadaniaItems, this);
        recView.setAdapter(adapter);
        recView.setLayoutManager((new LinearLayoutManager(this)));
        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState > 0){
                    floatingActionButton.hide();
                } else {
                    floatingActionButton.show();
                }
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout_task);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final Date date = new Date();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchZadanie(dateFormat.format(date));
            }
        });
        fetchZadanie(dateFormat.format(date));
}

private void fetchZadanie(String date){
        zadaniaItems.clear();
        db = new DatabaseHandler(getApplicationContext());
        List<Zadania> results = db.getAllTaskByDate(date);
        db.closeDB();
        for (Zadania zadania : results) {
            zadaniaItems.add(zadania);
        }
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = null;
        try {
            date1 = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long dzisiaj = new Date().getTime();
        dzisiaj -= dzisiaj%86400000;

        long due = date1.getTime();
        due -=due%86400000;

        long diff = ((due - dzisiaj)/86400000);
        if (diff == -1){
            //dzisiaj
            getSupportActionBar().setTitle("Wczoraj");
        } else if (diff == 0) {
            getSupportActionBar().setTitle("Dzisiaj");
        } else if (diff == 1) {
            getSupportActionBar().setTitle("Jutro");
        } else {
            getSupportActionBar().setTitle(date);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Pompuje menu, spowoduje dodanie elementów do paska akcji
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            return true;
        } else if (id == R.id.action_date){
            DatePickerDialog datePickerDialog = new DatePickerDialog(rview.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date1 = null;
                    try {
                        date1 = dateFormat.parse(date);
                    } catch (ParseException e){
                        e.printStackTrace();
                    }
                    fetchZadanie(dateFormat.format(date1));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.unfinished_task){
            startActivity(new Intent(MainActivity.this, UnfinishedTaskActivity.class));
        } else if (id == R.id.nav_task_manager){

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}