package com.example.projectfinal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class umer1 extends Activity {

	 CallbackManager callbackManager;
	    ShareDialog shareDialog;
	    String title;
	    String desc;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	
	        super.onCreate(savedInstanceState);
	        
	        FacebookSdk.sdkInitialize(getApplicationContext());
	        callbackManager = CallbackManager.Factory.create();
		setContentView(R.layout.ac1);
	        shareDialog = new ShareDialog(this);
	        // this part is optional
	      //  shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {  });
	    
	        Intent i=getIntent();
			title=i.getStringExtra("title");
			desc=i.getStringExtra("description");
			
			
}
	    public void share_button(View v)
	    {
	          	if (ShareDialog.canShow(ShareLinkContent.class)) {
			    		ShareLinkContent linkContent = new ShareLinkContent.Builder()
	    	            .setContentTitle(title)
	    	            .setContentDescription(desc)
	    	            .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
	    	            .build();

			    		shareDialog.show(linkContent);
			    	}
	    	  
	    	 
	    }
	    
	  
	    @Override
	    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        callbackManager.onActivityResult(requestCode, resultCode, data);
	    }
}
