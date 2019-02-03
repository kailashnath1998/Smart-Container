package com.texplorium.smartcontainer;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.HashMap;

public class ViewData<series> extends AppCompatActivity {

        // use default spinner item to show options in spinne

    public static final String ARG_SECTION_NUMBER = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        ArrayList<String> tmp = new ArrayList<String>();
        final HashMap<String, String> map = new HashMap<>();
        tmp.add("Temperature");
        map.put("Temperature", "Temp");
        tmp.add("Light");
        map.put("Light","Light");
        tmp.add("Humidity");
        map.put("Humidity","Humidity");
        tmp.add("Gas");
        map.put("Gas","Gas");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,tmp);
        final Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinner.setAdapter(adapter);

        Button btn = (Button) findViewById(R.id.button4);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = String.valueOf(mSpinner.getSelectedItem());
                System.out.println(val);
                dispData myfrag = (dispData) getSupportFragmentManager().findFragmentById(R.id.fragment);
                String x  = map.get(val);
                Toast.makeText(v.getContext(), x, Toast.LENGTH_LONG);
                myfrag.reset(x);
            }
        });

    }

    public void onSectionAttached(int anInt) {

    }


}
