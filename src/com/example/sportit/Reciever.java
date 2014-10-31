package com.example.sportit;

import java.util.ArrayList;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class Reciever extends BroadcastReceiver {
	public static final String PROCESS_RESPONSE = "com.as400samplecode.intent.action.PROCESS_RESPONSE";
	public static ArrayList<Marker> list = new ArrayList<Marker>();
	@Override
	public void onReceive(Context context, Intent intent) {
		String responseString = intent.getStringExtra(ParsePull.RESPONSE_STRING);
		String activity = intent.getStringExtra("Activity");
        String lat= intent.getStringExtra("Lat");
        String longi = intent.getStringExtra("Longi");
        String updated = intent.getStringExtra("update");
        String user = intent.getStringExtra("user");
        double first = Double.parseDouble(lat);
        double second = Double.parseDouble(longi);
        LatLng lt = new LatLng(first,second);
        if(GoogleMapsView.pMap != null)
        {
        	if(GoogleMapsView.error){
	        	for(int i = 0; i < list.size(); ++i)
	        	{
	        		Marker some = list.get(i);
	        		LatLng temp = some.getPosition();
	        		if(temp.latitude == first && temp.longitude == second){
	        			list.remove(i);
	        			GoogleMapsView.pMap.clear();
	        		}
	        		
	        	}
	        	
	        	/*for(int i = 0; i < list.size();++i)
	        	{
	        		Marker something = list.get(i);
	        		GoogleMapsView.pMap.addMarker(new MarkerOptions()
	                .position(something.getPosition())
	                .title(something.getTitle())
	                .snippet(something.getSnippet())
	            	.icon(BitmapDescriptorFactory.fromResource(R.drawable.newlog)));
	        	}*/
	        	GoogleMapsView.error = false;
        	}
        	
        	if(updated.equals("true"))
        	{
        		if(BasicUI.name.equals(user))
        		{
        			NotificationManager mNotificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        			NotificationCompat.Builder mBuilder =
        	                new NotificationCompat.Builder(context)
        	                        .setSmallIcon(R.drawable.ic_launcher)
        	                        .setContentTitle("New Member")
        	                        .setContentText(responseString);
        			mBuilder.setPriority(100);
        			mNotificationManager.notify(0, mBuilder.build());
        		}
        		//GoogleMapsView.toUpdate = false;
        	}
        	System.out.println("responseString + " + responseString);
        	Marker k = GoogleMapsView.pMap.addMarker(new MarkerOptions()
            .position(lt)
            .title(activity)
            .snippet(responseString)
        	.icon(BitmapDescriptorFactory.fromResource(R.drawable.newlog)));
        	list.add(k);
        }
	}

}
