package com.gnorsilva.android.myfridge.ui;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gnorsilva.android.myfridge.Item;
import com.gnorsilva.android.myfridge.R;
import com.gnorsilva.android.myfridge.provider.MyFridgeContract.Fridge;
import com.gnorsilva.android.myfridge.utils.WebSearch;


public class WebSearchResultsActivity extends ListActivity {
	private Item choosenItem;
	private Item[] searchResultItems;
	
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_web_search_results);
		String itemProductData = (String) getIntent().getStringExtra("itemProductData");
		String itemDataFormat = (String) getIntent().getStringExtra("itemDataFormat");
		
		//TODO Change the behaviour of the search - items should be added dynamically as they appear
		searchResultItems = WebSearch.searchDataContentsAndFormat(itemProductData, itemDataFormat);
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList()));
		new AddStringTask().execute();
	}
	
	public void onHomeClick(){
		
	}
	
	public void onRefreshClick(){
		
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id) {
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType(Fridge.CONTENT_ITEM_TYPE);
		
//		TextView textView = (TextView) v.findViewById(R.id.search_results_name);
//		String itemName = (String) textView.getText();
//		intent.putExtra(Fridge.NAME,itemName);
		
		choosenItem = searchResultItems[position];
		intent.putExtra("SelectedItem", choosenItem);
		startActivity(intent);
	}
	
	class AddStringTask extends AsyncTask<Void, String, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			for (Item item : searchResultItems) {
				publishProgress(item.getItemDetails());
//				SystemClock.sleep(200);
			}
			return(null);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void onProgressUpdate(String... item) {
			((ArrayAdapter)getListAdapter()).add(item[0]);
		}
		
		@Override
		protected void onPostExecute(Void unused) {
//			Toast.makeText(ItemSearchResults.this, "Done!", Toast.LENGTH_SHORT).show();
		}
	}
}
