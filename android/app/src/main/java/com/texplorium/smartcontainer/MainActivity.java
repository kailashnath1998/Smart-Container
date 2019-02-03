package com.texplorium.smartcontainer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.BreakIterator;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button record = (Button) findViewById(R.id.button);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Set Data", Toast.LENGTH_LONG).show();
                Intent x = new Intent(getApplicationContext(), FillData.class);
                startActivity(x);
            }
        });

        Button viewData = (Button) findViewById(R.id.button2);

        viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"View Data", Toast.LENGTH_LONG).show();
                Intent x = new Intent(getApplicationContext(), ViewData.class);
                startActivity(x);
            }
        });


        try {
            fillDropdown();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        GraphView graph = (GraphView) findViewById(R.id.graph);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph.addSeries(series);
    }

    public void fillDropdown() throws IOException, ParseException {
        RequestQueue queue = Volley.newRequestQueue( MainActivity.this);
        String url ="http://192.168.43.175:3000/sensordata/Temp";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("Response is: "+ response);
                        JSONParser parser = new JSONParser();
                        org.json.simple.JSONObject obj = null;
                        System.out.println(response.toString());
                        try {
                            obj = (org.json.simple.JSONObject) parser.parse(response.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this, obj.get("keys").toString(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        Toast.makeText(MainActivity.this, "Done01", Toast.LENGTH_LONG).show();
    }
}
