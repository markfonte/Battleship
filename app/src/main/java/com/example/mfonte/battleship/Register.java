package com.example.mfonte.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final Button createAccountButton = findViewById(R.id.RegisterButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText userText = findViewById(R.id.UsernameEntry);
                String username = userText.getText().toString();
                EditText passwordText = findViewById(R.id.PasswordEntry);
                String password = passwordText.getText().toString();
                EditText confirmPasswordText = findViewById(R.id.ConfirmPasswordEntry);
                String confirmPassword = confirmPasswordText.getText().toString();
                if(isUsernameValid(username) && isPasswordValid(password)) {
                    sendCreateAccountRequest(username, password, confirmPassword);
                }
            }
        });
        final Button backToLoginButton = findViewById(R.id.back_to_login_button);
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent openMainActivity= new Intent(Register.this, Login.class);
                openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(openMainActivity, 0);
            }
        });
    }

    private boolean isUsernameValid(String username) {
        EditText mUsername = findViewById(R.id.UsernameEntry);
        if(username.length() < 1 || username.length() > 20) {
            mUsername.setError(getString(R.string.error_invalid_username));
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.length() <= 32;
    }

    private int mUserId = 0;
    private String mUserName = "default";
    private String mSessionId = "default";
    private boolean mSuccess = false;

    @Override
    public void finish() {
        // Prepare data intent
        Intent data = new Intent(Register.this, MainActivity.class);
        data.putExtra("mUserId", mUserId);
        data.putExtra("mUserName", mUserName);
        data.putExtra("mSessionId", mSessionId);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data);
        super.finish();
    }

    protected void sendCreateAccountRequest(final String user, final String pass, final String confirm_pass) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2/api/create_account.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.get("success").toString().equals("false")) {
                            Log.d("Register.java", "Unsuccessful, reload page");
                            Log.d("Register.java", response);
                            Intent i = new Intent(Register.this, Register.class);
                            startActivity(i);
                        } else {
                            Log.d("Register.java", "Username and password are valid, transferring to lobby");
                            mUserId = Integer.parseInt(json.get("id").toString());
                            mUserName = json.get("name").toString();
                            mSessionId = json.get("session_id").toString();
                            mSuccess = true;
                            finish();
                        }

                    } catch (JSONException e) {
                        Log.d("Register.java", "JSONException");
                    }
                    Log.d("Response", response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.getNetworkTimeMs();
                    Log.d("Register.java", "Volley Error");
                }
            }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", "true");
                params.put("username", user);
                params.put("password", pass);
                params.put("confirm_password", confirm_pass);
                return params;
            }
        };
        queue.add(postRequest);
    }


    @Override
    public void onBackPressed() {
    }
}
