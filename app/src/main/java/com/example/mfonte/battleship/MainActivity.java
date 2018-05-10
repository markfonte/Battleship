package com.example.mfonte.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    protected void onResume() {
        super.onResume();
//        Bundle bundle = getIntent().getExtras();
//        try {
//            boolean value = bundle.getBoolean("success");
//        } catch (java.lang.NullPointerException e) {
//            Log.d("MainActivity.java", e.toString());
//        }
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
        LinearLayout lobbyListContainer = findViewById(R.id.lobbyLinearLayout);
        for(int x=0; x<3; ++x) {
            TextView user1 = new TextView(this);
            TextView user2 = new TextView(this);
            Button enterGame = new Button(this);
            user1.setText(userName); // TODO: change to user1
            user1.setPadding(8, 8 , 8, 8);
            user1.setTextSize(18);
            user2.setText(sessionId); //TODO: change to user2
            user2.setPadding(8, 8 , 8, 8);
            user2.setTextSize(18);
            enterGame.setText("Enter Game");
            LinearLayout lobbyRow = new LinearLayout(this);
            lobbyRow.addView(user1);
            lobbyRow.addView(user2);
            lobbyRow.addView(enterGame);
            lobbyListContainer.addView(lobbyRow);
        }
    }

    @Override
    public void onBackPressed() {
    }

    protected void startApp() {
        Intent i = new Intent(this, Login.class);
        startActivityForResult(i, 0);
    }

//    protected void sendGetRequest()  {
//        final TextView mTextView = findViewById(R.id.textBox1);
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "http://10.0.2.2/";
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("errorInfo", response);
//                        mTextView.setText(response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("errorInfo", error.getMessage());
//                mTextView.setText("Error using GET request");
//            }
//        });
//
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
//    }
}
