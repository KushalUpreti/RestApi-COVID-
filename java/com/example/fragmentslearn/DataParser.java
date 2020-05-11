package com.example.fragmentslearn;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataParser extends AsyncTask<String, Void, ArrayList<Cases>> implements Download.OnDownloadComplete {

    public ArrayList<Cases> mlist;
    private final OnDataAvailable mcallBack;
    private boolean runningOnSameThread;
    private static final String TAG = "DataParser";

    interface OnDataAvailable {
        void onDataAvailable(ArrayList<Cases> mPhotos);
    }

    public DataParser(OnDataAvailable mcallBack) {
        this.mcallBack = mcallBack;
        mlist = new ArrayList<>();
    }

    @Override
    protected ArrayList<Cases> doInBackground(String... strings) {
        Download d = new Download(this);
        d.execute(strings[0]);
        Log.d(TAG, "doInBackground: Ends");
        return mlist;
    }

    @Override
    public void onDownloadComplete(String s) {

        try {
            if(s != null || s.length()>0){
                JSONObject jsonData = new JSONObject(s);
                JSONArray itemArray = jsonData.getJSONArray("Countries");
                for (int i = 0; i < itemArray.length(); i++) {

                    JSONObject jsonInfo = itemArray.getJSONObject(i);
                    String countryName = jsonInfo.getString("Country");
                    int newConfirmed = jsonInfo.getInt("NewConfirmed");
                    int totalConfirmed = jsonInfo.getInt("TotalConfirmed");
                    int newDeaths = jsonInfo.getInt("NewDeaths");
                    int totalDeaths = jsonInfo.getInt("TotalDeaths");
                    int newRecovered = jsonInfo.getInt("NewRecovered");
                    int totalRecovered = jsonInfo.getInt("TotalRecovered");
                    Cases newCases = new Cases(countryName, newConfirmed, totalConfirmed, totalDeaths, newDeaths, newRecovered, totalRecovered);
                    mlist.add(newCases);
                    Log.d(TAG, "onDownloadComplete: Completed");
                }
                if (mcallBack != null) {
                    mcallBack.onDataAvailable(mlist);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "onDownloadComplete: Failed" + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Cases> cases) {

    }
}

