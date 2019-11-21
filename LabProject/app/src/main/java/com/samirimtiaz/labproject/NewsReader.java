package com.samirimtiaz.labproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsReader extends AppCompatActivity {

    ArrayList<String> titles=new ArrayList<>();
    ArrayList<String> contents=new ArrayList<>();
    ArrayAdapter arrayAdapter;
    SQLiteDatabase sqLiteDatabase;
    int initializer;
    int range;

    protected void updateListView(){
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM articleApi",null);
        int contentIndex=cursor.getColumnIndex("content");
        int titleIndex=cursor.getColumnIndex("title");
        if(cursor.moveToFirst()) {
            titles.clear();
            contents.clear();

            do {
                titles.add(cursor.getString(titleIndex));
                contents.add(cursor.getString(contentIndex));
            } while (cursor.moveToNext());
            titles.add ("GETTING DATA,PLEASE WAIT");
            contents.add ("GETTING DATA,PLEASE WAIT");
        }

        arrayAdapter.notifyDataSetChanged();
    }

    protected class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String result="";
            URL url;
            HttpURLConnection httpURLConnection;
            try {
                url=new URL(strings[0]);
                httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                int data=inputStreamReader.read();
                while (data!=-1){
                    result+=(char) data;
                    data = inputStreamReader.read();
                }

                JSONArray jsonArray=new JSONArray(result);
                sqLiteDatabase.execSQL("DELETE FROM articleApi");
                for(int i=initializer;i<=range;i++){
                    String articleInfo="",articleId=jsonArray.getString(i);
                    url=new URL("https://hacker-news.firebaseio.com/v0/item/"+articleId+".json?print=pretty");
                    httpURLConnection=(HttpURLConnection) url.openConnection();
                    inputStream=httpURLConnection.getInputStream();
                    inputStreamReader=new InputStreamReader(inputStream);
                    data=inputStreamReader.read();
                    while (data!=-1){
                        articleInfo+=(char) data;
                        data=inputStreamReader.read();
                    }
                    JSONObject jsonObject=new JSONObject(articleInfo);


                    if(!jsonObject.isNull("title") && !jsonObject.isNull("url")){

                        String articleTitle=jsonObject.getString("title");
                        String articleUrl=jsonObject.getString("url");
                        url=new URL(articleUrl);
                        httpURLConnection=(HttpURLConnection) url.openConnection();
                        inputStream=httpURLConnection.getInputStream();
                        inputStreamReader=new InputStreamReader(inputStream);
                        String articleContent="";
                        data=inputStreamReader.read();
                        while (data!=-1){
                            articleContent+=(char) data;
                            data=inputStreamReader.read();
                        }
                        String sqlCommand="INSERT INTO articleApi (articleId,title,content) VALUES  (?,?,?)";
                        SQLiteStatement sqLiteStatement=sqLiteDatabase.compileStatement(sqlCommand);
                        sqLiteStatement.bindString(1,articleId);
                        sqLiteStatement.bindString(2,articleTitle);
                        sqLiteStatement.bindString(3,articleContent);
                        sqLiteStatement.execute();
                    }


                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }

            return "";
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateListView();
            initializer=initializer+1;
            range=range+1;
            DownloadTask downloadTask=new DownloadTask();
            try {
                downloadTask.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate (values);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_reader);


        ListView listView=(ListView) findViewById(R.id.listView);
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,titles);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),ShowNews.class);
                intent.putExtra("contentHtml",contents.get(position));
                startActivity(intent);
            }
        });

        sqLiteDatabase=this.openOrCreateDatabase("NewsReaderDb",MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS articleApi (id INTEGER PRIMARY KEY,articleId INTEGER,title VARCHAR,content VARCHAR)");


        updateListView();
        titles.add ("GETTING DATA,PLEASE WAIT");
        contents.add ("GETTING DATA,PLEASE WAIT");
        DownloadTask downloadTask=new DownloadTask();
        try {
            downloadTask.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
        }
        catch (Exception e){
            e.printStackTrace();
        }





    }
}
