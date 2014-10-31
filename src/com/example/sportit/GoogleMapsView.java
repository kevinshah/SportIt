package com.example.sportit;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.os.Build;

public class GoogleMapsView extends Activity implements GoogleMap.OnMapClickListener, LocationListener{

	private static final long MIN_TIME_BW_UPDATES = 5;
	private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
	private GoogleMap gMap = null;
	public static GoogleMap pMap = null;
	private LocationManager locationManager;
	private boolean isGPSEnabled;
	private boolean isNetworkEnabled;
	private boolean canGetLocation;
	private double latitude;
	private double longitude;
	private Location location;
	private final Context mContext = this;
	Button myButton = null;
	private Reciever r;
	public static LatLng lt = null;
	PendingIntent pI = null;
	private double lat_actual = 0.0;
	private double longi_actual = 0.0;
	public static boolean error = false;
	public static String updateMade = "";
	public static boolean toUpdate = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_maps_view);
        IntentFilter filter = new IntentFilter(Reciever.PROCESS_RESPONSE);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
        r = new Reciever();
        registerReceiver(r,filter);
        
        Intent msgIntent = new Intent(GoogleMapsView.this, ParsePull.class);
        //startService(msgIntent);
        pI = PendingIntent.getService(this, 0, msgIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, pI);
        
		setUpMapIfNeeded();
        gMap.setMyLocationEnabled(true);
        gMap.setOnMapClickListener(this);
        pMap = gMap;
        gMap.setOnMarkerClickListener(new OnMarkerClickListener()
        {
			@Override
			public boolean onMarkerClick(Marker arg0) {
				Toast.makeText(GoogleMapsView.this, arg0.getTitle(),1000).show();
				myButton = new Button(GoogleMapsView.this);
				myButton.setText("Enroll");

				RelativeLayout ll = (RelativeLayout)findViewById(R.id.rView);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(0, 600,0,0);
				ll.addView(myButton, lp);
				myButton.setLayoutParams(lp);
				myButton.setBackgroundResource(R.drawable.custom_btn_beige);
				LatLng place = arg0.getPosition();
				GoogleMapsView.this.lat_actual = place.latitude;
				GoogleMapsView.this.longi_actual = place.longitude;
				myButton.setOnClickListener(new OnClickListener(){
					public void onClick(View v) {
						ParseQuery<ParseObject> teamQuery = ParseQuery.getQuery("Locations");
						teamQuery.findInBackground(new FindCallback<ParseObject>() {
							@Override
							public void done(List<ParseObject> arg0, ParseException arg1) {
								for(int i = 0; i < arg0.size(); ++i)
								{
									ParseObject p = arg0.get(i);
									double lat = p.getDouble("Latitude");
									double longat = p.getDouble("Longitude"); 
									
									if(lat == GoogleMapsView.this.lat_actual && GoogleMapsView.this.longi_actual == longat)
									{
										int Enrollment = p.getInt("Enrolled");
										int size = p.getInt("number");
										String n = p.getString("username");
										Enrollment++;
										if(Enrollment > size)
										{
											GoogleMapsView.this.error = true;
											Toast toast = Toast.makeText(GoogleMapsView.this, "Can't Enroll, size is full", Toast.LENGTH_LONG);
											toast.show();
											GoogleMapsView.this.myButton.setVisibility(View.GONE);
										}
										else
										{
											GoogleMapsView.this.error = false;
											p.put("Enrolled", Enrollment);
											p.put("updateMade",true);
											p.saveInBackground();
											GoogleMapsView.this.myButton.setVisibility(View.GONE);
											GoogleMapsView.this.error = true;
											//GoogleMapsView.this.updateMade=n;
											//GoogleMapsView.this.toUpdate = true;
										}
									}
									
								}
							}
						});
					}
				});
				return false;
			}

        }); 
        
        gMap.setOnMapClickListener(new OnMapClickListener() {
        	
            @Override
            public void onMapClick(LatLng point) {
            	if(myButton != null)
            		GoogleMapsView.this.myButton.setVisibility(View.GONE);
            	//AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
                //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 100, pI);
            }
        });
        
        
        this.getLocation();
        tempPlaces();
        
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context,"Long press to Create Event", Toast.LENGTH_LONG);
        toast.show();
        gMap.setOnMapLongClickListener(new OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
            	Context context = getApplicationContext();
        		CharSequence text = "Creating Event";
        		int duration = Toast.LENGTH_SHORT;
        		Toast toast = Toast.makeText(context, text, duration);
        		toast.show();
        		GoogleMapsView.this.nextScreen(latLng);
            }
        });
        //am.cancel(pI);
	}
	
	@Override
	public void onDestroy()
	{
		this.unregisterReceiver(r);
		super.onDestroy();
	}
	
	public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
 
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return location;
    }
	
	
	private void tempPlaces() {
		LatLng lt = new LatLng(32.7150, -117.273329);
		LatLng lt2 = new LatLng(32.84722,-117.27332);
		LatLng lt3 = new LatLng(37.714145400,-122.25000);
		
		gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 12),2000,null);
	}


	private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (gMap == null) {
            gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            
            // Check if we were successful in obtaining the map.
            if (gMap != null) {
                // The Map is verified. It is now safe to manipulate the map.
            	
            }
        }
    }


	@Override
	public void onMapClick(LatLng pos) {
		
		
		
	}
	
	public void nextFunc()
	{
		Intent intent = new Intent(GoogleMapsView.this, CreateNewEvent.class);
		startActivity(intent);
	}
	
	public void nextFunc1()
	{
		for(int i = 0; i < 100; ++i)
			Log.wtf("here", i + "");
		nextFunc();
	}
	
	public void nextScreen(LatLng ll){
		AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
		am.cancel(pI);
		
		lt = ll;
		nextFunc1();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	/////////////////////////////////////////////////////////////////////////////////////////////
	public void goBack(View view)
	{
		Intent intent = new Intent(this, ListOfActivities.class);
		startActivity(intent); 
	}
}
