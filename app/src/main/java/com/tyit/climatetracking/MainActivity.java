package com.tyit.climatetracking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ImageView weatherStatus;
    TextView temperature;
    TextView location;

    // Base URL for the OpenWeatherMap API. More secure https is a premium feature =(
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";

    final String LOGCAT_TAG = "LOG";

    // App ID to use OpenWeather data
    final String APP_ID = "e72ca729af228beabd5d20e3b7749713";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherStatus = findViewById(R.id.weatherStatus);
        temperature = findViewById(R.id.temperature);
        location = findViewById(R.id.locationName);

        ImageView changeLocation = findViewById(R.id.changeLocation);
        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ChangeLocation.class);
                startActivity(i);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!ChangeLocation.cityName.equals("")){
            RequestParams params = new RequestParams();
            params.put("q", ChangeLocation.cityName);
            params.put("appid", APP_ID);
            updateWeather(params);
        }
    }

    // This is the actual networking code. Parameters are already configured.
    private void updateWeather(RequestParams params) {

        // AsyncHttpClient belongs to the loopj dependency.
        AsyncHttpClient client = new AsyncHttpClient();

        // Making an HTTP GET request by providing a URL and the parameters.
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(LOGCAT_TAG, "Success! JSON: " + response.toString());
                WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
                updateUI(weatherData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.e(LOGCAT_TAG, "Fail " + e.toString());
                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();

                Log.d(LOGCAT_TAG, "Status code " + statusCode);
                if(response != null)
                    Log.d(LOGCAT_TAG, "Here's what we got instead " + response.toString());
            }

        });
    }

    // Updates the information shown on screen.
    private void updateUI(WeatherDataModel weather) {
        temperature.setText(weather.getTemperature());
        location.setText(weather.getCity());

        // Update the icon based on the resource id of the image in the drawable folder.
        int resourceID = getResources().getIdentifier(weather.getIconName(), "drawable", getPackageName());
        weatherStatus.setImageResource(resourceID);
    }
}
