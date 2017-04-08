package com.example.listview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Activity_ListView extends AppCompatActivity {

	private SharedPreferences myPreference;
	private SharedPreferences.OnSharedPreferenceChangeListener listener;
	private ListView my_listview;
	private ConnectivityCheck connect;
	private DownloadTask downloadJSON;
    protected String dataJSON;
    private ArrayList<BikeData> bikes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Change title to indicate sort by
		setTitle("Sort by:");

		//listview that you will operate on
		my_listview = (ListView)findViewById(R.id.lv);

		myPreference = PreferenceManager.getDefaultSharedPreferences(this);

		//On Update prefrences
		listener = new SharedPreferences.OnSharedPreferenceChangeListener(){
			public void onSharedPreferenceChanged(SharedPreferences prefs, String key){
				if(key.equals("listpref")){


				}
			}
		};
		//updates when prefrences change
		myPreference.registerOnSharedPreferenceChangeListener(listener);

		//toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();

		setupSimpleSpinner();

		if(connect.isNetworkReachableAlertUserIfNot(this)) {
			Toast toast = Toast.makeText(this, "Break reached", Toast.LENGTH_LONG);
            toast.show();
			if(downloadJSON != null) {
				downloadJSON.detach();
				downloadJSON = null;
			}

			downloadJSON = new DownloadTask(this);
			downloadJSON.execute(myPreference.getString("listpref","http://www.tetonsoftware.com/bikes/bikes.json"));
		}

		//set the listview onclick listener
		setupListViewOnClickListener();
	}

	private void setupListViewOnClickListener() {
		//TODO you want to call my_listviews setOnItemClickListener with a new instance of android.widget.AdapterView.OnItemClickListener() {
	}

	/**
	 * Takes the string of bikes, parses it using JSONHelper
	 * Sets the adapter with this list using a custom row layout and an instance of the CustomAdapter
	 * binds the adapter to the Listview using setAdapter
	 *
	 * @param JSONString  complete string of all bikes
	 */
	public void bindData(String JSONString) {

	}

	Spinner spinner;
	/**
	 * create a data adapter to fill above spinner with choices(Company,Location and Price),
	 * bind it to the spinner
	 * Also create a OnItemSelectedListener for this spinner so
	 * when a user clicks the spinner the list of bikes is resorted according to selection
	 * dontforget to bind the listener to the spinner with setOnItemSelectedListener!
	 */
	private void setupSimpleSpinner() {

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent myIntent = new Intent(this, activityPreference.class);
				startActivity(myIntent);
		default:
			break;
		}
		return true;
	}

	public void refresh() {
        bikes.clear();
        spinner.setSelection(0);
        downloadJSON = new DownloadTask(this);
        downloadJSON.execute(myPreference.getString("listpref","http://www.tetonsoftware.com/bikes/bikes.json"));
    }
}
