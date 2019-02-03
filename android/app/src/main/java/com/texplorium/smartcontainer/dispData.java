package com.texplorium.smartcontainer;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Random;

import static java.lang.Math.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link dispData.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link dispData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dispData extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;
    private LineGraphSeries<DataPoint> mSeries1;
    private LineGraphSeries<DataPoint> mSeries2;
    private double graph2LastXValue = 5d;
    public String option = "Temp";
    GraphView graph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_disp_data, container, false);

        graph = (GraphView) rootView.findViewById(R.id.graph2);
        DataPoint[] values = new DataPoint[0];
        mSeries1 = new LineGraphSeries<>(values);
        graph.addSeries(mSeries1);

//        GraphView graph2 = (GraphView) rootView.findViewById(R.id.graph2);
//        mSeries2 = new LineGraphSeries<>();
//        graph2.addSeries(mSeries2);
//        graph2.getViewport().setXAxisBoundsManual(true);
//        graph2.getViewport().setMinX(0);
//        graph2.getViewport().setMaxX(40);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        ((ViewData) activity).onSectionAttached(
//                getArguments().getInt(ViewData.ARG_SECTION_NUMBER));
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer1 = new Runnable() {
            @Override
            public void run() {
                generateData();
                mHandler.postDelayed(this, 5000);
            }
        };
        mHandler.postDelayed(mTimer1, 5000);

//        mTimer2 = new Runnable() {
//            @Override
//            public void run() {
//                graph2LastXValue += 1d;
//                mSeries2.appendData(new DataPoint(graph2LastXValue, getRandom()), true, 40);
//                mHandler.postDelayed(this, 200);
//            }
//        };
//        mHandler.postDelayed(mTimer2, 1000);
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer1);
//        mHandler.removeCallbacks(mTimer2);
        super.onPause();
    }

    private void generateData() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url ="http://ec2-13-127-242-145.ap-south-1.compute.amazonaws.com:3000/sensordata/" + option;
        System.out.println(url);

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
                            double minx = Integer.MAX_VALUE, maxx = Integer.MIN_VALUE;
                            obj = (org.json.simple.JSONObject) parser.parse(response.toString());
                            org.json.simple.JSONArray keys = (org.json.simple.JSONArray) parser.parse( obj.get("keys").toString());
                            final DataPoint[] values = new DataPoint[min(30,keys.size())];
                            for( int i = max(0,keys.size() - 30); i < keys.size(); i++ ) {
                                org.json.simple.JSONObject myobj = (org.json.simple.JSONObject) keys.get(i);
                                double x = Double.parseDouble(myobj.get("time").toString());
                                double y = Double.parseDouble(myobj.get("value").toString());
                                System.out.println(x + " , " + y);
                                DataPoint v = new DataPoint(x, y);
                                values[i] = v;
                                minx = min(minx, y);
                                maxx = max(maxx, y);
                            }
                            graph.getViewport().setScrollable(true);
                            graph.getViewport().setMinY(-30);
                            mSeries1.resetData(values);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(MainActivity.this, obj.get("keys").toString(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                System.out.println("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
//        Toast.makeText(MainActivity.this, "Done01", Toast.LENGTH_LONG).show();
//        int count = 30;
//        DataPoint[] values = new DataPoint[count];
//        for (int i=0; i<count; i++) {
//            double x = i;
//            double f = mRand.nextDouble()*0.15+0.3;
//            double y = Math.sin(i*f+2) + mRand.nextDouble()*0.3;
//            DataPoint v = new DataPoint(x, y);
//            values[i] = v;
//        }
//        return values;
    }

    public void reset(String x) {
        DataPoint[] values = new DataPoint[0];
        mSeries1.resetData(values);
        option = x;

    }

    double mLastRandom = 2;
    Random mRand = new Random();
    private double getRandom() {
        return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
    }
}
