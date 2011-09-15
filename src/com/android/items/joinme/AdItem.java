package com.android.items.joinme;

public class AdItem {
   String nickName;
   String request;
   String dateString;
   int level;
   
   public String getNickName(){
	   return nickName;
   }
   
   public String getRequest(){
	   return request;
   }
   
   public int getLevel(){
	   return level;
   }
   
   public String getDateString(){
	   return dateString;
   }
   
   public AdItem(String nickName, String request, int level, String dateString){
	   this.nickName = nickName;
	   this.request = request;
	   this.level = level;
	   this.dateString = dateString;
   }
}

