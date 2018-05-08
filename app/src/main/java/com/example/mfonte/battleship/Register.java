package com.example.mfonte.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final Button button = findViewById(R.id.RegisterButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //EditText userText = findViewById(R.id.UsernameEntry);
                //String username = userText.getText().toString();
                Intent openMainActivity= new Intent(Register.this, MainActivity.class);
                openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(openMainActivity, 0);
            }
        });
    }


    @Override
    public void onBackPressed() {
    }
}
