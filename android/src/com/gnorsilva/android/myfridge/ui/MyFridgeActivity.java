package com.gnorsilva.android.myfridge.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.gnorsilva.android.myfridge.R;
import com.gnorsilva.android.myfridge.provider.MyFridgeContract;
import com.gnorsilva.android.myfridge.provider.MyFridgeContract.Fridge;
import com.gnorsilva.android.myfridge.provider.MyFridgeContract.History;
import com.gnorsilva.android.myfridge.quickactions.AddToFridgeQA;
import com.gnorsilva.android.myfridge.quickactions.EditItemsQA;
import com.gnorsilva.android.myfridge.utils.Utils;
import com.gnorsilva.android.myfridge.utils.ZXingIntentIntegrator;
import com.gnorsilva.android.myfridge.utils.ZXingIntentResultHandler;

public class MyFridgeActivity extends ListActivity{
	AddToFridgeQA addItemsQA;
	Uri selectedItemUri;
	
	// TODO provide sorting mechanism

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_my_fridge);
		
		addItemsQA = new AddToFridgeQA(this);

		Cursor cursor = managedQuery(Fridge.CONTENT_URI_ITEMS, null, null, null, null);
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_my_fridge_contents, cursor,
				new String[] { Fridge.USE_BY_DATE, Fridge.QUANTITY, Fridge.NAME  },
				new int[] { R.id.fridge_item_usebydate, R.id.fridge_item_quantity, R.id.fridge_item_name });
        
        setListAdapter(adapter);
	}
	
	public void onAddItemsClick(View v){
		addItemsQA.show(v);
	}
	
    public void onHomeClick(View v) {
    	Utils.goHome(this);
    }

    public void onRefreshClick(View v) {
    }
    
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(intent == null){
			return;
		}
		
		switch (requestCode) {
			case ZXingIntentIntegrator.REQUEST_CODE:
				new ZXingIntentResultHandler().webSearch(requestCode, resultCode, intent, this);
			case Utils.HISTORY_PICK_REQUEST_ID:
				String name = (String) intent.getStringExtra(History.NAME);
				Intent anIntent = new Intent(Intent.ACTION_INSERT);
				anIntent.putExtra(Fridge.NAME,name);
				anIntent.setType(Fridge.CONTENT_ITEM_TYPE);
				startActivity(anIntent);
		}
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id){
		selectedItemUri = Fridge.CONTENT_URI_ITEMS.buildUpon().appendPath(Long.toString(id)).build();
		new EditItemsQA(this,selectedItemUri).show(v);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id){
		case MyFridgeContract.DELETE_CONFIRMATION_DIALOG_ID:
			return getDeleteConfirmationDialog();
		}
		return null;
	}

	private Dialog getDeleteConfirmationDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String yes = getResources().getString(R.string.yes);
		String no = getResources().getString(R.string.no);
		
		builder.setMessage(getResources().getString(R.string.delete_item_confirmation))
		       .setCancelable(false)
		       .setPositiveButton(yes, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   Uri uri = MyFridgeActivity.this.selectedItemUri;
		        	   MyFridgeActivity.this.getContentResolver().delete(uri, null, null);
		           }
		       })
		       .setNegativeButton(no, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });

		return builder.create();
	}
}
