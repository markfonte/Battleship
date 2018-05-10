package com.example.mfonte.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.lobbyButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        startApp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String userName = "";
        String sessionId = "";
        int userId = 0;
        if (resultCode == RESULT_OK && requestCode == 0 && data.hasExtra("mUserName")) {
            userName =  data.getExtras().getString("mUserName");
            userId = data.getExtras().getInt("mUserId");
            sessionId = data.getExtras().getString("mSessionId");
        }
        TextView username_display_text = findViewById(R.id.username_display_text);
        username_display_text.append(userName);
        sendGetRequestForLobbyData();
    }

    protected void insertLobbyRow(String username1, String username2, String state, String currentUserName) {
        LinearLayout lobbyListContainer = findViewById(R.id.lobbyLinearLayout);
        TextView user1 = new TextView(this);
        TextView user2 = new TextView(this);
        Button enterGame = new Button(this);
        boolean isUser1 = currentUserName.equals(username1);
        boolean isUser2 = currentUserName.equals(username2);
        user1.setText(username1);
        user1.setPadding(8, 8 , 8, 8);
        user1.setTextSize(18);
        user2.setText(username2);
        user2.setPadding(8, 8 , 8, 8);
        user2.setTextSize(18);
        switch(state) {
            case "join_game": {
                if(isUser1) {
                    enterGame.setText(getString(R.string.join_game_as_player));
                }
                else {
                    enterGame.setText(getString(R.string.join_open_game));
                }
            }
            break;
            case "setup": {
                if(isUser1 || isUser2) {
                    enterGame.setText(getString(R.string.join_game_as_player));
                }
                else {
                    enterGame.setText(getString(R.string.closed_game));
                }
            }
            break;
            case "gameplay": {
                if(isUser1 || isUser2) {
                    enterGame.setText(getString(R.string.join_game_as_player));
                }
                else {
                    enterGame.setText(getString(R.string.closed_game));
                }
            }
            break;
            case "game_over": {
                if(isUser1 || isUser2) {
                    enterGame.setText(getString(R.string.join_game_as_player));
                }
                else {
                    enterGame.setText(getString(R.string.closed_game));
                }
            }
            break;
            default: {
                Log.d("MainActivity.java", "default in switch case for game modes");
            }
        }

        LinearLayout lobbyRow = new LinearLayout(this);
        lobbyRow.addView(user1);
        lobbyRow.addView(user2);
        lobbyRow.addView(enterGame);
        lobbyListContainer.addView(lobbyRow);
    }

    private JSONObject mJSONContainer = null;

    protected void sendGetRequestForLobbyData()  {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2/api/game/lobby.php";

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("errorInfo", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorInfo", error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(getRequest);
    }

    @Override
    public void onBackPressed() {
    }

    protected void startApp() {
        Intent i = new Intent(this, Login.class);
        startActivityForResult(i, 0);
    }


}
