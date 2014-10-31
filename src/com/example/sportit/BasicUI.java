package com.example.sportit;
import java.util.List;

import com.parse.Parse;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class BasicUI extends Activity {

	public static String name = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Parse.initialize(this, "wYcHpD4g7scbDXVdQ2tcvjHZ160ZvZqS4sVZ9jDu", "eI04Fwpw2xVQXGApPO45WGVwyvmF9H7Ztika2d3l");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_ui);
	}
	
	
	
	//When the user clicks on the plus button
	//This method takes them into a different page to view
	public void createAccount(View view)
	{
		Intent intent = new Intent(this, CreateAccount.class);
		startActivity(intent);
	}
	
	
	//This puts the view when the done button is pressed
	//Takes you to the Google Maps page
	public void changeView(View view)
	{
		EditText username   = (EditText)findViewById(R.id.editText1);
		String u = username.getText().toString();
		name = u;
	
		
		ParseObject gameList = new ParseObject("Usernames");
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Usernames");
		query.whereEqualTo("usernames", u);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				EditText password   = (EditText)findViewById(R.id.editText2);
				String p = password.getText().toString();
				ParseObject user_pass = arg0.get(0);
				Context c = BasicUI.this;
				String password_user = user_pass.getString("passwords");
				if(p.equals(password_user))
				{
					Intent intent = new Intent(BasicUI.this, ListOfActivities.class);
					startActivity(intent);
				}
				else
					Toast.makeText(c, "Improper Login" , Toast.LENGTH_LONG).show();
			}
		});
	}
	
	
	public void fbView(View view)
	{
		Intent intent = new Intent (BasicUI.this,Loginfb.class);
		startActivity(intent);
	}

	

}
