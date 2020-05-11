package com.example.fragmentslearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataParser.OnDataAvailable, RecyclerViewTouchDetector.OnRecyclerClickListner {
    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    SearchView searchview;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        searchview = (SearchView) menuItem.getActionView();

        if(searchview!= null){
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
        if(item.getItemId() == R.id.about){
            Toast.makeText(this,"About: App",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(ArrayList<Cases> mPhotos) {
        Log.d(TAG, "onDataAvailable: Data now available");
        Log.d(TAG, "onDataAvailable: "+ mPhotos.size());
        recyclerViewAdapter.loadNewDataSet(mPhotos);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        Intent intent = new Intent(this, Details.class);
        intent.putExtra("Cases",recyclerViewAdapter.getPhoto(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
