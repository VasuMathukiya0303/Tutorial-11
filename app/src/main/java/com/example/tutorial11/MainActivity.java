package com.example.tutorial11;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    CustomAdapter adapter;
    ListView lstData;
    ProgressDialog dialog;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    JsonArrayRequest jsonArrayRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstData = findViewById(R.id.lstData);
        dialog = new ProgressDialog(MainActivity.this);
//        new MyAsyncTask().execute();

        VolleyNetworkCall();

        lstData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,UserDetail.class);
                intent.putExtra("userPosition",i);
                startActivity(intent);
            }
        });
    }

    private void VolleyNetworkCall() {
        jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                MyUtil.URL_USERS,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        MyUtil.jsonArray = response;
                        adapter = new CustomAdapter(MainActivity.this,MyUtil.jsonArray);
                        lstData.setAdapter(adapter);
                        if (dialog.isShowing())dialog.dismiss();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog.isShowing())dialog.dismiss();
                    }
                }
        );


        stringRequest = new StringRequest(
                Request.Method.GET,
                MyUtil.URL_USERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            MyUtil.jsonArray = new JSONArray(response);
                            adapter = new CustomAdapter(MainActivity.this,MyUtil.jsonArray);
                            lstData.setAdapter(adapter);
                            if (dialog.isShowing())dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog.isShowing())dialog.dismiss();
                    }
                }
        );
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        dialog.show();
        requestQueue.add(jsonArrayRequest);
        requestQueue.add(stringRequest);
    }
}