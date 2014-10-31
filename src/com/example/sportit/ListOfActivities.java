package com.example.sportit;

/*import android.support.v4.app.Fragment;*/
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.os.Build;

public class ListOfActivities extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_of_activities);
	}
	
	
	public void onClick(View view)
	{
		CheckedTextView chk = (CheckedTextView)view;
		chk.toggle();
	}

	public void changeView(View view)
	{
		Intent intent = new Intent(this, GoogleMapsView.class);
		startActivity(intent);
	}
}
