package com.samirimtiaz.labproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
    }
    public void buttonTapped(View view){
        switch (view.getId ()){
            case R.id.gameButton: {
               Intent intent = new Intent (getApplicationContext (), GameActivity.class);
               startActivity (intent);
                break;
            }
            case R.id.notepadButton:{
                Intent intent = new Intent (getApplicationContext (), NotesActivity.class);
                startActivity (intent);
                break;

            }
            case R.id.mapButton:{
                Intent intent = new Intent (getApplicationContext (), FavPlaceActivity.class);
                startActivity (intent);
                break;
            }
            case R.id.newspaperButton:{
                Intent intent=new Intent (getApplicationContext (),NewsReader.class);
                startActivity (intent);
                break;
            }
            default:
                break;

        }

    }
}
