package com.example.measureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // from HTTP library Volley
    private RequestQueue mQueue;

    // patients array (for patch method)
    private ArrayList<String> patientsArray = new ArrayList<String>();

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
        String patientName = editText.getText().toString();


        // url to post and get
        String url = "https://drf-firsttry.herokuapp.com/patients/";

        // key-value to post (body part)
        HashMap jsonBody = new HashMap();
        jsonBody.put("name", patientName);
        jsonBody.put("sbp_raw", 888);
        jsonBody.put("dbp_raw", 666);
        jsonBody.put("hr_raw", 666);

        // if the patient exists just update the data (not finished)
        for(String eachPatient: this.patientsArray){
            if(patientName.equals(eachPatient)) {
                Log.d("index", ""+this.patientsArray.indexOf(eachPatient)+1);
                JsonObjectRequest patchRequest = new JsonObjectRequest(Request.Method.PATCH, url + (this.patientsArray.indexOf(eachPatient)+1), new JSONObject(jsonBody), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // define textView dbp, sbp, hr
                        TextView dbpValue = (TextView) findViewById(R.id.textView_dbp_value);
                        TextView sbpValue = (TextView) findViewById(R.id.textView_sbp_value);
                        TextView hrValue = (TextView) findViewById(R.id.textView_hr_value);

                        try {
                            // show calculated data from server (dbp, sbp, ht)
                            dbpValue.setText(response.getString("dbp"));
                            sbpValue.setText(response.getString("sbp"));
                            hrValue.setText(response.getString("hr"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // copy from internet
                        error.printStackTrace();
                        Log.e("VOLLEY", error.toString());
                    }
                });
                mQueue.add(patchRequest);
                return;
            }
        }



        // post object (post PPG raw data and patient's name)
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonBody), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // define textView dbp, sbp, hr
                TextView dbpValue = (TextView) findViewById(R.id.textView_dbp_value);
                TextView sbpValue = (TextView) findViewById(R.id.textView_sbp_value);
                TextView hrValue = (TextView) findViewById(R.id.textView_hr_value);

                try {
                    // show calculated data from server (dbp, sbp, ht)
                    dbpValue.setText(response.getString("dbp"));
                    sbpValue.setText(response.getString("sbp"));
                    hrValue.setText(response.getString("hr"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // copy from internet
                error.printStackTrace();
                Log.e("VOLLEY", error.toString());
            }
        });
        // must add in queue
        this.patientsArray.add(patientName);
        mQueue.add(postRequest);

    }
}
