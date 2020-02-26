package com.example.measureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // from HTTP library Volley
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // default
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 大家都用 Queue 暫未理解原因
        mQueue = Volley.newRequestQueue(this);
    }

    /**
     * sends the raw data to server and gets sbp, dbp, hr back
     */
    public void getMeasure(View view) {

        // get the patient's name
        EditText editText = (EditText) findViewById(R.id.patient_name_text);
        String getPatientName = editText.getText().toString();

        // url to post and get
        String url = "https://drf-firsttry.herokuapp.com/patients/";

        // key-value to post (body part)
        HashMap jsonBody = new HashMap();
        jsonBody.put("name", getPatientName);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonBody), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // copy from internet
                error.printStackTrace();
                Log.e("VOLLEY", error.toString());
            }
        });
        mQueue.add(postRequest);
    }
}
