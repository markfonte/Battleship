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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private int mUserId = 0;
    private String mUserName = "default";
    private String mSessionId = "default";
    private boolean mSuccess = false;
    private boolean mIsLoggedIn = false;

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
                Log.d("Register.java", "here");
                if (isUsernameValid(username) && isPasswordValid(password)) {

                    Log.d("Register.java", " and here");
                    sendCreateAccountRequest(username, password, confirmPassword);
                }
            }
        });
        final Button backToLoginButton = findViewById(R.id.back_to_login_button);
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isUsernameValid(String username) {
        EditText mUsername = findViewById(R.id.UsernameEntry);
        if (username.length() < 1 || username.length() > 20) {
            mUsername.setError(getString(R.string.error_invalid_username));
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.length() <= 32;
    }

    @Override
    public void finish() {
        // Prepare data intent
        Intent data = new Intent(Register.this, MainActivity.class);
        data.putExtra("mUserId", mUserId);
        data.putExtra("mUserName", mUserName);
        data.putExtra("mSessionId", mSessionId);
        data.putExtra("logged_in", mIsLoggedIn);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data);
        super.finish();
    }

    protected void sendCreateAccountRequest(final String user, final String pass, final String confirm_pass) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2/api/create_account.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.get("success").toString().equals("false")) {
                                Log.d("Register.java", "Unsuccessful, reload page");
                                Log.d("Register.java", response);
                                //Intent i = new Intent(Register.this, Register.class);
                                //startActivity(i);
                                EditText mPasswordView = findViewById(R.id.ConfirmPasswordEntry);
                                EditText mUsernameView = findViewById(R.id.UsernameEntry);
                                JSONArray jsonArray = json.getJSONArray("errors");
                                String jsonArrayString = jsonArray.getString(0); //only display first error
                                if (jsonArrayString.equals("Password does not match confirm password")) {
                                    mPasswordView.setError(getString(R.string.passwords_do_not_match));
                                    mPasswordView.requestFocus();
                                } else if (jsonArrayString.equals("The username '" + user + "' already exists")) {
                                    mUsernameView.setError(getString(R.string.username_already_exists));
                                    mUsernameView.requestFocus();
                                } else {
                                    mPasswordView.setError(jsonArrayString);
                                    mPasswordView.requestFocus();
                                }
                            } else {
                                Log.d("Register.java", "Username and password are valid, transferring to lobby");
                                mUserId = Integer.parseInt(json.get("id").toString());
                                mUserName = json.get("name").toString();
                                mSessionId = json.get("session_id").toString();
                                mSuccess = true;
                                mIsLoggedIn = true;
                                finish();
                            }

                        } catch (JSONException e) {
                            Log.d("Register.java", "JSONException: " + e.toString());
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
            protected Map<String, String> getParams() {
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
