package com.samirimtiaz.labproject;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashSet;

public class MultiText extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_text);
        final SharedPreferences sharedPreferences=getSharedPreferences("com.samirimtiaz.labproject",MODE_PRIVATE);
        final EditText editText=(EditText) findViewById(R.id.editText);
        editText.setText(NotesActivity.arrayList.get(NotesActivity.noteId));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                NotesActivity.arrayList.set(NotesActivity.noteId,String.valueOf(s));
                NotesActivity.arrayAdapter.notifyDataSetChanged();
                HashSet<String> hashSet=new HashSet<>(NotesActivity.arrayList);
                sharedPreferences.edit().putStringSet("Data",hashSet).apply();



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
