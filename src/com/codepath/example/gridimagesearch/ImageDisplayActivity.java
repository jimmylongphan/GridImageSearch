package com.codepath.example.gridimagesearch;

import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		
		//String url = getIntent().getStringExtra("url");
		ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
		
		SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
		
		//ivImage.setImageUrl(url);
		ivImage.setImageUrl(result.getFullUrl());
		
		
	}
}
