package com.example.vdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView mtemp,wtype,minmax,wdetails,msunrise,msunset;
    ImageButton btn;
    EditText city;
    RequestQueue requestqueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //requestqueue = Volley.newRequestQueue(this);
        requestqueue = MyVolley.getInstance(this).getMrequestQueue();
    wdetails = findViewById(R.id.wdetails);
    mtemp = findViewById(R.id.temp);
    wtype = findViewById(R.id.wtype);
    minmax = findViewById(R.id.minmax);
    msunrise = findViewById(R.id.sunrise);
    msunset = findViewById(R.id.sunset);
    city = findViewById(R.id.city);
    btn = findViewById(R.id.button);
    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String apiKey="&APPID=511593e3a1aade6ba494e877363bcee6";
            String base_url = "https://api.openweathermap.org/data/2.5/weather?q=";
            String url = base_url+city.getText().toString()+apiKey;
//         Log.i("msg1","api:"+url);
            JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
//                                Log.i("msg2","response:"+response);
                                //fetching temp & other info
                                JSONObject info1 = response.getJSONObject("main");
                                String temp=info1.getString("temp");
                                String temp_min=info1.getString("temp_min");
                                String temp_max=info1.getString("temp_max");
                                String pressure=info1.getString("pressure");
                                String humidity=info1.getString("humidity");

                                //fetching wind
                                JSONObject info2=response.getJSONObject("wind");
                                String wind_speed=info2.getString("speed");

                                //fetching sunrise-sunset
                                JSONObject info3=response.getJSONObject("sys");
                                String epoch_sunrise=info3.getString("sunrise");
                                String epoch_sunset=info3.getString("sunset");
                                String sunrise=epochToIST(Long.valueOf(epoch_sunrise)); //pass only long value
                                String sunset=epochToIST(Long.valueOf(epoch_sunset));

                                //fetching weather type
                                JSONArray info4 = response.getJSONArray("weather"); //becoz weather is an Array object
                                JSONObject ob= info4.getJSONObject(0);
                                String type=ob.getString("main");

                                //kelvin temp. conversion
                                int t = (int)(Float.valueOf(temp)-273.15f);
                                int tmin = (int)(Float.valueOf(temp_min)-273.15f);
                                int tmax = (int)(Float.valueOf(temp_max)-273.15f);
                                mtemp.setText(t +"°C");
                                minmax.setText(tmin+"°C/"+tmax+"°C");
                                wtype.setText(type);
                                wdetails.setText("Pressure: "+pressure+" hPa\n"+
                                                  "Humidity: "+humidity+"%\n"+
                                                      "Wind: "+wind_speed+"km/hr\n");
                                msunrise.setText("Sunrise:"+sunrise);
                                msunset.setText("Sunset:"+sunset);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this,"Oops Error!",Toast.LENGTH_LONG).show();
                }
            });
            requestqueue.add(request);
        }
    });
    }

    private String epochToIST(Long epoch) {
        String am_pm="AM";
        String hour = new java.text.SimpleDateFormat("HH").format(new java.util.Date(epoch*1000));
        String min = new java.text.SimpleDateFormat("mm").format(new java.util.Date(epoch*1000));
        int hr=Integer.parseInt(hour);

        if(hr>12)
        {
            hr-=12;
            am_pm="PM";
        }
        String s = Integer.toString(hr)+":"+ min + am_pm;
        return s;
    }

}
