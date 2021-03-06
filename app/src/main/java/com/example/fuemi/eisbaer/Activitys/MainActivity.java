package com.example.fuemi.eisbaer.Activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.fuemi.eisbaer.R;

import java.util.ArrayList;

import Items.Order;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewFlipper vf;
    FloatingActionButton fab;
    SharedPreferences sd;

    boolean guest = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Neues Spiel starten", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        sd = getSharedPreferences("Login", Context.MODE_PRIVATE);
        System.out.println(sd.getString("Gast", ""));
        System.out.println(sd.getBoolean("LoggedIn", true));
        guest = sd.getString("Gast", "").equals("Gast");
        System.out.println(guest);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        if(guest){

            navigationView.inflateMenu(R.menu.activity_main_drawer_guest);
        }else{
            navigationView.inflateMenu(R.menu.activity_main_drawer_no_guest);
        }

        navigationView.setNavigationItemSelectedListener(this);




        vf = (ViewFlipper) findViewById(R.id.viewFlipper);
        Intent i = getIntent();
        //Funktioniert bei offline starten noch nicht
        if(i.hasExtra("OrderOfMerrit")){
            Bundle bundle = i.getBundleExtra("OrderOfMerrit");
            ArrayList<Order> orderOfMerrit = (ArrayList<Order>) bundle.getSerializable("OrderOfMerrit");

            TextView helloWorld = (TextView) findViewById(R.id.main);
            System.out.print(orderOfMerrit.size());

            helloWorld.setText(orderOfMerrit.get(0).getPosition() + " - " + orderOfMerrit.get(0).getName() + " - " + orderOfMerrit.get(0).getPreisgeld());

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_newGame) {
            // Handle the camera action
            //vf.setDisplayedChild(1);
            //fab.setVisibility(View.INVISIBLE);
            newGame();
        } else if (id == R.id.nav_friends) {
            vf.setDisplayedChild(2);
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_news) {
            vf.setDisplayedChild(0);
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_statistiken) {
            vf.setDisplayedChild(3);
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_einstellungen) {
            vf.setDisplayedChild(4);
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_ausloggen) {
            logOut();
        } else if (id == R.id.nav_einloggen) {
            logIn();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logIn() {
        sd = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sd.edit();
        editor.putString("Gast", "NoGast");
        editor.putBoolean("LoggedIn", false);
        editor.apply();
        Intent i = new Intent(MainActivity.this, LadescreenActivity.class);
        startActivity(i);
        finish();
    }

    private void logOut() {
        sd = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sd.edit();
        editor.putBoolean("LoggedIn", false);
        editor.putString("Gast", "NoGast");
        editor.apply();
        Intent i = new Intent(MainActivity.this, LadescreenActivity.class);
        startActivity(i);
        finish();
    }

    private void newGame(){
        Intent i = new Intent(MainActivity.this, GameConfigurationActivity.class);
        startActivity(i);
    }
}
