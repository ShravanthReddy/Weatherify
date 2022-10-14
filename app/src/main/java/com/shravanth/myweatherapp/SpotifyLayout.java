package com.shravanth.myweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class SpotifyLayout extends AppCompatActivity {

    private ImageView backButton, coverImage, playPauseButton, skipNext, skipPrevious;
    private TextView playlistName, artistName, trackName;
    private MaterialButton playlistPlay;
    private ProgressBar progressBar;
    private SpotifyAppRemote mSpotifyAppRemote;
    private static final String CLIENT_ID = "7a23226f61f64ba79311cb06cc117849";
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "http://com.shravanth.myweatherapp/callback";
    ConnectionParams connectionParams =
            new ConnectionParams.Builder(CLIENT_ID)
                    .setRedirectUri(REDIRECT_URI)
                    .showAuthView(true)
                    .build();
    int weatherCondition = 3;
    private String playlistID = null;
    private String uri = null;
    private TextView disclaimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_layout);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        Intent intent = getIntent();
        weatherCondition = intent.getIntExtra("weatherCondition", 3);

        if (weatherCondition == 2) { //Rain at Night
            playlistID = "https://api.spotify.com/v1/playlists/37i9dQZF1DXbcPC6Vvqudd";
            uri = "spotify:playlist:37i9dQZF1DXbcPC6Vvqudd";
        } else if(weatherCondition == 3) { //Rain at Day
            playlistID = "https://api.spotify.com/v1/playlists/37i9dQZF1DXbvABJXBIyiY1";
            uri = "spotify:playlist:37i9dQZF1DXbvABJXBIyiY";
        } else if(weatherCondition == 4) { //Clouds
            playlistID = "https://api.spotify.com/v1/playlists/00kJHJCMb91dDZ3LoLxvjW";
            uri = "spotify:playlist:00kJHJCMb91dDZ3LoLxvjW";
        } else if(weatherCondition == 5) { //Haze
            playlistID = "https://api.spotify.com/v1/playlists/6cSFHmRgZTkDRONocm2uCS";
            uri = "spotify:playlist:6cSFHmRgZTkDRONocm2uCS";
        } else if(weatherCondition == 6) { //Snow
            playlistID = "https://api.spotify.com/v1/playlists/4WCmHOBqKS7pac4s1lW2ZY";
            uri = "spotify:playlist:4WCmHOBqKS7pac4s1lW2ZY";
        } else if(weatherCondition == 7) { //Day
            playlistID = "https://api.spotify.com/v1/playlists/37i9dQZF1DXc5e2bJhV6pu";
            uri = "spotify:playlist:37i9dQZF1DXc5e2bJhV6pu";
        } else if(weatherCondition == 8) { //Night
            playlistID = "https://api.spotify.com/v1/playlists/37i9dQZF1DXdQvOLqzNHSW";
            uri = "spotify:playlist:37i9dQZF1DXdQvOLqzNHSW";
        }

        LoginActivity();

        backButton = findViewById(R.id.backButton);
        coverImage = findViewById(R.id.coverImage);
        progressBar = findViewById(R.id.progressBar);
        playlistName = findViewById(R.id.playlistName);
        playlistPlay = findViewById(R.id.playlistPlay);
        disclaimer = findViewById(R.id.disclaimer);

        playPauseButton = findViewById(R.id.playPauseButton);
        skipNext = findViewById(R.id.skipNext);
        skipPrevious = findViewById(R.id.skipPrevious);

        artistName = findViewById(R.id.artistName);
        trackName = findViewById(R.id.trackName);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        playlistPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpotifyAppRemote.connect(SpotifyLayout.this, connectionParams,
                        new Connector.ConnectionListener() {

                            @Override
                            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                                mSpotifyAppRemote = spotifyAppRemote;

                                mSpotifyAppRemote.getPlayerApi().play(uri);
                                playPauseButton.setVisibility(View.VISIBLE);
                                skipNext.setVisibility(View.VISIBLE);
                                skipPrevious.setVisibility(View.VISIBLE);
                                playlistName.setVisibility(View.INVISIBLE);
                                playlistPlay.setVisibility(View.INVISIBLE);
                                disclaimer.setVisibility(View.INVISIBLE);

                                mSpotifyAppRemote.getPlayerApi()
                                        .subscribeToPlayerState()
                                        .setEventCallback(playerState -> {
                                            final Track track = playerState.track;

                                            if (playerState.isPaused) {
                                                playPauseButton.setBackground(ContextCompat.getDrawable(SpotifyLayout.this, R.drawable.ic_baseline_play_circle_filled_24));
                                            } else {
                                                playPauseButton.setBackground(ContextCompat.getDrawable(SpotifyLayout.this, R.drawable.ic_baseline_pause_circle_filled_24));
                                            }
                                            if (track != null) {
                                                trackName.setText(track.name);
                                                artistName.setText(track.artist.name);

                                                trackName.setVisibility(View.VISIBLE);
                                                artistName.setVisibility(View.VISIBLE);

                                                mSpotifyAppRemote
                                                        .getImagesApi()
                                                        .getImage(playerState.track.imageUri, Image.Dimension.LARGE)
                                                        .setResultCallback(
                                                                bitmap -> {
                                                                    coverImage.setImageBitmap(bitmap);

                                                                });
                                            }
                                        });
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                                // Something went wrong when attempting to connect! Handle errors here
                            }
                        });
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(playerState -> {
                    if (playerState.isPaused) {
                        mSpotifyAppRemote
                                .getPlayerApi()
                                .resume();
                        playPauseButton.setBackground(ContextCompat.getDrawable(SpotifyLayout.this, R.drawable.ic_baseline_pause_circle_filled_24));
                    } else {
                        mSpotifyAppRemote
                                .getPlayerApi()
                                .pause();
                        playPauseButton.setBackground(ContextCompat.getDrawable(SpotifyLayout.this, R.drawable.ic_baseline_play_circle_filled_24));

                    }
                });
            }
        });

        skipNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpotifyAppRemote.getPlayerApi().skipNext();
            }
        });

        skipPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpotifyAppRemote.getPlayerApi().skipPrevious();
            }
        });
    }

    protected void LoginActivity() {

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            //Toast.makeText(MainActivity.this, "In On activity result", Toast.LENGTH_SHORT).show();
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    new AsyncTask().execute(response.getAccessToken());

                    break;

                // Auth flow returned an error
                case ERROR:
                    finish();
                    Toast.makeText(SpotifyLayout.this, "E" + response.getError(), Toast.LENGTH_SHORT).show();

                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    finish();
                    Toast.makeText(SpotifyLayout.this, "Default", Toast.LENGTH_SHORT).show();

                    // Handle other cases
            }
        }

    }

    public class AsyncTask extends android.os.AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String response = null;
            StringBuilder jsonString = new StringBuilder();
            try {
                URL url = new URL(playlistID);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("GET");

                httpConn.setRequestProperty("Accept", "application/json");
                httpConn.setRequestProperty("Content-Type", "application/json");
                httpConn.setRequestProperty("Authorization", "Bearer " + strings[0]);

                InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                        ? httpConn.getInputStream()
                        : httpConn.getErrorStream();
                Scanner s = new Scanner(responseStream).useDelimiter("\\A");
                response = s.hasNext() ? s.next() : "";

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String result) {
            JsonObject jsonObj = new JsonParser().parse(result).getAsJsonObject();
            String url = jsonObj.getAsJsonArray("images").get(0).getAsJsonObject().get("url").getAsString();

            Picasso.get().load(url).into(coverImage);
            String name = jsonObj.get("name").getAsString();
            playlistName.setText(name);

            progressBar.setVisibility(View.INVISIBLE);
            playlistName.setVisibility(View.VISIBLE);
            playlistPlay.setVisibility(View.VISIBLE);
            disclaimer.setVisibility(View.VISIBLE);
            coverImage.setVisibility(View.VISIBLE);

        }
    }
}