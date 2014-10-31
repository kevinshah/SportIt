package com.example.sportit;


import android.support.v4.app.Fragment;
import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class CreateAccount extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Parse.initialize(this, "wYcHpD4g7scbDXVdQ2tcvjHZ160ZvZqS4sVZ9jDu", "eI04Fwpw2xVQXGApPO45WGVwyvmF9H7Ztika2d3l");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
	}
	
	public void signUp(View view)
	{
		EditText username   = (EditText)findViewById(R.id.editText1);
		EditText password   = (EditText)findViewById(R.id.editText2);
		EditText repassword   = (EditText)findViewById(R.id.editText4);
		ParseObject gameScore = new ParseObject("Usernames");
		String p = password.getText().toString();
		String accountName = username.getText().toString();
		String re_p = repassword.getText().toString();
		final Context context = this;;
		if(p.equals(re_p))
		{
			gameScore.put("usernames",accountName );
			gameScore.put("passwords", p);
			gameScore.saveInBackground();
						
			Intent intent = new Intent(this, BasicUI.class);
			startActivity(intent);
		}
		else
			Toast.makeText(context , "Password didn't match",Toast.LENGTH_LONG).show();
	}

}
