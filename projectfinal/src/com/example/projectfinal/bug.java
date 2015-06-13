package com.example.projectfinal;

import com.splunk.mint.Mint;

import android.app.Activity;
import android.os.Bundle;

public class bug extends Activity{
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
		 
		 Mint.initAndStartSession(bug.this,"200951a5");
	 }
}
