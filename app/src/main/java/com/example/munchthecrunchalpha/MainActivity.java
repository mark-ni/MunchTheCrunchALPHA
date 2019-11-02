package com.example.munchthecrunchalpha;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    
    protected EditText userLocationEditText;
    protected EditText foodLocationEditText;
    protected Button goButton;

    protected String userLocation;
    protected String foodLocation;
    //AIzaSyBEXHwGH2vJ2urprU3TDFxvF4O245kPi-U
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userLocationEditText = findViewById(R.id.userLocationEditText);
        foodLocationEditText = findViewById(R.id.foodLocationEditText);
        goButton = findViewById(R.id.button);

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
        Toast toast = Toast.makeText(getApplicationContext(), userLocation + ", " + foodLocation,Toast.LENGTH_LONG);
        toast.show();
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {

            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "Can't make a map request", Toast.LENGTH_SHORT).show();
        }
        return false;

    }
}
