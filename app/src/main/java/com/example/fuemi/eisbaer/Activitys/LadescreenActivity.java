package com.example.fuemi.eisbaer.Activitys;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fuemi.eisbaer.R;

import AsyncTasks.GetNewsFromInternetTask;
import Interfaces.AsyncResponse;


public class LadescreenActivity extends AppCompatActivity implements AsyncResponse {

    ProgressBar progessBar;
    TextView textViewProgressBar;
    Button buttonOffline;
    Button buttonVerbinden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladescreen);


        initTextViews();
        initButtons();
        initProgressBar();
        tryLoadingNews();


    }

    private void initProgressBar() {
        progessBar = (ProgressBar) findViewById(R.id.progressBarLadescreen);
    }

    private void tryLoadingNews() {

        buttonOffline.setVisibility(View.INVISIBLE);
        buttonVerbinden.setVisibility(View.INVISIBLE);
        if(checkingForInternetConnection()){
            loadNews();
        }else{
            //fehler, keine Internetverbindung --> Offline möglichkeit
            System.out.println("Keine Internetverbidung");
            buttonOffline.setVisibility(View.VISIBLE);
            buttonVerbinden.setVisibility(View.VISIBLE);
        }
    }

    private void initButtons() {
        buttonOffline = (Button) findViewById(R.id.buttonLadescreenOffline);
        buttonOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //stopLoading, start Activity
                Intent i = new Intent(LadescreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        buttonVerbinden = (Button) findViewById(R.id.buttonLadescreenNewTry);
        buttonVerbinden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryLoadingNews();
            }
        });
    }

    private void initTextViews() {
        textViewProgressBar = (TextView) findViewById(R.id.textViewLadescreen);
    }

    private boolean checkingForInternetConnection() {
        textViewProgressBar.setText(R.string.check_internet_connection);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else{
            return false;
        }

    }

    private void loadNews() {
        //Asynk Task starten
        GetNewsFromInternetTask newsTask = new GetNewsFromInternetTask(this, this);
        newsTask.execute();
    }

    private void progessSuccess(int percent){
        switch(percent){
            case 5: textViewProgressBar.setText(R.string.check_internet_access);break;
            case 10: textViewProgressBar.setText(R.string.load_ranglisten);break;
        }
    }

    private void progressWithoutSuccess(int percent){
        switch(percent){
            case 5: textViewProgressBar.setText(R.string.no_internet_access);break;
            case 10: textViewProgressBar.setText(R.string.no_load_ranglisten);break;
        }
        buttonVerbinden.setVisibility(View.VISIBLE);
        buttonOffline.setVisibility(View.VISIBLE);
    }


    @Override
    public void onUpdateProgress(int percent, int success) {
        if(success == 1){
            progessSuccess(percent);
        }else{
            progressWithoutSuccess(percent);
        }

    }


    // 0% check Connection --> 5%
    // 5% check Access --> 10 %
    //10% load Rangliste --> 100 %

}
