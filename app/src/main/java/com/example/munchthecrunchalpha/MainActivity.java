package com.example.munchthecrunchalpha;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import androidx.appcompat.app.AppCompatActivity;

//import android.app.Dialog;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
//
//    protected EditText userLocationEditText;
//    protected EditText foodLocationEditText;
//    protected Button goButton;
//
//    protected String userLocation;
//    protected String foodLocation;
    //AIzaSyBEXHwGH2vJ2urprU3TDFxvF4O245kPi-U
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, MapsActivity.class);

        Log.d(TAG, "isServicesOK: checking google services version");

        if (isServicesOK()) {
            startActivity(intent);
        }

//        userLocationEditText = findViewById(R.id.userLocationEditText);
//        foodLocationEditText = findViewById(R.id.foodLocationEditText);
//        goButton = findViewById(R.id.button);
//
//        goButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userLocation = userLocationEditText.getText().toString();
//                foodLocation = foodLocationEditText.getText().toString();
//                go(userLocation, foodLocation);
//            }
//        });
    }

    public boolean isServicesOK() {

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
