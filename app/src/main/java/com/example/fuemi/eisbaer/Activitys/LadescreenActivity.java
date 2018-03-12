package com.example.fuemi.eisbaer.Activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
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

    SharedPreferences sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ladescreen);


        initTextViews();
        initButtons();

        initProgressBar();
        checkLogin();


    }





    private void checkLogin() {

        //checkIfAngemeldet
        //--> Nein, Login/Registrieren Seite
        sd = getSharedPreferences("Login", Context.MODE_PRIVATE);
        System.out.println(sd.getBoolean("LoggedIn",false));
        System.out.println(sd.getString("Gast",""));
        if(!sd.getBoolean("LoggedIn", false) && !sd.getString("Gast", "").equals("Gast")){
            Intent i = new Intent(LadescreenActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }else{

            tryLoadingNews();
        }

    }

    private void initProgressBar() {
        progessBar = (ProgressBar) findViewById(R.id.progressBarLadescreen);
        progessBar.setMax(100);
    }


    /*  buttons werden ausgeblendet
        checked Nach Internetverbindung --> einblenden der Buttons falls nicht verfügbar
    */
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

    /*
        initialiseren der Button + button listeners
     */

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

    /*
        überprüft ob Verbindung mit dem Internet hergestellt werden kann
        wenn ja --> true
        wenn nein --> false
     */


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

    /*
        startet Asyncronen Task um die Daten aus dem Internet zu laden
     */

    private void loadNews() {
        //Asynk Task starten
        GetNewsFromInternetTask newsTask = new GetNewsFromInternetTask(this, this);
        newsTask.execute();
    }

    /*
        verändert den dargestellten Text je nach Prozentstand
     */

    private void progessSuccess(int percent){
        switch(percent){
            case 5: textViewProgressBar.setText(R.string.check_internet_access);break;
            case 10: textViewProgressBar.setText(R.string.load_ranglisten);break;
            default: break;
        }
    }

    /*
            verändert den Text, wenn einer der Ladeschritt nicht erflogreich war
            blendet die Buttons ein
     */

    private void progressWithoutSuccess(int percent){
        switch(percent){
            case 5: textViewProgressBar.setText(R.string.no_internet_access);break;
            case 10: textViewProgressBar.setText(R.string.no_load_ranglisten);break;
            default: break;
        }
        buttonVerbinden.setVisibility(View.VISIBLE);
        buttonOffline.setVisibility(View.VISIBLE);
    }


    /*
        überschreibt Methode aus dem Interface "Asyncesponse", um Rückmeldung von Asnyc Task "GetNewsFromInternetTask" zu erhalten
        erhält zum einen die variable percent, die den Fortschritt des Ladevorgangs darstellt
        erhält die variable int success, diese ist 0, falls ein Problem im Ladevorgang auftritt
        success ist int statt boolean, da die bestehende Methode onProgressUpdate im Async Task nur Integer sein kann
     */

    @Override
    public void onUpdateProgress(int percent, int success) {
        progessBar.setProgress(percent);
        System.out.println(percent);
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
