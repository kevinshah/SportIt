package com.example.sportit;

import com.parse.Parse;
import com.parse.ParseObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
	
public class CreateNewEvent extends Activity {
	
	private Spinner ourcolorspinner; 
	private String selection; 
	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Parse.initialize(this, "wYcHpD4g7scbDXVdQ2tcvjHZ160ZvZqS4sVZ9jDu", "eI04Fwpw2xVQXGApPO45WGVwyvmF9H7Ztika2d3l");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_event);
	}

	
	public void changeView(View view)
	{
		Intent intent = new Intent(this, GoogleMapsView.class);
		startActivity(intent);
	}
	public void createView(View view)
	{
		Spinner spinner   = (Spinner)findViewById(R.id.activitySpinner);
		String text = spinner.getSelectedItem().toString();
		
		Spinner spinnerH = (Spinner)findViewById(R.id.hourSpinner);
		String hour = spinnerH.getSelectedItem().toString();
		
		Spinner spinnerM = (Spinner)findViewById(R.id.minuteSpinner);
		String minutes = spinnerH.getSelectedItem().toString();
		
		EditText num = (EditText)findViewById(R.id.numberPlayer);
		int nums = Integer.parseInt(num.getText().toString());
		
		RadioGroup rg=(RadioGroup)findViewById(R.id.radios);
		String radiovalue=  ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();  
	
		double latitude = GoogleMapsView.lt.latitude;
		double longitude = GoogleMapsView.lt.longitude;
		
		String username = BasicUI.name;
		
		ParseObject gg = new ParseObject("Locations");
		gg.put("Latitude",latitude );
		gg.put("Longitude", longitude);
		gg.put("Enrolled", 1);
		gg.put("name", text);
		gg.put("hour", hour);
		gg.put("minutes", minutes);
		gg.put("number", nums);
		gg.put("Notify", radiovalue);
		gg.put("username",username);
		gg.put("updateMade", false);
		gg.saveInBackground();
		Toast.makeText(context, "You have successfully created an activity for " + selection + ".", Toast.LENGTH_LONG).show();

		Intent intent = new Intent(this, GoogleMapsView.class);
		startActivity(intent);
		
	}
}
