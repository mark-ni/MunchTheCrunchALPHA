package com.example.munchthecrunchalpha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected EditText userLocationEditText;
    protected EditText foodLocationEditText;
    protected Button goButton;
    protected TextView testing;

    protected String userLocation;
    protected String foodLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userLocationEditText = findViewById(R.id.userLocationEditText);
        foodLocationEditText = findViewById(R.id.foodLocationEditText);
        goButton = findViewById(R.id.button);

        testing = findViewById(R.id.testing);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLocation = userLocationEditText.getText().toString();
                foodLocation = foodLocationEditText.getText().toString();
                go(userLocation, foodLocation);
            }
        });
    }

    protected void go(String theUserLocation, String theFoodLocation) {
        testing.setText("" + userLocation + ", " + foodLocation);
    }
}
