package com.example.weatherapp;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView weatherReport,maxTemp,minTemp,temp,humidity,windSpeed,conditions,sunRise,sunSet,pressure,day,date,cc, cityName;

    // Convert Unix timestamp to milliseconds
    public String[] convertHumanReadableTime(long sunriseTimestamp,long sunsetTimestamp){

        Date sunriseDate = new Date(sunriseTimestamp * 1000);
        Date sunsetDate = new Date(sunsetTimestamp * 1000);
        return new String[]{sunriseDate.toString(),sunsetDate.toString()};
    }

    private  void searchCity(){
        SearchView searchView=findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                call(query.toString().trim());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                call(newText.toString().trim());
                return false;
            }
        });
    }

    private void call(String city){
        cityName=findViewById(R.id.cityname);
        weatherReport=findViewById(R.id.weather);
        maxTemp=findViewById(R.id.maxtemp);
        minTemp=findViewById(R.id.mintemp);
        temp=findViewById(R.id.temp);
        humidity=findViewById(R.id.humidity);
        windSpeed=findViewById(R.id.wind);
        conditions=findViewById(R.id.condition);
        sunRise=findViewById(R.id.sunrise);
        sunSet=findViewById(R.id.sunset);
        pressure=findViewById(R.id.sea);
        day=findViewById(R.id.day);
        date=findViewById(R.id.date);
        cc=findViewById(R.id.condition);
        cityName.setText(" "+city);
        RequestQueue requestQueue=Volley.newRequestQueue(MainActivity.this);
        String url="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=2dfed47a21e99527a0bf3cff399961f8";

//        {"coord":{"lon":75.8167,"lat":26.9167},
//        "weather":[{"id":721,"main":"Haze","description":"haze","icon":"50n"}],
//        "base":"stations","main":{"temp":302.77,"feels_like":301.43,"temp_min":302.77,"temp_max":302.77,"pressure":1011,"humidity":28},
//        "visibility":4000,"wind":{"speed":1.54,"deg":0},
//        "clouds":{"all":40},
//        "dt":1711210627,
//        "sys":{"type":1,"id":9170,"country":"IN","sunrise":1711155434,"sunset":1711199352},
//        "timezone":19800,"id":1269515,"name":"Jaipur","cod":200}

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, jsonObject -> {
            try {
                JSONObject weatherObject=jsonObject.getJSONArray("weather").getJSONObject(0);
                String descrition=weatherObject.getString("description");
                cc.setText(descrition);
                weatherReport.setText(descrition);
                setBackground(descrition);//set bacground
                JSONObject jsonObject1=jsonObject.getJSONObject("main");
                String temp_min=jsonObject1.getString("temp_min").toString();
                String max_temp=jsonObject1.getString("temp_max").toString();
                String main_temp=jsonObject1.getString("temp").toString();
                maxTemp.setText("Max : "+(String.format("%.2f",Float.parseFloat(max_temp)-273.16))+" °C");
                minTemp.setText("Min : "+(String.format("%.2f",Float.parseFloat(temp_min)-273.16))+" °C");
                temp.setText((String.format("%.2f",Float.parseFloat(main_temp)-273.16))+" °C");
                String humi=jsonObject1.getString("humidity").toString();
                humidity.setText(humi+"%");
                String pre=jsonObject1.getString("pressure").toString();
                pressure.setText(pre+"hPa");

                //for wind
                JSONObject jsonObject2=jsonObject.getJSONObject("wind");
                String wind=jsonObject2.getString("speed").toString();
                windSpeed.setText(wind+"m/s");

                //for sun rise and sun set
                JSONObject jsonObject3=jsonObject.getJSONObject("sys");
                String sunrise=jsonObject3.getString("sunrise");
                String sunset=jsonObject3.getString("sunset");
                String[]arr=convertHumanReadableTime(Long.parseLong(sunrise),Long.parseLong(sunset));
                String[]sunRises=arr[0].split(" ");
                sunRise.setText(sunRises[3]);
                HashMap<String,String>map=new HashMap<>();
                map.put("Sat","saturday");
                map.put("Mon","monday");
                map.put("Tue","Tuesday");
                map.put("Sun","Sunday");
                map.put("Wed","Wednesday");
                map.put("Thur","Thursday");
                map.put("Fri","Friday");
                day.setText(map.getOrDefault(sunRises[0],sunRises[0]));
                String[]sunSetData=arr[1].split(" ");
                sunSet.setText(sunSetData[3]);
                date.setText(sunRises[2]+" "+sunRises[1]+" "+sunRises[5]);
            }catch (Exception e){

            }
        }, volleyError -> {

        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        call("Patna");
        searchCity();
    }
     private void setBackground(String description){
         LottieAnimationView lottieAnimationView=findViewById(R.id.lottieAnimationView);
         if(description.contains("rain")) {
             findViewById(R.id.main).setBackgroundResource(R.drawable.rain_background);
             lottieAnimationView.setAnimation(R.raw.rain);
             lottieAnimationView.playAnimation();
         }
         else if(description.contains("cloud")||description.contains("haze")||description.contains("mist")) {
             findViewById(R.id.main).setBackgroundResource(R.drawable.colud_background);
             lottieAnimationView.setAnimation(R.raw.cloud);
             lottieAnimationView.playAnimation();
         }else if(description.contains("snow")){
             findViewById(R.id.main).setBackgroundResource(R.drawable.snow_background);
             lottieAnimationView.setAnimation(R.raw.snow);
             lottieAnimationView.playAnimation();
         }else {
             findViewById(R.id.main).setBackgroundResource(R.drawable.sunny_background);
             lottieAnimationView.setAnimation(R.raw.sun);
             lottieAnimationView.playAnimation();
         }
     }
}