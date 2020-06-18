package com.example.fragmentslearn;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements DataParser.OnDataAvailable, RecyclerViewTouchDetector.OnRecyclerClickListner {
    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    SearchView searchview;
    ArrayList<Cases> list;
    ArrayList<String> countries;
    private static final int CHANNEL_ID = 101;
    private static final int notifId = 27;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Starts");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchDetector(this, recyclerView, this));

        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<Cases>());
        recyclerView.setAdapter(recyclerViewAdapter);
        DataParser download = new DataParser(this);
        download.execute("https://api.covid19api.com/summary");

        countries = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        createNotificationChannel();;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        searchview = (SearchView) menuItem.getActionView();

        if (searchview != null) {
            searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    recyclerViewAdapter.getFilter().filter(newText);
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.about) {
            Toast.makeText(this, "About: App", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId()==R.id.clear){
            SharedPreferences countryname = getSharedPreferences("CountryData", MODE_PRIVATE);
            SharedPreferences.Editor editor = countryname.edit();
            editor.clear();
            editor.apply();

            SharedPreferences sp = getSharedPreferences("NepalData", MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sp.edit();
            editor1.clear();
            editor1.apply();

            Toast.makeText(this, "All favorites cleared", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onDataAvailable(ArrayList<Cases> mPhotos) {
        Log.d(TAG, "onDataAvailable: Data now available");
        recyclerViewAdapter.loadNewDataSet(mPhotos);
        this.list = mPhotos;
        saveData();
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts at " + position);
        Intent intent = new Intent(this, Details.class);
        intent.putExtra("Cases", recyclerViewAdapter.getPhoto(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        if (list != null) {
            SharedPreferences countryName = getSharedPreferences("CountryData", MODE_PRIVATE);
            ConstraintLayout background = view.findViewById(R.id.background);

            Cases value = list.get(position);
            String name = value.getCountryName();
            Dialog dialog = new Dialog(name,countryName,position,background);
            dialog.show(getSupportFragmentManager(), "ok");
        }
    }

    //118
    private void checkIncrement(String name, int deathcount) {

        SharedPreferences sp = getSharedPreferences("NepalData", MODE_PRIVATE);
        int currentCount = sp.getInt(name, 0);
        Log.d(TAG, "checkIncrement: "+ name +" " +deathcount+" " + currentCount);
        if (deathcount > currentCount) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(name, deathcount);
            editor.apply();
            Toast.makeText(this,name+ " toll increased by"+ (deathcount-currentCount),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void saveData(){
        SharedPreferences countryname = getSharedPreferences("CountryData", MODE_PRIVATE);
        Map<String,?> map = countryname.getAll();
        SharedPreferences sp = getSharedPreferences("NepalData", MODE_PRIVATE);

        for(Map.Entry entry:map.entrySet()){
            Log.d(TAG, "saveData: "+ entry.getValue());
            Cases cases = list.get((Integer) entry.getValue());
            String name = cases.getCountryName();
            Log.d(TAG, "saveData: "+ name);
            int totalCase = cases.getTotalDeaths();
            if(!sp.contains(name)){
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(name, totalCase);
                editor.apply();
            }
            checkIncrement(name,totalCase);
        }
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            CharSequence name = getString(R.string.channel_name);
            String desciption = getString(R.string.channel_description);
            int priorityLevel = NotificationManager.IMPORTANCE_DEFAULT;
            String channelid = getString(R.string.channel1_id);

            NotificationChannel channel1 = new NotificationChannel(channelid,name,priorityLevel);
            channel1.enableLights(true);
            channel1.enableVibration(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
        }
    }

    private void buildNotifications(String countryName, int deathIncrement){
        CharSequence title = "Death toll increased in " + countryName;
        CharSequence body = "Additional "+deathIncrement+ " deaths in "+ countryName+ "due to COVID-19 \uD83D\uDE22";
        NotificationCompat.Builder alertNotify = new NotificationCompat.Builder(this,getString(R.string.channel1_id))
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setAutoCancel(true); //removes notification when user clicks on it


    }
}
