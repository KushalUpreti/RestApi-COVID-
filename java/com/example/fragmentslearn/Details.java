package com.example.fragmentslearn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Details extends AppCompatActivity {
    TextView countryName;
    TextView newConfirmed;
    TextView totalConfirmed;
    TextView newDeaths;
    TextView totalDeaths;
    TextView newRecovered;
    TextView totalRecovered;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        countryName = findViewById(R.id.name);
        newConfirmed = findViewById(R.id.newConfirmed);
        totalConfirmed = findViewById(R.id.totalConfirmed);
        newDeaths = findViewById(R.id.newDeaths);
        totalDeaths = findViewById(R.id.totalDeaths);
        newRecovered = findViewById(R.id.newRecovered);
        totalRecovered = findViewById(R.id.totalRecovered);
        imageView = findViewById(R.id.reactionImage);

        Intent intent = getIntent();
        Cases input = (Cases) intent.getSerializableExtra("Cases");

        countryName.setText(input.getCountryName());
        newConfirmed.setText("New Confirmed Cases:    " + input.getNewConfirmed());
        totalConfirmed.setText("Total Confirmed Cases:  " + input.getTotalConfirmed());
        newDeaths.setText("New Deaths: " + input.getNewDeaths());
        totalDeaths.setText("Total Deaths:   " + input.getTotalDeaths());
        newRecovered.setText("New Recovered:  " + input.getNewRecovered());
        totalRecovered.setText("Total Recovered:    " + input.getTotalRecovered());

        if (input.getTotalDeaths() > 500) {
            imageView.setImageResource(R.drawable.cry);
        }
        else if(input.getTotalDeaths()<500 && input.getTotalDeaths()>100){
            imageView.setImageResource(R.drawable.mild);
        }else {
            imageView.setImageResource(R.drawable.smile);
        }
    }
}
