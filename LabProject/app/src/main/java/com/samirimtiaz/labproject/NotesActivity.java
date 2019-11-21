package com.samirimtiaz.labproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class NotesActivity extends AppCompatActivity {

    static ArrayList<String> arrayList;
    static ArrayAdapter arrayAdapter;
    static ListView listView;
    static int noteId;
    Intent intent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.menuItem:
                noteId=arrayList.size();
                arrayList.add("");
                Toast.makeText(NotesActivity.this,String.valueOf(noteId),Toast.LENGTH_SHORT).show();
                intent=new Intent(getApplicationContext(),MultiText.class);
                startActivity(intent);
                return true;


            default:
                return false;
            ///add menu item
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        final SharedPreferences sharedPreferences=getSharedPreferences("com.samirimtiaz.labproject",MODE_PRIVATE);
        HashSet<String> hashSet=new HashSet<>();
        hashSet=(HashSet<String>) sharedPreferences.getStringSet("Data",hashSet);
    arrayList = new ArrayList<String> (hashSet);
        listView=(ListView) findViewById(R.id.listView);
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        intent=new Intent(getApplicationContext(),MultiText.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                noteId=position;
                startActivity(intent);

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                noteId=position;

                new AlertDialog.Builder(NotesActivity.this).setTitle("DELETE").setMessage("Do you want to delete the selected item?").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arrayList.remove(noteId);
                        arrayAdapter.notifyDataSetChanged();
                        HashSet<String > hashSet=new HashSet<>(arrayList);
                        sharedPreferences.edit().putStringSet("Data",hashSet).apply();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

                return true;

            }
        });

    }
}
