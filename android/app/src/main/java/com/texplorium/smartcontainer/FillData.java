package com.texplorium.smartcontainer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;


import static java.lang.Math.max;
import static java.lang.Math.min;

public class FillData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_data);

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
        final Spinner mSpinner = (Spinner) findViewById(R.id.spinner2);
        mSpinner.setAdapter(adapter);

        Button btn = (Button) findViewById(R.id.button3);
        final TextView txt = (TextView) findViewById(R.id.editText);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = String.valueOf(mSpinner.getSelectedItem());
                System.out.println(val);;
                final String parameter  = map.get(val);
                final String value = txt.getText().toString();

                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\n\t\"parameter\": \" "+ parameter.toString()+"\",\n\t\"value\": "+value.toString()+"\n}");
                Request request = new  Request.Builder()
                        .url("http://ec2-13-127-242-145.ap-south-1.compute.amazonaws.com:3000/setLimits")
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            call.cancel();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            System.out.println("Done");
//                            Toast.makeText(FillData.this, "Done", Toast.LENGTH_LONG).show();
                            Log.d("TAG",Integer.toString(response.code()));
                            Intent x = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(x);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }
}
