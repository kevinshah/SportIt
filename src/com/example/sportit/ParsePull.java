package com.example.sportit;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class ParsePull extends IntentService {

	public static final String REQUEST_STRING = "myRequest";
    public static final String RESPONSE_STRING = "myResponse";
    public static final String RESPONSE_MESSAGE = "myResponseMessage";
    public String total_string = null;
    public double Lat = 0.0;
    public double Long = 0.0;

	public ParsePull() {
		super("ParsePull");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Parse.initialize(this, "wYcHpD4g7scbDXVdQ2tcvjHZ160ZvZqS4sVZ9jDu", "eI04Fwpw2xVQXGApPO45WGVwyvmF9H7Ztika2d3l");
		//while(true){
			ParseQuery<ParseObject> teamQuery = ParseQuery.getQuery("Locations");
			teamQuery.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> arg0, ParseException arg1) {
					String s = null;
					int ii = 0;
					
					for(int i = 0; i < arg0.size(); ++i)
					{
						ParseObject p = arg0.get(i);
						s = p.getString("name");
						ii = p.getInt("number");
						int en = p.getInt("Enrolled");
						ParsePull.this.total_string = p.getInt("Enrolled") + " /" + ii + " " + "enrolled -Starts at: " + p.getString("hour") + ":" + p.getString("minutes") + "0PM";
						ParsePull.this.Lat = p.getDouble("Latitude");
						ParsePull.this.Long = p.getDouble("Longitude");	
						boolean needUpdate = p.getBoolean("updateMade");
						String user = p.getString("username");
						
						Intent broadcastIntent = new Intent();
				        broadcastIntent.setAction(Reciever.PROCESS_RESPONSE);
				        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
				        broadcastIntent.putExtra(RESPONSE_STRING, ParsePull.this.total_string);
				        broadcastIntent.putExtra("Lat",ParsePull.this.Lat + "");
				        broadcastIntent.putExtra("Longi",ParsePull.this.Long + "");
				        broadcastIntent.putExtra("Activity",s);
				        broadcastIntent.putExtra("update", needUpdate+"");
				        broadcastIntent.putExtra("user", user);
				        sendBroadcast(broadcastIntent);
						
					}
				}
			});
			//SystemClock.sleep(1000);
		//}
	}

}
