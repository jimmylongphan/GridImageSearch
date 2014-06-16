package com.codepath.example.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {

    // constants
    private static final int REQUEST_CODE = 10;
    public static final String MAX_RESULTS_SIZE = "8";
    
    // Views references
	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	
	// client to make network calls
	AsyncHttpClient client;
	
	// saved options
	String optionImageSize = "";
	String optionColorFilter = "";
	String optionImageType = "";
	String optionSiteFilter = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		client = new AsyncHttpClient();
		
		gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				
				//i.putExtra("url", imageResult.getFullUrl() );
                i.putExtra("result", imageResult );

				startActivity(i);
			}
			
		});
		
		// Attach the listener to the AdapterView onCreate
		gvResults.setOnScrollListener(new EndlessScrollListener() {
            
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                // Triggered only when new data needs to be appended to the list
                // Append new items to adapterView
                
                customLoadMoreDataFromApi(page);
                
                //customLoadMoreDataFromApi(totalItemCount);
            }
        });
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.menu, menu);
	    return true;
	}

	
	/**
	 * Append more data into the adapter.
	 * 
	 * @param offset pageOffset
	 */
	public void customLoadMoreDataFromApi(int offset) {
        String query = etQuery.getText().toString();
        
        String offsetStr = Integer.toString(offset);
        
        // set our max results
        // Use the offset value and add it as a parameter to the API request to retrieve paginated data.
        String url = "https://ajax.googleapis.com/ajax/services/search/images?rsz=" + MAX_RESULTS_SIZE + "&"
                + "start=" + offsetStr + generateOptions() + "&v=1.0&q=" + Uri.encode(query);
        
        // Sends out network request
        // sent out the ajax request
        // The handler converts the JSON arrays into an array of ImageResults
        // create an anonymous class to handle http request
        client.get(url,
                new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                JSONArray imageJsonResults = null;
                try {                 
                    // Look at the structure of the JSON object being returned
                    // results is an array inside responseData
                    imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
                    
                    // Do not clear
                    // only append to image results
                    
                    // appends new data items to adapter
                    // Model will parse the array for us into an ImageResult
                    // Deserialize API response and then construct new objects to append to the adapter.
                    imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
                    
                    // let image adapter know that data has changed
                    // imageAdapter.notifyDataSetChanged();
                    
                    // Can see the results in the log message
                    Log.d("DEBUG", imageResults.toString() );
                } catch( JSONException e ) {
                    e.printStackTrace();
                }
            }
        });
    }

	
    /**
	 * 
	 */
	private void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);
	}
	
	/**
	 * Handler onClick method from btnSearch
	 * 
	 * @param v
	 */
	public void onImageSearch(View v) {
	    String query = etQuery.getText().toString();
		Toast.makeText(this,  "Searching for " + query, Toast.LENGTH_SHORT).show();
				
	      // taken from Google developers website
        /*
        URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
                "v=1.0&q=barack%20obama&userip=INSERT-USER-IP");
        */
        
        // set our max results
        String url = "https://ajax.googleapis.com/ajax/services/search/images?rsz=" + MAX_RESULTS_SIZE + "&"
                + "start=" + 0 + generateOptions() + "&v=1.0&q=" + Uri.encode(query);
        
        // sent out the ajax request
        // The handler converts the JSON arrays into an array of ImageResults
        // create an anonymous class to handle http request
        client.get(url,
                new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                JSONArray imageJsonResults = null;
                try {                 
                    // Look at the structure of the JSON object being returned
                    // results is an array inside responseData
                    imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
                    
                    // clear since we are calling from button
                    imageResults.clear();
                    
                    // Model will parse the array for us into an ImageResult
                    imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
                    
                    // let image adapter know that data has changed
                    // imageAdapter.notifyDataSetChanged();
                    
                    // Can see the results in the log message
                    Log.d("DEBUG", imageResults.toString() );
                } catch( JSONException e ) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	/**
	 * Settings handler onClick
	 * Called from clicking ActionBar
	 * @param mi
	 */
	public void onSettings( MenuItem mi ) {
	    Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
	    
	    i.putExtra(this.getString(R.string.constant_image_size) , optionImageSize);
        i.putExtra(this.getString(R.string.constant_color_filter) , optionColorFilter);
        i.putExtra(this.getString(R.string.constant_image_type) , optionImageType);
        i.putExtra(this.getString(R.string.constant_site_filter) , optionSiteFilter);
        
	    // invoke settings activity to get user settings
	    startActivityForResult(i, REQUEST_CODE);
	}
	
	
	/**
	 * Getting results from settings
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if ( resultCode == RESULT_OK && requestCode == REQUEST_CODE ) {
	        optionImageSize = data.getStringExtra(this.getString(R.string.constant_image_size));
	        optionColorFilter = data.getStringExtra(this.getString(R.string.constant_color_filter));
	        optionImageType = data.getStringExtra(this.getString(R.string.constant_image_type));
	        optionSiteFilter = data.getStringExtra(this.getString(R.string.constant_site_filter) );
	    }
	}
	
	/**
	 * Method to generate the options string based on user settings
	 * @return
	 */
	protected String generateOptions() {
	    String result = "";
	    
	    if ( !optionImageSize.isEmpty() ) {
	        result += "&imgsz=" + optionImageSize;
	    }
	    if ( !optionColorFilter.isEmpty() ) {
	        result += "&imgcolor=" + optionColorFilter;
	    }
	    if ( !optionImageType.isEmpty() ) {
	        result += "&imgtype=" + optionImageType;
	    }
	    if ( !optionSiteFilter.isEmpty() ) {
	        result += "&as_sitesearch=" + optionSiteFilter;
	    }
	    
	    return result;
	}
}
