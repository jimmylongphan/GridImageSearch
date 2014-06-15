package com.codepath.example.gridimagesearch;

import java.util.List;

import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultArrayAdapter(Context context, List<ImageResult> images ) {
		super( context, R.layout.item_image_result, images );
	}

	/**
	 * 
	 * 
	 * @param position of the item
	 * @param convertView reuse views
	 * @param parent access to gridview itself
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// access the imageclass
		ImageResult imageInfo = this.getItem(position);
		SmartImageView ivImage;
		
		// if already exists then use convertView
		if ( convertView == null) {
			// take arbitrary and covert to in memory object view
			LayoutInflater inflator = LayoutInflater.from(getContext());
			ivImage = (SmartImageView) inflator.inflate(R.layout.item_image_result, parent, false);
		} else {
			// already exists, then convert
			ivImage = (SmartImageView) convertView;
			ivImage.setImageResource(android.R.color.transparent);
		}
		
		ivImage.setImageUrl(imageInfo.getThumbUrl());
		
		return ivImage;
	}

	
	
}
