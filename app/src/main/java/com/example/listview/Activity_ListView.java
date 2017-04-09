package com.example.listview;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Activity_ListView extends AppCompatActivity {

	private SharedPreferences myPreference;
	private SharedPreferences.OnSharedPreferenceChangeListener listener;
	private ListView my_listview;
	private listViewAdapter adapter;
	private ConnectivityCheck connect;
	private DownloadTask downloadJSON;
    protected String dataJSON;
    private List<BikeData> bikes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		connect = new ConnectivityCheck();

		// Change title to indicate sort by
		setTitle("Sort by:");

		//listview that you will operate on
		my_listview = (ListView)findViewById(R.id.lv);

		myPreference = PreferenceManager.getDefaultSharedPreferences(this);

		//On Update prefrences
		listener = new SharedPreferences.OnSharedPreferenceChangeListener(){
			public void onSharedPreferenceChanged(SharedPreferences prefs, String key){
				if(key.equals("websiteData")){
                       refresh();
				}
			}
		};
		//updates when prefrences change
		myPreference.registerOnSharedPreferenceChangeListener(listener);

		//toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();

		if(connect.isNetworkReachableAlertUserIfNot(this)) {
			//Toast toast = Toast.makeText(this, "Break reached", Toast.LENGTH_LONG);
            //toast.show();
			if(downloadJSON != null) {
				downloadJSON.detach();
				downloadJSON = null;
			}

			runDownload();
		}

		//set the listview onclick listener
		setupListViewOnClickListener();
	}

	private void setupListViewOnClickListener() {
		my_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Activity_ListView.this);
				builder.setMessage(bikes.get(position).toString() + "");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
	}

	/**
	 * Takes the string of bikes, parses it using JSONHelper
	 * Sets the adapter with this list using a custom row layout and an instance of the CustomAdapter
	 * binds the adapter to the Listview using setAdapter
	 *
	 * @param JSONString  complete string of all bikes
	 */
	protected void bindData(String JSONString) {
		JSONHelper helper = new JSONHelper();
		bikes = helper.parseAll(JSONString);
		adapter = new listViewAdapter(this, bikes);
        my_listview.setAdapter( adapter );
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
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(adapterView.getItemAtPosition(i).toString()) {
                    case "Company":
                        Collections.sort(bikes, new ComparatorCompany());
                        break;
                    case "Model":
                        Collections.sort(bikes, new ComparatorModel());
                        break;
                    case "Location":
                        Collections.sort(bikes, new ComparatorLocation());
                        break;
                    case "Price":
                        Collections.sort(bikes, new ComparatorPrice());
                        break;
                }
				adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
	}

	private void runDownload() {
		downloadJSON = new DownloadTask(this);
		downloadJSON.execute(myPreference.getString("listPref","http://www.tetonsoftware.com/bikes/bikes.json"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		setupSimpleSpinner();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent myIntent = new Intent(this, activityPreference.class);
				startActivity(myIntent);
				return true;

			case(R.id.action_refresh):
				refresh();
				return true;
		default:
			break;
		}
		return true;
	}

	public void refresh() {
        if(bikes != null) {
			bikes.clear();
		}
        spinner.setSelection(0);
        runDownload();
    }
}

class ComparatorModel implements Comparator<BikeData> {

    public int compare(BikeData myData1, BikeData myData2) {

// if both equal then 0

        return (myData1.getModel().compareTo(myData2.getModel()));

    }

}

class ComparatorCompany implements Comparator<BikeData> {

    public int compare(BikeData myData1, BikeData myData2) {

// if both equal then 0

        return (myData1.getCompany().compareTo(myData2.getCompany()));

    }

}

class ComparatorLocation implements Comparator<BikeData> {

    public int compare(BikeData myData1, BikeData myData2) {

// if both equal then 0

        return (myData1.getLocation().compareTo(myData2.getLocation()));

    }

}

class ComparatorPrice implements Comparator<BikeData> {

    public int compare(BikeData myData1, BikeData myData2) {

// if both equal then 0

        return ((int)(myData1.getPrice()*100))-((int)(myData2.getPrice()*100));

    }

}