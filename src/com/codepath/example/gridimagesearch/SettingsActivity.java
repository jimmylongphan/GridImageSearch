package com.codepath.example.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

    // References to views
    Spinner spinImageSize;
    Spinner spinColorFilter;
    Spinner spinImageType;
    EditText etSiteFilter;
    
    // Adapters for spinners
    ArrayAdapter<CharSequence> adapterImageSize;
    ArrayAdapter<CharSequence> adapterColorFilter;
    ArrayAdapter<CharSequence> adapterImageType;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        spinImageSize = (Spinner) findViewById(R.id.spinImageSize);
        spinColorFilter = (Spinner) findViewById(R.id.spinColorFilter);
        spinImageType = (Spinner) findViewById(R.id.spinImageType);
        etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
        
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapterImageSize = ArrayAdapter.createFromResource(this, R.array.image_sizes, 
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterImageSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinImageSize.setAdapter(adapterImageSize);
        
        // color filter
        adapterColorFilter = ArrayAdapter.createFromResource(this, R.array.image_colors, 
                android.R.layout.simple_spinner_item);
        adapterColorFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinColorFilter.setAdapter(adapterColorFilter);
        
        // image types
        adapterImageType = ArrayAdapter.createFromResource(this, R.array.image_types, 
                android.R.layout.simple_spinner_item);
        adapterImageType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinImageType.setAdapter(adapterImageType);
        
        
        // load the spinners based on values
        String imageSize = getIntent().getStringExtra(this.getString(R.string.constant_image_size));
        String colorFilter = getIntent().getStringExtra(this.getString(R.string.constant_color_filter));
        String imageType = getIntent().getStringExtra(this.getString(R.string.constant_image_type));
        String siteFilter = getIntent().getStringExtra(this.getString(R.string.constant_site_filter));
        
        for( int i=0; i < adapterImageSize.getCount(); i++ ) {
            if ( adapterImageSize.getItem(i).equals(imageSize)) {
                spinImageSize.setSelection(i);
                break;
            }
        }
        
        for( int i=0; i < adapterColorFilter.getCount(); i++ ) {
            if ( adapterColorFilter.getItem(i).equals(colorFilter)) {
                spinColorFilter.setSelection(i);
                break;
            }
        }
        
        for( int i=0; i < adapterImageType.getCount(); i++ ) {
            if ( adapterImageType.getItem(i).equals(imageType)) {
                spinImageType.setSelection(i);
                break;
            }
        }
        
        // load the site filter based on previous value
        etSiteFilter.setText(siteFilter);
    }
    
    /**
     * Handler for btnSave
     * 
     * Return the saved values to the SearchActivity
     * 
     * @param v the button triggering this method
     * 
     */
    public void onSave( View v ) {
        String imageSize = spinImageSize.getSelectedItem().toString();
        String colorFilter = spinColorFilter.getSelectedItem().toString();
        String imageType = spinImageType.getSelectedItem().toString();
        String siteFilter = etSiteFilter.getText().toString();
        
        Intent data = new Intent();
        data.putExtra(this.getString(R.string.constant_image_size) , imageSize);
        data.putExtra(this.getString(R.string.constant_color_filter) , colorFilter);
        data.putExtra(this.getString(R.string.constant_image_type) , imageType);
        data.putExtra(this.getString(R.string.constant_site_filter) , siteFilter);
        
        String log = String.format("imageSize=%s, colorFilter=%s, imageType=%s, siteFilter=%s", 
                imageSize, colorFilter, imageType, siteFilter);
        Log.d("OPTION", log);
        
        // Activity finished, return data
        setResult(RESULT_OK, data);
        super.finish();
    }
    
}
