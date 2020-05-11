package com.example.fragmentslearn;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Download extends AsyncTask<String, Void, String> {
    public interface OnDownloadComplete{
        void onDownloadComplete(String s);
    }

    private static final String TAG = "Download";
    private OnDownloadComplete callback;

    Download(OnDownloadComplete callback){
        this.callback =  callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null){
            return null;
        }
        try {

            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was" + responseCode);
            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            for(String line = reader.readLine(); line!=null; line = reader.readLine()){
                result.append(line).append("\n");
            }

            return result.toString();
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL found" + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IO Exception" + e.getMessage());
        } catch (SecurityException s) {
            Log.e(TAG, "doInBackground: Permission Required." + s.getMessage());
        }finally {
            if(connection!=null){
                connection.disconnect();
            }
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if( s!= null){
            callback.onDownloadComplete(s);
        }
        super.onPostExecute(s);
    }

}
