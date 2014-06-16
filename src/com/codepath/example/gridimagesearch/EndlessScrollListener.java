/**
 * 
 */
package com.codepath.example.gridimagesearch;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * Triggered whenever a user scrolls through the collection.
 * AdapterView has support for binding to the OnScrollListener.
 * 
 */
public abstract class EndlessScrollListener implements OnScrollListener {

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    
    // The current offset index of data you have loaded
    private int currentPage = 0;
    
    // The total number of items in the dataset after the Last Load
    private int previousTotalItemCount = 0;
    
    // true if we are still waiting for the last set of data to load
    private boolean loading = true;
    
    // sets the starting page index
    private int startingPageIndex = 0;
    
    /**
     * Default constructor
     * 
     */
    public EndlessScrollListener() {
        
    }
    
    /**
     * Constructor setting threshold
     * @param visibleThreshold minimum amount of items below scroll position
     */
    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }
    

    /**
     * Occurs many times a second during a scroll.
     * First we check if we are waiting for the previous load to finish.
     * 
     * 
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount ) {
        
        // If the total item count is zero and the previous isn't, assume the 
        // list is invalidated and should be reset back to initial state
        if ( totalItemCount < previousTotalItemCount ) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if ( totalItemCount == 0 ) {
                this.loading = true;
            }
        }
        
        // If it's still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and item count.
        if ( loading && ( totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }
        
        // If it isn't currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if ( !loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            onLoadMore(currentPage + 1, totalItemCount);
            loading = true;
        }
    }

    /**
     * Defines the process for actually loading more data based on page
     * @param page page of the scrolling
     * @param totalItemCount itemCount from scrolling
     */
    public abstract void onLoadMore(int page, int totalItemCount);
    
    /**
     * Don't take any action on changed
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState ) {
        
    }
    
}
