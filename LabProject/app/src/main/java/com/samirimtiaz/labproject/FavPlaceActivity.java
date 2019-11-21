package com.samirimtiaz.labproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class FavPlaceActivity extends AppCompatActivity {

    ListView listView;
    Intent intent;
    static SharedPreferences sharedPreferences;

    static ArrayList<String> arrayList=new ArrayList<String>();
    static ArrayList<Double> longitude=new ArrayList<Double>();
    static ArrayList<Double> latitude=new ArrayList<Double>();

    public void buttonMainTapped(View view){
        intent=new Intent(getApplicationContext(),MapsActivity.class);
        intent.putExtra("addData",true);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_place);
        sharedPreferences=this.getSharedPreferences("com.samirimtiaz.labproject",Context.MODE_PRIVATE);
        //      if((ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("Name",ObjectSerializer.serialize(new ArrayList<String>()))))
        arrayList=(ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("Name",ObjectSerializer.serialize(new ArrayList<String>())));
        latitude=(ArrayList<Double>) ObjectSerializer.deserialize(sharedPreferences.getString("Latitude",ObjectSerializer.serialize(new ArrayList<LatLng>())));
        longitude=(ArrayList<Double>) ObjectSerializer.deserialize(sharedPreferences.getString("Longitude",ObjectSerializer.serialize(new ArrayList<Double>())));
        listView=(ListView) findViewById(R.id.listView);
        ArrayAdapter arrayAdapter=new ArrayAdapter(FavPlaceActivity.this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent=new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("dataNumber",position);
                startActivity(intent);
                finish();
            }
        });
    }
}

