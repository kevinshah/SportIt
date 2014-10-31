

package com.example.sportit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AppEventsLogger;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

public class Loginfb extends Activity {

    private Button buttonLoginLogout;
    private boolean logged = false;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_ui);
        buttonLoginLogout = (Button)findViewById(R.id.login_button);

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
            	Log.d("Loginfb", "1");
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
            	Log.d("Loginfb", "2");
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }

        updateView();
    }

    @Override
    public void onStart() {
    	Log.d("Loginfb", "4");
        super.onStart();
        Log.d("Loginfb", "5");
        Session.getActiveSession().addCallback(statusCallback);
        Log.d("Loginfb", "5");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Loginfb", "her");
        Session.getActiveSession().removeCallback(statusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }
    
    private void updateView() {
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
        	Log.d("Loginfb", "6");
        	Intent intent = new Intent(Loginfb.this, ListOfActivities.class);
    		startActivity(intent);
            
        	
        } else {
        	onClickLogin();
        	Log.d("Loginfb", "7");

        }
    }
//--------------------------------------------------------------------------------
    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
        	Log.d("Loginfb", "8999999999999999999999999999999999999999999999");
        	
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            Log.d("Loginfb", "14");
        } else {
        	
        	Log.d("Loginfb", "900000000000000000000000000");
            Session.openActiveSession(this, true, statusCallback);
            Log.d("Loginfb", "11");
        }
    }

    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
        	Log.d("Loginfb", "10");
    		updateView();
  
            Log.d("Loginfb", "12");
        }
    }
}
