package com.example.projectfinal;

import android.graphics.Bitmap;

public class news {
	
	
	String title;
	String description;
	String url;
	Bitmap img;
	String category;
	news()
	{
		
	}
	news(String t,String d,String u,Bitmap i,String c)
	{
		
		title=t;
		description=d;
		url=u;
		img=i;
		category=c;
		
	}
	
	public void settitle(String t)
	{
		title=t;
		
		
	}
	public void setcategory(String t)
	{
		category=t;
		
		
	}
	public void setdescription(String d)
	{
		description=d;
	}
	public void seturl(String u)
	{
		url=u;
	}
	public void setimg(Bitmap s)
	{
		img=s;
		
	}

	public String gettitle()
	{
		return title;
	}
	public String  getdescription()
	{
		return description;
	}
	public String geturl()
	{
		return url;
	}
	public String getcategory()
	{
		return category;
	}
	public Bitmap getimg()
	{
		return img;
	}
}

