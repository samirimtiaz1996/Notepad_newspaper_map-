package com.samirimtiaz.labproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    boolean gameIsActive=true;
    int activePlayer=0;//0=yellow, 1=red
    int gameState[]={2,2,2,2,2,2,2,2,2};
    int [][]winningPosition={{0,1,2},{0,3,6},{0,4,8},{2,4,6},{2,5,8},{1,4,7},{3,4,5},{6,7,8}};
    public void dropIn(View view) {
        ImageView Box = (ImageView) view;
        int tapperBox =Integer.parseInt (Box.getTag().toString());
        if (gameState[tapperBox-1] == 2 && gameIsActive) {
            gameState[tapperBox-1]=activePlayer;
            if (activePlayer == 0) {
                Box.setImageResource(R.drawable.yellow);
                activePlayer = 1;

            } else {
                Box.setImageResource(R.drawable.red);
                activePlayer = 0;

            }
            Box.setTranslationY(-1000f);
            Box.animate().translationYBy(1000f).setDuration(750);


            for(int[]Position:winningPosition) {
                if (gameState[Position[0]] == gameState[Position[1]] && gameState[Position[1]] == gameState[Position[2]] && gameState[Position[0]] != 2) {
                    Log.i("outer", "condition");
                    if (gameState[Position[0]] == 0) {
                        TextView textView1=(TextView) findViewById(R.id.textView1);
                        textView1.setText("Yellow won");
                        gameIsActive = false;
                        Log.i("yellow","condition");
                        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.linearLayout);
                        linearLayout.setVisibility(View.VISIBLE);


                    } else {
                        TextView textView1 = (TextView) findViewById(R.id.textView1);
                        textView1.setText("Red won");
                        gameIsActive = false;
                        Log.i("red","condition");
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
                        linearLayout.setVisibility(View.VISIBLE);

                    }
                }
            }
            if(gameIsActive){
                boolean gameIsOver=true;
                for(int state:gameState)
                {
                    if(state==2 ) {gameIsOver=false;}

                }
                if(gameIsOver) {
                    Log.i("draw outside ", "condition");

                    TextView textView1 = (TextView) findViewById(R.id.textView1);
                    textView1.setText("Draw");
                    Log.i("draw inside ", "condition");
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
                    linearLayout.setVisibility(View.VISIBLE);
                    gameIsActive = false;

                }
            }


        }


    }

    public void playAgain(View view)
    {

        this.recreate();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}
