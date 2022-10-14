package com.shravanth.myweatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = null;
    private ImageView headerSIV, spotifyPage;
    private ScrollView scrollLayout;
    private ShapeableImageView refreshButton;
    private ImageView refreshIcon;
    private MotionLayout motionLayout;
    private ImageView searchIconIV, conditionIV;
    private weatherAdapterRV weatherAdapterRV;
    private weatherAdapterDailyForecast weatherAdapterDailyForecast;
    private TextInputLayout cityNameTIL;
    private TextInputEditText cityNameTIET;
    private TextView temperatureTV, conditionTV, sunsetResultTV, sunriseResultTV, pressureResultTV, humidityResultTV, dateTimeTV;
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    private ConstraintLayout cardBackground;
    private RelativeLayout cardBackgroundRL;
    private RecyclerView recyclerView, dailyForecastRecyclerView;
    private ArrayList<weatherComponents> weatherRVComponentsArrayList = new ArrayList<>();
    private ArrayList<weatherDailyForecast> weatherDailyForecastArrayList;
    private int PERMISSION_CODE = 1;
    private int condition = 0;
    private int weatherSet = 0;
    private String API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        headerSIV = findViewById(R.id.headerSIV);
        spotifyPage = findViewById(R.id.spotifyPage);
        scrollLayout = findViewById(R.id.scrollLayout);
        refreshIcon = findViewById(R.id.refreshIcon);
        refreshButton = findViewById(R.id.refreshButton);
        motionLayout = findViewById(R.id.motionLayout);
        searchIconIV = findViewById(R.id.searchIconIV);
        conditionIV = findViewById(R.id.conditionIV);
        cityNameTIL = findViewById(R.id.cityNameTIL);
        cityNameTIET = findViewById(R.id.cityNameTIET);
        temperatureTV = findViewById(R.id.temperatureTV);
        conditionTV = findViewById(R.id.conditionTV);
        sunsetResultTV = findViewById(R.id.sunsetResultTV);
        sunriseResultTV = findViewById(R.id.sunriseResultTV);
        pressureResultTV = findViewById(R.id.pressureResultTV);
        humidityResultTV = findViewById(R.id.humidityResultTV);
        dateTimeTV = findViewById(R.id.dateTimeTV);
        dailyForecastRecyclerView = findViewById(R.id.dailyForecastRecyclerView);
        recyclerView = findViewById(R.id.recyclerView);
        cardBackgroundRL = findViewById(R.id.cardBackgroundRL);
        cardBackground = findViewById(R.id.cardBackground);

        API_KEY = "15ec6ebd98cb9e279015acfd8660bbf5";

        weatherDailyForecastArrayList = new ArrayList<>();
        weatherAdapterDailyForecast = new weatherAdapterDailyForecast(this, weatherDailyForecastArrayList);
        dailyForecastRecyclerView.setAdapter(weatherAdapterDailyForecast);

        weatherRVComponentsArrayList = new ArrayList<>();
        weatherAdapterRV = new weatherAdapterRV(this, weatherRVComponentsArrayList);
        recyclerView.setAdapter(weatherAdapterRV);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setDuration(1000);

        getWeatherInfo(17.38, 78.48);

        locationGetter();

        spotifyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(weatherSet == 1) {
                    Intent intent = new Intent(MainActivity.this, SpotifyLayout.class);
                    intent.putExtra("weatherCondition", condition);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Weather is not updated yet, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cityNameTIET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    keyboardHide();
                    getCityName();
                }
                return false;
            }
        });

        searchIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                getCityName();
            }

        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                refreshIcon.startAnimation(rotateAnimation);

                locationGetter();

            }
        });

    }

    public void locationGetter() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        boolean isEnabled = false;
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        if (isEnabled) {

            getLocation();

        } else {

            setGPS();

        }
    }

    private void setGPS() {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, 101);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        locationGetter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(MainActivity.this, "Please provide location permissions", Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }


    private void getLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                refreshIcon.clearAnimation();
                double Longitude = location.getLongitude();
                double Latitude = location.getLatitude();

                getLongLat(Latitude, Longitude);
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1000, locationListener);

    }

    private void getWeatherInfo(double latitude, double longitude) {

        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + latitude + "&lon=" + longitude + "&exclude=minutely&appid=" + API_KEY + "&units=metric";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onResponse(JSONObject response) {
                        String timeZone = null;
                        weatherRVComponentsArrayList.clear();
                        weatherDailyForecastArrayList.clear();

                        try {
                            String temperatureCurrent = temperatureConverter(response.getJSONObject("current").getString("temp"));

                            temperatureTV.setText(temperatureCurrent);

                            String conditionWeather = response.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("main");
                            conditionTV.setText(conditionWeather);


                            timeZone = response.getString("timezone");

                            String dateTime = getDate(response.getJSONObject("current").getString("dt"), timeZone);
                            dateTimeTV.setText(dateTime);

                            String conditionIcon = response.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("icon");
                            String urlIcon = "https://openweathermap.org/img/wn/" + conditionIcon + "@2x.png";
                            Picasso.get().load(urlIcon).into(conditionIV);

                            char dayOrNight = conditionIcon.charAt(conditionIcon.length() - 1);

                            int weatherCondition = Character.compare(dayOrNight, 'd');

                            if (weatherCondition == 0) { //Day
                                condition = 7;
                                headerSIV.setImageResource(R.drawable.day_bgv2);
                                refreshButton.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.dailyforecast_bg_day));
                                cardBackground.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.card_bg_dayv2));
                            } else {
                                condition = 8; //Night
                                headerSIV.setImageResource(R.drawable.night_bgv2);
                                refreshButton.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.dailyforecast_bg_night));
                                cardBackground.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.card_bg_nightv2));

                            }

                            if(conditionWeather.equals("Rain") || conditionWeather.equals("Drizzle") || conditionWeather.equals("Thunderstorm")) { //Rain and night
                                if(weatherCondition == 0) {
                                    condition = 3;
                                } else {
                                    condition = 2;
                                }
                            } else if(conditionWeather.equals("Clouds")) { //Clouds
                                condition = 4;
                            } else if(conditionWeather.equals("Haze") || conditionWeather.equals("Mist") || conditionWeather.equals("Fog")) { //Fod or Mist or Haze
                                condition = 5;
                            } else if(conditionWeather.equals("Snow")) { //Snow
                                condition = 6;
                            }

                            String humidity = response.getJSONObject("current").getString("humidity");
                            String windSpeed = response.getJSONObject("current").getString("wind_speed");

                            humidityResultTV.setText(humidity + "%");
                            pressureResultTV.setText(windSpeed + " m/s");

                            String sunrise = response.getJSONObject("current").getString("sunrise");
                            String sunset = response.getJSONObject("current").getString("sunset");

                            String sunriseTime = getTime(sunrise, timeZone);
                            String sunsetTime = getTime(sunset, timeZone);

                            sunriseResultTV.setText(sunriseTime);
                            sunsetResultTV.setText(sunsetTime);

                            JSONArray JsonArrayHourly = response.getJSONArray("hourly");

                            for ( int i = 0; i < JsonArrayHourly.length() - 24; i++) {

                                String temperatureHourly = temperatureConverter(JsonArrayHourly.getJSONObject(i).getString("temp"));
                                String dewPointHourly = temperatureConverter(JsonArrayHourly.getJSONObject(i).getString("dew_point"));
                                String humidityHourly = temperatureConverter(JsonArrayHourly.getJSONObject(i).getString("humidity"));
                                String pressureHourly = temperatureConverter(JsonArrayHourly.getJSONObject(i).getString("pressure"));
                                String timeHourly = getTime(JsonArrayHourly.getJSONObject(i).getString("dt"), timeZone);
                                String iconHourly = JsonArrayHourly.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                                String windSpeedHourly = JsonArrayHourly.getJSONObject(i).getString("wind_speed");


                                String urlIconHourly = "https://openweathermap.org/img/wn/" + iconHourly + "@2x.png";

                                weatherRVComponentsArrayList.add(new weatherComponents(timeHourly, temperatureHourly, urlIconHourly, windSpeedHourly,
                                        conditionIcon, humidityHourly, pressureHourly, dewPointHourly, false));

                            }

                            weatherAdapterRV.notifyDataSetChanged();

                            JSONArray JsonArrayDaily = response.getJSONArray("daily");

                            for (int j = 0; j < 8; j++) {

                                String maxTemp = temperatureConverter(JsonArrayDaily.getJSONObject(j).getJSONObject("temp").getString("max"));
                                String minTemp = temperatureConverter(JsonArrayDaily.getJSONObject(j).getJSONObject("temp").getString("min"));
                                String date = getDate(JsonArrayDaily.getJSONObject(j).getString("dt"), timeZone);

                                String conditionDaily = JsonArrayDaily.getJSONObject(j).getJSONArray("weather").getJSONObject(0).getString("description");
                                String conditionIconDaily = JsonArrayDaily.getJSONObject(j).getJSONArray("weather").getJSONObject(0).getString("icon");

                                String urlConditionIconDaily = "https://openweathermap.org/img/wn/" + conditionIconDaily + "@2x.png";

                                String humid = JsonArrayDaily.getJSONObject(j).getString("humidity");
                                String windSpd = JsonArrayDaily.getJSONObject(j).getString("wind_speed");

                                String sunriseDailyForecast = JsonArrayDaily.getJSONObject(j).getString("sunrise");
                                String sunsetDailyForecast = JsonArrayDaily.getJSONObject(j).getString("sunset");

                                String sunriseTimeDaily = getTime(sunriseDailyForecast, timeZone);
                                String sunsetTimeDaily = getTime(sunsetDailyForecast, timeZone);

                                weatherDailyForecastArrayList.add(new weatherDailyForecast(date, conditionDaily, maxTemp, minTemp, urlConditionIconDaily, humid, windSpd, sunriseTimeDaily, sunsetTimeDaily, false));
                            }

                            weatherAdapterDailyForecast.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Location failed", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

        weatherSet = 1;
        requestQueue.add(jsonObjectRequest);
    }

    private String getTime(String time, String timeZone) {

        Long timeUNIXLong = Long.parseLong(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        String convertedTime = simpleDateFormat.format(new java.util.Date(timeUNIXLong * 1000L));
        return convertedTime;
    }

    private String getDate(String time, String timeZone) {

        Long timeUNIXLong = Long.parseLong(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        String convertedTime = simpleDateFormat.format(new java.util.Date(timeUNIXLong * 1000L));
        return convertedTime;
    }

    private void keyboardHide() {
        InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view2 = MainActivity.this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view2 == null) {
            view2 = new View(MainActivity.this);
        }
        imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
    }

    private String temperatureConverter(String temp) {

        char temp1 = 'a';
        char temp2 = 'b';
        int i = 1;
        int j = 1;

        if (temp.length() > 1) {
            temp1 = temp.charAt(0);
            temp2 = temp.charAt(1);
            i = Character.compare(temp1, '-');
            j = Character.compare(temp2, '0');
        }

        String tempWithoutDecimal = null;

        if (i == 0 && j == 0) {
            tempWithoutDecimal = "0";
        } else {
            double temperatureWithDecimal = Double.parseDouble(temp);
            tempWithoutDecimal = String.format("%.0f", temperatureWithDecimal);
        }
        return tempWithoutDecimal;
    }

    private void getLongLat(double latitude, double longitude) {

        Geocoder coder = new Geocoder(MainActivity.this);
        List<Address> address;

        try {
            address = coder.getFromLocation(latitude, longitude, 5);

            if (address == null) {
                Toast.makeText(MainActivity.this, "No location found", Toast.LENGTH_SHORT).show();
            }
            Address location = address.get(0);
            String state = location.getAdminArea();
            String cityName = location.getLocality();

            if (cityName == null) {
                cityNameTIET.setText(state);
            } else {
                cityNameTIET.setText(cityName);
            }
            getWeatherInfo(latitude, longitude);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getCityName(){

        refreshIcon.clearAnimation();

        String city = cityNameTIET.getText().toString();
        Geocoder coder = new Geocoder(MainActivity.this);

        keyboardHide();
        cityNameTIET.clearFocus();

        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(city, 5);
            //Toast.makeText(MainActivity.this, "" +address.toString(), Toast.LENGTH_SHORT).show();
            if (address == null) {
                Toast.makeText(MainActivity.this, "No location found", Toast.LENGTH_SHORT).show();
                ;
            }
            Address location = address.get(0);
            String state = location.getAdminArea();
            String cityName = location.getLocality();
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

            double latitude = p1.latitude;
            double longitude = p1.longitude;

            if (cityName == null) {
                cityNameTIET.setText(state);
            } else {
                cityNameTIET.setText(cityName);
            }

            getWeatherInfo(latitude, longitude);

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

}


