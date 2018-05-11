package com.example.mfonte.battleship;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String mLoggedInUserName = "";
    private int mLoggedInUserId = 0;
    private String mLoggedInSessionId = "";
    private JSONArray mJSONContainer = null;

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
        if (resultCode == RESULT_OK && requestCode == 0 && data.hasExtra("mUserName") && data.getExtras() != null) {
            mLoggedInUserName = data.getExtras().getString("mUserName");
            mLoggedInUserId = data.getExtras().getInt("mUserId");
            mLoggedInSessionId = data.getExtras().getString("mSessionId");
        }
        TextView username_display_text = findViewById(R.id.username_display_text);
        username_display_text.append(mLoggedInUserName);
        sendGetRequestForLobbyData();
    }

    protected void insertLobbyRow(String username1, String username2, int stateId, String currentUserName) {
        LinearLayout lobbyListContainer = findViewById(R.id.lobbyLinearLayout);
        TextView user1 = new TextView(this);
        TextView user2 = new TextView(this);
        Button enterGame = new Button(this);
        enterGame.setWidth(400);
        user1.setTextColor(Color.rgb(0,0,140));
        user1.setWidth(440);
        user2.setWidth(440);
        if(username2.equals("")) {
            username2 = "OPEN";
            user2.setTextColor(Color.rgb(0,160,0));
        }
        else {
            user2.setTextColor(Color.rgb(139,0,139));
        }
        boolean isUser1 = currentUserName.equals(username1);
        boolean isUser2 = currentUserName.equals(username2);
        user1.setText(username1);
        user1.setPadding(8, 8, 8, 8);
        user1.setTextSize(18);
        user2.setText(username2);
        user2.setPadding(8, 8, 8, 8);
        user2.setTextSize(18);
        String state;
        switch (stateId) {
            case 0:
                state = "join_game";
                break;
            case 1:
                state = "setup";
                break;
            case 2:
                state = "game_play";
                break;
            case 3:
                state = "game_over";
                break;
            default:
                state = "game_over";
        }
        switch (state) {
            case "join_game": {
                if (isUser1) {
                    enterGame.setText(getString(R.string.join_game_as_player));
                    enterGame.setTextColor(Color.rgb(0,128,0));
                } else {
                    enterGame.setText(getString(R.string.join_open_game));
                    enterGame.setTextColor(Color.rgb(0,128,0));
                }
            }
            break;
            case "setup": {
                if (isUser1 || isUser2) {
                    enterGame.setText(getString(R.string.join_game_as_player));
                    enterGame.setTextColor(Color.rgb(0,128,0));
                } else {
                    enterGame.setText(getString(R.string.closed_game));
                    enterGame.setTextColor(Color.rgb(255,0,0));
                }
            }
            break;
            case "game_play": {
                if (isUser1 || isUser2) {
                    enterGame.setText(getString(R.string.join_game_as_player));
                    enterGame.setTextColor(Color.rgb(0,128,0));
                } else {
                    enterGame.setText(getString(R.string.closed_game));
                    enterGame.setTextColor(Color.rgb(255,0,0));
                }
            }
            break;
            case "game_over": {
                if (isUser1 || isUser2) {
                    enterGame.setText(getString(R.string.join_game_as_player));
                    enterGame.setTextColor(Color.rgb(0,128,0));
                } else {
                    enterGame.setText(getString(R.string.closed_game));
                    enterGame.setTextColor(Color.rgb(255,0,0));
                }
            }
            break;
            default: {
                Log.d("MainActivity.java", "default in switch case for game modes");
            }
        }
        if(isUser1) {
            user1.setTextColor(Color.rgb(0, 128,0));
        }
        if(isUser2) {
            user2.setTextColor(Color.rgb(0, 128,0));
        }
        LinearLayout lobbyRow = new LinearLayout(this);
        lobbyRow.addView(user1);
        lobbyRow.addView(user2);
        lobbyRow.addView(enterGame);
        lobbyListContainer.addView(lobbyRow);
    }

    protected void sendGetRequestForLobbyData() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
       // String url = "http://10.0.2.2/api/game/lobby.php";
        String url = "http://dmilazterns01.learninga-z.com:8080/api/game/lobby.php";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("MainActivity.java", "Success: " + response.toString());
                        try {
                            if (Integer.parseInt(response.get("length").toString()) > 0) {
                                LinearLayout lobbyListContainer = findViewById(R.id.lobbyLinearLayout);
                                TextView user1 = new TextView(MainActivity.this);
                                TextView user2 = new TextView(MainActivity.this);
                                TextView user3 = new TextView(MainActivity.this);
                                user1.setText(getString(R.string.player_one));
                                user1.setPadding(8, 8, 8, 8);
                                user1.setTextSize(20);
                                user1.setWidth(440);
                                user1.setTypeface(null, Typeface.BOLD);
                                user2.setText(getString(R.string.player_two));
                                user2.setPadding(8, 8, 8, 8);
                                user2.setTextSize(20);
                                user2.setWidth(440);
                                user2.setTypeface(null, Typeface.BOLD);
                                user3.setWidth(400);
                                LinearLayout lobbyRow = new LinearLayout(MainActivity.this);
                                lobbyRow.addView(user1);
                                lobbyRow.addView(user2);
                                lobbyRow.addView(user3);
                                lobbyListContainer.addView(lobbyRow);
                                mJSONContainer = response.getJSONArray("games");
                                for (int x = 0; x < Integer.parseInt(response.get("length").toString()); ++x) {
                                    JSONObject jsonObject = mJSONContainer.getJSONObject(x);
                                    int mGameId = Integer.parseInt(jsonObject.getString("id"));
                                    int mMode = Integer.parseInt(jsonObject.getString("mode"));
                                    String mUser1Name = jsonObject.getString("user1_name");
                                    String mUser2Name = jsonObject.getString("user2_name");
                                    insertLobbyRow(mUser1Name, mUser2Name, mMode, mLoggedInUserName);
                                }
                            }
                        } catch (org.json.JSONException e) {
                            Log.d("MainActivity.java", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MainActivity.java", "Error: " + error.getMessage());
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
