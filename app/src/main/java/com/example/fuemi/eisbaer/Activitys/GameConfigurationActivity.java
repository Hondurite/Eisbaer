package com.example.fuemi.eisbaer.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.example.fuemi.eisbaer.R;

public class GameConfigurationActivity extends Activity {

    Button startGame;
    ToggleButton bestOf,firstTo,sets,legs;
    EditText playerOne,playerTwo;
    int beginScore,matches;
    boolean mode = true, //true = legs false = sets
            goal = true; //true = bestOf false = firstTo

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_new_game);

        setButtons();
        setTogglButtons();
    }

    //startButton um Spiel zu starten
    private void setButtons(){
        startGame = (Button)findViewById(R.id.start_game);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(checkStrings()){
                   EditText score = (EditText) findViewById(R.id.Score_input);
                   beginScore = Integer.parseInt(score.getText().toString());
                   EditText match = (EditText) findViewById(R.id.Matches_input);
                   matches = Integer.parseInt(match.getText().toString());

                   Intent i = new Intent(GameConfigurationActivity.this, GameActivity.class);
                   i.putExtra("player1",playerOne.getText().toString());
                   i.putExtra("player2",playerTwo.getText().toString());
                   i.putExtra("beginScore",beginScore);
                   i.putExtra("matches",matches);
                   startActivity(i);
               }
            }
        });
    }

    //überprüfung ob beide namen eingegeben wurden
    private boolean checkStrings(){
        playerOne = (EditText) findViewById(R.id.player1_input);
        playerTwo = (EditText) findViewById(R.id.player2_input);
        String one = playerOne.getText().toString();
        String two = playerTwo.getText().toString();
        if(one.length() > 0){
            if(two.length() > 0){
                return true;
            }else{
                playerTwo.setHint("name fehlt");
            }
        }else{
            playerOne.setHint("name fehlt");
        }
        return false;
    }

    //Die Togglebutton für die legs,Sets und BestOf,firstTo
    private void setTogglButtons(){
        //Buttons müssen unterschiedlich beginnen TODO: user preferences
        bestOf = (ToggleButton) findViewById(R.id.BestOf_input);
        bestOf.setChecked(true);
        firstTo = (ToggleButton) findViewById(R.id.FirstTo_input);
        sets = (ToggleButton) findViewById(R.id.Sets_input);
        legs = (ToggleButton) findViewById(R.id.Legs_input);
        legs.setChecked(true);
        bestOf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    firstTo.setChecked(false);
                    goal = true;
                }else{
                    firstTo.setChecked(true);
                    goal = false;
                }

            }
        });
        firstTo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    bestOf.setChecked(false);
                    goal = false;
                }else{
                    bestOf.setChecked(true);
                    goal = true;
                }
            }
        });
        sets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    legs.setChecked(false);
                    mode = false;
                }else{
                    legs.setChecked(true);
                    mode = true;
                }
            }
        });
        legs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sets.setChecked(false);
                    mode = true;
                }else{
                    sets.setChecked(true);
                    mode = false;
                }
            }
        });
    }
}
