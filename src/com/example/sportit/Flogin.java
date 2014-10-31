package com.example.sportit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AppEventsLogger;

public class Flogin extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flogin);
        Intent intent = new Intent(Flogin.this, Loginfb.class);
        startActivity(intent);
            
     
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an app may be launched into.
        AppEventsLogger.activateApp(this);
    }
}
