package com.codepath.example.gridimagesearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = -1177049424902340190L;
    private String fullUrl;
	private String thumbUrl;
	
	
	/**
	 *
	 * @param json Dictionary of image objects
	 */
	public ImageResult( JSONObject json) {
		try {
			this.fullUrl = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");
		} catch( JSONException e) {
			this.fullUrl = null;
			this.thumbUrl = null;
		}
	}
	
	////////////
	// Getters Setters
	////////////
	public String getFullUrl() {
		return fullUrl;
	}
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	/**
	 * 
	 */
	public String toString() {
		return this.thumbUrl;
	}

	/**
	 * static so we can access directly from ImageResult
	 * @param imageJsonResults
	 * @return
	 */
	public static ArrayList<ImageResult> fromJSONArray(
			JSONArray array) {
		
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		
		// go through every item in json array
		// construct an ImageResult
		// add the new ImageResult to the results
		for ( int x = 0; x < array.length(); x++ ) {
			try {
				results.add(new ImageResult(array.getJSONObject(x)));
			} catch( JSONException e ) {
				e.printStackTrace();
			}
		}
		
		return results;
	}
}
